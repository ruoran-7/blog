package com.ysp.service;

import com.ysp.bean.User;


public interface UserService {

    User checkUser(String username, String password);
}
