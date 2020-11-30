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
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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




        String prod_names[]=new String[CartProd];
        String prod_prices[]=new String[CartProd];
        String prod_imgs[]=new String[CartProd];
        String prod_descp[]=new String[CartProd];
        Button back=new Button("Back");
        HBox hbBack=new HBox(back);
        back.setAlignment(Pos.CENTER_LEFT);
        hbBack.setAlignment(Pos.TOP_CENTER);




        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(50));
        tilePane.setVgap(70);
        tilePane.setHgap(20);
        tilePane.setPrefColumns(1);
        tilePane.setStyle("-fx-background-color:black;");
        HBox tiles[] = new HBox[CartProd];
        Statement statement= con.createStatement();
        //int total_amt=0;
        for(int i=0;i<CartProd;i++)
        {
            String Cquery = "select product_name,price,product_img,description from product where product_id='"+prod_id[i]+"';";

            ResultSet CresultSet = statement.executeQuery(Cquery);
            CresultSet.next();
            String pname=CresultSet.getString("product_name");
            String pprice=CresultSet.getString("price");
            String pimg=CresultSet.getString("product_img");
            String pdes=CresultSet.getString("description");

            Image image = new Image(pimg);
            //Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/E1266601_%285398889640%29.jpg/1200px-E1266601_%285398889640%29.jpg");

            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(200);
            imageView.setSmooth(true);
            imageView.setCache(true);
            imageView.setPreserveRatio(true);
            VBox vbxImg = new VBox();
            vbxImg.setAlignment(Pos.CENTER_LEFT);
            Text text_1 = new Text(pname);
            TextFlow prod_name = new TextFlow(text_1);
            text_1.setFont(Font.font("Verdana", FontPosture.ITALIC ,13));
            vbxImg.getChildren().addAll(imageView,prod_name);

            VBox details =new VBox();
            Label prod_price = new Label("Price:"+pprice);
            Label description = new Label("Description:"+pdes);
            Label quantity = new Label("Quantity:"+prod_qty[i]);
            details.getChildren().addAll(description,prod_price,quantity);

            tiles[i] = new HBox(10,vbxImg,details);
            tiles[i].setAlignment(Pos.CENTER);
            tiles[i].setStyle("-fx-border-color: black;-fx-background-color:#e8dcf6;");
            tiles[i].setPrefWidth(1000);
            tiles[i].setPrefHeight(100);
            tiles[i].setPadding(new Insets(10,10,50,10));
            tilePane.getChildren().add(tiles[i]);


        }
        //tilePane.setAlignment(Pos.CENTER);
        tilePane.setOrientation(Orientation.HORIZONTAL);
//        tilePane.setPrefTileHeight(100);
//        tilePane.setPrefTileWidth(500);
        Button place_order=new Button("PLACE ORDER");
        HBox place=new HBox(place_order);

        VBox box=new VBox();
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(hbBack,tilePane,place);




        ScrollPane sp1 = new ScrollPane();
        sp1.setFitToWidth(true);
        sp1.setFitToHeight(true);
        ScrollBar scroll = new ScrollBar();
        scroll.setMin(0);
        sp1.setContent(box);
        Scene nscene =new Scene(sp1, 800, 600);
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
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }
}