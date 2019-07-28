package com.mobile.doctalk_doctor.model;

import java.util.List;

public class RequestNotification {
    public String content;
    public String title ;
    public String imageUrl ;
    public String gameUrl;
    public List<String> userIds;

    public RequestNotification() {
    }

    public RequestNotification(String content, String title, String imageUrl, String gameUrl, List<String> userIds) {
        this.content = content;
        this.title = title;
        this.imageUrl = imageUrl;
        this.gameUrl = gameUrl;
        this.userIds = userIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
