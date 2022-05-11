import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Program extends Application {


    @Override
    public void start(Stage primaryStage) {

        //Skapar fönstret och knappen
        primaryStage.setTitle("PathFinder");
        Button btn = new Button();
        btn.setText("Find Path");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            //Detta kan göras om till en inre klass
            @Override
            public void handle(ActionEvent event) {
                //Skriv vad som ska ske när knappen trycks ned här
                System.out.println("Find path (kod ska utföras)");
            }
        });

        //Skapar en BorderPane och lägger till rutan där "fileMenu" ska ligga
        BorderPane root = new BorderPane();
        VBox vBox = new VBox();
        root.setTop(vBox);

        //Skapar "File" högst upp i programmet
        MenuBar menuBar = new MenuBar();
        vBox.getChildren().add(menuBar);
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);

        //Lägger till val i FileMenu
        MenuItem newMapItem = new MenuItem("New Map");
        fileMenu.getItems().add(newMapItem);

        MenuItem openItem = new MenuItem("Open");
        fileMenu.getItems().add(openItem);

        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);

        MenuItem saveImageItem = new MenuItem("Save Image");
        fileMenu.getItems().add(saveImageItem);

        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);

        //Lägger till knappen, ändrar storlek på Scenen och visar den
        root.setCenter(btn);
        primaryStage.setScene(new Scene(root, 300, 70));
        primaryStage.show();
    }
}
