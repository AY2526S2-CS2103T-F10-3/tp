package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;


/**
 * Parse remark command
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {
    /**
     * Parse remark command
     */
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_REMARK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemarkCommand.MESSAGE_USAGE), ive);
        }

        Optional<String> optionalRemark = argMultimap.getValue(PREFIX_REMARK);

        Remark remark;
        if (optionalRemark.isPresent() && !optionalRemark.get().isBlank()) {
            remark = new Remark(optionalRemark.get());
        } else {
            // Handle missing remark safely, e.g., use null, throw an exception, or default to a placeholder
            remark = null; // or: remark = new Remark("No remark provided");
        }

        return new RemarkCommand(index, remark);
    }
}
