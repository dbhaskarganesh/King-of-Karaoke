package com.ganesh_project.kingofkaraoke.score.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ganesh_project.kingofkaraoke.R;
import com.ganesh_project.kingofkaraoke.model.QuestionInfo;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    ArrayList<QuestionInfo> questionList = new ArrayList<>();
    Context context;

    public ScoreAdapter(Context context, ArrayList<QuestionInfo> questionInfoList) {
        this.questionList = questionInfoList;
        this.context = context;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout

        View photoView = inflater.inflate(R.layout.question_result_info, parent, false);

        ScoreViewHolder viewHolder = new ScoreViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ScoreViewHolder viewHolder, final int position) {
        final int index = viewHolder.getAdapterPosition();
        viewHolder.questionAppCompatTextView.setText(questionList.get(position).getQuestion());
        viewHolder.answerAppCompatTextView.setText(questionList.get(position).getAnswer());
        viewHolder.yourAnswerAppCompatTextView.setText((questionList.get(position).isAnswerCorrect()) ? "Correct Answer" : "Wrong Answer");
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class ScoreViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView questionAppCompatTextView, answerAppCompatTextView, yourAnswerAppCompatTextView;

        ScoreViewHolder(View itemView) {
            super(itemView);
            questionAppCompatTextView = (AppCompatTextView) itemView.findViewById(R.id.questionAppCompatTextView);
            answerAppCompatTextView = (AppCompatTextView) itemView.findViewById(R.id.answerAppCompatTextView);
            yourAnswerAppCompatTextView = (AppCompatTextView) itemView.findViewById(R.id.yourAnswerAppCompatTextView);

        }
    }

}