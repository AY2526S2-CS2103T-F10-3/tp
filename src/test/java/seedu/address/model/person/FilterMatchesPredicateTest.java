package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class FilterMatchesPredicateTest {

    @Test
    public void equals() {
        Optional<CourseId> firstCourseId = Optional.of(new CourseId("CS2103T"));
        Optional<CourseId> secondCourseId = Optional.of(new CourseId("CS2101"));
        Optional<TGroup> firstTGroup = Optional.of(new TGroup("T01"));
        Optional<TGroup> secondTGroup = Optional.of(new TGroup("T02"));

        FilterMatchesPredicate firstPredicate = new FilterMatchesPredicate(firstCourseId, Optional.empty());
        FilterMatchesPredicate secondPredicate = new FilterMatchesPredicate(secondCourseId, Optional.empty());
        FilterMatchesPredicate thirdPredicate = new FilterMatchesPredicate(firstCourseId, firstTGroup);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        FilterMatchesPredicate firstPredicateCopy = new FilterMatchesPredicate(firstCourseId, Optional.empty());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different course ID -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // different combination (course + tgroup vs course only) -> returns false
        assertFalse(firstPredicate.equals(thirdPredicate));
    }

    @Test
    public void test_courseIdMatches_returnsTrue() {
        // Exact match
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty());

        assertTrue(predicate.test(new PersonBuilder().withCourseId("CS2103T").build()));

        // Case insensitive match
        assertTrue(predicate.test(new PersonBuilder().withCourseId("cs2103t").build()));
        assertTrue(predicate.test(new PersonBuilder().withCourseId("Cs2103T").build()));
    }

    @Test
    public void test_courseIdMatches_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty());

        assertFalse(predicate.test(new PersonBuilder().withCourseId("CS2101").build()));
        assertFalse(predicate.test(new PersonBuilder().withCourseId("MA2001").build()));
    }

    @Test
    public void test_tGroupMatches_returnsTrue() {
        // Exact match
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T01")));

        assertTrue(predicate.test(new PersonBuilder().withTGroup("T01").build()));

        // Case insensitive match
        assertTrue(predicate.test(new PersonBuilder().withTGroup("t01").build()));
        assertTrue(predicate.test(new PersonBuilder().withTGroup("T01").build()));
    }

    @Test
    public void test_tGroupMatches_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T01")));

        assertFalse(predicate.test(new PersonBuilder().withTGroup("T02").build()));
        assertFalse(predicate.test(new PersonBuilder().withTGroup("G01").build()));
    }

    @Test
    public void test_bothFiltersMatch_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.of(new TGroup("T01")));

        // Both match exactly
        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .build()));

        // Both match with case insensitivity
        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("cs2103t")
                .withTGroup("t01")
                .build()));
    }

    @Test
    public void test_bothFiltersMatch_returnsFalse() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.of(new TGroup("T01")));

        // Course matches but tGroup doesn't
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T02")
                .build()));

        // TGroup matches but course doesn't
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("CS2101")
                .withTGroup("T01")
                .build()));

        // Neither matches
        assertFalse(predicate.test(new PersonBuilder()
                .withCourseId("MA2001")
                .withTGroup("G05")
                .build()));
    }

    @Test
    public void test_courseIdOnlyFilter_ignoresTGroup() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty());

        // Should match regardless of tGroup when only courseId is filtered
        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .build()));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T99")
                .build()));
    }

    @Test
    public void test_tGroupOnlyFilter_ignoresCourseId() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T01")));

        // Should match regardless of courseId when only tGroup is filtered
        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .build()));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("MA2001")
                .withTGroup("T01")
                .build()));
    }

    @Test
    public void test_noFilters_returnsTrue() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(Optional.empty(), Optional.empty());

        // Should match all persons when no filters are provided
        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .build()));

        assertTrue(predicate.test(new PersonBuilder()
                .withCourseId("MA2001")
                .withTGroup("G05")
                .build()));
    }

    @Test
    public void test_specialCharactersInCourseId() {
        // Test course IDs with spaces, hyphens, etc. (based on validation regex)
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103-T")), Optional.empty());

        assertTrue(predicate.test(new PersonBuilder().withCourseId("CS2103-T").build()));
        assertTrue(predicate.test(new PersonBuilder().withCourseId("cs2103-t").build()));
    }

    @Test
    public void test_toString() {
        Optional<CourseId> courseId = Optional.of(new CourseId("CS2103T"));
        Optional<TGroup> tGroup = Optional.of(new TGroup("T01"));

        FilterMatchesPredicate predicate = new FilterMatchesPredicate(courseId, tGroup);

        String expected = "seedu.address.model.person.FilterMatchesPredicate{courseId=" + courseId + ", tGroup="
                + tGroup + "}";
        assertEquals(expected, predicate.toString());
    }

}
