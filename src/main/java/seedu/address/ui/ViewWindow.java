package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * A UI for displaying the view popup window.
 */
public class ViewWindow extends UiPart<Region> {

    private static final String FXML = "ViewWindow.fxml";

    private final Stage stage;

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
     * Creates a new {@code ViewWindow}.
     */
    public ViewWindow() {
        super(FXML);
        stage = new Stage();
        stage.setTitle("View");
        stage.setScene(new Scene(getRoot()));
    }

    /**
     * Shows the view window.
     */
    public void show() {
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Hides the view window.
     */
    public void hide() {
        stage.hide();
    }

    /**
     * Returns true if the view window is currently being shown.
     *
     * @return true if the window is showing, false otherwise
     */
    public boolean isShowing() {
        return stage.isShowing();
    }

    /**
     * Focuses on the view window.
     */
    public void focus() {
        stage.requestFocus();
    }

    /**
     * Sets the person details to be shown in the view window.
     *
     * @param person the person whose details are to be displayed
     */
    public void setPerson(Person person) {
        nameLabel.setText(person.getName().toString());
        studentIdLabel.setText(person.getStudentId().toString());
        courseIdLabel.setText(person.getCourseId().toString());
        tGroupLabel.setText(person.getTGroup().toString());

        resetGridWithHeader();
        populateRemarkRows(person.getRemarks());
    }

    private void resetGridWithHeader() {
        remarksGrid.getChildren().clear();

        Label dateHeader = new Label("Date");
        dateHeader.setStyle("-fx-font-weight: bold;");
        Label remarkHeader = new Label("Remark");
        remarkHeader.setStyle("-fx-font-weight: bold;");

        remarksGrid.add(dateHeader, 0, 0);
        remarksGrid.add(remarkHeader, 1, 0);
    }

    private void populateRemarkRows(java.util.List<Remark> remarks) {
        int row = 1;
        for (Remark remark : remarks) {
            Label dateLabel = new Label(remark.getDate().toString());
            Label remarkLabel = new Label(remark.getText());

            dateLabel.setWrapText(true);
            remarkLabel.setWrapText(true);

            remarksGrid.add(dateLabel, 0, row);
            remarksGrid.add(remarkLabel, 1, row);
            row++;
        }
    }
}
