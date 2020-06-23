package com.cyj.ems.dto;

import java.io.Serializable;

public class UploadFileResultDTO implements Serializable {

    private boolean isLogin;
    private boolean isUpload;
    private String photoURL;

    @Override
    public String toString() {
        return "EditPhotoResultDTO{" +
                "isLogin=" + isLogin +
                ", isUpload=" + isUpload +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
