package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final DeleteConfirmationChecker deleteConfirmationChecker;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor,
                    DeleteConfirmationChecker deleteConfirmationChecker) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.deleteConfirmationChecker = deleteConfirmationChecker;
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
    }

    /**
     * Returns true if the given command text is a delete command that should show
     * the confirmation popup.
     */
    @FunctionalInterface
    public interface DeleteConfirmationChecker {
        boolean canShowDeleteConfirmation(String commandText);
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();

        if (commandText.equals("")) {
            return;
        }

        try {
            boolean shouldShowPopup = deleteConfirmationChecker.canShowDeleteConfirmation(commandText);

            if (shouldShowPopup) {
                boolean confirmed = showDeleteConfirmationPopup();

                if (!confirmed) {
                    return;
                }
            }

            commandExecutor.execute(commandText);
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

    /**
     * Shows a confirmation popup for delete command.
     *
     * @return true if user clicks OK, false otherwise.
     */
    private boolean showDeleteConfirmationPopup() {
        Stage owner = (Stage) commandTextField.getScene().getWindow();
        DeleteConfirmationWindow deleteConfirmationWindow = new DeleteConfirmationWindow(owner);
        deleteConfirmationWindow.setMessage("Are you sure you want to delete this student?");
        return deleteConfirmationWindow.showAndWait();
    }
}
