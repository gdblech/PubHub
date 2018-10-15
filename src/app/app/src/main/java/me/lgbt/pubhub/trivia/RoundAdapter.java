package me.lgbt.pubhub.trivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import me.lgbt.pubhub.R;

public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.ViewHolder> {

    private ArrayList<TriviaRound> roundList;

    public RoundAdapter(ArrayList<TriviaRound> roundList) {
        this.roundList = roundList;
    }

    @NonNull
    @Override
    public RoundAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View roundView = inflater.inflate(R.layout.recycler_object, parent, false);

        return new ViewHolder(roundView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoundAdapter.ViewHolder viewHolder, int position) {
        TriviaRound round = roundList.get(position);

        TextView textView = viewHolder.title;
        ImageButton imageButton = viewHolder.editButton;

        textView.setText(round.getTitle());
        //TODO add imageButton touch listener
    }

    @Override
    public int getItemCount() {
        return roundList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton editButton;
        public ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.roundListTitle);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
