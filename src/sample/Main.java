package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application
{
    GridPane root;
    Label email_error = new Label   ("");
    Label contact_error = new Label ("");
    Label username_error = new Label("");
    Label password_error = new Label("");
    Label username_error_login = new Label("");
    Label password_error_login = new Label("");
    TextField entered_username = new TextField();
    PasswordField entered_password = new PasswordField();
    TextField entered_email = new TextField();
    TextField entered_contact = new TextField();
    Connection con;
    Label signup_result = new Label("");
    Label redirect = new Label("");
    TextField usernametext = new TextField();
    PasswordField passwordField = new PasswordField();
    int detail_not_complete=0;
    int detail_duplicate=0;
    int login_fail=0;
    @Override

    public void start(Stage stage) throws Exception
    {
        stage.setTitle("STOP & SHOP");
        GridPane rootnode = new GridPane();
        rootnode.setAlignment(Pos.CENTER_RIGHT);

        rootnode.setHgap(10);
        rootnode.setVgap(10);

        rootnode.setPadding(new Insets(25, 25, 25, 25));


        HBox hBox = new HBox(5);
        HBox hBox1 = new HBox(5);

        Label username = new Label("Username :");
        username.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));


        usernametext.setPrefWidth(200);


        username_error_login.setTranslateX(125);

        hBox.getChildren().addAll(username, usernametext);

        Label password = new Label("Password  :");
        password.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));


        passwordField.setPrefWidth(200);


        password_error_login.setTranslateX(125);

        hBox1.getChildren().addAll(password, passwordField);

        VBox vBox = new VBox(5);
        vBox.setTranslateX(-170);

        Button login = new Button("Login");
        login.setTranslateX(40);

        Label not_member = new Label("Not a member yet?");
        not_member.setAlignment(Pos.CENTER);
        not_member.setTranslateX(100);
        not_member.setTextFill(Color.RED);

        Label signup = new Label("SignUp");
        signup.setTranslateX(110);
        signup.setTextFill(Color.DARKCYAN);

        Label login_text = new Label("LogIn");
        login_text.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC,35));
        login_text.setTextFill(Color.DARKCYAN);
        login_text.setTranslateX(120);
        login_text.setTranslateY(-20);

        HBox hBox2 = new HBox(5);

        hBox2.getChildren().addAll(login,signup);

        hBox2.setAlignment(Pos.CENTER);

        HBox hBox3 = new HBox(2);
        hBox3.getChildren().addAll(not_member,signup);
        Image image=new Image("https://p.kindpng.com/picc/s/450-4502425_new-stop-and-shop-logo-hd-png-download.png");
        ImageView imgview=new ImageView(image);
        imgview.setFitHeight(150);
        imgview.setFitWidth(300);
        imgview.setTranslateX(40);
        imgview.setTranslateY(-50);

        vBox.getChildren().addAll(imgview,login_text,hBox,username_error_login, hBox1,password_error_login, hBox2,hBox3);


        rootnode.add(vBox, 0, 0);

        Image imge = new Image("https://i.pinimg.com/originals/c1/d8/ab/c1d8abec7e0f0fb4ae66d7bba679b4a0.jpg");


        BackgroundImage backgroundImage = new BackgroundImage(imge, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);


        Background background = new Background(backgroundImage);


        rootnode.setBackground(background);

        Scene scene = new Scene(rootnode);
        root=rootnode;

        stage.setMaximized(true);


        stage.setScene(scene);

        stage.show();

        create_connection();
        initialization_tables();

        signup.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                sign_up(stage,scene);
            }
        });
        login.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                try {
                    app_login(stage,scene);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void app_login(Stage stage,Scene scene) throws Exception
    {

        usernametext.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                if(t1.length()>s.length())
                {
                    username_error_login.setText("");
                }
                if(t1.length()==0)
                {
                    username_error_login.setText("Username not Entered!");
                    username_error_login.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                    username_error_login.setTextFill(Color.RED);
                    login_fail=1;
                }

            }
        });
        passwordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                if(t1.length()>s.length())
                {
                    password_error_login.setText("");
                }
                if(t1.length()==0)
                {
                    password_error_login.setText("Password not Entered!");
                    login_fail=1;
                }

            }
        });
        if(login_fail==0) {

            String login_username = usernametext.getText().strip();
            String login_password = passwordField.getText().strip();

            Statement stmt = con.createStatement();
            String query = "select username,password from user where username='" + login_username + "'";
            ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next() == false) {
                username_error_login.setText("Profile does not exist");
                username_error_login.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                username_error_login.setTextFill(Color.RED);
            } else {
                String retrieved_username;
                String retrieved_password;

                retrieved_username = resultSet.getString("username");
                retrieved_password = resultSet.getString("password");
                System.out.println(retrieved_username);
                System.out.println(retrieved_password);

                if (retrieved_username.equals(login_username) && retrieved_password.equals(login_password))
                {
                    System.out.println(login_username);
                    dashboard.star(stage,scene);
                }
                else {
                    password_error_login.setText("Incorrect Password!");
                    password_error_login.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                    password_error_login.setTextFill(Color.RED);
                }
            }
        }



    }


    public static void main(String[] args)
    {
        launch(args);
    }

    public void create_connection()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ecommerce", "root", "shreya");
//here ecommerce is database name, root is username and adwait is the password
            Database_Connection.getInstance().getConnection(con);


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void initialization_tables() throws SQLException
    {
         con=Database_Connection.getInstance().con;

        Statement stmt1 = con.createStatement();

        stmt1.executeUpdate("create table if not exists user(username varchar(20) ,email varchar(30),password varchar(20),contact_no varchar(10),primary key(username));");

        Statement stm2=con.createStatement();

        stm2.executeUpdate("create table if not exists cart(cart_id varchar(20),primary key(cart_id));");

        Statement stmt3 =con.createStatement();
        stmt3.executeUpdate("create table if not exists orders(order_id varchar(20),username varchar(20),tot_amt float,product_qty integer, primary key(order_id),foreign key(username) references user(username));");

        Statement stmt4=con.createStatement();
        stmt4.executeUpdate("create table if not exists product(product_id varchar(20),product_name varchar(100),product_category varchar(20),price float(10),product_img varchar(500),description varchar(200),primary key(product_id));");

        Statement stmt5= con.createStatement();
        stmt5.executeUpdate("create table if not exists contains(product_id varchar(20),order_id varchar(20),foreign key(product_id) references product(product_id),foreign key(order_id) references orders(order_id));");

        Statement stmt6 = con.createStatement();
        stmt6.executeUpdate("create table if not exists has(cart_id varchar(20),product_id varchar(20),foreign key(cart_id) references cart(cart_id),foreign key(product_id) references product(product_id));");

        Statement stmt7 = con.createStatement();
        stmt7.executeUpdate("insert ignore into product values('D_01','SASSAFRAS Sea Wash Peterpan Collar Pleated Dress','dresses',799,'https://shoprapy.com/wp-content/uploads/2020/03/bd8f2ff9-186b-4983-b9b6-88654f2d13661564380201384-SASSAFRAS-Women-Blue-A-Line-Dress-9861564380199640-1.jpg','Periwinkle blue Fit n flared flowy silhouette')");

        Statement stmt8 = con.createStatement();
        stmt8.executeUpdate("insert ignore into product values('D_02','Libas Women Black Embroidered A-Line Dress','dresses',1209,'https://rukminim1.flixcart.com/image/714/857/jmp79u80/kurti/8/8/s/l-7422-black-libas-original-imaf9gh4nuxnrbgy.jpeg?q=50','Black embroidered woven A-line dress, has a keyhole neck, three-quarter sleeves, hook and eye closure, and flared hem')");

        Statement stmt9 = con.createStatement();
        stmt9.executeUpdate("insert ignore into product values('D_03','Forever21 Women multicolour Corduroy Pinafore Dress','dresses',1999,'https://adn-static1.nykaa.com/nykdesignstudio-images/tr:w-824,/pub/media/catalog/product/f/r/frev-373704_1.jpg?rnd=20200526195200','Green solid knitted corduroy pinafore dress, has shoulder straps, sleeveless, and straight hem')");

        Statement stmt10 = con.createStatement();
        stmt10.executeUpdate("insert ignore into product values('D_04','SASSAFRAS Women Green Solid Accordion Pleated A-Line Dress','dresses',719,'https://assets.myntassets.com/dpr_1.5,q_60,w_400,c_limit,fl_progressive/h_373,q_80,w_280/v1/assets/images/11364126/2020/1/28/45359a1c-5240-4837-8878-05f7f3feee4d1580197901836-SASSAFRAS-Women-Green-Solid-A-Line-Dress-9491580197899621-1.jpg','Green solid woven accordion pleated A-line dress with layered detail, has shoulder straps, sleeveless, an attached lining, and flared hem')");

        Statement stmt11 = con.createStatement();
        stmt11.executeUpdate("insert ignore into product values('D_05','BerryLush Women Blue Printed Maxi Dress','dresses',1299,'https://cdn.shopify.com/s/files/1/1018/4207/products/3_3_090cb2ba-f0e3-4188-b7fc-5788d3426814_2000x.jpg?v=1597139795','Blue and Maroon printed woven maxi dress, has shoulder straps, sleeveless, concealed zip closure, front slit and flared hem')");

        Statement stmt12 = con.createStatement();
        stmt12.executeUpdate("insert ignore into product values('D_06','SASSAFRAS Women Maroon Solid Tiered Maxi Dress','dresses',879,'https://img.looksgud.com/upload/item-image/1973/16b2f/16b2f-sassafras-women-maroon-solid-tiered-maxi-dress_500x500_0.jpg','Maroon solid woven tiered maxi dress, has a square neck, short sleeves, concealed zip closure, an attached lining, and flounce hem Comes with a belt')");

        Statement stmt13 = con.createStatement();
        stmt13.executeUpdate("insert ignore into product values('D_07','RARE Women Black Printed Fit and Flare Dress','dresses',899,'https://img.pricue.com/images/ar/rare-women-black-printed-fit-and-flare-dress396ad2473dff2d.jpg','Black printed woven fit and flare dress, has a round neck, long sleeves, button closure, an attached lining, flared hem')");

        Statement stmt14 = con.createStatement();
        stmt14.executeUpdate("insert ignore into product values('D_08','BerryLush Women Brown & White Floral Printed Two Piece Maxi Dress','dresses',984,'https://cdn.shopify.com/s/files/1/1018/4207/products/2_2b53443b-d5f8-44be-97ad-b65dc110263f_2000x.jpg?v=1596533707','Brown and white floral printed woven two piece maxi dress, has an off-shoulder neck, short sleeves, concealed zip closure, and flared hem with a high side slit')");

        Statement stmt15 = con.createStatement();
        stmt15.executeUpdate("insert ignore into product values('D_09','Athena Women Burgundy & Brown Embellished Sheath Dress','dresses',1499,'https://assets.ajio.com/medias/sys_master/root/h8e/hfb/16545573109790/-1117Wx1400H-461175128-burgundy-MODEL.jpg','Burgundy and Gold-toned embellished woven sheath dress, has a V-neck, sleeveless, and straight hem')");

        Statement stmt16 = con.createStatement();
        stmt16.executeUpdate("insert ignore into product values('D_10','BerryLush Women Blue Solid Maxi Dress','dresses',999,'https://cdn.shopify.com/s/files/1/1018/4207/products/1_31d8f801-1311-41e1-ab7b-7934f24d13a5_1080x.jpg?v=1575524207','Blue solid woven maxi dress, has shoulder straps, sleeveless, concealed zip closure, and flared hem')");

        Statement stmt17 = con.createStatement();
        stmt17.executeUpdate("insert ignore into product values('S_01','U.S. Polo Assn. Men Maroon Regular Fit Solid Formal Shirt','shirts',999,'https://cdn03.nnnow.com/web-images/large/styles/A1W0D512SBR/1598865809856/8.jpg','Maroon solid formal shirt, has a spread collar, long sleeves, button placket, curved hem, and 1 patch pocket')");

        Statement stmt18 = con.createStatement();
        stmt18.executeUpdate("insert ignore into product values('S_02','Tistabene Men Green & White Slim Fit Striped Casual Shirt','shirts',1299,'https://assets.myntassets.com/dpr_1.5,q_60,w_400,c_limit,fl_progressive/assets/images/12317358/2020/9/9/8ab380a4-6e1d-418f-958e-dc5495b0a8ba1599651303949-Tistabene-Men-Green--White-Comfort-Slim-Fit-Striped-Casual-S-1.jpg','Green and White striped casual shirt, has a spread collar, long sleeves, button placket, curved hem, and 1 patch pocket')");

        Statement stmt19  = con.createStatement();
        stmt19.executeUpdate("insert ignore into product values('S_03','Louis Philippe Men Red & Blue Super Slim Fit Checked Formal Shirt','shirts',1559,'https://louisphilippe.imgix.net/img/app/product/4/415740-2474538.jpg?auto=format&w=618','Red and Blue checked formal shirt, has a spread collar, long sleeves, button placket, curved hem, and 1 patch pocket')");

        Statement stmt20 = con.createStatement();
        stmt20.executeUpdate("insert ignore into product values('S_04','Hancock Men Navy Blue Slim Fit Solid Formal Shirt','shirts',999,'https://img.looksgud.com/upload/item-image/2083/18nxw/18nxw-hancock-men-navy-blue-slim-fit-solid-formal-shirt_500x500_2.jpg','Navy Blue solid formal shirt, has a spread collar, long sleeves, straight hem, one patch pocket')");

        Statement stmt21 = con.createStatement();
        stmt21.executeUpdate("insert ignore into product values('S_05','Arrow Men White Slim Fit Solid Formal Shirt','shirts',1799,'https://img.looksgud.com/upload/item-image/1878/149b2/149b2-arrow-men-white-slim-fit-solid-casual-shirt_500x500_0.jpg','White solid formal shirt, has a cutaway collar, long sleeves, button placket, curved hem, and 1 patch pocket')");

        Statement stmt22 = con.createStatement();
        stmt22.executeUpdate("insert ignore into product values('S_06','Louis Philippe Men Red & White Slim Fit Checked Formal Shirt','shirts',1399,'https://img.looksgud.com/upload/item-image/2252/1ca4h/1ca4h-louis-philippe-men-red-white-slim-fit-checked-formal-shirt_500x500_0.jpg','Red and White checked formal shirt, has a spread collar, long sleeves, button placket, curved hem, and 1 patch pocket')");

        Statement stmt23 = con.createStatement();
        stmt23.executeUpdate("insert ignore into product values('S_07','JAINISH Men Grey Classic Slim Fit Solid Formal Shirt','shirts',493,'https://assets.myntassets.com/h_1440,q_100,w_1080/v1/assets/images/9328037/2019/4/16/af888120-bf6a-4459-a2a4-cdb99f7100801555403650419-JAINISH-Men-Grey-Slim-Fit-Solid-Formal-Shirt-281155540364955-1.jpg','Grey solid formal shirt, has a spread collar, button placket, na pockets, long sleeves, straight hem')");

        Statement stmt24 = con.createStatement();
        stmt24.executeUpdate("insert ignore into product values('S_08','NVICTUS Men Blue & White Slim Fit Checked Formal Shirt','shirts',899,'https://rukminim1.flixcart.com/image/714/857/shirt/k/p/a/689429-f-invictus-44-original-imae7ha8f9vfugyq.jpeg?q=50','Blue and white checked formal shirt, has a spread collar,long sleeves, straight hem')");

        Statement stmt25 = con.createStatement();
        stmt25.executeUpdate("insert ignore into product values('S_09','Louis Philippe Men Purple Classic Regular Fit Self Design Formal Shirt','shirts',1499,'https://assets.myntassets.com/fl_progressive/h_960,q_80,w_720/v1/assets/images/7852461/2018/12/3/a19396f3-0c74-42c7-8686-f1bb075c37281543828652177-Louis-Philippe-Men-Purple-Classic-Regular-Fit-Self-Design-Fo-1.jpg','Purple self-design formal shirt, has a spread collar, long sleeves, button placket, straight hem, and 1 patch pocket')");

        Statement stmt26 = con.createStatement();
        stmt26.executeUpdate("insert ignore into product values('S_10','SOJANYA Men Blue Classic Regular Fit Solid Formal Shirt','shirts',674,'https://sojanya.com/pub/media/catalog/product/cache/b47dab4c1b2aa306dcdd4a51ed7c4687/s/j/sjr-shirt-plain-11014-blue-1.jpg','Blue solid formal shirt, has a spread collar, long sleeves, button placket, and curved hem')");

        Statement stmt27 = con.createStatement();
        stmt27.executeUpdate("insert ignore into product values('Tr_01','Black coffee Men Grey Solid Slim Fit Flat-Front Trousers','trousers',659,'https://assets.myntassets.com/h_1440,q_100,w_1080/v1/assets/images/1488165/2016/8/31/11472626250497-Black-coffee-Men-Grey-Solid-Slim-Fit-Flat-Front-Trousers-5461472626250285-1.jpg','Grey solid formal mid-rise flat-front trousers, has four pockets, a zip fly and button closure, a waistband with belt loops, unsitched hems')");

        Statement stmt28 = con.createStatement();
        stmt28.executeUpdate("insert ignore into product values('Tr_02','Hangup Men Black & Black Solid Formal Trousers','trousers',751,'https://assets.myntassets.com/fl_progressive/h_960,q_80,w_720/v1/assets/images/7271326/2018/9/4/27ff27d1-0f05-4ea6-9445-54b651ea1f521536045059208-hangup-solid-mens-trousers-6601536045059050-2.jpg','Black and black solid mid-rise formal trousers, has a button closure, three pockets.The style does not come with a belt')");

        Statement stmt29 = con.createStatement();
        stmt29.executeUpdate("insert ignore into product values('Tr_03','Hangup Men Blue Smart Regular Fit Solid Formal Trousers','trousers',1599,'https://assets.myntassets.com/h_1440,q_100,w_1080/v1/assets/images/9302095/2019/4/13/8a35eae6-31c0-4671-9f43-16509b7ddfb11555135437583-Hangup-Men-Blue--Blue-Regular-Fit-Solid-Formal-Trousers-4481-1.jpg','Blue solid mid-rise formal trousers, has a hook and bar closure, four pockets')");

        Statement stmt30 = con.createStatement();
        stmt30.executeUpdate("insert ignore into product values('Tr_04','Black coffee Men Black & Grey Slim Fit Checked Formal Trousers','trousers',619,'https://assets.myntassets.com/dpr_1.5,q_60,w_400,c_limit,fl_progressive/assets/images/productimage/2020/1/1/177c5bdd-28a7-42fc-a4c4-02c3c6525f661577831644619-1.jpg','Black and Grey checked mid-rise trousers, button closure, and 4 pockets')");

        Statement stmt31 = con.createStatement();
        stmt31.executeUpdate("insert ignore into product values('Tr_05','Peter England Men Beige Slim Fit Solid Formal Trousers','trousers',1169,'https://assets.abfrlcdn.com/img/app/product/5/512905-3863024.jpg?auto=format','Beige solid mid-rise formal trousers, has a button closure closure, 4 pockets.Our stylist has paired these trousers with a belt.This pair of trousers does not come with a belt')");

        Statement stmt32 = con.createStatement();
        stmt32.executeUpdate("insert ignore into product values('Tr_06','Louis Philippe Ath.Work Men Beige Comfy Tapered Fit Solid Formal Trousers','trousers',1839,'https://assets.myntassets.com/dpr_1.5,q_60,w_400,c_limit,fl_progressive/h_373,q_80,w_280/v1/assets/images/10857548/2019/11/25/303e8035-3cd0-4b5a-a252-557caed0c2031574664790187-Louis-Philippe-AthWork-Men-Khaki-Comfort-Tapered-Fit-Self-De-1.jpg','Beige solid mid-rise trousers, button closure, and 4 pockets.The model is wearing a belt from the stylists collection. The product does not come with the belt.')");

        Statement stmt33 = con.createStatement();
        stmt33.executeUpdate("insert ignore into product values('Tr_07','SOJANYA Men Grey & Blue Smart Fit Checked Formal Trousers','trousers',896,'https://shoprapy.com/wp-content/uploads/2020/08/bec72062-8a00-4170-9f18-eabcc720f4811580468275879-SOJANYA-Men-Blue-Smart-Regular-Fit-Checked-Regular-Trousers-1.jpg','Green and blue checked mid-rise knitted formal trousers, button closure, zip fly and 5 pockets')");

        Statement stmt34 = con.createStatement();
        stmt34.executeUpdate("insert ignore into product values('Tr_08','SOJANYA Men Grey & Off-White Smart Fit Striped Formal Trousers','trousers',896,'https://sojanya.com/pub/media/catalog/product/cache/b47dab4c1b2aa306dcdd4a51ed7c4687/s/j/sjr-trou-strip-803-grey-1.jpg','Grey and Off-white striped woven mid-rise formal trousers, button closure, zip fly and 5 pockets')");

        Statement stmt35 = con.createStatement();
        stmt35.executeUpdate("insert ignore into product values('Tr_09','SOJANYA Men Navy Blue & Pink Smart Fit Striped Formal Trousers','trousers',896,'https://assets.myntassets.com/dpr_1.5,q_60,w_400,c_limit,fl_progressive/assets/images/11398086/2020/2/10/ca7d6a04-996c-4e71-a17e-5c7bfda64b571581330771117-SOJANYA-Men-Navy-Blue--Pink-Smart-Regular-Fit-Striped-Regula-1.jpg','Navy blue and pink striped mid-rise formal trousers, button closure, and five pockets')");

        Statement stmt36 = con.createStatement();
        stmt36.executeUpdate("insert ignore into product values('Tr_10','MANQ Men Coffee Brown Smart Slim Fit Solid Formal Trousers','trousers',764,'https://www.manq.in/wp-content/uploads/2019/12/1-6.jpg','Coffee Brown solid mid-rise trousers, button closure,zip fly  and 5 pockets.Our stylist has paired these trousers with a belt.This pair of trousers does not come with a beltS')");
        //Tops
        Statement stmt37 = con.createStatement();
        stmt37.executeUpdate("insert ignore into product values('T_01','SASSAFRAS Women Black Solid High Neck Cropped Top','tops',599,'https://img.looksgud.com/upload/item-image/2035/17mg7/17mg7-sassafras-women-black-solid-crop-top_500x500_2.jpg','Black solid knitted crop top, has a high neck, and long sleeves')");

        Statement stmt38 = con.createStatement();
        stmt38.executeUpdate("insert ignore into product values('T_02','Carlton London Women Black Solid Victorian Crop Fitted Top','tops',764,'https://i.pinimg.com/originals/8b/21/c9/8b21c91d5b007282806bc606b3ca9e42.jpg','Black solid woven victorian crop fitted top with gathers, has a sweetheart neck, long cuffed sleeves, and button closures')");

        Statement stmt39 = con.createStatement();
        stmt39.executeUpdate("insert ignore into product values('T_03','Pannkh Women Navy Blue Printed Top','tops',519,'https://cdn.shopify.com/s/files/1/0505/7888/2746/products/5150-4_1800x1800.jpg?v=1603712647','Navy, rust orange and beige printed woven top, has a round neck, three-quarter sleeves')");

        Statement stmt40 = con.createStatement();
        stmt40.executeUpdate("insert ignore into product values('T_04','Roadster Women Yellow Checked Blouson Top','tops',769,'https://cdn.fashiola.in/L67521843/roadster-women-mustard-yellow-lace-up-solid-schiffli-embroidered-top.jpg','Yellow checked woven blouson top, has a sweetheart neck, and short sleeves')");

        Statement stmt41 = con.createStatement();
        stmt41.executeUpdate("insert ignore into product values('T_05','DOROTHY PERKINS Women Beige Solid Blouson Top','tops',2392,'https://assets.myntassets.com/h_1440,q_100,w_1080/v1/assets/images/8403369/2019/1/17/6f2d437b-1f0f-40b9-a914-ec38baca1d0f1547715754654-DOROTHY-PERKINS-Women-Tops-4271547715753122-1.jpg','Beige solid woven blouson top with gathered detail, has a round neck, long sleeves, and button closure')");

        Statement stmt42 = con.createStatement();
        stmt42.executeUpdate("insert ignore into product values('T_06','SASSAFRAS Women White Solid High-Neck Top','tops',599,'https://img.looksgud.com/upload/item-image/2276/1csg5/1csg5-clafoutis-solid-women-high-neck-white-t-shirt_500x500_0.jpg','White solid knitted regular top, has a high neck, and long sleeves')");

        Statement stmt43 = con.createStatement();
        stmt43.executeUpdate("insert ignore into product values('T_07','DressBerry Women Purple Solid Top','tops',374,'https://assets.myntassets.com/fl_progressive/h_960,q_80,w_720/v1/assets/images/2522016/2018/8/8/58f3d849-bda6-4abc-97cd-4be7dbc0a57d1533729418602-DressBerry-Women-Burgundy-Solid-Top-5071533729416542-1.jpg','Purple solid regular top, has a round neck and short sleeves')");

        Statement stmt44 = con.createStatement();
        stmt44.executeUpdate("insert ignore into product values('T_08','Os Women Blue Self Design A-Line Top','tops',879,'https://img.looksgud.com/upload/item-image/2044/17tgi/17tgi-all-about-you-women-blue-self-design-a-line-top_500x500_0.jpg','Blue self-design woven A-line top, has a round neck, and three-quarter sleeves')");

        Statement stmt45 = con.createStatement();
        stmt45.executeUpdate("insert ignore into product values('T_09','Mayra Women Black Printed Shirt Style Top','tops',398,'https://rukminim1.flixcart.com/image/714/857/j2516kw0/top/p/j/y/s-1704t09843-mayra-original-imaetjzx99ru5uah.jpeg?q=50','Black and grey printed woven shirt style top, has a shirt collar, long sleeves, button closure')");

        Statement stmt46 = con.createStatement();
        stmt46.executeUpdate("insert ignore into product values('T_10','DOROTHY PERKINS Women White Solid Top with Net Sleeves','tops',1992,'https://cdn.fashiola.in/L66725279/dorothy-perkins-women-white-solid-top-with-net-sleeves.jpg','White solid knitted regular top, has a square neck, and three-quarter net sleeves')");

        Statement stmt47 = con.createStatement();
        stmt47.executeUpdate("insert ignore into product values('T_11','Xpose Women Blue Solid Denim A-Line Top','tops',1049,'https://assets.myntassets.com/fl_progressive/h_960,q_80,w_720/v1/assets/images/9937273/2019/7/4/0dc437b1-753e-4557-9537-bd405d2928091562245775626-Xpose-Women-Blue-Chambray-Solid-A-Line-Top-651562245774236-1.jpg','Blue solid woven denim A-line top, has a round neck, sleeveless, ruffled hem')");

        Statement stmt48 = con.createStatement();
        stmt48.executeUpdate("insert ignore into product values('T_12','RHHENSO Women Red Solid Bardot Top','tops',6500,'https://cdn.shopify.com/s/files/1/1018/4207/products/3_6f0de782-83f8-4a9e-ba9c-21cffea1ff54_2000x.jpg?v=1571553325','Red solid knitted and woven bardot top, has an off-shoulder neck, and long sleeves')");

        Statement stmt49 = con.createStatement();
        stmt49.executeUpdate("insert ignore into product values('T_13','KASSUALLY Women Black Solid Peplum Top','tops',610,'https://shoprapy.com/wp-content/uploads/2020/10/aae6d5da-cee2-4fdf-99cd-b5421d86d68a1591654989641-1.jpg','Black solid knitted peplum top, has a high neck, three-quarter sleeves, and hook and eye closure')");

        Statement stmt50 = con.createStatement();
        stmt50.executeUpdate("insert ignore into product values('T_14','SASSAFRAS Women Peach-Coloured Satin Finish Solid Shirt Style Top','tops',584,'https://i2.wp.com/www.meroekchaa.com/wp-content/uploads/2018/09/11512454408561-SASSAFRAS-Women-Pink-Solid-Shirt-Style-Top-1481512454408474-3.jpg?fit=768%2C1024&ssl=1','Peach-coloured solid woven satin finish shirt style top, has a tie-up neck with sequinned detail, long sleeves, button placket')");

        Statement stmt51 = con.createStatement();
        stmt51.executeUpdate("insert ignore into product values('T_15','Veni Vidi Vici Women White Solid Fitted Top','tops',580,'https://img.looksgud.com/upload/item-image/2328/1dx03/1dx03-veni-vidi-vici-women-white-solid-fitted-top_500x500_2.jpg','White solid knitted fitted top, has a high neck, long sleeves, concealed zip closure')");

        Statement stmt52 = con.createStatement();
        stmt52.executeUpdate("insert ignore into product values('T_16','plusS Women Sea Green Self Design Peplum Top','tops',531,'https://i.pinimg.com/736x/1e/25/92/1e2592fc0903519f44c6b5193d2bc8e2.jpg','Sea Green self-design woven peplum top, has a mandarin collar, three-quarter sleeves')");

        Statement stmt53 = con.createStatement();
        stmt53.executeUpdate("insert ignore into product values('T_17','Sangria Women Green Printed High-Low Top','tops',854,'https://img.looksgud.com/upload/item-image/1759/11pjl/11pjl-sangria-women-green-printed-high-low-top_500x500_0.jpg','Green in colour, this top from Sangria boasts of floral block print all over and a clean style. Tailored from cotton, this top has a round neck and 3/4th flounce sleeves that gives a sharp, elegant look, while the golden stitch details on the front and peplum design makes it visually appealing. This top can be styled with a pair of black jeans and brown sandals to look your best.')");

        Statement stmt54 = con.createStatement();
        stmt54.executeUpdate("insert ignore into product values('T_18','SASSAFRAS Women Green Ruffled Shirt Style Top','tops',449,'https://rukminim1.flixcart.com/image/400/400/jx502vk0/top/g/a/k/l-sftops4059-sassafras-original-imafhz6s4gwybwqq.jpeg?q=90','Green solid woven shirt style top with ruffles, has a mandarin collar, long sleeves, button closure')");

        Statement stmt55 = con.createStatement();
        stmt55.executeUpdate("insert ignore into product values('T_19','Athena Women Red Solid Bardot Top','tops',734,'https://img.looksgud.com/upload/item-image/2164/1ae8i/1ae8i-rivi-women-red-solid-bardot-top_500x500_4.jpg','Red solid knitted bardot top, has an off-shoulder neck, and long sleeves')");

        Statement stmt56 = con.createStatement();
        stmt56.executeUpdate("insert ignore into product values('T_20','plusS Women Olive Green Solid A-Line Top','tops',466,'https://img.looksgud.com/upload/item-image/2167/1agkx/1agkx-label-ritu-kumar-women-olive-green-golden-self-striped_500x500_0.jpg','Olive green solid woven A-line top with crochet inserts and gather, has a tie-up neck, three-quarter sleeves')");


    }

    public void sign_up(Stage stage,Scene scene)
    {
        GridPane root_signup = new GridPane();

        Image image = new Image("https://i.pinimg.com/originals/c1/d8/ab/c1d8abec7e0f0fb4ae66d7bba679b4a0.jpg");


        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);


        Background background = new Background(backgroundImage);


        root_signup.setBackground(background);

        Label username = new Label("Username:");

        username.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));

        entered_username.setPrefWidth(200);

        Label password = new Label("Password: ");

        password.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));

        entered_password.setPrefWidth(200);

        Label email = new Label("Email:       ");

        email.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));

        entered_email.setPrefWidth(200);

        Label contactno = new Label("Contact:   ");

        contactno.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));

        entered_contact.setPrefWidth(200);
        entered_contact.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                if(s.length()<t1.length())
                {
                    if(t1.length()>10) {

                        contact_error.setText("Contact cannot be greater than 10 digits!");
                        contact_error.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                        contact_error.setTextFill(Color.RED);
                        detail_not_complete=1;
                    }
                    else if(t1.length()==10)
                    {
                        contact_error.setText("");
                        for(int i=0;i<t1.length();i++)
                        {
                            if(!Character.isDigit(t1.charAt(i)))
                            {
                                contact_error.setText("Contact can only contain numerical digits!");
                                contact_error.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                                contact_error.setTextFill(Color.RED);
                                detail_not_complete=1;
                                break;

                            }
                        }

                    }
                    else if(t1.length()<10)
                    {
                        contact_error.setText("Contact should contain 10 digits!");
                        contact_error.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                        contact_error.setTextFill(Color.RED);
                        detail_not_complete=1;


                    }
                    else
                    {
                        contact_error.setText("");
                    }
                }


            }
        });
        entered_email.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                if(t1.length()>s.length())
                {
                    for(int i=0;i<t1.length();i++)
                    {
                        if(!(Character.isDigit(t1.charAt(i))||Character.isAlphabetic(t1.charAt(i))||t1.charAt(i)=='_'||t1.charAt(i)=='.'||t1.charAt(i)=='@'))
                        {
                            email_error.setText("Email not entered correctly!");
                            email_error.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                            email_error.setTextFill(Color.RED);
                            detail_not_complete=1;
                            break;
                        }
                    }
                    int num_of_at=0;
                    for(int i=0;i<t1.length();i++)
                    {

                        if(t1.charAt(i)=='@')
                        {
                            num_of_at+=1;
                        }

                    }
                    if(num_of_at>1)
                    {
                        email_error.setText("Email not entered correctly!");
                        email_error.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                        email_error.setTextFill(Color.RED);
                        detail_not_complete=1;


                    }
                    else if(num_of_at==0)
                    {
                        email_error.setText("Email not entered correctly!");
                        email_error.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                        email_error.setTextFill(Color.RED);
                        detail_not_complete=1;

                    }
                    else
                    {
                        email_error.setText("");
                    }
                }


            }
        });

        Button back = new Button("BACK");

        back.setTranslateY(-120);
        back.setTranslateX(-480);

        Button reset = new Button("Reset");

        reset.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                entered_username.clear();
                entered_password.clear();
                entered_contact.clear();
                entered_email.clear();
                username_error.setText("");
                password_error.setText("");
                contact_error.setText("");
                email_error.setText("");
                redirect.setText("");
                signup_result.setText("");

            }
        });

        Button submit = new Button("Submit");



        HBox hBox = new HBox(11);
        HBox hBox1 = new HBox(10);
        HBox hBox2 = new HBox(9);
        HBox hBox3 = new HBox(13);
        HBox hBox4 = new HBox(10);
        hBox4.setAlignment(Pos.CENTER_LEFT);
        reset.setTranslateX(110);
        submit.setTranslateX(130);
        reset.setPrefWidth(60);
        submit.setPrefWidth(60);


        hBox.getChildren().addAll(username,entered_username);
        hBox1.getChildren().addAll(password,entered_password);
        hBox2.getChildren().addAll(email,entered_email);
        hBox3.getChildren().addAll(contactno,entered_contact);
        hBox4.getChildren().addAll(reset,submit);

        VBox vBox = new VBox(8);

        Label Signup= new Label("SIGN UP");




        Signup.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC,35));
        Signup.setTextFill(Color.DARKCYAN);
        Signup.setTranslateX(95);
        Signup.setTranslateY(-20);

        username_error.setTranslateX(110);
        password_error.setTranslateX(110);
        email_error.setTranslateX(110);
        contact_error.setTranslateX(110);


        vBox.getChildren().addAll(Signup,hBox,username_error,hBox1,password_error,hBox2,email_error,hBox3,contact_error,hBox4,signup_result,redirect);
        vBox.setTranslateX(-50);

        root_signup.add(back,0,0);
        root_signup.add(vBox,2,2);


        image = new Image("https://images.wallpaperscraft.com/image/trolley_people_entertainment_92179_1920x1080.jpg");

        backgroundImage = new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT);

        background = new Background(backgroundImage);

        root_signup.setBackground(background);

        root_signup.setAlignment(Pos.CENTER);

        scene.setRoot(root_signup);

        stage.show();

        back.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {

                entered_contact.clear();
                entered_email.clear();
                entered_password.clear();
                entered_username.clear();
                signup_result.setText("");
                redirect.setText("");
                scene.setRoot(root);
                stage.setScene(scene);
                stage.show();



            }
        });

        entered_username.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                if(t1.length()>s.length())
                {
                    username_error.setText("");
                }

            }
        });

        entered_password.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                if(t1.length()>s.length())
                {
                    password_error.setText("");
                }


            }
        });


        submit.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                detail_not_complete=0;
                detail_duplicate=0;
                String username = entered_username.getText().strip();
                String password = entered_password.getText().strip();
                String email = entered_email.getText().strip();
                String contact = entered_contact.getText().strip();


                if(entered_username.getText().isEmpty())
                {
                    username_error.setText("Username not Entered!");
                    username_error.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                    username_error.setTextFill(Color.RED);
                    detail_not_complete=1;
                }
                else
                {
                    try
                    {
                        Statement statement = con.createStatement();
                        String query="select username from user where username='"+username+"'";

                        ResultSet resultSet = statement.executeQuery(query);

                        if(!(resultSet.next()==false))
                        {
                            detail_duplicate=1;
                            username_error.setText("Username already taken");
                            username_error.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                            username_error.setTextFill(Color.RED);

                        }



                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }
                if(entered_password.getText().isEmpty())
                {
                    password_error.setText("Password not Entered!");
                    password_error.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                    password_error.setTextFill(Color.RED);
                    detail_not_complete=1;
                }
                if(entered_email.getText().isEmpty())
                {
                    email_error.setText("Email not Entered!");
                    email_error.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                    email_error.setTextFill(Color.RED);
                    detail_not_complete=1;
                }
                if (entered_contact.getText().isEmpty())
                {
                    contact_error.setText("Contact not Entered!");
                    contact_error.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                    contact_error.setTextFill(Color.RED);
                    detail_not_complete=1;
                }
                else
                {
                    try
                    {
                        Statement statement = con.createStatement();
                        String query="select contact_no from user where contact_no='"+contact+"'";

                        ResultSet resultSet = statement.executeQuery(query);

                        if(!(resultSet.next()==false))
                        {
                            detail_duplicate=1;
                            contact_error.setText("Number linked to another account!");
                            contact_error.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                            contact_error.setTextFill(Color.RED);


                        }



                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }


                }


                if(detail_not_complete==0 && detail_duplicate==0)
                {
                    try
                    {
                        user_details();
                    } catch (SQLException throwables)
                    {
                        throwables.printStackTrace();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                if(detail_not_complete==1)
                {
                    signup_result.setText("Fill in the details!!");
                    signup_result.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                    signup_result.setTextFill(Color.RED);
                    signup_result.setTranslateX(130);

                    redirect.setText("");
                }







            }
        });




    }

    private void user_details() throws SQLException, InterruptedException
    {
        String username = entered_username.getText().strip();
        String password = entered_password.getText().strip();
        String email = entered_email.getText().strip();
        String contact = entered_contact.getText().strip();

        Statement stmt =con.createStatement();

        String query="insert into user values("+"'"+username+"','"+email+"',"+"'"+password+"',"+contact+");";

        //System.out.print(username+" "+contact+" "+email+" "+password);

        //System.out.print(query);

        stmt.executeUpdate(query);

        //Thread.sleep(2000);

        signup_result.setText("Sign Up Successful!");
        signup_result.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
        signup_result.setTextFill(Color.GREEN);
        signup_result.setTranslateX(130);


        redirect.setText("Return to the Main Page and Login!");
        redirect.setFont(Font.font("Aerial",FontWeight.NORMAL,FontPosture.ITALIC,13));
        redirect.setTranslateX(85);





    }


}