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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.sql.*;

import static sample.Cart.cart;

public class dashboard
{
    static Connection con;

    static String usern;
    static String order_id;


    public static void star(Stage primaryStage,Scene scene,String username) throws Exception
    {
        usern=username;
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

        CartImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Cart.cart(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
//        cartItem.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                try {
//                    Cart.cart(primaryStage);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

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


        MenuButton filterButton = new MenuButton("Filter By Price");
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

        //String prod_ids[]=new String[90];

        String prod_names[]=new String[90];
        String prod_prices[]=new String[90];
        String prod_imgs[]=new String[90];
        int num=0;
        while(resultSet.next())
        {
            //prod_ids[num]=resultSet.getString("product_id");

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
                display_products(primaryStage,Shirt_names,Shirt_prices,Shirt_imgs, finalShirtnum,menubars,filter,username);

                HtoL.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String HtoLquery = "select product_name,price,product_img from product where product_category='shirts' order by price DESC ;";
                        try {
                            HightoLowFilter(primaryStage,menubars,filter,HtoLquery,username);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });
                LtoH.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String LtoHquery = "select product_name,price,product_img from product where product_category='shirts' order by price ;";
                        try {
                            LowToHighFilter(primaryStage,menubars,filter,LtoHquery,username);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });


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
                display_products(primaryStage,Tr_names,Tr_prices,Tr_imgs, finalTrnum,menubars,filter,username);

                HtoL.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String HtoLquery = "select product_name,price,product_img from product where product_category='trousers' order by price DESC ;";
                        try {
                            HightoLowFilter(primaryStage,menubars,filter,HtoLquery,username);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });
                LtoH.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String LtoHquery = "select product_name,price,product_img from product where product_category='trousers' order by price ;";
                        try {
                            LowToHighFilter(primaryStage,menubars,filter,LtoHquery,username);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });
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
                display_products(primaryStage,Tp_names,Tp_prices,Tp_imgs, finalTpnum,menubars,filter,username);
                HtoL.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String HtoLquery = "select product_name,price,product_img from product where product_category='tops' order by price DESC ;";
                        try {
                            HightoLowFilter(primaryStage,menubars,filter,HtoLquery,username);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });
                LtoH.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String LtoHquery = "select product_name,price,product_img from product where product_category='tops' order by price ;";
                        try {
                            LowToHighFilter(primaryStage,menubars,filter,LtoHquery,username);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });


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
                display_products(primaryStage,Dr_names,Dr_prices,Dr_imgs, finalDrnum,menubars,filter,username);
                HtoL.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String HtoLquery = "select product_name,price,product_img from product where product_category='dresses' order by price DESC ;";
                        try {
                            HightoLowFilter(primaryStage,menubars,filter,HtoLquery,username);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });
                LtoH.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String LtoHquery = "select product_name,price,product_img from product where product_category='dresses' order by price ;";
                        try {
                            LowToHighFilter(primaryStage,menubars,filter,LtoHquery,username);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });



            }
        });


        HtoL.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String HtoLquery = "select product_name,price,product_img from product order by price DESC;";
                    HightoLowFilter(primaryStage,menubars,filter,HtoLquery,username);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        LtoH.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent actionEvent) {
                try {
                    String LtoHquery = "select product_name,price,product_img from product order by price;";
                    LowToHighFilter(primaryStage,menubars,filter,LtoHquery,username);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });





        display_products(primaryStage,prod_names,prod_prices,prod_imgs,num,menubars,filter,username);

    }
    public static void display_products(Stage primaryStage, String prod_names[], String prod_prices[], String prod_imgs[], int pcount,HBox menubars,HBox filter,String username){

        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(70));
        tilePane.setVgap(20);
        tilePane.setHgap(20);
        tilePane.setPrefColumns(2);
        tilePane.setStyle("-fx-background-color:black;");

        VBox tiles[] = new VBox[pcount];
        Button add_to_cart_button[] = new Button[pcount];
        //final int[] lastClickedIndex = {-1};
        final int[] lastClickedIndex = {-1};

        for (int i = 0; i <pcount; i++)
        {
            final int buttonInd = i;

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

            HBox cartFunctions= new HBox(20);

            add_to_cart_button[i] = new Button("Add to Cart");

            add_to_cart_button[i].setStyle("-fx-background-color: #b38de3;-fx-border-color: black;-fx-hovered-background:white");
            add_to_cart_button[i].setTranslateY(20);
            Label quantity= new Label();
            final int[] count = {1};

            int finalI = i;


            add_to_cart_button[i].setOnAction(actionEvent ->{
                //lastClickedIndex[0] =buttonInd;
                //System.out.print("Button pressed "+((Button)actionEvent.getSource()).getText()+ lastClickedIndex[0]);
                //System.out.print("Button pressed "+((Button)actionEvent.getSource()).getText()+ finalI);
                for(int m=0;m<count[0];m++)
                {
                    try
                    {
                        Statement prodIdstmt = con.createStatement();
                        String prodIdquery = "select product_id from product where product_name='"+ prod_names[finalI]+"';";
                        ResultSet prodIdresultSet = prodIdstmt.executeQuery(prodIdquery);
                        String product_id;
                        prodIdresultSet.next();
                        product_id=prodIdresultSet.getString("product_id");
                        Statement Cartstmt = con.createStatement();
                        Cartstmt.executeUpdate("insert ignore into in_cart values('"+username+"','"+product_id+"');");


                    } catch (SQLException throwables)
                    {
                        throwables.printStackTrace();
                    }

                }

                try {
                    place_order();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


            } );

            HBox prodCount=new HBox();
            Button addProd=new Button("+");
            addProd.setStyle("-fx-border-color:grey;-fx-border-style: solid solid solid none;");
            Button reduceProd=new Button("- ");
            reduceProd.setTextAlignment(TextAlignment.CENTER);
            reduceProd.setStyle("-fx-border-color:grey;-fx-border-style: solid none solid solid;-fx-padding:5px 5px 3px 12px;");

            addProd.setOnAction((new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    count[0]++;
                    quantity.setText(Integer.toString(count[0]));
                }
            }));
            reduceProd.setOnAction((new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if(count[0]!=1)
                    {
                        count[0]--;
                        quantity.setText(Integer.toString(count[0]));
                    }
                    }
            }));
            int labelCount=count[0];


            quantity.setText(Integer.toString(labelCount));
            quantity.setStyle("-fx-border-color:grey;-fx-border-style: solid none solid none;-fx-padding:3px 3px 5px 3px;");
            quantity.setAlignment(Pos.CENTER);

            prodCount.getChildren().addAll(reduceProd,quantity,addProd);
            //prodCount.setTranslateX(65);
            prodCount.setTranslateY(20);

            cartFunctions.getChildren().addAll(add_to_cart_button[i],prodCount);

            tiles[i] = new VBox(10,hbxImg,prod_name,price,cartFunctions);
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
    public  static void HightoLowFilter(Stage primaryStage,HBox menubars,HBox filter,String filter_query,String username) throws SQLException {
        Statement HtoLstmt = con.createStatement();
        String HtoLquery= filter_query;
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
        display_products(primaryStage,HtoL_names,HtoL_prices,HtoL_imgs, finalHtoLnum,menubars,filter,username);
    }
    public  static void LowToHighFilter(Stage primaryStage,HBox menubars,HBox filter,String filter_query,String username) throws SQLException{
        Statement LtoHstmt = con.createStatement();
        String LtoHquery = filter_query;
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
        display_products(primaryStage,LtoH_names,LtoH_prices,LtoH_imgs, finalLtoHnum,menubars,filter,username);

        }
    public static void place_order() throws SQLException
    {
        Statement stmt=con.createStatement();
        String get_products = "select product_id,count(*) as qty from in_cart group by product_id";
        ResultSet prod_list = stmt.executeQuery(get_products);
        int count=0;

        while(prod_list.next())
        {
            count+=1;
        }
        System.out.println(count);
        String[] product_id = new String[count];
        int[] qty =new int[count];

        int pos=0;

        ResultSet prod_list1 = stmt.executeQuery(get_products);

        while (prod_list1.next())
        {

            String prod_id = prod_list1.getString("product_id");
            int quant = prod_list1.getInt("qty");

            product_id[pos]=prod_id;
            qty[pos]=quant;

            System.out.println(prod_id);
            System.out.println(quant);

            pos+=1;
        }



        Statement statement = con.createStatement();
        String find_order = "select order_id from orders where order_id regexp '^O_"+usern+"_';";

        ResultSet resultSet = statement.executeQuery(find_order);
        int order_no=1;

        while (resultSet.next())
        {
            String orderid = resultSet.getString("order_id");
            order_no+=1;

        }
        String s_order_no=Integer.toString(order_no);

        String f_order_id = "O_"+usern+"_"+s_order_no;

        Statement statement1 = con.createStatement();

        float total_amt=0;
        int total_prod=0;

        for(int i=0;i<product_id.length;i++)
        {
            String query = "select price from product where product_id='"+product_id[i]+"';";

            ResultSet resultSet1=statement1.executeQuery(query);

            float price=0;
            resultSet1.next();

            price = resultSet1.getFloat("price");

            price = price*qty[i];
            total_amt+=price;

        }
        for(int i=0;i<qty.length;i++)
        {
            total_prod+=qty[i];
        }

        Statement ins_order = con.createStatement();

        String ins_query = "insert into orders values('"+f_order_id+"','"+usern+"',"+total_amt+","+total_prod+");";

        ins_order.execute(ins_query);

        for(int i=0;i<qty.length;i++)
        {
            String contains_query = "insert into contains values('"+product_id[i]+"','"+f_order_id+"');";

            Statement statement2 = con.createStatement();

            statement2.execute(contains_query);
        }


        for(int i=0;i<qty.length;i++)
        {
            for(int j=0;j<qty[i];j++)
            {
                Statement statement2 = con.createStatement();
                String q = "delete from in_cart where username='" + usern + "' and product_id='" + product_id[i] + "';";
                statement2.execute(q);
            }
        }
















    }



}