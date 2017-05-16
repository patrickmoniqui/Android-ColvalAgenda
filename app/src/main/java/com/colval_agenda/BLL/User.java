package com.colval_agenda.BLL;

/**
 * Created by macbookpro on 17-05-16.
 */

public class User {
    private String username;
    private String pwd;
    private String displayName;

    public User() {}

    public User(String username, String pwd, String displayName) {
        this.username = username;
        this.pwd = pwd;
        this.displayName = displayName;
    }
}
