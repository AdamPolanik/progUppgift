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

    //Konstruktor för showConnection dialogrutan som är icke editerbar
    public ConnectionDialog(String from, String to, String sNameField, int iTimeField) {
        super(AlertType.CONFIRMATION);
        setNameField(sNameField).setEditable(false); setTimeField(iTimeField).setEditable(false);
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
    //Konstruktor för change connection
    public ConnectionDialog(String from, String to, String sNameField, int iTimeField, int newTime) {
        super(AlertType.CONFIRMATION);
        setNameField(sNameField).setEditable(false); setTimeField(newTime).setEditable(true);
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

    //Sätter namnfältet i dialogrutan vid showConnection
    public TextField setNameField(String text){
        this.nameField.setText(text);
        return nameField;
    }

    //Sätter tidfältet i dialogrutan vid showConnection
    public TextField setTimeField(int iTimeField){
        String sTimeField = String.valueOf(iTimeField);
        this.timeField.setText(sTimeField);
        return timeField;
    }
}
