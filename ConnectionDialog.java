/**
 * PROG2 VT2022, Inlämningsuppgift, del 2
 * Grupp 089
 * Artin Mohseni armo9784
 * Adam Polanik adan7490
 */
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
        createDialog(from, to);
    }

    //Konstruktor för showConnection dialogrutan som är icke editerbar
    public ConnectionDialog(String from, String to, String stringNameField, int intTimeField) {
        super(AlertType.CONFIRMATION);
        setNameField(stringNameField).setEditable(false);
        setTimeField(intTimeField).setEditable(false);
        createDialog(from, to);
    }
    //Konstruktor för change connection
    public ConnectionDialog(String from, String to, String stringNameField, int intTimeField, int newTime) {
        super(AlertType.CONFIRMATION);
        setNameField(stringNameField).setEditable(false);
        createDialog(from, to);
    }

    public String getName() {
        return nameField.getText();
    }
    public int getTime() {
        return Integer.parseInt(timeField.getText());
    }

    private void createDialog(String from, String to){
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

    //Sätter namnfältet i dialogrutan vid showConnection
    public TextField setNameField(String text){
        this.nameField.setText(text);
        return nameField;
    }

    //Sätter tidfältet i dialogrutan vid showConnection
    public TextField setTimeField(int intTimeField){
        String stringTimeField = String.valueOf(intTimeField);
        this.timeField.setText(stringTimeField);
        return timeField;
    }
}
