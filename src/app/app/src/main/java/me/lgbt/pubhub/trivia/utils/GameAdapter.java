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

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    private final ClickListener listener;
    private ArrayList<GameDisplay> gameList;

    public GameAdapter(ArrayList<GameDisplay> gameList, ClickListener listener) {
        this.gameList = gameList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View gameView = inflater.inflate(R.layout.recycler_object, parent, false);

        return new GameAdapter.ViewHolder(gameView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder viewHolder, int position) {
        GameDisplay game = gameList.get(position);

        TextView textView = viewHolder.title;
        ImageButton editButton = viewHolder.editButton;
        ImageButton playButton = viewHolder.playButton;
        ImageButton deleteButton = viewHolder.deleteButton;

        String title = game.getName() + ", " + game.getHost();

        textView.setText(title);
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageButton editButton;
        ImageButton deleteButton;
        ImageButton playButton;
        private WeakReference<ClickListener> listenerRef;

        ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);
            title = itemView.findViewById(R.id.roundListTitle);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            playButton = itemView.findViewById(R.id.playButton);
            playButton.setVisibility(View.VISIBLE);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
            playButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listenerRef.get().onPositionClicked(getAdapterPosition(), view.getId());
        }
    }
}
