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
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import javax.swing.*;
import java.util.List;
import java.util.Optional;


public class Program extends Application {

    ListGraph listGraph = new ListGraph();
    BorderPane root;
    Pane bottom = new Pane();
    Image image = new Image("file:europa.gif");
    ImageView imageView = new ImageView(image);
    private Button newPlaceBtn;
    private Button newConnectionBtn;
    private Button showConnectionBtn;
    private Place from = null;
    private Place to = null;

    @Override
    public void start(Stage primaryStage) {

        //Skapar fönstret och knappen
        primaryStage.setTitle("PathFinder");

        Button findPathBtn = new Button();
        findPathBtn.setText("Find Path");

        showConnectionBtn = new Button();
        showConnectionBtn.setText("Show Connection");
        showConnectionBtn.setOnAction(new ShowConnectionHandler());

        newPlaceBtn = new Button();
        newPlaceBtn.setText("New Place");
        newPlaceBtn.setOnAction(new NewPlaceBtnHandler());

        newConnectionBtn = new Button();
        newConnectionBtn.setText("New Connection");
        newConnectionBtn.setOnAction(new NewConnectionHandler());

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
        root = new BorderPane();
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
                bottom.getChildren().add(imageView);
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
        root.setBottom(bottom);
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setPadding(new Insets(5));
        controls.setHgap(10);
        controls.setPrefWrapLength(600); //Fixar så alla knappar är på samma rad
        controls.getChildren().addAll(findPathBtn, showConnectionBtn, newPlaceBtn, newConnectionBtn, changeConnectionBtn);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    //Klass för newPlace knappen
    class NewPlaceBtnHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            imageView.setOnMouseClicked(new MapClickHandler());
            imageView.setCursor(Cursor.CROSSHAIR);
            newPlaceBtn.setDisable(true);
        }
    }

    class NewConnectionHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent){
            if(from == null || to == null){
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("Two places must be selected!");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            }
            else if(listGraph.getEdgeBetween(to.getName(), from.getName()) != null){
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("Places already connected!");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            }
            else{
                try {
                    ConnectionDialog dialog = new ConnectionDialog(from.getName(), to.getName());
                    Optional<ButtonType> result = dialog.showAndWait();
                    if (result.isPresent() && result.get() != ButtonType.OK)
                        return;
                    String name = dialog.getName();
                    int time = dialog.getTime();

                    listGraph.connect(from.getName(), to.getName(), name, time);
                    System.out.println("Connected!");

                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Fel!");
                    alert.showAndWait();
                }
            }
        }
    }

    //Klass för att lägga till noder på kartan
    class MapClickHandler implements EventHandler<MouseEvent> {
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

            if (name != null && result.isPresent()) {
                listGraph.add(name);
                Place place = new Place(x, y, name);
                bottom.getChildren().add(place);
                place.setOnMouseClicked(new PlaceClickHandler());
            }

            System.out.println(listGraph.getNodes().toString());
            imageView.setOnMouseClicked(null);
            imageView.setCursor(Cursor.DEFAULT);
            newPlaceBtn.setDisable(false);
        }
    }

    class PlaceClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Place p = (Place) mouseEvent.getSource();
            if (from == null) {
                from = p;
                p.setFill(Color.RED);
                System.out.println("Selected: " + p.getName());
            } else if (to == null && p != from) {
                to = p;
                p.setFill(Color.RED);
                System.out.println("Selected: " + p.getName());
            } else if (p == from) {
                p.setFill(Color.BLUE);
                System.out.println("Deselected: " + p.getName());
                from = null;
            } else if (p == to) {
                p.setFill(Color.BLUE);
                System.out.println("Deselected: " + p.getName());
                to = null;
            }
        }
    }

    //Dialogruta för Show Connection knappen
    class ShowConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String transport = "";
            int time = 0;
            if (from == null || to == null) {
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("Two places must be selected!");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            } else if (listGraph.getPath(from.getName(), to.getName()) == null) {
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("No connection established");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            } else {
                try {
                    List<Edge> listEdges = listGraph.getPath(from.getName(), to.getName());
                    System.out.println(listEdges);
                    for (Edge e : listEdges){
                        transport = e.getName();
                        time = e.getWeight();
                        System.out.println(e.getName());
                        System.out.println(e.getWeight());
                    }
                    ConnectionDialog dialog = new ConnectionDialog(from.getName(), to.getName(), transport, time);
                    dialog.showAndWait();
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Fel!");
                    alert.showAndWait();
                }
            }
        }
    }
}
