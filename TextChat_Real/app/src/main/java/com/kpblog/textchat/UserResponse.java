package com.kpblog.textchat;

import com.kpblog.textchat.model.User;

/**
 * Created by Khushvinders on 18-Nov-16.
 */

public class UserResponse {
    private User user[];

    public User[] getUser() {
        return user;
    }

    public void setUser(User[] user) {
        this.user = user;
    }
}
