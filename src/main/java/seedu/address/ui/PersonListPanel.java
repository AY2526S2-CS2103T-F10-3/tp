package seedu.address.ui;

import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
    }

    /**
     * Sets a click handler for the person list.
     *
     * @param handler The consumer to handle the clicked person.
     */
    public void setClickHandler(Consumer<Person> handler) {
        personListView.setOnMouseClicked(event -> {
            Person selected = personListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                handler.accept(selected);
            }
        });
    }

    /**
     * Selects and scrolls to the given person in the list.
     *
     * @param person The person to select.
     */
    public void selectPerson(Person person) {
        if (person != null) {
            personListView.getSelectionModel().select(person);
            personListView.scrollTo(person);
        } else {
            personListView.getSelectionModel().clearSelection();
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }

}
