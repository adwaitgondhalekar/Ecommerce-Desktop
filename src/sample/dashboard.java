package sample;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.sql.SQLException;
import java.sql.Statement;

public class dashboard
{
    static Connection con;


    public static void star(Stage primaryStage,Scene scene) throws Exception
    {

        String styles =
                "-fx-font-size:25px;" +
                        "-fx-padding:10px;" +
                       // " -fx-background-color : #C6DEF1;"+
                        " -fx-background-color : #b2c7d8;"+
                        "-fx-font-color:#FDFCDC;";
        String hoverstyle= "-fx-background-color: transparent;";

        String filterstyle =
                "-fx-font-size:15px;" +
                        "-fx-padding:10px;" +
                        " -fx-background-color : #b2c7d8;"+
                        "-fx-font-color:#FDFCDC;";
        String menuitemstyle= "-fx-font-size:15px;";

        Image LogoImage=new Image("https://p.kindpng.com/picc/s/450-4502425_new-stop-and-shop-logo-hd-png-download.png");
        ImageView LogoimageView=new ImageView(LogoImage);
        LogoimageView.setFitHeight(40);
        LogoimageView.setFitWidth(80);
        LogoimageView.setPreserveRatio(true);

        MenuBar leftBar = new MenuBar();

//        leftBar.setPrefHeight(40);
        Menu logoitem = new Menu("",LogoimageView);
        leftBar.getMenus().add(logoitem);
        logoitem.setStyle(hoverstyle);

        Menu men = new Menu("MEN");
        men.setStyle(hoverstyle);
        MenuItem shirts=new MenuItem("SHIRTS");
        MenuItem trousers=new MenuItem("TROUSERS");
        shirts.setStyle(menuitemstyle);
        trousers.setStyle(menuitemstyle);
        men.getItems().addAll(shirts,trousers);


        Menu women = new Menu("WOMEN");
        women.setStyle(hoverstyle);
        leftBar.setStyle(styles);
        MenuItem tops=new MenuItem("TOPS");
        MenuItem dresses=new MenuItem("DRESSES");
        tops.setStyle(menuitemstyle);
        dresses.setStyle(menuitemstyle);
        women.getItems().addAll(tops,dresses);

        leftBar.getMenus().add(men);
        leftBar.getMenus().add(women);

        MenuBar rightBar = new MenuBar();
        Image CartImg=new Image("http://www.pngmart.com/files/7/Cart-PNG-Clipart.png");
        ImageView CartImageView=new ImageView(CartImg);
        CartImageView.setFitHeight(40);
        CartImageView.setFitWidth(80);
        CartImageView.setPreserveRatio(true);
        Menu cartItem = new Menu("",CartImageView);
        rightBar.getMenus().add(cartItem);
        cartItem.setStyle(hoverstyle);

        Menu account = new Menu("ACCOUNT");
        account.setStyle(hoverstyle);
        MenuItem orders=new MenuItem("ORDERS");
        MenuItem signout=new MenuItem("SIGN OUT");
        orders.setStyle(menuitemstyle);
        signout.setStyle(menuitemstyle);
        account.getItems().addAll(orders,signout);
        rightBar.getMenus().add(account);

        rightBar.setStyle(styles);
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        spacer.setStyle(styles);

        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(leftBar, spacer, rightBar);
        menubars.setPrefWidth(1600);
        menubars.setPrefWidth(40);
        menubars.setStyle(styles);

//        ObservableList<String> options =
//                FXCollections.observableArrayList(
//                        "Price:Low to High",
//                        "Price:High to Low"
//                );
//
//        final ComboBox filterBox = new ComboBox(options);
//        filterBox.setValue("Filter By");
//        HBox filter = new HBox(filterBox);
//        filterBox.setTranslateX(1100);
//        filter.setStyle(" -fx-background-color : black;");
//        filterBox.setStyle(filterstyle);
//        filter.setPadding(new Insets(20));

        MenuButton filterButton = new MenuButton("Filter By");
        MenuItem HtoL=new MenuItem("High to Low");
        MenuItem LtoH=new MenuItem("Low to High");
        filterButton.getItems().addAll(HtoL,LtoH);
        HBox filter = new HBox(filterButton);
        filterButton.setTranslateX(1100);
        filter.setStyle(" -fx-background-color : black;");
        filterButton.setStyle(filterstyle);
        filter.setPadding(new Insets(20));


        con=Database_Connection.getInstance().con;
        Statement stmt = con.createStatement();
        String query = "select product_name,price,product_img from product ;";
        ResultSet resultSet = stmt.executeQuery(query);
        String prod_names[]=new String[70];
        String prod_prices[]=new String[70];
        String prod_imgs[]=new String[70];
        int num=0;
        while(resultSet.next()) {
            prod_names[num]=resultSet.getString("product_name");
            prod_prices[num]=resultSet.getString("price");
            prod_imgs[num]=resultSet.getString("product_img");
            num++;

        }


        Statement Shirtstmt = con.createStatement();
        String Shirtquery = "select product_name,price,product_img from product where product_category='shirts';";
        ResultSet ShirtresultSet = Shirtstmt.executeQuery(Shirtquery);
        String Shirt_names[]=new String[70];
        String Shirt_prices[]=new String[70];
        String Shirt_imgs[]=new String[70];
        int Shirtnum=0;
        while(ShirtresultSet.next()) {
            Shirt_names[Shirtnum]=ShirtresultSet.getString("product_name");
            Shirt_prices[Shirtnum]=ShirtresultSet.getString("price");
            Shirt_imgs[Shirtnum]=ShirtresultSet.getString("product_img");
            Shirtnum++;
        }
        int finalShirtnum = Shirtnum;
        shirts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                display_products(primaryStage,Shirt_names,Shirt_prices,Shirt_imgs, finalShirtnum,menubars,filter);
            }
        });

        Statement Trstmt = con.createStatement();
        String Trquery = "select product_name,price,product_img from product where product_category='trousers';";
        ResultSet TrresultSet = Trstmt.executeQuery(Trquery);
        String Tr_names[]=new String[70];
        String Tr_prices[]=new String[70];
        String Tr_imgs[]=new String[70];
        int Trnum=0;
        while(TrresultSet.next()) {
            Tr_names[Trnum]=TrresultSet.getString("product_name");
            Tr_prices[Trnum]=TrresultSet.getString("price");
            Tr_imgs[Trnum]=TrresultSet.getString("product_img");
            Trnum++;
        }
        int finalTrnum = Trnum;
        trousers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent actionEvent) {
                display_products(primaryStage,Tr_names,Tr_prices,Tr_imgs, finalTrnum,menubars,filter);

            }
        });

        Statement Tpstmt = con.createStatement();
        String Tpquery = "select product_name,price,product_img from product where product_category='tops';";
        ResultSet TpresultSet = Tpstmt.executeQuery(Tpquery);
        String Tp_names[]=new String[70];
        String Tp_prices[]=new String[70];
        String Tp_imgs[]=new String[70];
        int Tpnum=0;
        while(TpresultSet.next()) {
            Tp_names[Tpnum]=TpresultSet.getString("product_name");
            Tp_prices[Tpnum]=TpresultSet.getString("price");
            Tp_imgs[Tpnum]=TpresultSet.getString("product_img");
            Tpnum++;
        }
        int finalTpnum = Tpnum;
        tops.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent actionEvent) {
                display_products(primaryStage,Tp_names,Tp_prices,Tp_imgs, finalTpnum,menubars,filter);

            }
        });

        Statement Drstmt = con.createStatement();
        String Drquery = "select product_name,price,product_img from product where product_category='dresses';";
        ResultSet DrresultSet = Drstmt.executeQuery(Drquery);
        String Dr_names[]=new String[70];
        String Dr_prices[]=new String[70];
        String Dr_imgs[]=new String[70];
        int Drnum=0;
        while(DrresultSet.next()) {
            Dr_names[Drnum]=DrresultSet.getString("product_name");
            Dr_prices[Drnum]=DrresultSet.getString("price");
            Dr_imgs[Drnum]=DrresultSet.getString("product_img");
            Drnum++;
        }
        int finalDrnum = Drnum;
        dresses.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent actionEvent) {
                display_products(primaryStage,Dr_names,Dr_prices,Dr_imgs, finalDrnum,menubars,filter);

            }
        });

        Statement HtoLstmt = con.createStatement();
        String HtoLquery = "select product_name,price,product_img from product order by price DESC;";
        ResultSet HtoLresultSet = HtoLstmt.executeQuery(HtoLquery);
        String HtoL_names[]=new String[70];
        String HtoL_prices[]=new String[70];
        String HtoL_imgs[]=new String[70];
        int HtoLnum=0;
        while(HtoLresultSet.next()) {
            HtoL_names[HtoLnum]=HtoLresultSet.getString("product_name");
            HtoL_prices[HtoLnum]=HtoLresultSet.getString("price");
            HtoL_imgs[HtoLnum]=HtoLresultSet.getString("product_img");
            HtoLnum++;
        }
        int finalHtoLnum = HtoLnum;
        HtoL.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent actionEvent) {
                display_products(primaryStage,HtoL_names,HtoL_prices,HtoL_imgs, finalHtoLnum,menubars,filter);

            }
        });

        Statement LtoHstmt = con.createStatement();
        String LtoHquery = "select product_name,price,product_img from product order by price;";
        ResultSet LtoHresultSet = LtoHstmt.executeQuery(LtoHquery);
        String LtoH_names[]=new String[70];
        String LtoH_prices[]=new String[70];
        String LtoH_imgs[]=new String[70];
        int LtoHnum=0;
        while(LtoHresultSet.next()) {
            LtoH_names[LtoHnum]=LtoHresultSet.getString("product_name");
            LtoH_prices[LtoHnum]=LtoHresultSet.getString("price");
            LtoH_imgs[LtoHnum]=LtoHresultSet.getString("product_img");
            LtoHnum++;
        }
        int finalLtoHnum = LtoHnum;
        LtoH.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent actionEvent) {
                display_products(primaryStage,LtoH_names,LtoH_prices,LtoH_imgs, finalLtoHnum,menubars,filter);

            }
        });





        display_products(primaryStage,prod_names,prod_prices,prod_imgs,num,menubars,filter);

    }
    public static void display_products(Stage primaryStage, String prod_names[], String prod_prices[], String prod_imgs[], int pcount,HBox menubars,HBox filter){

        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(70));
        tilePane.setVgap(20);
        tilePane.setHgap(20);
        tilePane.setPrefColumns(2);
        tilePane.setStyle("-fx-background-color:black;");

        VBox tiles[] = new VBox[pcount];
        for (int i = 0; i <pcount; i++)
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
            Button add_to_cart = new Button("Add to Cart");
            add_to_cart.setStyle("-fx-background-color: #a29aac;-fx-border-color: black;-fx-hovered-background:white");
            add_to_cart.setTranslateY(20);

            tiles[i] = new VBox(10,hbxImg,prod_name,price,add_to_cart);
            //tiles[i].setAlignment(Pos.CENTER);
            tiles[i].setStyle("-fx-border-color: black;-fx-background-color:#e8dcf6;");
            tiles[i].setPrefWidth(200);
            tiles[i].setPrefHeight(250);
            tiles[i].setPadding(new Insets(10,10,50,10));
            tilePane.getChildren().add(tiles[i]);
        }
        tilePane.setAlignment(Pos.CENTER);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().add(menubars);
        vbox.getChildren().add(filter);
        vbox.getChildren().add(tilePane);
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        ScrollBar scroll = new ScrollBar();
        scroll.setMin(0);
        sp.setContent(vbox);
        Scene nscene =new Scene(sp, 800, 600);
        primaryStage.setScene(nscene);
        primaryStage.setMaximized(true);
        nscene.setRoot(sp);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}