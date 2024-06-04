package com.example.tasklist.service;

import com.example.tasklist.model.entity.User;

import java.util.Properties;

public interface MailService {
    void sendMail(User user, Properties params);
}
