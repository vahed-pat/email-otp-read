package org.example;

import org.example.util.OtpReader;

import javax.mail.MessagingException;

public class Main {
    public static void main(String[] args) throws MessagingException {
        System.out.println(OtpReader.getOtp("email", "password", "Your secret login code"));
    }
}