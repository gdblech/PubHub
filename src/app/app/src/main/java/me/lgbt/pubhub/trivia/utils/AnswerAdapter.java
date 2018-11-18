package me.lgbt.pubhub.trivia.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.trivia.utils.interfaces.ClickListener;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    private final ClickListener listener;
    private ArrayList<String> answerList;

    public AnswerAdapter(ArrayList<String> answerList, ClickListener listener) {
        this.answerList = answerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View gameView = inflater.inflate(R.layout.object_answer, parent, false);

        return new AnswerAdapter.ViewHolder(gameView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerAdapter.ViewHolder viewHolder, int position) {
        String answer = answerList.get(position);

        TextView textView = viewHolder.title;
        FloatingActionButton select = viewHolder.select;

        textView.setText(answer);
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        FloatingActionButton select;
        private WeakReference<ClickListener> listenerRef;

        ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);
            title = itemView.findViewById(R.id.teamAnswerText);
            select = itemView.findViewById(R.id.teamAnswerButton);
        }

        @Override
        public void onClick(View view) {
            listenerRef.get().onPositionClicked(getAdapterPosition(), view.getId());
        }
    }

}
