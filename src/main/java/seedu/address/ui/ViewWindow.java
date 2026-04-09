package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * A UI component for displaying a detailed view of a student's information and remarks in a separate window.
 */
public class ViewWindow extends UiPart<Region> {

    private static final String FXML = "ViewWindow.fxml";
    private static final int FIRST_REMARK_ROW_INDEX = 1;
    private static final int COL_INDEX = 0;
    private static final int COL_DATE = 1;
    private static final int COL_TEXT = 2;

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Person person;
    private PersonListPanel personListPanel;

    @FXML
    private Label nameLabel;
    @FXML
    private Label studentIdLabel;
    @FXML
    private Label courseIdLabel;
    @FXML
    private Label tGroupLabel;
    @FXML
    private GridPane remarksGrid;

    /**
     * Creates a new {@code ViewWindow} that is intended to be embedded inside
     * the main application window (no separate Stage).
     *
     * @param personList The observable list of persons to listen to for changes.
     * @param personListPanel The person list panel to control selection.
     */
    public ViewWindow(ObservableList<Person> personList, PersonListPanel personListPanel) {
        super(FXML);
        this.personListPanel = personListPanel;
        personList.addListener(this::onPersonListChanged);
    }

    /**
     * Sets the person whose details are to be displayed and refreshes the UI.
     *
     * @param person The student to display.
     */
    public void setPerson(Person person) {
        requireNonNull(person);
        this.person = person;
        updateDisplay();
    }

    /**
     * Handles changes in the person list.
     *
     * @param c The change notification.
     */
    private void onPersonListChanged(ListChangeListener.Change<? extends Person> c) {
        if (person == null) {
            return; // Nothing to do if no person is being viewed
        }

        while (c.next()) {
            c.getAddedSubList().stream()
                    .filter(p -> p.isSamePerson(person))
                    .findFirst()
                    .ifPresent(updatedPerson -> {
                        setPerson(updatedPerson);
                        personListPanel.selectPerson(updatedPerson);
                    });

            if (c.getRemoved().stream().anyMatch(p -> p.isSamePerson(person))) {
                boolean wasUpdated = c.getAddedSubList().stream().anyMatch(p -> p.isSamePerson(person));
                if (!wasUpdated) {
                    clear();
                    personListPanel.selectPerson(null);
                }
            }
        }
    }

    /**
     * Updates all UI components with the data from the current person.
     */
    private void updateDisplay() {
        updateMetadata();
        refreshRemarksGrid();
    }

    /**
     * Updates the text labels in the header section with the person's basic information.
     */
    private void updateMetadata() {
        nameLabel.setText(person.getName().fullName);
        studentIdLabel.setText(person.getStudentId().value);
        courseIdLabel.setText(person.getCourseId().value);
        tGroupLabel.setText(person.getTGroup().value);
    }

    /**
     * Clears existing remark rows and repopulates the grid with the current remarks.
     * The header row is defined in FXML and preserved.
     */
    private void refreshRemarksGrid() {
        clearRemarksGrid();
        int rowIndex = FIRST_REMARK_ROW_INDEX;
        for (Remark remark : person.getRemarks()) {
            addRemarkRow(remark, rowIndex);
            rowIndex++;
        }
    }

    /**
     * Adds a single remark as a new row in the grid.
     *
     * @param remark The remark data to add.
     * @param rowIndex The index of the row where the remark should be placed.
     */
    private void addRemarkRow(Remark remark, int rowIndex) {
        int displayIndex = rowIndex - FIRST_REMARK_ROW_INDEX + 1;
        Label indexLabel = new Label(String.valueOf(displayIndex));
        indexLabel.getStyleClass().add("index-cell");
        indexLabel.setMaxWidth(Double.MAX_VALUE);

        Label dateLabel = new Label(remark.getDate().toString());
        dateLabel.getStyleClass().add("date-cell");
        dateLabel.setMaxWidth(Double.MAX_VALUE);

        Label remarkLabel = new Label(remark.getText());
        remarkLabel.getStyleClass().add("remark-cell");
        remarkLabel.setWrapText(true);
        remarkLabel.setMaxWidth(Double.MAX_VALUE);
        remarksGrid.add(indexLabel, COL_INDEX, rowIndex);
        remarksGrid.add(dateLabel, COL_DATE, rowIndex);
        remarksGrid.add(remarkLabel, COL_TEXT, rowIndex);
    }
    /**
     * Clears the current view and resets UI components.
     */
    public void clear() {
        this.person = null;
        nameLabel.setText("");
        studentIdLabel.setText("");
        courseIdLabel.setText("");
        tGroupLabel.setText("");
        clearRemarksGrid();
    }

    /**
     * Clears all remark rows from the grid, preserving the header.
     */
    private void clearRemarksGrid() {
        remarksGrid.getChildren().removeIf(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            return (rowIndex != null && rowIndex >= FIRST_REMARK_ROW_INDEX) || (rowIndex == null && node != null);
        });
    }
}
