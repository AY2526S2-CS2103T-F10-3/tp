package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.TGroup;

/**
 * Contains unit tests for {@link FilterCommandParser}.
 */
public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser, "", "Invalid command format! At least one filter must be provided.\n"
                + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidPrefix_failure() {
        assertParseFailure(parser, " x/CS2103T", "Invalid prefix in filter command: x/CS2103T\n"
                + "Allowed prefixes are: crs/ and tg/\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_missingValue_failure() {
        assertParseFailure(parser, " crs/", "Missing value for prefix: crs/\n"
                + "Course ID cannot be empty.\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_unexpectedTextBeforePrefixes_failure() {
        assertParseFailure(parser, " hello crs/CS2103T", "Unexpected text before prefixes: \"hello\"\n"
                + "Only these prefixes are allowed: crs/ and tg/\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_unexpectedTrailingText_failure() {
        assertParseFailure(parser, " crs/CS2103T extra",
                "Unexpected text after course ID.\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_missingTGroupValue_failure() {
        assertParseFailure(parser, " tg/", "Missing value for prefix: tg/\n"
                + "Tutorial ID cannot be empty.\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_unexpectedTrailingTextAfterTGroup_failure() {
        assertParseFailure(parser, " tg/T01 extra",
                "Unexpected text after tutorial group.\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidCourseId_failure() {
        assertParseFailure(parser, " crs/!", "Invalid course ID: !\n"
                + CourseId.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTGroup_failure() {
        assertParseFailure(parser, " tg/!", "Invalid tutorial ID: !\n"
                + TGroup.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, " crs/CS2103T crs/CS2103T",
                "Multiple values specified for the following single-valued field(s): crs/");
    }

    @Test
    public void parse_validArgsReversedOrder_success() throws ParseException {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(Optional.of(new CourseId("CS2103T")),
                        Optional.of(new TGroup("T01"))));
        assertParseSuccess(parser, " tg/T01 crs/CS2103T", expectedCommand);
    }

    @Test
    public void parse_unknownPrefixAfterValidPrefix_failure() {
        assertParseFailure(parser, " crs/CS2103T group/T01",
                "Invalid prefix in filter command: group/T01\n"
                        + "Allowed prefixes are: crs/ and tg/\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_unknownPrefixAfterTGroup_failure() {
        assertParseFailure(parser, " tg/T01 bad/value",
                "Invalid prefix in filter command: bad/value\n"
                        + "Allowed prefixes are: crs/ and tg/\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_duplicateTGroupPrefix_failure() {
        assertParseFailure(parser, " tg/T01 tg/T02",
                "Multiple values specified for the following single-valued field(s): tg/");
    }

    @Test
    public void parse_duplicateMixedPrefix_failure() {
        assertParseFailure(parser, " crs/CS2103T tg/T01 crs/CS2040",
                "Multiple values specified for the following single-valued field(s): crs/");
    }

    @Test
    public void parse_bothPrefixesMissingValues_failure() {
        assertParseFailure(parser, " crs/ tg/",
                "Missing value for prefix: crs/\n"
                        + "Course ID cannot be empty.\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_unexpectedTrailingTextAfterBothPrefixes_failure() {
        assertParseFailure(parser, " crs/CS2103T tg/T01 extra",
                "Unexpected text after tutorial group.\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidCourseIdWithValidTGroup_failure() {
        assertParseFailure(parser, " crs/! tg/T01",
                "Invalid course ID: !\n" + CourseId.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTGroupWithValidCourseId_failure() {
        assertParseFailure(parser, " crs/CS2103T tg/!",
                "Invalid tutorial ID: !\n" + TGroup.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_unexpectedTextBeforeBothPrefixes_failure() {
        assertParseFailure(parser, " hello crs/CS2103T tg/T01",
                "Unexpected text before prefixes: \"hello\"\n"
                        + "Only these prefixes are allowed: crs/ and tg/\n" + FilterCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_validArgs_success() throws ParseException {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(Optional.of(new CourseId("CS2103T")), Optional.empty()));
        assertParseSuccess(parser, " crs/CS2103T", expectedCommand);

        expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(Optional.empty(), Optional.of(new TGroup("T01"))));
        assertParseSuccess(parser, " tg/T01", expectedCommand);

        expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(Optional.of(new CourseId("CS2103T")),
                        Optional.of(new TGroup("T01"))));
        assertParseSuccess(parser, " crs/CS2103T tg/T01", expectedCommand);

        expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(Optional.of(new CourseId("CS2103T")),
                        Optional.of(new TGroup("T01"))));
        assertParseSuccess(parser, "  crs/CS2103T   tg/T01  ", expectedCommand);
    }
}
