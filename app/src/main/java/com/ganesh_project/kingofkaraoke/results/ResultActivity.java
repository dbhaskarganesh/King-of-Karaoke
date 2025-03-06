package com.ganesh_project.kingofkaraoke.results;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ganesh_project.kingofkaraoke.R;
import com.ganesh_project.kingofkaraoke.databinding.ActivityResultBinding;
import com.ganesh_project.kingofkaraoke.model.QuestionInfo;
import com.ganesh_project.kingofkaraoke.score.view.ScoreAdapter;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;
    ArrayList<QuestionInfo> questionList = new ArrayList<>();
    float finalScore;

    private ScoreAdapter scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
    }

    private void initUI() {
        questionList = getIntent().getParcelableArrayListExtra("QUESTION_LIST");
        finalScore = getIntent().getFloatExtra("FINAL_SCORE", 0);
        scoreAdapter = new ScoreAdapter(ResultActivity.this, questionList);

        binding.finalScoreAppCompatTextView.setText(getResources().getString(R.string.final_score)+ ": " + String.valueOf(finalScore));
        binding.questionRecyclerView.setAdapter(scoreAdapter);

        binding.backAppCompatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}