// package seedu.address.logic.commands;

// import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
// import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
// import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
// import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import seedu.address.model.Model;
// import seedu.address.model.ModelManager;
// import seedu.address.model.UserPrefs;

// /**
//  * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
//  */
// public class ListCommandTest {

//     private Model model;
//     private Model expectedModel;

//     @BeforeEach
//     public void setUp() {
//         model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
//         expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
//     }

//     @Test
//     public void execute_listIsNotFiltered_showsSameList() {
//         assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
//     }

//     @Test
//     public void execute_listIsFiltered_showsEverything() {
//         showPersonAtIndex(model, INDEX_FIRST_PERSON);
//         assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
//     }
// }
package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.Tele;

public class ListCommandTest {

    private Model model;
    private Person alice;
    private Person bob;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();

        alice = new Person(
                new Name("Alice"),
                new CourseId("CS2103"),
                new Email("alice@u.nus.edu"),
                new StudentId("A1234567X"),
                new TGroup("T01"),
                new Tele("91234567")
        );

        bob = new Person(
                new Name("Bob"),
                new CourseId("CS2101"),
                new Email("bob@u.nus.edu"),
                new StudentId("A7654321X"),
                new TGroup("T02"),
                null
        );
    }

    @Test
    public void execute_listShowsPersonsSortedByName() {
        // Add in WRONG order
        model.addPerson(bob);
        model.addPerson(alice);

        new ListCommand().execute(model);

        List<Person> list = model.getFilteredPersonList();

        assertEquals("Alice", list.get(0).getName().fullName);
        assertEquals("Bob", list.get(1).getName().fullName);
    }

    @Test
    public void addCommand_doesNotBreakSorting() {
        model.addPerson(alice);

        Person aaron = new Person(
                new Name("Aaron"),
                new CourseId("CS2103"),
                new Email("aaron@u.nus.edu"),
                new StudentId("A9999999X"),
                new TGroup("T99"),
                null
        );

        model.addPerson(aaron);

        new ListCommand().execute(model);

        List<Person> list = model.getFilteredPersonList();

        assertEquals("Aaron", list.get(0).getName().fullName);
        assertEquals("Alice", list.get(1).getName().fullName);
    }

    @Test
    public void deleteCommand_keepsSortingCorrect() {
        model.addPerson(alice);
        model.addPerson(bob);

        model.deletePerson(alice);

        new ListCommand().execute(model);

        List<Person> list = model.getFilteredPersonList();

        assertEquals(1, list.size());
        assertEquals("Bob", list.get(0).getName().fullName);
    }

    @Test
    public void nullTele_doesNotCrash() {
        model.addPerson(bob);

        new ListCommand().execute(model);

        Person p = model.getFilteredPersonList().get(0);
        assertNull(p.getTele());
    }

    @Test
    public void execute_listAfterFiltering_showsAllPersons() {
        // Add two persons
        model.addPerson(alice);
        model.addPerson(bob);

        // Apply a filter (only show Alice)
        model.updateFilteredPersonList(p -> p.getName().fullName.equals("Alice"));

        // Sanity check: only 1 person visible
        assertEquals(1, model.getFilteredPersonList().size());

        // Execute ListCommand
        new ListCommand().execute(model);

        // Now should show ALL persons again
        List<Person> list = model.getFilteredPersonList();

        assertEquals(2, list.size());
    }
}
