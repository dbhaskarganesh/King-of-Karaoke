package com.ganesh_project.kingofkaraoke.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.ganesh_project.kingofkaraoke.login.LoginActivity;

public class SharedPreference {

    SharedPreferences sharedPreferences;
    String ACCESS_TOKEN = "ACCESS_TOKEN";
    String EMAIL_ADDRESS = "EMAIL_ADDRESS";
    String NAME = "NAME";

    public SharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("King_Of_Karaoke", MODE_PRIVATE);
    }

    public void saveToken(String accessToken) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(ACCESS_TOKEN, accessToken);
        myEdit.commit();
    }


    public String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN, null);
    }

    public void saveEmail(String email) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(EMAIL_ADDRESS, email);
        myEdit.commit();
    }


    public String getEmail() {
        return sharedPreferences.getString(EMAIL_ADDRESS, null);
    }

    public void saveName(String name) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(NAME, name);
        myEdit.commit();
    }


    public String getName() {
        return sharedPreferences.getString(NAME, null);
    }
}
