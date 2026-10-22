package com.sideby.user.vo;

public class CurrentUserVO {
    private String id;
    private String username;
    private String email;
    private String displayName;
    private String avatarUrl;
    private String bio;

    public CurrentUserVO() {
    }

    public CurrentUserVO(String id, String username, String email, String displayName, String avatarUrl, String bio) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}