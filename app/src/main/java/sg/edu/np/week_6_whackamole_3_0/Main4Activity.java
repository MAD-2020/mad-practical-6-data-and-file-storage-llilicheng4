package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    int CurrentLevel;
    int GameTime;
    int NumOfMoles;
    Button BackButton;
    TextView score;
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    MyDBHandler dbHandler;
    UserData CurrentUser;

    private void readyTimer(final int gameTime, final int numOfMoles){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        readyTimer = new CountDownTimer(10000, 1000){
            public void onTick(long millisUntilFinished){
                Toast.makeText(Main4Activity.this, "Get Ready In "+millisUntilFinished/1000 +" Seconds", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Ready CountDown! " + millisUntilFinished/ 1000);
            }

            public void onFinish(){
                Log.v(TAG, "Ready CountDown Complete!");
                Toast.makeText(Main4Activity.this, "GO!", Toast.LENGTH_SHORT).show();
                placeMoleTimer(gameTime, numOfMoles);
                readyTimer.cancel();
            }
        };
        readyTimer.start();
    }

    private void placeMoleTimer(int gameTime, final int numOfMoles){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        Log.d(TAG, "placeMoleTimer: speed is"+ gameTime);
        newMolePlaceTimer = new CountDownTimer(100000,gameTime) {
            @Override
            public void onTick(long l) {
                setNewMole(numOfMoles);
                Log.v(TAG, "New Mole Location!");
            }

            @Override
            public void onFinish() {
                newMolePlaceTimer.start();
            }
        };
        newMolePlaceTimer.start();
    }


    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.button,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */
        dbHandler = new MyDBHandler(this,null,null,1);
        score = findViewById(R.id.advancedscore);
        BackButton = findViewById(R.id.backButton);
        CurrentLevel = Integer.parseInt(getIntent().getStringExtra("Level"));
        CurrentUser = (UserData) getIntent().getSerializableExtra("User");

        if(CurrentLevel == 1){
            GameTime = 10000;
            NumOfMoles = 1;
        }
        else if (CurrentLevel == 2){
            GameTime = 9000;
            NumOfMoles = 1;
        }
        else if (CurrentLevel == 3){
            GameTime = 8000;
            NumOfMoles = 1;
        }
        else if (CurrentLevel == 4){
            GameTime = 7000;
            NumOfMoles = 1;
        }
        else if (CurrentLevel == 5){
            GameTime = 6000;
            NumOfMoles = 1;
        }
        else if (CurrentLevel == 6){
            GameTime = 5000;
            NumOfMoles = 2;
        }
        else if (CurrentLevel == 7){
            GameTime = 4000;
            NumOfMoles = 2;
        }
        else if (CurrentLevel == 8){
            GameTime = 3000;
            NumOfMoles = 2;
        }
        else if (CurrentLevel == 9){
            GameTime = 2000;
            NumOfMoles = 2;
        }
        else{
            GameTime = 1000;
            NumOfMoles = 2;
        }


        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            Button newButton = findViewById(id);

            final Button finalNewButton = newButton;
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doCheck(finalNewButton);
                }
            });
        }
        readyTimer(GameTime, NumOfMoles);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
            }
        });
    }


    @Override
    protected void onStart(){
        super.onStart();

    }

    private void doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        int currentScore = Integer.parseInt(score.getText().toString());
        if (checkButton.getText() == "*") {
            currentScore += 1;
            score.setText(String.valueOf(currentScore));
            Log.v(TAG, "Hit, score added!”");
            checkButton.setText("O");

        } else if (checkButton.getText() != "*" && currentScore > 0) {
            currentScore -= 1;
            score.setText(String.valueOf(currentScore));
            Log.v(TAG, "Missed, score deducted!”");
        } else {
            Log.v(TAG, "Error Occurred”");
        }

    }

    public void setNewMole(int numOfMoles)
    {
        int firstRandomLocation;
        int secondRandomLocation;
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        if(numOfMoles == 2) {
            for (final int id : BUTTON_IDS) {
                Button renewButton = findViewById(id);
                renewButton.setText("O");
            }


            Random ran = new Random();
            firstRandomLocation = ran.nextInt(9);
            secondRandomLocation = ran.nextInt(9);

            if (firstRandomLocation != secondRandomLocation) {
                int buttonId = (int) Array.get(BUTTON_IDS, firstRandomLocation);
                int buttonId2 = (int) Array.get(BUTTON_IDS, secondRandomLocation);
                Button setButton = findViewById(buttonId);
                Button setButton2 = findViewById(buttonId2);
                setButton.setText("*");
                setButton2.setText("*");
            }
            else{
                secondRandomLocation = ran.nextInt(9);
                int buttonId = (int) Array.get(BUTTON_IDS, firstRandomLocation);
                int buttonId2 = (int) Array.get(BUTTON_IDS, secondRandomLocation);
                Button setButton = findViewById(buttonId);
                Button setButton2 = findViewById(buttonId2);
                setButton.setText("*");
                setButton2.setText("*");
            }
        }

        else{
            for (final int id : BUTTON_IDS) {
                Button renewButton = findViewById(id);
                renewButton.setText("O");
            }


            Random ran = new Random();
            int randomLocation = ran.nextInt(9);
            int buttonId = (int) Array.get(BUTTON_IDS, randomLocation);
            Button setButton = findViewById(buttonId);
            setButton.setText("*");
        }
    }

    private void updateUserScore()
    {

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
     if(newMolePlaceTimer != null) {
         newMolePlaceTimer.cancel();
     }
     readyTimer.cancel();
     int NewScore = Integer.parseInt(score.getText().toString());
     dbHandler.updatePoints(CurrentUser.getMyUserName(),CurrentLevel, NewScore);
     UserData CurrentUpdatedUser = dbHandler.findUser(CurrentUser.getMyUserName());
     Intent backToHome = new Intent(Main4Activity.this, Main3Activity.class);
     backToHome.putExtra("CurrentUser", CurrentUpdatedUser);
     startActivity(backToHome);
    }


}
