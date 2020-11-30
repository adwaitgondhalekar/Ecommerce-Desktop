package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

public class Cart
{
    public static void cart(Stage primaryStage) throws Exception
    {
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(70));
        tilePane.setVgap(20);
        tilePane.setHgap(20);
        tilePane.setPrefColumns(2);
        tilePane.setStyle("-fx-background-color:black;");
        HBox tiles[] = new HBox[50];
        for(int i=0;i<50;i++)
        {
            //Image image = new Image(prod_imgs[i]);
            Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/E1266601_%285398889640%29.jpg/1200px-E1266601_%285398889640%29.jpg");

            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(250);
            imageView.setFitHeight(250);
            imageView.setSmooth(true);
            imageView.setCache(true);
            imageView.setPreserveRatio(true);
            VBox vbxImg = new VBox();
            vbxImg.setAlignment(Pos.CENTER);
            Label name = new Label("Name:");
            vbxImg.getChildren().addAll(imageView,name);

            VBox details =new VBox();
            Label price = new Label("Price:");
            Label description = new Label("Description:");
            Label quantity = new Label("Quantity:");
            details.getChildren().addAll(description,price,quantity);

            tiles[i] = new HBox(10,vbxImg,details);
            //tiles[i].setAlignment(Pos.CENTER);
            tiles[i].setStyle("-fx-border-color: black;-fx-background-color:#e8dcf6;");
//            tiles[i].setPrefWidth(200);
//            tiles[i].setPrefHeight(250);
            tiles[i].setPadding(new Insets(10,10,50,10));
            tilePane.getChildren().add(tiles[i]);


        }
        tilePane.setAlignment(Pos.CENTER);
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        ScrollBar scroll = new ScrollBar();
        scroll.setMin(0);
        sp.setContent(tilePane);
        Scene nscene =new Scene(sp, 800, 600);
        primaryStage.setScene(nscene);
        primaryStage.setMaximized(true);
        nscene.setRoot(sp);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}