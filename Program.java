import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Program extends Application {

    Image image = new Image("file:europa.gif");
    ImageView imageView = new ImageView(image);

    @Override
    public void start(Stage primaryStage) {

        //Skapar fönstret och knappen
        primaryStage.setTitle("PathFinder");

        Button findPathBtn = new Button();
        findPathBtn.setText("Find Path");

        Button showConnectionBtn = new Button();
        showConnectionBtn.setText("Show Connection");

        Button newPlaceBtn = new Button();
        newPlaceBtn.setText("New Place");
        newPlaceBtn.setOnAction(new NewPlaceBtnHandler());

        Button newConnectionBtn = new Button();
        newConnectionBtn.setText("New Connection");

        Button changeConnectionBtn = new Button();
        changeConnectionBtn.setText("Change Connection");

        findPathBtn.setOnAction(new EventHandler<ActionEvent>() {

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

        //Lägger till val i FileMenu (fliken New Map)
        MenuItem newMapItem = new MenuItem("New Map");
        fileMenu.getItems().add(newMapItem);
        newMapItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                root.setBottom(imageView);
                primaryStage.sizeToScene(); //Ändrar storleken på fönstret så man ser kartan
            }
        });


        MenuItem openItem = new MenuItem("Open");
        fileMenu.getItems().add(openItem);

        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);

        MenuItem saveImageItem = new MenuItem("Save Image");
        fileMenu.getItems().add(saveImageItem);

        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);

        //Lägger till knappen, ändrar storlek på Scenen och visar den
        FlowPane controls = new FlowPane();
        root.setCenter(controls);
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setPadding(new Insets(5));
        controls.setHgap(10);
        controls.setPrefWrapLength(600); //Fixar så alla knappar är på samma rad
        controls.getChildren().addAll(findPathBtn, showConnectionBtn, newPlaceBtn, newConnectionBtn, changeConnectionBtn);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    class NewPlaceBtnHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            imageView.setOnMouseClicked(new MapClickHandler());
            imageView.setCursor(Cursor.CROSSHAIR);
        }
    }

    class MapClickHandler implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent mouseEvent) {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            Alert msgBox = new Alert(Alert.AlertType.INFORMATION, "Destination info osv");
            msgBox.showAndWait();
            System.out.println("New place at: (X: " + x + " Y: " + y + ")");

            imageView.setCursor(Cursor.DEFAULT);
        }
    }

}
