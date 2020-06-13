package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toast box if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    TextView SignUpLink;
    TextView UserName;
    TextView PassWord;
    MyDBHandler dbHandler;
    UserData dbData;
    Button Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new MyDBHandler(this, null,null,1);
        SignUpLink = findViewById(R.id.SignUpLink);
        UserName = findViewById(R.id.Username);
        PassWord = findViewById(R.id.Password);
        Login = findViewById(R.id.Login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = UserName.getText().toString();
                String Password = PassWord.getText().toString();

                if(isValidUser(Username, Password)){
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");
                    Log.v(TAG, FILENAME + ": Logging in with: " + Username+ ": " + Password);
                    Toast.makeText(MainActivity.this, "Valid User! Logging in", Toast.LENGTH_SHORT).show();
                    login(Username);
                }
                else{
                    Log.v(TAG, FILENAME + ": Invalid user!");
                }
            }
        });

        SignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignUp = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(toSignUp);
                Log.v(TAG, FILENAME + ": Create new user!");
            }
        });
        /* Hint:
            This method creates the necessary login inputs and the new user creation on touch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */




    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){
        boolean result;
        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */

        dbData = dbHandler.findUser(userName);
        Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
        if(dbData == null){
            Toast.makeText(this, "Create new user", Toast.LENGTH_SHORT).show();

            result = false;
        }
        else{
            dbData = dbHandler.findUser(userName);
            result = (dbData.getMyPassword().equals(password));
        }
        return result;
    }

    public void login(String userName){
        dbData = dbHandler.findUser(userName);
        Intent Activity3 = new Intent(MainActivity.this, Main3Activity.class);
        Activity3.putExtra("CurrentUser", dbData);
        startActivity(Activity3);
    }
}
