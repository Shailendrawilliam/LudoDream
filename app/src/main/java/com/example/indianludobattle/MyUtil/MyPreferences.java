package com.example.indianludobattle.MyUtil;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class MyPreferences {

    Context context;
    String email;
    private boolean loginstatus = false;
    String mobile;
    String name;
    String profile;
    SharedPreferences sharedPreferences;
    String userId;
    String loginby;
    String AllGameStatus;
    String selectedDeliveryId;

    public String getLoginBy() {
        this.loginby = this.sharedPreferences.getString(Constants.loginby, null);
        return this.loginby;
    }

    public void setLoginBy(String loginby) {
        this.loginby = loginby;
        this.sharedPreferences.edit().putString(Constants.loginby, loginby).commit();
    }

    public String getSelectedDeliveryId() {
        this.selectedDeliveryId = this.sharedPreferences.getString(Constants.selectedDeliveryId, null);
        return this.selectedDeliveryId;
    }

    public void setSelectedDeliveryId(String selectedDeliveryId) {
        this.selectedDeliveryId = selectedDeliveryId;
        this.sharedPreferences.edit().putString(Constants.selectedDeliveryId, selectedDeliveryId).commit();
    }


    public String getProfile() {
        this.profile = this.sharedPreferences.getString(Constants.profile, null);
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
        this.sharedPreferences.edit().putString(Constants.profile, profile).commit();
    }

    public String getEmail() {
        this.email = this.sharedPreferences.getString(Constants.email, null);
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
        this.sharedPreferences.edit().putString(Constants.email, str).commit();
    }

    public String getMobile() {
        this.mobile = this.sharedPreferences.getString(Constants.mobile, null);
        return this.mobile;
    }

    public void setMobile(String str) {
        this.mobile = str;
        this.sharedPreferences.edit().putString(Constants.mobile, str).commit();
    }

    public String getAllGameStatus() {
        this.AllGameStatus = this.sharedPreferences.getString(Constants.AllGameStatus, null);
        return this.AllGameStatus;
    }

    public void setAllGameStatus(String str) {
        this.AllGameStatus = str;
        this.sharedPreferences.edit().putString(Constants.AllGameStatus, str).commit();
    }

    public String getName() {
        this.name = this.sharedPreferences.getString(Constants.name, null);
        return this.name;
    }
    public String getWallet() {
        this.name = this.sharedPreferences.getString(Constants.wallet, null);
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
        this.sharedPreferences.edit().putString(Constants.name, str).commit();
    }

    public void setWallet(String str) {
        this.name = str;
        this.sharedPreferences.edit().putString(Constants.wallet, str).commit();
    }


    public String getUserId() {
        this.userId = this.sharedPreferences.getString(Constants.seller_id, null);
        return this.userId;
    }

    public void setUserId(String str) {
        this.userId = str;
        this.sharedPreferences.edit().putString(Constants.seller_id, str).commit();
    }

    public MyPreferences(Context context2) {
        this.context = context2;
        this.sharedPreferences = context2.getSharedPreferences("userinfo", 0);
    }

    public void logOut() {
        this.sharedPreferences.edit().clear().commit();
    }

    public void logOut(Context context2) {
        this.sharedPreferences.edit().clear().commit();
        deleteCache(context2);
    }

    public static void deleteCache(Context context2) {
        try {
            deleteDir(context2.getCacheDir());
        } catch (Exception unused) {
        }
    }

    public static boolean deleteDir(File file) {
        if (file != null && file.isDirectory()) {
            String[] list = file.list();
            for (String file2 : list) {
                if (!deleteDir(new File(file, file2))) {
                    return false;
                }
            }
            return file.delete();
        } else if (file == null || !file.isFile()) {
            return false;
        } else {
            return file.delete();
        }
    }
}
