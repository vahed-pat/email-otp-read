package org.example.util;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class OtpReader {
    private final static String host = "imap.gmail.com";
    private final static String mailStoreType = "imaps";
    private final static String port = "993";
    private final static String otpPhrase = "This is your secret login code: ";

    public static String getOtp(String emailId, String password, String emailSubject) throws MessagingException {
        Store store = null;
        Folder inbox = null;
        try {
            String otp = "";
            Properties properties = new Properties();
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", port);
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);

            Session emailSession = Session.getDefaultInstance(properties);
            store = emailSession.getStore(mailStoreType);
            store.connect(host, emailId, password);
            inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            // all unread emails from Inbox folder is to be in the messages array
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            // filter list to get required emails only
            List<Message> filteredList = Arrays.asList(messages).stream().filter(message -> {
                try {
                    return message.getSubject().equals(emailSubject);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            if (filteredList == null || filteredList.isEmpty()) {
                return otp;
            }
            Message latestMessage = filteredList.get(filteredList.size() - 1);
            latestMessage.setFlag(Flags.Flag.SEEN, true);
            String messageBody = latestMessage.getContent().toString();
            // find index inside the message body where OTP is written
            int indexOfOtpStart = messageBody.indexOf(otpPhrase) + otpPhrase.length();
            // get 6-digit OTP
            otp = otp + messageBody.substring(indexOfOtpStart + 37, indexOfOtpStart + 37 + 6);
            return otp;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("There are problems with reading emails.");
        } finally {
            if (inbox != null)
                inbox.close(false);
            if (store != null)
                store.close();
        }
    }
}