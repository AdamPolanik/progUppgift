import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class Program extends Application {

    ListGraph listGraph = new ListGraph();
    BorderPane root;
    Pane bottom = new Pane();
    Image image = new Image("file:europa.gif");
    ImageView imageView = new ImageView(image);
    private Button findPathBtn;
    private Button newPlaceBtn;
    private Button newConnectionBtn;
    private Button showConnectionBtn;
    private Button changeConnectionBtn;
    private Place from = null;
    private Place to = null;
    private boolean edited = false;

    @Override
    public void start(Stage primaryStage) {

        //Skapar fönstret och knappen
        primaryStage.setTitle("PathFinder");

        findPathBtn = new Button();
        findPathBtn.setText("Find Path");
        findPathBtn.setOnAction(new FindPathHandler());

        showConnectionBtn = new Button();
        showConnectionBtn.setText("Show Connection");
        showConnectionBtn.setOnAction(new ShowConnectionHandler());

        newPlaceBtn = new Button();
        newPlaceBtn.setText("New Place");
        newPlaceBtn.setOnAction(new NewPlaceBtnHandler());

        newConnectionBtn = new Button();
        newConnectionBtn.setText("New Connection");
        newConnectionBtn.setOnAction(new NewConnectionHandler());

        changeConnectionBtn = new Button();
        changeConnectionBtn.setText("Change Connection");
        changeConnectionBtn.setOnAction(new ChangeConnectionHandler());

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
        saveItem.setOnAction(new SaveHandler());

        MenuItem saveImageItem = new MenuItem("Save Image");
        fileMenu.getItems().add(saveImageItem);
        saveImageItem.setOnAction(new SaveImageHandler());

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
            edited = true;
        }
    }

    class NewConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (from == null || to == null) {
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("Two places must be selected!");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            } else if (listGraph.getEdgeBetween(to.getName(), from.getName()) != null) {
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("Places already connected!");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            } else {
                try {
                    ConnectionDialog dialog = new ConnectionDialog(from.getName(), to.getName());
                    Optional<ButtonType> result = dialog.showAndWait();
                    if (result.isPresent() && result.get() != ButtonType.OK)
                        return;
                    String name = dialog.getName();
                    int time = dialog.getTime();

                    listGraph.connect(from.getName(), to.getName(), name, time);
                    System.out.println("Connected!");
                    edited = true;

                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Fel!");
                    alert.showAndWait();
                }
            }
        }
    }

    class FindPathHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            if(from == null || to == null){
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("Two places must be selected!");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            }
            else if (listGraph.getPath(from.getName(), to.getName()) == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ingen väg finns!");
                alert.showAndWait();
            }
            else{
                int total = 0;
                StringBuilder sb = new StringBuilder();
                for (Object object : listGraph.getPath(from.getName(),to.getName())) {
                    sb.append(object);

                    //Lägg till säkerhet/felhantering här under...
                    int lastIndex = sb.toString().lastIndexOf(" ");
                    String number = sb.toString().substring(lastIndex+1);
                    total += Integer.parseInt(number);

                    sb.append("\n");
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Message");
                alert.setContentText(sb.toString() + "total: " + total);
                alert.showAndWait();

                System.out.println(sb.toString());
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
            edited = true;

            if (name != null && result.isPresent()) {
                listGraph.add(name);
                Place place = new Place(x, y, name);
                bottom.getChildren().add(place);
                place.setOnMouseClicked(new PlaceClickHandler());
                edited = true;
            }

            System.out.println(listGraph.getNodes().toString());
            imageView.setOnMouseClicked(null);
            imageView.setCursor(Cursor.DEFAULT);
            newPlaceBtn.setDisable(false);
            edited = true;
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
            } else if (listGraph.getEdgeBetween(to.getName(), from.getName()) == null) {
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("No connection established");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            } else {
                try {
                    Edge edge = listGraph.getEdgeBetween(to.getName(), from.getName());
                    System.out.println(edge);
                    transport = edge.getName();
                    time = edge.getWeight();
                    ConnectionDialog dialog = new ConnectionDialog(from.getName(), to.getName(), transport, time);
                    dialog.showAndWait();
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Fel!");
                    alert.showAndWait();
                }
            }
        }
    }

    class ChangeConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if (from == null || to == null) {
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("Two places must be selected!");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            } else if (listGraph.getEdgeBetween(to.getName(), from.getName()) == null) {
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("No connection established");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            } else {
                try {
                    String transport = listGraph.getEdgeBetween(from.getName(), to.getName()).getName();
                    int newTime = listGraph.getEdgeBetween(from.getName(), to.getName()).getWeight();
                    ConnectionDialog dialog = new ConnectionDialog(from.getName(), to.getName(), transport, newTime);
                    dialog.setTimeField(newTime).setEditable(true);
                    dialog.showAndWait();
                    listGraph.setConnectionWeight(from.getName(), to.getName(), dialog.getTime());
                    edited = true;
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Fel!");
                    alert.showAndWait();
                }
            }
        }
    }

    class SaveHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            save();
        }
    }

    private void save(){
        try{
            FileWriter file = new FileWriter("europa.graph");
            PrintWriter out = new PrintWriter(file);
            out.println("europa.gif");

            for(Object object : bottom.getChildren()){
                if(object instanceof Place){
                    Place place = (Place) object;
                    out.print(place.getName() + ";" + place.getX() + ";" + place.getY() + ";");
                }
            }

            out.print("\n");

            for(Object name : listGraph.getNodes()){
                for(Object destination : listGraph.getNodes()){
                    if(listGraph.getEdgeBetween(name,destination) !=null){
                        Edge connection = listGraph.getEdgeBetween(name,destination);
                        out.println(name + ";" + connection.getDestination() + ";" + connection.getName()
                                + ";" + connection.getWeight() + ";");
                    }
                }
            }

            System.out.println("Saved");
            out.close();
            file.close();
            edited = false;

        } catch (FileNotFoundException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Kan inte öppna filen");
            alert.showAndWait();
        } catch (IOException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "IO_fel: " + exception.getMessage());
            alert.showAndWait();
        }
    }

    class SaveImageHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            try {
                WritableImage image = root.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", new File("capture.png"));
            }catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Fel!");
                alert.showAndWait();
            }
        }
    }

    class ExitHandler implements EventHandler<WindowEvent>{
        @Override
        public void handle(WindowEvent event){
            if(edited){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Unsaved Changes, exit anyways?");
                alert.setContentText(null);

                Optional<ButtonType> userChoice = alert.showAndWait();

                if (userChoice.isPresent() && userChoice.get() != ButtonType.OK){
                    event.consume();

                }
            }
        }
    }

}
