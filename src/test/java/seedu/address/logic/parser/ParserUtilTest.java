package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Name;
import seedu.address.model.person.Progress;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.Week;

/**
 * Contains unit tests for ParserUtil.
 */
public class ParserUtilTest {

    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_STUDENT_ID = "123";
    private static final String INVALID_COURSE = "CS 2103";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TG = "T 1";
    private static final String INVALID_TELE = "#handle";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_STUDENT_ID = "A1234567X";
    private static final String VALID_COURSE = "CS2103T";
    private static final String VALID_EMAIL = "e1234567@u.nus.edu";
    private static final String VALID_TG = "T01";
    private static final String VALID_TELE = "@johndoe";

    private static final String MESSAGE_USAGE = "Usage: view INDEX";

    // ----------------------- INDEX PARSING -----------------------

    @Test
    public void parseIndex_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseIndex(null));
    }

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        // EP: Non-numeric strings
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_zeroInput_throwsParseException() {
        // BVA: Zero (Classic off-by-one boundary for 1-based indexing)
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex("0"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        // BVA: Integer overflow
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1L)));
    }

    @Test
    public void parseIndex_validInput_returnsIndex() throws Exception {
        // EP: Smallest valid value (1)
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));
        // EP: Value with whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseIndex_withUsageEmptyArgs_throwsParseException() {
        assertThrows(ParseException.class, ParserUtil.MESSAGE_MISSING_INDEX + "\n" + MESSAGE_USAGE, ()
                -> ParserUtil.parseIndex("  ", MESSAGE_USAGE));
    }

    @Test
    public void parseIndex_withUsageTooManyArgs_throwsParseException() {
        // EP: Multiple tokens when only one is expected
        assertThrows(ParseException.class, ParserUtil.MESSAGE_TOO_MANY_ARGUMENTS + "\n" + MESSAGE_USAGE, ()
                -> ParserUtil.parseIndex("1 2", MESSAGE_USAGE));
    }

    @Test
    public void parseIndex_withUsageValidArgs_returnsIndex() throws Exception {
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex(" 1 ", MESSAGE_USAGE));
    }

    // ----------------------- KEYWORD PARSING -----------------------

    @Test
    public void parseKeywords_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseKeywords(null));
    }

    @Test
    public void parseKeywords_emptyArgs_throwsParseException() {
        // EP: Empty string and whitespace-only strings
        assertThrows(ParseException.class, ParserUtil.MESSAGE_EMPTY_KEYWORDS, ()
                -> ParserUtil.parseKeywords(""));
        assertThrows(ParseException.class, ParserUtil.MESSAGE_EMPTY_KEYWORDS, ()
                -> ParserUtil.parseKeywords("   "));
    }

    @Test
    public void parseKeywords_invalidCharacters_throwsParseException() {
        // EP: Numbers
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_KEYWORDS, ()
                -> ParserUtil.parseKeywords("Alice123"));
        // EP: Symbols (Requirement: Alphabetical only)
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_KEYWORDS, ()
                -> ParserUtil.parseKeywords("Alice@Bob"));
        // EP: Mix of valid and invalid
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_KEYWORDS, ()
                -> ParserUtil.parseKeywords("Alice !@#$"));
    }

    @Test
    public void parseKeywords_validArgs_returnsKeywordsList() throws Exception {
        // EP: Single keyword
        List<String> expectedSingle = List.of("Alice");
        assertEquals(expectedSingle, ParserUtil.parseKeywords("Alice"));

        // EP: Multiple keywords + varying whitespace + newlines
        List<String> expectedMultiple = Arrays.asList("Alice", "Bob", "Charlie");
        assertEquals(expectedMultiple, ParserUtil.parseKeywords("  Alice   Bob \n Charlie  "));
    }

    // ----------------------- FIELD PARSING (NAME, ID, ETC) -----------------------

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = "  " + VALID_NAME + "  ";
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseStudentId_validValue_returnsStudentId() throws Exception {
        assertEquals(new StudentId(VALID_STUDENT_ID), ParserUtil.parseStudentId(VALID_STUDENT_ID));
    }

    @Test
    public void parseCourseId_validValue_returnsCourseId() throws Exception {
        assertEquals(new CourseId(VALID_COURSE), ParserUtil.parseCourseId(VALID_COURSE));
    }

    @Test
    public void parseTGroup_validValue_returnsTGroup() throws Exception {
        assertEquals(new TGroup(VALID_TG), ParserUtil.parseTGroup(VALID_TG));
    }

    // ----------------------- PROGRESS & ATTENDANCE -----------------------

    @Test
    public void parseProgress_validValues_returnsProgress() throws Exception {
        // Normalization Test: Case insensitivity
        assertEquals(Progress.ON_TRACK, ParserUtil.parseProgress("on_track"));
        assertEquals(Progress.NEEDS_ATTENTION, ParserUtil.parseProgress("NEEDS_ATTENTION"));
        // Mapping Test: 'clear' maps to NOT_SET
        assertEquals(Progress.NOT_SET, ParserUtil.parseProgress("clear"));
    }

    @Test
    public void parseAttendanceStatus_validValue_returnsStatus() throws Exception {
        // EP: Lowercase and Uppercase
        assertEquals(Week.Status.Y, ParserUtil.parseAttendanceStatus("  y  "));
        assertEquals(Week.Status.A, ParserUtil.parseAttendanceStatus("A"));
    }

    @Test
    public void parseWeekIndex_zeroOrNegative_throwsParseException() {
        // BVA: Zero and Negative values
        assertThrows(ParseException.class, "Week index must be a positive integer.", ()
                -> ParserUtil.parseWeekIndex("0"));
        assertThrows(ParseException.class, "Week index must be a positive integer.", ()
                -> ParserUtil.parseWeekIndex("-1"));
    }

    // ----------------------- ABSENCE COUNT -----------------------

    @Test
    public void parseAbsenceCount_negativeValue_throwsParseException() {
        // BVA: Negative value (Boundary of non-negative integer)
        assertThrows(ParseException.class, "Absence count must be a non-negative integer.", ()
                -> ParserUtil.parseAbsenceCount("-1"));
    }

    @Test
    public void parseAbsenceCount_validValue_returnsInteger() throws Exception {
        // BVA: Zero (Minimum valid)
        assertEquals(0, ParserUtil.parseAbsenceCount("0"));
        // EP: Positive integer
        assertEquals(5, ParserUtil.parseAbsenceCount("  5  "));
    }
}
