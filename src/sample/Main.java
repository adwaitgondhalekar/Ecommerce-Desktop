package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.sql.*;

public class Main extends Application
{
    GridPane root;
    Label email_error = new Label  ("                                                                         ");
    Label contact_error = new Label("                                                                         ");
    TextField entered_username = new TextField();
    PasswordField entered_password = new PasswordField();
    TextField entered_email = new TextField();
    TextField entered_contact = new TextField();
    Connection con;
    @Override

    public void start(Stage stage) throws Exception
    {
        stage.setTitle("STOP & SHOP");
        GridPane rootnode = new GridPane();
        rootnode.setAlignment(Pos.CENTER_RIGHT);

        rootnode.setHgap(10);
        rootnode.setVgap(10);

        rootnode.setPadding(new Insets(25, 25, 25, 25));

        //Text scenetitle = new Text("Welcome!");

        //scenetitle.setFont(Font.font("Tahoma",FontWeight.NORMAL,30));

        //rootnode.add(scenetitle,1,0,1,1);

        HBox hBox = new HBox(5);
        HBox hBox1 = new HBox(5);

        Label username = new Label("Username :");
        username.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));

        TextField usernametext = new TextField();

        Label username_error = new Label("Username");
        username_error.setTranslateX(85);

        hBox.getChildren().addAll(username, usernametext);

        Label password = new Label("Password  :");
        password.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));

        PasswordField passwordField = new PasswordField();

        Label password_error = new Label("Pass");
        password_error.setTranslateX(85);

        hBox1.getChildren().addAll(password, passwordField);

        VBox vBox = new VBox(5);
        vBox.setTranslateX(-170);

        Button login = new Button("Login");

        Label not_member = new Label("Not a member yet? ");
        not_member.setAlignment(Pos.CENTER);
        not_member.setTranslateX(50);

        Label signup = new Label("SignUp");
        signup.setTranslateX(60);

        Label login_text = new Label("LogIn");
        login_text.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC,35));
        login_text.setTextFill(Color.DARKCYAN);

        HBox hBox2 = new HBox(5);

        hBox2.getChildren().addAll(login,signup);

        hBox2.setAlignment(Pos.CENTER);

        HBox hBox3 = new HBox(2);
        hBox3.getChildren().addAll(not_member,signup);

        vBox.getChildren().addAll(login_text,hBox,username_error, hBox1,password_error, hBox2,hBox3);


        rootnode.add(vBox, 0, 0);

        Image image = new Image("https://i.pinimg.com/originals/c1/d8/ab/c1d8abec7e0f0fb4ae66d7bba679b4a0.jpg");


        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);


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
                    "jdbc:mysql://localhost:3306/ecommerce", "root", "adwait");
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

        stmt1.executeUpdate("create table if not exists user(username varchar(20) ,email varchar(30),password varchar(20),contact_no int(10),primary key(username));");

        Statement stm2=con.createStatement();

        stm2.executeUpdate("create table if not exists cart(cart_id varchar(20),primary key(cart_id));");

        Statement stmt3 =con.createStatement();
        stmt3.executeUpdate("create table if not exists orders(order_id varchar(20),username varchar(20),tot_amt float, primary key(order_id),foreign key(username) references user(username));");

        Statement stmt4=con.createStatement();
        stmt4.executeUpdate("create table if not exists product(product_id varchar(20),product_name varchar(20),product_category varchar(20),price float(10),product_img varchar(50),description varchar(40),primary key(product_id));");

        Statement stmt5= con.createStatement();
        stmt5.executeUpdate("create table if not exists contains(product_id varchar(20),order_id varchar(20),foreign key(product_id) references product(product_id),foreign key(order_id) references orders(order_id));");

        Statement stmt6 = con.createStatement();
        stmt6.executeUpdate("create table if not exists has(cart_id varchar(20),product_id varchar(20),foreign key(cart_id) references cart(cart_id),foreign key(product_id) references product(product_id));");



    }

    public void sign_up(Stage stage,Scene scene)
    {
        GridPane root_signup = new GridPane();

        Image image = new Image("https://i.pinimg.com/originals/c1/d8/ab/c1d8abec7e0f0fb4ae66d7bba679b4a0.jpg");


        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);


        Background background = new Background(backgroundImage);


        root_signup.setBackground(background);

        Label username = new Label("Username:");
        Label username_error = new Label("                                                                        ");
        username.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));

        entered_username.setPrefWidth(200);

        Label password = new Label("Password: ");
        Label password_error = new Label("                                                                        ");
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
                    }
                    if(t1.length()==10)
                    {
                        for(int i=0;i<t1.length();i++)
                        {
                            if(!Character.isDigit(t1.charAt(i)))
                            {
                                contact_error.setText("Contact can only contain numerical digits!");
                                contact_error.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                                contact_error.setTextFill(Color.RED);
                                break;

                            }
                        }

                    }
                }
                else
                {
                    contact_error.setText("");
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


                    }
                    else if(num_of_at==0)
                    {
                        email_error.setText("Email not entered correctly!");
                        email_error.setFont(Font.font("Tahoma",FontWeight.NORMAL,13));
                        email_error.setTextFill(Color.RED);

                    }
                    else
                    {
                        email_error.setText("");
                    }
                }


            }
        });

        Button back = new Button("BACK");

        back.setTranslateY(-300);
        back.setTranslateX(-400);

        Button reset = new Button("Reset");

        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                entered_username.clear();
                entered_password.clear();
                entered_contact.clear();
                entered_email.clear();

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


        hBox.getChildren().addAll(username,entered_username,username_error);
        hBox1.getChildren().addAll(password,entered_password,password_error);
        hBox2.getChildren().addAll(email,entered_email,email_error);
        hBox3.getChildren().addAll(contactno,entered_contact,contact_error);
        hBox4.getChildren().addAll(reset,submit);

        VBox vBox = new VBox(15);
        Label Signup= new Label("SIGN UP");
        Signup.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC,35));
        Signup.setTextFill(Color.DARKCYAN);
        Signup.setTranslateX(95);
        Signup.setTranslateY(-20);


        vBox.getChildren().addAll(Signup,hBox,hBox1,hBox2,hBox3,hBox4);
        vBox.setTranslateX(100);

        root_signup.add(back,0,0);
        root_signup.add(vBox,0,0);


        image = new Image("https://images.wallpaperscraft.com/image/trolley_people_entertainment_92179_1920x1080.jpg");

        backgroundImage = new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT);

        background = new Background(backgroundImage);

        root_signup.setBackground(background);

        root_signup.setAlignment(Pos.CENTER);

        scene.setRoot(root_signup);

        stage.show();

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                scene.setRoot(root);
                stage.setScene(scene);
                stage.show();



            }
        });

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {


                user_details();


            }
        });




    }

    private void user_details() throws SQLException {
        String username = entered_username.getText();
        String password = entered_password.getText();
        String email = entered_email.getText();
        String contact = entered_contact.getText();

        Statement stmt =con.createStatement();

    }

    public void contact_validation(String s,String t1)
    {
        if(s.length()<t1.length())
        {
            if(t1.length()>10) {

                contact_error.setText("Contact cannot be greater than 10 digits!");
                contact_error.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                contact_error.setTextFill(Color.RED);
            }
            if(t1.length()==10)
            {
                for(int i=0;i<t1.length();i++)
                {
                    if(!Character.isDigit(t1.charAt(i)))
                    {
                        contact_error.setText("Contact can only contain numerical digits!");
                        contact_error.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                        contact_error.setTextFill(Color.RED);
                        break;

                    }
                }

            }
        }
        else
        {
            contact_error.setText("");
        }
    }
}