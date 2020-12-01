package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class Cart
{
    static Connection con;
    public static void cart(Stage primaryStage,Scene rootscene,String username) throws Exception
    {
         con=Database_Connection.getInstance().con;
        Statement stmt = con.createStatement();
        String inCartquery = "select product_id,count(*) as qty from in_cart where username='"+username+"' group by product_id;";
        ResultSet inCartResult=stmt.executeQuery(inCartquery);
        int CartProd=0;
        while(inCartResult.next())
        {
            CartProd+=1;
        }

        String prod_id[]=new String[CartProd];
        int prod_qty[]=new int[CartProd];
        int pos=0;
        ResultSet inCartResult1=stmt.executeQuery(inCartquery);


        while(inCartResult1.next())
        {
            String id=inCartResult1.getString("product_id");
            int quant=inCartResult1.getInt("qty");
            prod_id[pos]=id;
            System.out.println(prod_id[pos]);
            prod_qty[pos]=quant;
            System.out.println(prod_qty[pos]);
            pos+=1;

        }
        Label Header = new Label("YOUR CART");
        Button back=new Button("Back");
        back.setStyle("-fx-font-size:15px;");
        Header.setStyle("-fx-font-size:30px;-fx-background-color:#0a043c;");
        Header.setTextFill(Color.WHITE);
        HBox CartHeader= new HBox(20,back,Header);
        CartHeader.setAlignment(Pos.TOP_CENTER);
        back.setAlignment(Pos.BASELINE_LEFT);
        back.setTranslateX(-520);
        Header.setAlignment(Pos.CENTER);
        CartHeader.setPadding(new Insets(20));



        String prod_names[]=new String[CartProd];
        String prod_prices[]=new String[CartProd];
        String prod_imgs[]=new String[CartProd];
        String prod_descp[]=new String[CartProd];


        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(90));
        tilePane.setVgap(70);
        //tilePane.setHgap(20);
        tilePane.setPrefColumns(1);
        tilePane.setStyle("-fx-background-color:#0a043c;");
        HBox tiles[] = new HBox[CartProd];
        Statement statement= con.createStatement();
        Button removeButton[] = new Button[CartProd];
        Label removeLabel[]=new Label[CartProd];
        //int total_amt=0;
        for(int i=0;i<CartProd;i++)
        {
            String Cquery = "select product_id,product_name,price,product_img,description from product where product_id='"+prod_id[i]+"';";
            ResultSet CresultSet = statement.executeQuery(Cquery);
            CresultSet.next();
            String pid=CresultSet.getString("product_id");
            String pname=CresultSet.getString("product_name");
            String pprice=CresultSet.getString("price");
            String pimg=CresultSet.getString("product_img");
            String pdes=CresultSet.getString("description");

            Image image = new Image(pimg);
            //Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/E1266601_%285398889640%29.jpg/1200px-E1266601_%285398889640%29.jpg");

            ImageView imageView = new ImageView();
            imageView.setImage(image);
            //imageView.setPreserveRatio(true);
            imageView.setFitWidth(150);
            imageView.setFitHeight(200);
            imageView.setSmooth(true);
            VBox vbxImg = new VBox(5);
            vbxImg.setAlignment(Pos.BASELINE_LEFT);
            Text text_1 = new Text(pname);
            TextFlow prod_name = new TextFlow(text_1);
            text_1.setFont(Font.font("Verdana", FontPosture.ITALIC ,16));
            vbxImg.getChildren().addAll(imageView,prod_name);

            VBox details =new VBox(10);
            Label prod_price = new Label("Price: ₹"+pprice);
            prod_price.setStyle("-fx-font-size:15");
            Text text_2 = new Text("Description:"+pdes);
            TextFlow description = new TextFlow(text_2);
            description.setStyle("-fx-font-size:15");
            Label quantity = new Label("Quantity:"+prod_qty[i]);
            quantity.setStyle("-fx-font-size:15");
            //Button removeProduct=new Button("Remove from cart");
            removeLabel[i]=new Label("");
            removeButton[i] = new Button("Remove from Cart");
            details.getChildren().addAll(description,prod_price,quantity,removeButton[i],removeLabel[i]);

            int finalI = i;
            removeButton[i].setOnAction(actionEvent ->{
                try {
                    for(int j = 0; j<prod_qty[finalI]; j++)
                    {
                        Statement statement1 = con.createStatement();
                        System.out.println("hi");
                        String query="delete from in_cart where product_id='"+pid+"' and username='"+username+"';";

                        statement1.execute(query);
                    }
                    removeLabel[finalI].setText("Product has been deleted from cart");
                    removeLabel[finalI].setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                    removeLabel[finalI].setTextFill(Color.BLACK);




                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }



            } );

            tiles[i] = new HBox(vbxImg,details);
            tiles[i].setAlignment(Pos.CENTER);
            tiles[i].setTranslateX(70);
            tiles[i].setStyle("-fx-border-color: black;-fx-background-color:#e8dcf6;");
            tiles[i].setPrefWidth(1000);
            tiles[i].setPrefHeight(220);
            tiles[i].setPadding(new Insets(10,10,50,10));
            tilePane.getChildren().add(tiles[i]);


        }
        //tilePane.setAlignment(Pos.CENTER);
        tilePane.setOrientation(Orientation.HORIZONTAL);
//        tilePane.setPrefTileHeight(100);
//        tilePane.setPrefTileWidth(500);
        Button place_order=new Button("PLACE ORDER");
        place_order.setAlignment(Pos.CENTER);

        Label message=new Label("");
        VBox place=new VBox(5,place_order,message);
        place.setStyle("-fx-background-color:#0a043c;");
        place.setAlignment(Pos.CENTER);
        place.setPadding(new Insets(20));

        VBox box=new VBox();
        if(CartProd==0){
            VBox no_items=new VBox(50);
            Label zero_items=new Label("There are no items in your cart!");
            zero_items.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
            zero_items.setTextFill(Color.WHITE);
            no_items.getChildren().add(zero_items);
            no_items.setAlignment(Pos.CENTER);
            no_items.setPadding(new Insets(100));
            box.getChildren().addAll(CartHeader,no_items);

        }
        else{
            box.getChildren().addAll(CartHeader,tilePane,place);
        }
        box.setAlignment(Pos.TOP_CENTER);


        box.setStyle("-fx-background-color:#0a043c;");
        ScrollPane sp1 = new ScrollPane();
        sp1.setFitToWidth(true);
        sp1.setFitToHeight(true);
        ScrollBar scroll = new ScrollBar();
        scroll.setMin(0);
        sp1.setContent(box);
        Scene nscene = new Scene(sp1);
        primaryStage.setScene(nscene);
        primaryStage.setMaximized(true);
        nscene.setRoot(sp1);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                //rootscene.setRoot(sp);
                primaryStage.setScene(rootscene);
                primaryStage.show();


            }
        });
        place_order.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    dashboard.place_order();
                    message.setText("YOUR ORDER HAS BEEN PLACED!");
                    message.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                    message.setTextFill(Color.WHITE);
                    //TimeUnit.SECONDS.sleep(5);
                    //primaryStage.setScene(rootscene);
                    //primaryStage.show();



                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }
}