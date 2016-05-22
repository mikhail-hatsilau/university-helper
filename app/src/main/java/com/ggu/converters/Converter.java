package com.ggu.converters;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Михаил on 09.07.2014.
 */
public class Converter {

    public static String md5(String password) {
        StringBuilder hex_password=null;
        String hex=null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte[] symbols = digest.digest();

            hex_password = new StringBuilder();

            for (byte symbol : symbols){
                hex=Integer.toHexString(0xFF & symbol);
                while (hex.length()<2){
                    hex="0"+hex;
                }
                hex_password.append(hex);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hex_password.toString();
    }

}
