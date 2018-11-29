package me.lgbt.pubhub.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.lgbt.pubhub.R;

/**
 * @author Geoffrey Blech
 * Adapter for showing the list of scores for each team.
 */
public class teamScoreAdapter extends RecyclerView.Adapter<teamScoreAdapter.ViewHolder> {
    private ArrayList<teamScoreObject> teams;

    public teamScoreAdapter(ArrayList<teamScoreObject> teams) {
        this.teams = teams;
    }

    @NonNull
    @Override
    public teamScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View gameView = inflater.inflate(R.layout.object_scores, parent, false);

        return new teamScoreAdapter.ViewHolder(gameView);
    }

    @Override
    public void onBindViewHolder(@NonNull teamScoreAdapter.ViewHolder viewHolder, int i) {
        teamScoreObject team = teams.get(i);
        String teamsName = team.getTeamName();
        String teamScore = Integer.toString(team.getScore());
        String plc = Integer.toString(i + 1);
        TextView score = viewHolder.score;
        TextView teamName = viewHolder.teamName;
        TextView place = viewHolder.place;

        score.setText(teamScore);
        teamName.setText(teamsName);
        place.setText(plc);
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView score;
        TextView teamName;
        TextView place;

        ViewHolder(View itemView) {
            super(itemView);
            score = itemView.findViewById(R.id.teamScore);
            teamName = itemView.findViewById(R.id.teamNameScores);
            place = itemView.findViewById(R.id.teamPlaceScore);

        }
    }


}
