package com.ganesh_project.kingofkaraoke.model;

public class HighScoreResponse {
    String email;
    float score;
    long timing;

    public HighScoreResponse(String s, float i, long i1) {
        email = s;
        score = i;
        timing = i1;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setTiming(long timing) {
        this.timing = timing;
    }


    public float getScore() {
        return score;
    }

    public long getTiming() {
        return timing;
    }

    public String getEmail() {
        return email;
    }
}
