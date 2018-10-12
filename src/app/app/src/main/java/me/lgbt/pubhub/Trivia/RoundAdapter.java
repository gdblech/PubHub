package me.lgbt.pubhub.Trivia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import me.lgbt.pubhub.R;

public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.ViewHolder> {

    private ArrayList<TriviaRound> roundsList;

    public RoundAdapter(ArrayList<TriviaRound> roundsList){
        this.roundsList = roundsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageButton editButton;

        public ViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.roundListTitle);
            editButton = itemView.findViewById(R.id.editRoundButton);
        }
    }

    @Override
    public RoundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View roundView = inflater.inflate(R.layout.round_list_rv, parent, false);

        return new ViewHolder(roundView);
    }

    @Override
    public void onBindViewHolder(RoundAdapter.ViewHolder viewHolder, int position){
        TriviaRound round = roundsList.get(position);

        TextView textView = viewHolder.title;
        ImageButton imageButton = viewHolder.editButton;

        textView.setText(round.getTitle());
        //TODO add imageButton touch listener
    }

    @Override
    public int getItemCount() {
        return roundsList.size();
    }
}
