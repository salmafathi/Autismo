package com.salma.loginlayout.validations;

import android.content.Context;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {
    Context cn;

        public Validations(Context cn){
            this.cn=cn;
        }
        //check valid mail
        public boolean isValidEmail(String mail){
            boolean check;
            Pattern p;
            Matcher m;

            String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            p = Pattern.compile(EMAIL_STRING);
            m = p.matcher(mail);
            check = m.matches();
            return check;
        }


        //check valid password
        public boolean isValidPassword(String userPassword) {
            boolean check;
            check=userPassword.length()>6;
            return check;
        }

        // check empty password
        public boolean isEmptyPassword(String userPassword){
            return TextUtils.isEmpty(userPassword);
        }
    // check empty password
    public boolean isEmptyName(String username){
        return TextUtils.isEmpty(username);
    }

        //check empty mail
        public boolean isEmptyMail(String userEmail){
            return TextUtils.isEmpty(userEmail);
        }

        // encrypt passwords
        public StringBuffer passwordMD5Hash(String userPassword)
        {
            StringBuffer MD5Hash = null;
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                digest.update(userPassword.getBytes());
                byte[] messageDigest = digest.digest();
                MD5Hash= new StringBuffer();
                for (byte b : messageDigest) {
                    String h = Integer.toHexString(0xFF & b);
                    while (h.length() < 2)
                        h = "0" + h;
                    MD5Hash.append(h);
                }
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            return MD5Hash;
        }

}

