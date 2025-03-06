package com.ganesh_project.kingofkaraoke.quiz.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ganesh_project.kingofkaraoke.model.QuestionInfo;
import com.ganesh_project.kingofkaraoke.quiz.QuizFragment;

import java.util.ArrayList;

public class QuizCustomViewPager extends FragmentStateAdapter {
    private static final int NUM_PAGES = 8;
    private ArrayList<QuestionInfo> questionArrayList;


    public QuizCustomViewPager(FragmentActivity fa, ArrayList<QuestionInfo> questionInfoArrayList) {
        super(fa);
        this.questionArrayList = questionInfoArrayList;
    }

    @Override
    public Fragment createFragment(int position) {
        return QuizFragment.newInstance(position, questionArrayList);
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
