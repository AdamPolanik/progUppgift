/**
 * PROG2 VT2022, Inlämningsuppgift, del 2
 * Grupp 089
 * Artin Mohseni armo9784
 * Adam Polanik adan7490
 */
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PathFinder extends Application {

    private final ListGraph listGraph = new ListGraph();
    private BorderPane root;
    private Pane outputArea = new Pane();
    private Button btnFindPath;
    private Button btnNewPlace;
    private Button btnNewConnection;
    private Button btnShowConnection;
    private Button btnChangeConnection;
    private Node from;
    private Node to;
    private boolean edited;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        //Skapar fönstret och knappen
        primaryStage.setTitle("PathFinder");

        btnFindPath = new Button();
        btnFindPath.setText("Find Path");
        btnFindPath.setOnAction(new FindPathHandler());

        btnShowConnection = new Button();
        btnShowConnection.setText("Show Connection");
        btnShowConnection.setOnAction(new ShowConnectionHandler());

        btnNewPlace = new Button();
        btnNewPlace.setText("New Place");
        btnNewPlace.setOnAction(new NewPlaceBtnHandler());

        btnNewConnection = new Button();
        btnNewConnection.setText("New Connection");
        btnNewConnection.setOnAction(new NewConnectionHandler());

        btnChangeConnection = new Button();
        btnChangeConnection.setText("Change Connection");
        btnChangeConnection.setOnAction(new ChangeConnectionHandler());

        //Skapar en BorderPane och lägger till rutan där "menuFile" ska ligga
        root = new BorderPane();
        VBox vbox = new VBox();
        root.setTop(vbox);

        //Skapar "File" högst upp i programmet
        MenuBar menu = new MenuBar();
        vbox.getChildren().add(menu);
        Menu menuFile = new Menu("File");
        menu.getMenus().add(menuFile);

        //Lägger till val i FileMenu (fliken New Map)
        MenuItem menuNewMap = new MenuItem("New Map");
        menuFile.getItems().add(menuNewMap);
        menuNewMap.setOnAction(new NewMapHandler());

        MenuItem menuOpenFile = new MenuItem("Open");
        menuFile.getItems().add(menuOpenFile);
        menuOpenFile.setOnAction(new OpenFileHandler());

        MenuItem menuSaveFile = new MenuItem("Save");
        menuFile.getItems().add(menuSaveFile);
        menuSaveFile.setOnAction(new SaveHandler());

        MenuItem menuSaveImage = new MenuItem("Save Image");
        menuFile.getItems().add(menuSaveImage);
        menuSaveImage.setOnAction(new SaveImageHandler());

        MenuItem menuExit = new MenuItem("Exit");
        menuFile.getItems().add(menuExit);
        menuExit.setOnAction(new ExitItemHandler());

        //Lägger till knappen, ändrar storlek på Scenen och visar den
        FlowPane controls = new FlowPane();
        root.setCenter(controls);
        root.setBottom(outputArea);
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setPadding(new Insets(5));
        controls.setHgap(10);
        controls.setPrefWrapLength(600); //Fixar så alla knappar är på samma rad
        controls.getChildren().addAll(btnFindPath, btnShowConnection, btnNewPlace, btnNewConnection, btnChangeConnection);

        primaryStage.setOnCloseRequest(new ExitHandler());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        menu.setId("menu");
        menuFile.setId("menuFile");
        menuNewMap.setId("menuNewMap");
        menuOpenFile.setId("menuOpenFile");
        menuSaveFile.setId("menuSaveFile");
        menuSaveImage.setId("menuSaveImage");
        menuExit.setId("menuExit");

        btnFindPath.setId("btnFindPath");
        btnShowConnection.setId("btnShowConnection");
        btnNewPlace.setId("btnNewPlace");
        btnChangeConnection.setId("btnChangeConnection");
        btnNewConnection.setId("btnNewConnection");
        outputArea.setId("outputArea");

    }

    private void showImage(String fileName){
        from = null;
        to = null;
        outputArea.getChildren().clear();
        listGraph.clearNodes();
        Image image = new Image(fileName);
        ImageView imageView = new ImageView(image);
        outputArea.getChildren().add(imageView);
    }

    class OpenFileHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            if(edited){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Unsaved changes, open anyways?");
                alert.setContentText(null);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK){
                    load();
                    primaryStage.sizeToScene();
                    edited = true;
                }
            }
            else{
                load();
                edited = true;
                primaryStage.sizeToScene();
            }
        }
    }

    class NewMapHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            if(edited){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Unsaved changes, open anyways?");
                alert.setContentText(null);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK){
                    showImage("europa.gif");
                    primaryStage.sizeToScene();
                    edited = true;
                }
            }
            else{
                showImage("europa.gif");
                edited = true;
                primaryStage.sizeToScene();
            }
        }
    }

    //Klass för newPlace knappen
    class NewPlaceBtnHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            outputArea.setOnMouseClicked(new MapClickHandler());
            outputArea.setCursor(Cursor.CROSSHAIR);
            btnNewPlace.setDisable(true);
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
            } else if (listGraph.getEdgeBetween(to, from) != null) {
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

                    listGraph.connect(from, to, name, time);
                    Line line = new Line(from.getX(),from.getY(),to.getX(),to.getY());
                    line.setStrokeWidth(3);
                    line.setDisable(true);
                    outputArea.getChildren().add(line);

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
            else if (listGraph.getPath(from, to) == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ingen väg finns!");
                alert.showAndWait();
            }
            else{
                int total = 0;
                StringBuilder sb = new StringBuilder();
                List list = listGraph.getPath(from,to);
                for (Object object : list) {
                    sb.append(object);

                    //Lägg till säkerhet/felhantering här under...
                    int lastIndex = sb.toString().lastIndexOf(" ");
                    String number = sb.toString().substring(lastIndex+1);
                    total += Integer.parseInt(number);

                    sb.append("\n");
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Message");
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setPadding(new Insets(10));
                grid.setHgap(5);
                grid.setVgap(10);
                TextArea textArea = new TextArea();
                textArea.appendText(sb + "Total " + total);
                grid.addRow(0, textArea);
                alert.setHeaderText("Connection from " + from + " to " + to );
                alert.getDialogPane().setContent(grid);
                alert.showAndWait();
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

                Node place = new Node(x, y, name);
                listGraph.add(place);
                place.setId(place.getName());

                Text text = new Text(name);
                text.setX(x);
                text.setY(y+30);
                text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                outputArea.getChildren().addAll(place,text);
                place.setOnMouseClicked(new PlaceClickHandler());
                edited = true;
            }

            outputArea.setOnMouseClicked(null);
            outputArea.setCursor(Cursor.DEFAULT);
            btnNewPlace.setDisable(false);
            edited = true;
        }
    }

    class PlaceClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Node p = (Node) mouseEvent.getSource();
            if (from == null && p != to){
                from = p;
                p.setFill(Color.RED);
            } else if (to == null && p != from) {
                to = p;
                p.setFill(Color.RED);
            } else if (p == from) {
                p.setFill(Color.BLUE);
                from = null;
            } else if (p == to) {
                p.setFill(Color.BLUE);
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
            } else if (listGraph.getEdgeBetween(to, from) == null) {
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("No connection established");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            } else {
                try {
                    Edge edge = listGraph.getEdgeBetween(to, from);
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
            } else if (listGraph.getEdgeBetween(to, from) == null) {
                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setContentText("No connection established");
                errorMsg.setHeaderText("");
                errorMsg.setTitle("Error!");
                errorMsg.show();
            } else {
                try {
                    String transport = listGraph.getEdgeBetween(from, to).getName();
                    int newTime = listGraph.getEdgeBetween(from, to).getWeight();
                    ConnectionDialog dialog = new ConnectionDialog(from.getName(), to.getName(), transport, newTime);
                    dialog.setTimeField(newTime).setEditable(true);
                    dialog.showAndWait();
                    listGraph.setConnectionWeight(from, to, dialog.getTime());
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
            out.println("file:europa.gif");

            for(Object object : outputArea.getChildren()){
                if(object instanceof Node){
                    Node place = (Node) object;
                    out.print(place.getName() + ";" + place.getX() + ";" + place.getY() + ";");
                }
            }

            out.print("\n");

            for(Object name : listGraph.getNodes()){
                for(Object destination : listGraph.getNodes()){
                    if(listGraph.getEdgeBetween(name,destination) !=null){
                        Edge connection = listGraph.getEdgeBetween(name,destination);
                        out.println(name + ";" + connection.getDestinationString() + ";" + connection.getName()
                                + ";" + connection.getWeight());
                    }
                }
            }

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

    private void load() {
        try {
            from = null;
            to = null;

            ArrayList<String> connections = new ArrayList<>();
            ArrayList<Node> places = new ArrayList<>();
            FileReader file = new FileReader("europa.graph");
            BufferedReader in = new BufferedReader(file);

            String line = in.readLine();
            showImage(line);

            line = in.readLine();

            String[] splits = line.split(";");
            for (int i = 0; i < splits.length; ) {
                String name = splits[i];
                double x = Double.parseDouble(splits[i + 1]);
                double y = Double.parseDouble(splits[i + 2]);


                Node place = new Node(x, y, name);
                listGraph.add(place);
                place.setId(place.getName());
                places.add(place);

                Text text = new Text(name);
                text.setX(x);
                text.setY(y + 30);
                text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                outputArea.getChildren().addAll(place, text);
                place.setOnMouseClicked(new PlaceClickHandler());
                i = i + 3;
            }

            while ((line = in.readLine()) != null) {
                connections.add(line);
            }

            for (int i = 0; i < connections.size(); i++) {
                String[] edges = connections.get(i).split(";");

                if (listGraph.getEdgeBetween(getPlace(places,edges[1]), getPlace(places,edges[0])) == null) {
                    Node fromPlace = getPlace(places, edges[0]);
                    Node toPlace = getPlace(places, edges[1]);

                    listGraph.connect(fromPlace, toPlace, edges[2], Integer.parseInt(edges[3]));

                    Line connectionLine = new Line(fromPlace.getX(), fromPlace.getY(), toPlace.getX(), toPlace.getY());
                    connectionLine.setStrokeWidth(3);
                    connectionLine.setDisable(true);
                    outputArea.getChildren().add(connectionLine);

                }
            }
            file.close();
            in.close();

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

    class ExitItemHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    class ExitHandler implements EventHandler<WindowEvent>{
        @Override
        public void handle(WindowEvent event){
            if(edited){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Unsaved changes, exit anyways?");
                alert.setContentText("");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() != ButtonType.OK){
                    event.consume();
                }
            }
        }
    }

    private Node getPlace(ArrayList<Node> places, String name){
        for(Node place : places){
            if(place.getName().equals(name)){
                return place;
            }
        }
        return null;
    }

}
