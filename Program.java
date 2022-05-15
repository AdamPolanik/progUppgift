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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;


import java.util.Optional;


public class Program extends Application {

    ListGraph listGraph = new ListGraph();
    BorderPane root;
    Pane bottom = new Pane();
    Image image = new Image("file:europa.gif");
    ImageView imageView = new ImageView(image);
    private Button btnNewPlace;

    @Override
    public void start(Stage primaryStage) {

        //Skapar fönstret och knappen
        primaryStage.setTitle("PathFinder");

        Button btnFindPath = new Button();
        btnFindPath.setText("Find Path");

        Button btnShowConnection = new Button();
        btnShowConnection.setText("Show Connection");

        btnNewPlace = new Button();
        btnNewPlace.setText("New Place");
        btnNewPlace.setOnAction(new NewPlaceBtnHandler());

        Button btnNewConnection = new Button();
        btnNewConnection.setText("New Connection");

        Button btnChangeConnection = new Button();
        btnChangeConnection.setText("Change Connection");

        btnFindPath.setOnAction(new EventHandler<ActionEvent>() {

            //Detta kan göras om till en inre klass
            @Override
            public void handle(ActionEvent event) {
                //Skriv vad som ska ske när knappen trycks ned här
                System.out.println("Find path (kod ska utföras)");
            }
        });

        //Skapar en BorderPane och lägger till rutan där "menuFile" ska ligga
        root = new BorderPane();
        VBox vBox = new VBox();
        root.setTop(vBox);

        //Skapar "File" högst upp i programmet
        MenuBar menu = new MenuBar();
        vBox.getChildren().add(menu);
        Menu menuFile = new Menu("File");
        menu.getMenus().add(menuFile);

        //Lägger till val i FileMenu (fliken New Map)
        MenuItem newMapItem = new MenuItem("New Map");
        menuFile.getItems().add(newMapItem);
        newMapItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                bottom.getChildren().add(imageView);
                primaryStage.sizeToScene(); //Ändrar storleken på fönstret så man ser kartan
            }
        });


        MenuItem menuOpenFile = new MenuItem("Open");
        menuFile.getItems().add(menuOpenFile);

        MenuItem menuSaveItem = new MenuItem("Save");
        menuFile.getItems().add(menuSaveItem);

        MenuItem menuSaveImage = new MenuItem("Save Image");
        menuFile.getItems().add(menuSaveImage);

        MenuItem menuExit = new MenuItem("Exit");
        menuFile.getItems().add(menuExit);

        //Lägger till knappen, ändrar storlek på Scenen och visar den
        FlowPane controls = new FlowPane();
        root.setCenter(controls);
        root.setBottom(bottom);
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setPadding(new Insets(5));
        controls.setHgap(10);
        controls.setPrefWrapLength(600); //Fixar så alla knappar är på samma rad
        controls.getChildren().addAll(btnFindPath, btnShowConnection, btnNewPlace, btnNewConnection, btnChangeConnection);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    //Klass för newPlace knappen
    class NewPlaceBtnHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            imageView.setOnMouseClicked(new MapClickHandler());
            imageView.setCursor(Cursor.CROSSHAIR);
            btnNewPlace.setDisable(true);
        }
    }

    //Klass för att lägga till noder på kartan
    class MapClickHandler implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent mouseEvent) {
            String name;
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setContentText("Name of place: ");
            inputDialog.setTitle("Name");
            inputDialog.setHeaderText("");
            Optional<String> result = inputDialog.showAndWait();
            name = inputDialog.getEditor().getText();


            if(name != null && result.isPresent()){
                listGraph.add(name);
                bottom.getChildren().add(new Place(x,y));
            }

            System.out.println(listGraph.getNodes().toString());
            System.out.println(name);
            imageView.setCursor(Cursor.DEFAULT);
            btnNewPlace.setDisable(false);

        }
    }

    //Ska rita ut linjen mellan två markerade noder
    class EstablishConnection implements EventHandler<ActionEvent>{
        @Override
        public void handle (ActionEvent event){
            imageView.setOnMouseClicked(new NodeConnectionHandler());
        }
    }

    //Klass för att dra linjer mellan noder, funkar ej.
    class NodeConnectionHandler implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent event) {
            Place node = new Place(20, 20);
            Path connectionPath = new Path();

            MoveTo start = new MoveTo();
            start.setX(node.getLayoutX());
            start.setY(node.getLayoutY());

            LineTo end = new LineTo();
            end.setX(event.getX());
            end.setY(event.getY());

            connectionPath.getElements().addAll(start, end);
        }
    }

}
