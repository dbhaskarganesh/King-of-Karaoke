package com.ganesh_project.kingofkaraoke.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import kotlinx.parcelize.Parcelize;

@Parcelize
public class QuestionInfo implements Parcelable {
    private int id;
    private String question;
    private String song_file;
    private String album_photo;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String answer;
    private boolean user_hint;
    private boolean isAnswerCorrect;
    private int used_time;


    protected QuestionInfo(Parcel in) {
        id = in.readInt();
        question = in.readString();
        song_file = in.readString();
        album_photo = in.readString();
        option_a = in.readString();
        option_b = in.readString();
        option_c = in.readString();
        option_d = in.readString();
        answer = in.readString();
        user_hint = in.readByte() != 0;
        isAnswerCorrect = in.readByte() != 0;
        used_time = in.readInt();
    }

    public static final Creator<QuestionInfo> CREATOR = new Creator<QuestionInfo>() {
        @Override
        public QuestionInfo createFromParcel(Parcel in) {
            return new QuestionInfo(in);
        }

        @Override
        public QuestionInfo[] newArray(int size) {
            return new QuestionInfo[size];
        }
    };

    public void setAlbum_photo(String album_photo) {
        this.album_photo = album_photo;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }

    public void setSong_file(String song_file) {
        this.song_file = song_file;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setUsed_time(int used_time) {
        this.used_time = used_time;
    }

    public void setUser_hint(boolean user_hint) {
        this.user_hint = user_hint;
    }

    public boolean isUser_hint() {
        return user_hint;
    }

    public int getId() {
        return id;
    }

    public String getAlbum_photo() {
        return album_photo;
    }

    public String getAnswer() {
        return answer;
    }

    public String getOption_a() {
        return option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public String getOption_d() {
        return option_d;
    }

    public String getQuestion() {
        return question;
    }

    public String getSong_file() {
        return song_file;
    }

    public int getUsed_time() {
        return used_time;
    }

    public void setAnswerCorrect(boolean answerCorrect) {
        isAnswerCorrect = answerCorrect;
    }

    public boolean isAnswerCorrect() {
        return isAnswerCorrect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeString(song_file);
        dest.writeString(album_photo);
        dest.writeString(option_a);
        dest.writeString(option_b);
        dest.writeString(option_c);
        dest.writeString(option_d);
        dest.writeString(answer);
        dest.writeByte((byte) (user_hint ? 1 : 0));
        dest.writeByte((byte) (isAnswerCorrect ? 1 : 0));
        dest.writeInt(used_time);
    }
}
