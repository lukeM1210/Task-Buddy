import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Callback;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

/*
 * This JavaFX application uses a Linked List to manage tasks entered by the user. 
 * Upon entering a task, the user is prompted to specify a due date, after which they can click 'Add Task' to save it. 
 * The tasks are displayed in a ListView, where users also have the option to delete tasks once they are completed.
 */
public class App extends Application {

    private LinkedList<String> tasks = new LinkedList<>();
    private ListView<String> taskListView = new ListView<>();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Override
    public void start(Stage primaryStage) {

        // Title label 
        Label titleLabel = new Label("Task Buddy");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Courier New'; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);  

        // TextField for users to type tasks
        TextField taskInput = new TextField();
        taskInput.setPrefWidth(300);
        taskInput.setPromptText("Enter a task");
        taskInput.setStyle("-fx-font-family: 'Courier New';");

        // DatePicker for users to add a due date
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select due date");
        datePicker.setPrefWidth(150);
        datePicker.setStyle("-fx-font-family: 'Courier New';");

        // Button to add the task
        Button addButton = new Button("Add Task");
        addButton.setStyle("-fx-font-family: 'Courier New'; -fx-font-weight: bold; -fx-padding: 10px;");

        // Action for the button to add tasks and dates to a ListView
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String task = taskInput.getText(); // user text input
                LocalDate date = datePicker.getValue(); // user selection input

                // Both TextField and DatePicker must be filled out
                if (!task.isEmpty() && date != null) {
                    String formattedTask = task + " (Due: " + dateFormatter.format(date) + ")";
                    tasks.add(formattedTask);
                    taskListView.getItems().add(formattedTask);  // Add task to ListView
                    taskInput.clear(); // Clear the TextField after adding the task
                    datePicker.setValue(null); // Clear the datePicker after adding the task
                }
            }
        });

        // VBox for the task input (left side)
        VBox leftSide = new VBox(10);
        leftSide.getChildren().addAll(taskInput, datePicker, addButton);
        leftSide.setAlignment(Pos.TOP_LEFT);
        leftSide.setPadding(new Insets(20));

        // ListView to display tasks (right side)
        taskListView.setPrefWidth(300);
        taskListView.setMaxHeight(400);

        // Set custom cell factory for taskListView to include a red X button next to each task
        taskListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new TaskCell();
            }
        });

        // HBox to hold both the left and right side
        HBox mainContent = new HBox(30); 
        mainContent.getChildren().addAll(leftSide, taskListView);
        mainContent.setAlignment(Pos.TOP_LEFT);
        mainContent.setPadding(new Insets(20));

        // VBox to hold the title and the main content
        VBox root = new VBox(20);  
        root.getChildren().addAll(titleLabel, mainContent);
        root.setAlignment(Pos.TOP_CENTER);  
        root.setPadding(new Insets(20));

        // Create a scene
        Scene scene = new Scene(root, 700, 500);

        // Set the stage with the scene and show it
        primaryStage.setTitle("Task Buddy v1.1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Custom ListCell to display task with a red X button on the far right
    private class TaskCell extends ListCell<String> {
        private HBox hbox = new HBox();
        private Label taskLabel = new Label();
        private Button deleteButton = new Button("X");
        private Region spacer = new Region();  // Spacer to push the red x button to the right

        public TaskCell() {
            super();
            hbox.setSpacing(10);
            hbox.setAlignment(Pos.CENTER_LEFT);

            // Set up the red X button
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: 'Courier New'");
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String task = getItem();
                    getListView().getItems().remove(task);  // Remove the task from the list
                }
            });

            // Spacer fills the space between taskLabel and deleteButton
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Add the task label, spacer, and delete button to the HBox
            hbox.getChildren().addAll(taskLabel, spacer, deleteButton);
        }

        @Override
        protected void updateItem(String task, boolean empty) {
            super.updateItem(task, empty);
            
            // Clear cell content if empty or task is null
            if (empty || task == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Customize task text
                taskLabel.setText(task);  
                taskLabel.setStyle("-fx-font-family: 'Courier New';");
                setGraphic(hbox);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
