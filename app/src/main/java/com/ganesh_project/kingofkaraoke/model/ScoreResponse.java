package com.ganesh_project.kingofkaraoke.model;


public class ScoreResponse {
    private HighScoreResponse score;

    public ScoreResponse(HighScoreResponse highScoreResponse) {
        score = highScoreResponse;
    }

    public HighScoreResponse getScore() {
        return score;
    }

    public void setScore(HighScoreResponse score) {
        this.score = score;
    }
}
