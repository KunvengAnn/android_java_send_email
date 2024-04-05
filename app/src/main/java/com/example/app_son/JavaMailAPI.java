package com.example.app_son;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "JavaMailAPI";

    private Context mContext;
    private Session mSession;
    private String mEmail;
    private String mSubject;
    private String mMessage;
    private ProgressDialog mProgressDialog;

    // Constructor
    public JavaMailAPI(Context context, String SendToGmail, String subject, String message) {
        this.mContext = context;
        this.mEmail = SendToGmail;
        this.mSubject = subject;
        this.mMessage = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Show progress dialog while sending email
        mProgressDialog = ProgressDialog.show(mContext, "Sending message", "Please wait...", false, false);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        // Dismiss progress dialog
        mProgressDialog.dismiss();

        if (success) {
            // Show success toast
            Log.d(TAG, "Email sent successfully");
            // Show success toast
            Toast.makeText(mContext, "Email sent successfully", Toast.LENGTH_SHORT).show();

        } else {
            // Show failure toast
            Log.e(TAG, "Failed to send email");
            // Show failure toast
            Toast.makeText(mContext, "Failed to send email", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // Creating properties
        Properties props = new Properties();

        // Configuring properties for Gmail
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // Creating a new session
        mSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    // Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD);
                    }
                });

        try {
            // Creating MimeMessage object
            MimeMessage mm = new MimeMessage(mSession);

            // Setting sender address
            mm.setFrom(new InternetAddress(Utils.EMAIL));
            // Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));
            // Adding subject
            mm.setSubject(mSubject);
            // Adding message
            mm.setText(mMessage);

            // Sending email
            Transport.send(mm);
            return true; // Email sent successfully
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Failed to send email
        }
    }
}
