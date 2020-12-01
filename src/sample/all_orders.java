package sample;

import com.mysql.cj.protocol.Resultset;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class all_orders {
    static Connection con;
    public static void display_orders(Stage stage,String username,Scene rootscene) throws SQLException {
        con=Database_Connection.getInstance().con;
        Statement statement=con.createStatement();
        String O_query="Select order_id,tot_amt,timestamp from orders where username='"+username+"' order by timestamp desc;";
        ResultSet resultset=statement.executeQuery(O_query);
        int no_of_orders=0;
        while(resultset.next())
        {
            no_of_orders+=1;

        }
        String order_id[]=new String[no_of_orders];
        int tot_amt[]=new int[no_of_orders];
        String timestamps[]=new String[no_of_orders];
        int pos=0;
        ResultSet resultSet1=statement.executeQuery(O_query);
        while (resultSet1.next())
        {
            order_id[pos]=resultSet1.getString("order_id");
            tot_amt[pos]=resultSet1.getInt("tot_amt");
            timestamps[pos]=resultSet1.getString("timestamp");
            pos+=1;
        }

       // Scene scene = new Scene(new Group());
        //stage.setTitle("Table View Sample");



        Label Header = new Label("ALL ORDERS");
        Button back=new Button("Back");
        back.setStyle("-fx-font-size:15px;");
        Header.setStyle("-fx-font-size:30px;-fx-background-color:#0a043c;");
        Header.setTextFill(Color.WHITE);
        HBox OrderHeader= new HBox(20,back,Header);
        OrderHeader.setAlignment(Pos.TOP_CENTER);
        back.setAlignment(Pos.BASELINE_LEFT);
        back.setTranslateX(-520);
        Header.setAlignment(Pos.CENTER);
        OrderHeader.setPadding(new Insets(20));

        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(50));
        tilePane.setVgap(70);
        tilePane.setHgap(20);
        tilePane.setPrefColumns(1);

        Statement statement1= con.createStatement();
        VBox tiles[] = new VBox[no_of_orders];
        for(int i=0;i<no_of_orders;i++)
        {
            Label OrderLabel = new Label("Order id: "+order_id[i]);
            OrderLabel.setStyle("-fx-font-size:15px;-fx-font-weight: bold");
            Label Timestamp = new Label(timestamps[i]);
            Timestamp.setStyle("-fx-font-weight: bold");


            TableView tableView = new TableView();

            TableColumn<Map, String> pIdColumn = new TableColumn<>("Product id");
            pIdColumn.setCellValueFactory(new MapValueFactory<>("Product_id"));
            TableColumn<Map, String> pNameColumn = new TableColumn<>("Product Name");
            pNameColumn.setCellValueFactory(new MapValueFactory<>("Product_Name"));
            TableColumn<Map, String> pQtyColumn = new TableColumn<>("Quantity");
            pQtyColumn.setCellValueFactory(new MapValueFactory<>("Quantity"));
            TableColumn<Map, String> pCostColumn = new TableColumn<>("Price");
            pCostColumn.setCellValueFactory(new MapValueFactory<>("Price"));

            tableView.getColumns().add(pIdColumn);
            tableView.getColumns().add(pNameColumn);
            tableView.getColumns().add(pQtyColumn);
            tableView.getColumns().add(pCostColumn);

            String get_pid="select  product_id,count(*) as pqty from contains where order_id='"+order_id[i]+ "'group by product_id;";
            ResultSet prod_id_list = statement1.executeQuery(get_pid);
            int pcount=0;
            while(prod_id_list.next())
            {
                pcount+=1;
            }
            //System.out.println("Count="+pcount);
            String[] product_id = new String[pcount];
            int[] pqty =new int[pcount];
            int position=0;
            Statement products=con.createStatement();
            ResultSet prod_id_list1 = products.executeQuery(get_pid);
            while (prod_id_list1.next()) //create pid  and quantity array
            {

                String prod_id = prod_id_list1.getString("product_id");
                int quant = prod_id_list1.getInt("pqty");
                product_id[position]=prod_id;
                pqty[position]=quant;
                //System.out.println(prod_id);
                //System.out.println(quant);
                position+=1;
            }

            Statement statement2=con.createStatement();
            for(int p=0;p<pcount;p++)
            {
                String Pquery="select product_name,price from product where product_id='"+product_id[p]+"';";
                //System.out.println(Pquery);
                ResultSet CresultSet = statement2.executeQuery(Pquery);
                CresultSet.next();
                String pname=CresultSet.getString("product_name");
                String pprice=CresultSet.getString("price");
                ObservableList<Map<String, Object>> items =
                        FXCollections.<Map<String, Object>>observableArrayList();
                Map<String, Object> item = new HashMap<>();
                item.put("Product_id", product_id[p]);
                item.put("Product_Name" , pname);
                item.put("Quantity", pqty[p]);
                item.put("Price" , pprice);
                items.add(item);
                tableView.getItems().add(item);
            }
            Label TotalPriceLabel = new Label("Total Amount: ₹ "+Integer.toString(tot_amt[i]));
            TotalPriceLabel.setStyle("-fx-font-size:15px;-fx-font-weight: bold");
            TotalPriceLabel.setAlignment(Pos.BASELINE_RIGHT);


            tiles[i] = new VBox(10,OrderLabel,Timestamp,tableView,TotalPriceLabel);
            tiles[i].setAlignment(Pos.CENTER);
            tiles[i].setStyle("-fx-border-color: black;-fx-background-color:#e8dcf6;");
            tiles[i].setPrefWidth(900);
            tiles[i].setPrefHeight(300);
            tiles[i].setPadding(new Insets(10,10,10,10));

            tiles[i].setSpacing(10);
            tilePane.getChildren().add(tiles[i]);
           // tiles[i].setPadding(new Insets(20, 0, 0, 20));

        }
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setOrientation(Orientation.HORIZONTAL);




        VBox vBox=new VBox();
        vBox.getChildren().addAll(OrderHeader,tilePane);
        vBox.setStyle("-fx-background-color:#0a043c;");
        //vBox.getChildren().addAll(heading,tableView);


        //((Group) scene.getRoot()).getChildren().addAll(tilePane);
        javafx.scene.control.ScrollPane sp1 = new ScrollPane();
        sp1.setFitToWidth(true);
        sp1.setFitToHeight(true);
        ScrollBar scroll = new ScrollBar();
        scroll.setMin(0);
        sp1.setContent(vBox);
        Scene nscene =new Scene(sp1, 800, 600);
        stage.setScene(nscene);
        nscene.setRoot(sp1);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.show();
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                //rootscene.setRoot(sp);
                stage.setScene(rootscene);
                stage.show();


            }
        });
    }
}
