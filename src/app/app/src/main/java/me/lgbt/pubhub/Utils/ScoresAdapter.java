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

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {
    private ArrayList<ScoreObject> teams;

    public ScoresAdapter(ArrayList<ScoreObject> teams) {
        this.teams = teams;
    }

    @NonNull
    @Override
    public ScoresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View gameView = inflater.inflate(R.layout.object_answer, parent, false);

        return new ScoresAdapter.ViewHolder(gameView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoresAdapter.ViewHolder viewHolder, int i) {
        ScoreObject team = teams.get(i);
        String teamsName = team.getTeamName();
        int teamScore = team.getScore();
        TextView score = viewHolder.score;
        TextView teamName = viewHolder.teamName;
        TextView place = viewHolder.place;

        score.setText(teamScore);
        teamName.setText(teamsName);
        place.setText(i);
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
