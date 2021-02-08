package com.mycode.base.retrofitextension.utility;

import java.security.MessageDigest;

import androidx.annotation.Nullable;

/**
 * 2019-05-20 kyunghoon
 *
 * Band 에서 가져왔습니다.
 */
public class ShaUtility {
    @Nullable
    public static String md5(String source) {
        return getHash(source, "MD5");
    }

    @Nullable
    public static String sha256(String source) {
        String hash = getHash(source, "SHA-256");
        if(hash == null) {
            hash = getHash(source, "SHA256");
        }
        return hash;
    }

    @Nullable
    private static String getHash(String txt, String hashType) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (Exception e) {
            //error action
        }
        return null;
    }

}
