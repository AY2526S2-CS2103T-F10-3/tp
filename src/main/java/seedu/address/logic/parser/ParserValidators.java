package seedu.address.logic.parser;

import java.util.Arrays;
import java.util.Optional;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Procedural validation helpers for parsers. These methods perform checks on the
 * tokenized arguments and throw {@link ParseException} with consistent messages
 * produced by {@link ParserMessages}.
 */
public final class ParserValidators {

    private ParserValidators() {}

    /**
     * Checks both the preamble and all prefix values for tokens that look like bare prefixes
     * (e.g., "crs" instead of "crs/"). This catches errors at the start of input
     * and those swallowed by the greedy tokenizer (e.g., "filter abs/2 crs").
     *
     * @param argMultimap the tokenized argument map to inspect
     * @param allowedPrefixes the set of prefixes that are valid for the command
     * @param commandUsage usage text of the command; appended to the error message
     * @throws ParseException if a bare prefix token is detected anywhere
     */
    public static void checkForBarePrefixes(ArgumentMultimap argMultimap, Prefix[] allowedPrefixes,
                                            String commandUsage) throws ParseException {
        // 1. Check Preamble
        checkStringForBarePrefixes(argMultimap.getPreamble(), allowedPrefixes, commandUsage);

        // 2. Check all Values
        for (Prefix p : allowedPrefixes) {
            Optional<String> value = argMultimap.getValue(p);
            if (value.isPresent()) {
                checkStringForBarePrefixes(value.get(), allowedPrefixes, commandUsage);
            }
        }
    }

    /**
     * Helper method to scan a specific string for tokens matching bare prefix names.
     */
    private static void checkStringForBarePrefixes(String input, Prefix[] allowedPrefixes,
                                                   String commandUsage) throws ParseException {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            return;
        }

        for (String token : trimmedInput.split("\\s+")) {
            for (Prefix p : allowedPrefixes) {
                String bare = p.getPrefix().replace("/", "");
                if (token.equalsIgnoreCase(bare)) {
                    throw new ParseException(ParserMessages.possiblePrefixMissingSlash(commandUsage));
                }
            }
        }
    }

    /**
     * Scans the raw input string for tokens that contain a slash and verifies
     * that they start with one of the {@code allowedPrefixes}.
     */
    public static void checkForUnknownPrefixTokens(String args, Prefix[] allowedPrefixes,
                                                   String allowedPrefixesHumanReadable,
                                                   String commandUsage) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return;
        }

        for (String part : trimmedArgs.split("\\s+")) {
            if (!part.contains("/")) {
                continue;
            }
            boolean matchesAny = Arrays.stream(allowedPrefixes)
                    .anyMatch(prefix -> part.startsWith(prefix.getPrefix()));
            if (!matchesAny) {
                throw new ParseException(ParserMessages.invalidPrefix(allowedPrefixesHumanReadable, commandUsage));
            }
        }
    }

    /**
     * Ensures that the preamble is empty.
     */
    public static void checkForUnexpectedPreamble(ArgumentMultimap argMultimap, String commandUsage)
            throws ParseException {
        if (!argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(ParserMessages.unexpectedPreamble(commandUsage));
        }
    }

    /**
     * Checks that the given prefixes have non-blank values.
     */
    public static void checkForMissingValues(ArgumentMultimap argMultimap, Prefix[] prefixes,
                                             String[] prefixStrings, String[] detailMessages,
                                             String commandUsage) throws ParseException {
        assert prefixes.length == prefixStrings.length : "Prefix arrays mismatch";
        assert prefixes.length == detailMessages.length : "Detail message arrays mismatch";

        for (int i = 0; i < prefixes.length; i++) {
            if (argMultimap.isValueBlank(prefixes[i])) {
                throw new ParseException(
                        ParserMessages.missingPrefixValue(prefixStrings[i], detailMessages[i], commandUsage));
            }
        }
    }
}
