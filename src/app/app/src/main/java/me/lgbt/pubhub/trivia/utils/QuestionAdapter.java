package me.lgbt.pubhub.trivia.utils;

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

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private ArrayList<TriviaQuestion> questionList;

    public QuestionAdapter(ArrayList<TriviaQuestion> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View roundView = inflater.inflate(R.layout.recycler_object, parent, false);

        return new ViewHolder(roundView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder viewHolder, int position) {
        TriviaQuestion question = questionList.get(position);

        TextView textView = viewHolder.title;
        ImageButton editButton = viewHolder.editButton;
        ImageButton deleteButton = viewHolder.deleteButton;

        textView.setText(question.getTitle());
        //TODO add imageButton touch listener
    }

    @Override
    public int getItemCount() {
        return questionList.size();
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
