package com.ganesh_project.kingofkaraoke.model;

import java.util.ArrayList;

public class MusicInfoResponse {

    private ArrayList<QuestionInfo> music_info = new ArrayList<>();

    public ArrayList<QuestionInfo> getMusic_info() {
        return music_info;
    }

    public void setMusic_info(ArrayList<QuestionInfo> music_info) {
        this.music_info = music_info;
    }
}
