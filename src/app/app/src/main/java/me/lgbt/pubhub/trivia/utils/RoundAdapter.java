package me.lgbt.pubhub.trivia.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.interfaces.ClickListener;

public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.ViewHolder> {

    private final ClickListener listener;
    private ArrayList<TriviaRound> roundList;

    public RoundAdapter(ArrayList<TriviaRound> roundList, ClickListener listener) {
        this.roundList = roundList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoundAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View roundView = inflater.inflate(R.layout.recycler_object, parent, false);

        return new ViewHolder(roundView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoundAdapter.ViewHolder viewHolder, int position) {
        TriviaRound round = roundList.get(position);

        TextView textView = viewHolder.title;
        ImageButton imageButton = viewHolder.editButton;
        ImageButton deleteButton = viewHolder.deleteButton;

        textView.setText(round.getTitle());
    }

    @Override
    public int getItemCount() {
        return roundList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageButton editButton;
        ImageButton deleteButton;
        private WeakReference<ClickListener> listenerRef;


        ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);
            title = itemView.findViewById(R.id.roundListTitle);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listenerRef.get().onPositionClicked(getAdapterPosition(), view.getId());
        }

    }
}
