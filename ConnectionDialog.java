import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ConnectionDialog extends Alert{

    private TextField nameField = new TextField();
    private TextField timeField = new TextField();

    public ConnectionDialog(String from, String to) {
        super(AlertType.CONFIRMATION);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(10);
        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Time:"), timeField);
        setHeaderText("Connection from " + from + " to " + to );
        getDialogPane().setContent(grid);
    }
    public String getName() {
        return nameField.getText();
    }
    public int getTime() {
        return Integer.parseInt(timeField.getText());
    }
}
