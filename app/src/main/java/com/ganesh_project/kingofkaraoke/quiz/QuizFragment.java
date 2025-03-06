package com.ganesh_project.kingofkaraoke.quiz;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ganesh_project.kingofkaraoke.R;
import com.ganesh_project.kingofkaraoke.databinding.FragmentQuizBinding;
import com.ganesh_project.kingofkaraoke.model.QuestionInfo;
import com.ganesh_project.kingofkaraoke.score.ScoreActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuizFragment extends Fragment {
    private static String POSITION_INFO = "POSITION_INFO";
    private static String QUESTION_LIST = "QUESTION_LIST";
    int positionInfo = 0;
    ArrayList<QuestionInfo> questionList = new ArrayList<>();
    private FragmentQuizBinding binding;
    MediaPlayer mediaPlayer;
    QuestionInfo questionInfo;
    private static QuizFragment instance;

    public static QuizFragment newInstance(int position, ArrayList<QuestionInfo> questionList) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_INFO, position);
        args.putParcelableArrayList(QUESTION_LIST, questionList);
        fragment.setArguments(args);
        return fragment;
    }
    public static QuizFragment getInstance()
    {
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        positionInfo = getArguments().getInt(POSITION_INFO);
        questionList = getArguments().getParcelableArrayList(QUESTION_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance= this;

        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.questionAppCompatTextView.setText(String.valueOf(positionInfo));

        if(positionInfo == 7) {
            binding.nextAppCompatButton.setEnabled(false);
        } else {
            binding.nextAppCompatButton.setEnabled(true);
        }

        if(positionInfo == 0) {
            binding.previousAppCompatButton.setEnabled(false);
        } else {
            binding.previousAppCompatButton.setEnabled(true);
        }

        questionInfo = questionList.get(positionInfo);
        setInfoDetails(questionInfo);
        initUI();
    }

    private void initUI() {

        binding.musicPlayTrackSeekBar.setProgress(0);
        binding.playAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });

        binding.albumAppCompatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(requireContext())
                        .load(questionInfo.getAlbum_photo())
                        .error(getResources().getDrawable(R.mipmap.ic_launcher))
                        .into(binding.albumAppCompatImageView);

                questionList.get(positionInfo).setUser_hint(true);
            }
        });

        binding.pauseAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAudio();
            }
        });

        binding.optionSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = getView().findViewById(checkedId);

                if(questionInfo.getAnswer().equals(radioButton.getText().toString())) {
                    questionList.get(positionInfo).setAnswerCorrect(true);
                } else {
                    questionList.get(positionInfo).setAnswerCorrect(false);
                }
            }
        });

        binding.nextAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positionInfo < questionList.size() - 1) {
                    ((QuizActivity) requireActivity()).changeViewPagerPosition(positionInfo + 1);
                }
            }
        });

        binding.submitAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeoutFinishQuiz();;
            }
        });


        binding.previousAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positionInfo > 0) {
                    ((QuizActivity) requireActivity()).changeViewPagerPosition(positionInfo - 1);
                }
            }
        });
    }

    private void setInfoDetails(QuestionInfo questionInfo) {
        binding.questionAppCompatTextView.setText(questionInfo.getQuestion());

        binding.optionA.setText(questionInfo.getOption_a());
        binding.optionB.setText(questionInfo.getOption_b());
        binding.optionC.setText(questionInfo.getOption_c());
        binding.optionD.setText(questionInfo.getOption_d());
    }

    private void pauseAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            Toast.makeText(requireContext(), "Audio has been paused", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Audio has not played", Toast.LENGTH_SHORT).show();
        }
    }
    public void run() {

        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();


        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            binding.musicPlayTrackSeekBar.setProgress(currentPosition);

        }
    }

    private void playAudio() {

        String audioUrl = questionInfo.getSong_file();

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(audioUrl);
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            binding.musicPlayTrackSeekBar.setMax(mediaPlayer.getDuration());

            mediaPlayer.start();
            new Thread().start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // below line is use to display a toast message.
        Toast.makeText(requireContext(), "Audio started playing..", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPause() {
        super.onPause();

        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    public void timeoutFinishQuiz() {

        List<QuestionInfo> finalQuestion = questionList.stream().limit(8).collect(Collectors.toList());

        ArrayList<QuestionInfo> finalQuestionList = new ArrayList<>();
        finalQuestionList.addAll(finalQuestion);
        long currentTime = ((QuizActivity) requireActivity()).finalRemainingTime();
        Intent intent = new Intent(requireActivity(), ScoreActivity.class);
        intent.putExtra("QUESTION_LIST", finalQuestionList);
        intent.putExtra("TIME_REMAINING", currentTime);

        startActivity(intent);

        requireActivity().finish();
    }
}