package org.example;

import org.example.util.OtpReader;

import javax.mail.MessagingException;

public class Main {
    public static void main(String[] args) throws MessagingException {
        System.out.println(OtpReader.getOtp("a71983628@gmail.com", "fulm huoz xnsi ygnh", "Your secret login code"));
    }
}