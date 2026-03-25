---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# TeachAssist User Guide

TeachAssist is a **desktop app for university Teaching Assistants (TAs) to manage their students**, optimized for use via a Command Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, TeachAssist can get your student management tasks done faster than traditional GUI apps.

**Target User:** Full-time university TAs who manage multiple classes and tutorial groups each semester, and prefer a fast, keyboard-driven workflow.

**Value Proposition:** TeachAssist helps TAs efficiently track student information, attendance, progress, and remarks — all in one place, without tedious manual data management.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

<box type="tip" seamless>

**Tip:** A person can have any number of tags (including 0)
</box>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Finding students by name: `find`

Finds students whose names contain words that start with any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]...`

<box type="info" seamless>

**Notes:**
* The search is case-insensitive. e.g. `hans` will match `Hans`
* The order of keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name field is searched.
* Keywords match the **start of words** in names (prefix matching). Substrings in the middle of a word are **not** matched.
    * e.g. `Han` matches `Hans`
    * e.g. `an` will **not** match `Hans`
* Students matching **at least one** keyword are returned (i.e. `OR` search).
    * e.g. `Hans Bo` returns `Hans Gruber`, `Bo Yang`
* Keywords must contain only alphabetic characters (A–Z, a–z).
</box>

Examples:
* `find Jo` returns `John Doe`, `Joy Tan`
* `find alex david` returns `Alex Yeoh`, `David Li`

![Result for 'find alex david'](images/findAlexDavidResult.png)

### Filtering students : `filter`

Filters the student list by one or more criteria such as course, tutorial group, progress status, or number of absences.

Format: `filter [crs/COURSE_ID] [tg/TUTORIAL_GROUP] [p/PROGRESS] [abs/ABSENCE_COUNT]`

<box type="info" seamless>

**Notes:**
* At least one filter criterion must be provided.
* All provided criteria must match for a student to be included (i.e. `AND` search).
* `COURSE_ID` and `TUTORIAL_GROUP` matching is case-insensitive. e.g. `crs/cs2103t` matches `CS2103T`
* Valid values for `PROGRESS` (must be exact, case-sensitive):

| Value | Meaning |
|-------|---------|
| `NOT_SET` | No progress status assigned |
| `ON_TRACK` | Student is keeping up with the course |
| `NEEDS_ATTENTION` | Student may need additional support |
| `AT_RISK` | Student is struggling and needs intervention |

* `ABSENCE_COUNT` filters students with **at least** that many absences. Must be a non-negative integer.
</box>

<box type="tip" seamless>

**Tip:** Combine multiple filters to narrow down your list. For example, `filter crs/CS2103T tg/T01 p/AT_RISK` shows only at-risk students in tutorial group T01 of CS2103T.
</box>

Examples:
* `filter crs/CS2103T` returns all students enrolled in CS2103T.
* `filter tg/T01` returns all students in tutorial group T01.
* `filter p/AT_RISK` returns all students with a progress status of `AT_RISK`.
* `filter abs/3` returns all students with 3 or more absences.
* `filter crs/CS2103T tg/T01` returns all students in tutorial group T01 for course CS2103T.
* `filter crs/CS2103T p/NEEDS_ATTENTION abs/2` returns students in CS2103T with progress `NEEDS_ATTENTION` and at least 2 absences.

### Viewing a student's full details : `view`

Displays the complete details of a student, including their attendance record and remarks.

Format: `view INDEX`

* Displays the student at the specified `INDEX`.
* The index refers to the index number shown in the **currently displayed** student list.
* The index **must be a positive integer** 1, 2, 3, …​

<box type="tip" seamless>

**Tip:** Use `find` or `filter` to narrow down your list first, then use `view` to inspect a specific student's full details.
</box>

Examples:
* `list` followed by `view 2` displays the full details of the 2nd student in the student list.
* `find Alex` followed by `view 1` displays the full details of the 1st student returned by the `find` command.
* `filter tg/T01` followed by `view 3` displays the full details of the 3rd student in tutorial group T01.

### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Clear**  | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Filter** | `filter [crs/COURSE_ID] [tg/TUTORIAL_GROUP] [p/PROGRESS] [abs/ABSENCE_COUNT]`<br> e.g., `filter crs/CS2103T tg/T01`, `filter p/AT_RISK`, `filter abs/3`
**Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List**   | `list`
**Help**   | `help`
**View**   | `view INDEX`<br> e.g., `view 1`
