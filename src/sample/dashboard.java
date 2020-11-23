package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class dashboard
{
    static Connection con;


    public static void star(Stage primaryStage,Scene scene) throws Exception
    {

        String styles =
                "-fx-font-size:25px;" +
                        "-fx-padding:10px;" +
                        " -fx-background-color : #C6DEF1;"+
                        "-fx-font-color:#FDFCDC;";


        //String color = ;
        Image LogoImage=new Image("https://p.kindpng.com/picc/s/450-4502425_new-stop-and-shop-logo-hd-png-download.png");
        ImageView LogoimageView=new ImageView(LogoImage);
        LogoimageView.setFitHeight(40);
        LogoimageView.setFitWidth(80);
        LogoimageView.setPreserveRatio(true);

        MenuBar leftBar = new MenuBar();

//        leftBar.setPrefHeight(40);
        Menu logoitem = new Menu("",LogoimageView);
        leftBar.getMenus().add(logoitem);
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
//        rightBar.setPrefHeight(40);
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        spacer.setStyle(styles);

        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(leftBar, spacer, rightBar);
        menubars.setPrefWidth(1600);
        menubars.setPrefWidth(40);
        menubars.setStyle(styles);

        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(70));
        tilePane.setVgap(20);
        tilePane.setHgap(20);
        tilePane.setPrefColumns(2);
        //tilePane.setStyle("-fx-background-color:#c6e79d;");
        //tilePane.setStyle("-fx-background-color:#c3b2d8;");
        tilePane.setStyle("-fx-background-color:black;");

        con=Database_Connection.getInstance().con;
        Statement stmt = con.createStatement();
        String query = "select product_name,price,product_img from product;";
        ResultSet resultSet = stmt.executeQuery(query);
        String prod_names[]=new String[30];
        String prod_prices[]=new String[30];
        String prod_imgs[]=new String[30];
        int i=0;
        while(resultSet.next()) {
            prod_names[i]=resultSet.getString("product_name");
            prod_prices[i]=resultSet.getString("price");
            prod_imgs[i]=resultSet.getString("product_img");
            i++;

        }
        VBox tiles[] = new VBox[50];
        for (i = 0; i < 30; i++)
        {

            Image image = new Image(prod_imgs[i]);
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(250);
            imageView.setFitHeight(250);
            imageView.setSmooth(true);
            imageView.setCache(true);
            imageView.setPreserveRatio(true);
            HBox hbxImg = new HBox();
            hbxImg.setAlignment(Pos.CENTER);
            hbxImg.getChildren().add(imageView);

            Text text_1 = new Text(prod_names[i]);
            TextFlow prod_name = new TextFlow(text_1);
            text_1.setFont(Font.font("Verdana", FontPosture.ITALIC ,13));
            Label price = new Label("Price:"+prod_prices[i]);
            price.setAlignment(Pos.CENTER_LEFT);
            price.setTranslateY(20);
            price.setFont(Font.font(13));
            //Label desc = new Label("Description"+"Awesome shirt");
            //Label size = new Label("20");
            Button add_to_cart = new Button("Add to Cart");
            add_to_cart.setStyle("-fx-background-color: #D9C6F1;-fx-border-color: black;");
            add_to_cart.setTranslateY(20);

            tiles[i] = new VBox(10,hbxImg,prod_name,price,add_to_cart);
            //tiles[i].setAlignment(Pos.CENTER);
            tiles[i].setStyle("-fx-border-color: black;-fx-background-color:#e8dcf6;");
            //
            tiles[i].setPrefWidth(200);
            tiles[i].setPrefHeight(250);
            tiles[i].setPadding(new Insets(10,10,50,10));
            tilePane.getChildren().add(tiles[i]);
        }
        tilePane.setAlignment(Pos.CENTER);

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