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
import java.util.Timer;

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
        stmt3.executeUpdate("create table if not exists orders(order_id varchar(20),username varchar(20),tot_amt float, primary key(order_id),foreign key(username) references user(username));");

        Statement stmt4=con.createStatement();
        stmt4.executeUpdate("create table if not exists product(product_id varchar(20),product_name varchar(20),product_category varchar(20),price float(10),product_img varchar(200),description varchar(40),primary key(product_id));");

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