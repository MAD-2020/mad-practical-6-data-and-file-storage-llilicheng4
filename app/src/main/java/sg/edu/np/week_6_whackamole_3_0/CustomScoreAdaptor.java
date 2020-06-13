package sg.edu.np.week_6_whackamole_3_0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    UserData userData;
    ArrayList<Integer> LevelList;
    ArrayList<Integer> ScoreList;
    Activity activity;

    public CustomScoreAdaptor(UserData userdata, Activity CurrentActivity){
        /* Hint:
        This method takes in the data and readies it for processing.
         */
        userData = userdata;
        LevelList = userData.getLevels();
        ScoreList = userData.getScores();
        activity = CurrentActivity;
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        /* Hint:
        This method dictates how the viewHolder layout is to be once the viewHolder is created.
         */
        //This line create the UI from the XML
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        //return the ViewHolder
        return new CustomScoreViewHolder(item);
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){

        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */
        //Using the row id to retrieve data from list


        String Level = String.valueOf(LevelList.get(position));
        String Score = String.valueOf(ScoreList.get(position));
        //Display the information on to the UI
        holder.lvl.setText(Level);
        holder.score.setText(Score);
        Log.v(TAG, FILENAME + " Showing level " + LevelList.get(position) + " with highest score: " + ScoreList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentLevel = LevelList.get(position);
                Log.d(TAG, "onClick: "+currentLevel);
                Intent goToLevel = new Intent(activity, Main4Activity.class);
                goToLevel.putExtra("Level", String.valueOf(currentLevel));
                goToLevel.putExtra("User", userData);
                activity.startActivity(goToLevel);
                Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + userData.getMyUserName());
            }
        });
    }

    public int getItemCount(){
        /* Hint:
        This method returns the the size of the overall data.
         */
        return ScoreList.size();
    }

}