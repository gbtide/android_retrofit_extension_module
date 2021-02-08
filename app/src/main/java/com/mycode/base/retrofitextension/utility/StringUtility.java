package com.mycode.base.retrofitextension.utility;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

public class StringUtility {
    private static final String LETTER_OR_NUMBER_REGEX = "[^\uAC00-\uD7A3xfe0-9a-zA-Z]";

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNullOrEmpty(CharSequence str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }

        if (str1 == null) {
            return false;
        }

        return str1.equals(str2);
    }

    public static boolean equals(String str1, Object str2) {
        if (str1 == str2) {
            return true;
        }

        if (str1 == null) {
            return false;
        }

        if (str2 == null) {
            return false;
        }

        return str1.equals(str2.toString());
    }

    public static boolean equalsAtLeast(String source, String... targets) {
        if (targets == null) {
            return false;
        }

        for (String target : targets) {
            if (equals(source, target)) {
                return true;
            }
        }

        return false;
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }

        if (str1 == null) {
            return false;
        }

        return str1.equalsIgnoreCase(str2);
    }

//    public static long getSafeLongValue(String value, long defaultValue) {
//        try {
//            if (!isNullOrEmpty(value)) {
//                return Long.valueOf(value);
//            }
//        } catch (NumberFormatException nfe) {
//            logger.e(nfe);
//        }
//        return defaultValue;
//    }

//    public static int getSafeIntegerValue(String value, int defaultValue) {
//        try {
//            if (!isNullOrEmpty(value)) {
//                return Integer.valueOf(value);
//            }
//        } catch (NumberFormatException nfe) {
//            logger.e(nfe);
//        }
//        return defaultValue;
//    }

//    public static boolean getSafeBooleanValue(String value, boolean defaultValue) {
//        try {
//            if (!isNullOrEmpty(value)) {
//                return Boolean.valueOf(value);
//            }
//        } catch (NumberFormatException nfe) {
//            logger.e(nfe);
//        }
//        return defaultValue;
//    }

    public static String[] makeStringFrom(Context context, int... resIds) {
        if (context == null || resIds == null || resIds.length == 0) {
            return new String[]{};
        }
        String[] strings = new String[resIds.length];
        for (int index = 0; index < resIds.length; index++) {
            strings[index] = context.getString(resIds[index]);
        }
        return strings;
    }


    public final static String format(String formatString, Object... args) {
        return String.format(formatString, args);
    }

    public static String removeZeroWidthSpace(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return str.replaceAll("[\\p{Cf}]", "");
    }

    public static String cutByBytes(String text, int limit, String charset) {
        Charset CHARSET = Charset.forName(charset);
        byte[] bytes = text.getBytes(CHARSET);
        CharsetDecoder decoder = CHARSET.newDecoder();

        decoder.onMalformedInput(CodingErrorAction.IGNORE);
        decoder.reset();

        try {
            CharBuffer decoded = decoder.decode(ByteBuffer.wrap(bytes, 0, limit));
            return decoded.toString();
        } catch (Exception exception) {
            return "";
        }
    }
}
