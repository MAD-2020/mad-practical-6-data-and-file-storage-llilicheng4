package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to sign up
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toast box if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toast box message will appear.
        5. There is an option to cancel. This loads the login user page.
     */


    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";


    TextView username;
    TextView password;
    Button cancel;
    Button create;
    ArrayList<Integer> Levels;
    ArrayList<Integer> Scores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        cancel = findViewById(R.id.Cancel);
        create = findViewById(R.id.Create);
        Levels = new ArrayList<Integer>(10);
        Scores = new ArrayList<Integer>(10);
        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

         */

        for(int i = 1; i<11; i++){
            Levels.add(i);
            Scores.add(0);
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = username.getText().toString();
                if(!Username.isEmpty()){
                    newAccount();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToLogin = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(backToLogin);
            }
        });
    }

    protected void onStop() {
        super.onStop();
        finish();
    }

    public void newAccount() {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        String Username = username.getText().toString();
        String Password = password.getText().toString();
        UserData Account = new UserData(Username, Password, Levels, Scores);

        Log.v(TAG, "New User creation with : "+ Username);

        if (dbHandler.findUser(Username) == null) {
            dbHandler.addUser(Account);
            Intent Activity3 = new Intent(Main2Activity.this, Main3Activity.class);
            Activity3.putExtra("CurrentUser", Account);
            startActivity(Activity3);
            Log.v(TAG, FILENAME + ": New user created successfully!");
        }
        else if(dbHandler.findUser(Username) != null){
            Toast.makeText(this,"User already exists!\n Please try again" ,Toast.LENGTH_SHORT).show();
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

        }
    }


}
