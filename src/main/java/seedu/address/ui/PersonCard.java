package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;
import seedu.address.model.person.Week;
import seedu.address.model.person.WeekList;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label studentId;
    @FXML
    private Label courseId;
    @FXML
    private Label tGroup;
    @FXML
    private Label email;
    @FXML
    private Label tele;
    @FXML
    private FlowPane weekAttendance;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        studentId.setText(person.getStudentId().value);
        courseId.setText(person.getCourseId().value);
        tGroup.setText(person.getTGroup().value);
        email.setText(person.getEmail().value);
        tele.setText(person.getTele() == null ? "-" : person.getTele().value);

        renderWeekAttendance(weekAttendance, (WeekList) person.getWeeklyAttendanceList());
    }

    /**
     * Render a Trackable attendance list as colored squares in a FlowPane.
     *
     * @param weekPane The FlowPane to render into.
     * @param weekList The WeekList to render.
     */
    private void renderWeekAttendance(FlowPane weekPane, WeekList weekList) {
        weekPane.getChildren().clear(); // clear old boxes

        // Loop through all weeks
        for (int i = 0; i < WeekList.NUMBER_OF_WEEKS; i++) {
            Week week = (Week) weekList.getWeeks()[i];

            // Week number label
            Label weekLabel = new Label("W" + (i + 1));
            weekLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: white;");

            // Colored square
            Label weekSquare = new Label();
            weekSquare.setMinSize(16, 16);
            weekSquare.setMaxSize(16, 16);

            // Assign color based on status
            switch (week.getStatus()) {
            case "Y" -> weekSquare.getStyleClass().add("week-green");
            case "A" -> weekSquare.getStyleClass().add("week-red");
            default -> weekSquare.getStyleClass().add("week-grey");
            }

            // Wrap in VBox
            VBox weekVBox = new VBox(2); // spacing 2px
            weekVBox.setAlignment(Pos.CENTER);
            weekVBox.getChildren().addAll(weekLabel, weekSquare);

            // Add to FlowPane
            weekPane.getChildren().add(weekVBox);
        }

        // Center all weeks
        weekPane.setAlignment(Pos.CENTER);
        weekPane.setHgap(5); // optional spacing
    }

    /**
     * Call this in the update method for the card:
     */
    public void updateWeekAttendance(Person person) {
        renderWeekAttendance(weekAttendance, (WeekList) person.getWeeklyAttendanceList());
    }
}
