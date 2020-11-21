package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;

import javafx.geometry.Insets;

public class dashboard
{
    public static void star(Stage primaryStage,Scene scene) throws Exception
    {
        String styles =
                "-fx-font-size:20px;" +
                        "-fx-padding:10px;" +
                        " -fx-background-color : #C6DEF1;" +
                        "-fx-font-color:#FDFCDC;";
        //String color = ;
        MenuBar leftBar = new MenuBar();
        leftBar.setPrefHeight(40);
        Menu men = new Menu("Men");
        men.getItems().addAll(new MenuItem("Shirts"), new MenuItem("Trousers"));
        Menu women = new Menu("Women");
        leftBar.setStyle(styles);
        women.getItems().addAll(new MenuItem("Tops"), new MenuItem("Dresses"));
        leftBar.getMenus().add(men);
        leftBar.getMenus().add(women);
        MenuBar rightBar = new MenuBar();
        rightBar.getMenus().addAll(new Menu("Cart"), new Menu("Sign out"));
        rightBar.setStyle(styles);
        rightBar.setPrefHeight(40);
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        spacer.setStyle(styles);

        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(leftBar, spacer, rightBar);
//        BorderPane root= new BorderPane();
        menubars.setPrefWidth(1600);
//        GridPane home_root = new GridPane();
//        home_root.add(menubars, 0, 0, 1, 1);


        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(50));
        tilePane.setVgap(20);
        tilePane.setHgap(20);
        tilePane.setPrefColumns(4);
        tilePane.setStyle("-fx-background-color: lightblue;");

        VBox tiles[] = new VBox[50];
        for (int i = 0; i < 50; i++)
        {
            ImageView imageView = new ImageView("https://images.unsplash.com/photo-1483985988355-763728e1935b?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
            imageView.setFitWidth(250);
            imageView.setFitHeight(250);
            imageView.setPreserveRatio(true);

            Label prod_name = new Label("Shirt");
            Label price = new Label("₹200");
            Label desc = new Label("Awesome shirt");
            Label size = new Label("20");
            Button add_to_cart = new Button("Add to Cart");

            tiles[i] = new VBox(imageView,prod_name,price,desc,size,add_to_cart);
            tiles[i].setStyle("-fx-border-color: black;");
            tiles[i].setPrefWidth(200);
            tiles[i].setPrefHeight(250);
            tiles[i].setPadding(new Insets(10,10,50,10));
            tilePane.getChildren().add(tiles[i]);


        }

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_CENTER);
        //vbox.setStyle("-fx-background-color: blue;");
        vbox.getChildren().add(menubars);
        vbox.getChildren().add(tilePane);

        StackPane stack = new StackPane();

        stack.getChildren().add(vbox);

        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        ScrollBar scroll = new ScrollBar();
        sp.setContent(stack);

        primaryStage.setScene(new Scene(sp, 800, 600));
        //primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}