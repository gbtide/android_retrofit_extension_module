package com.mycode.base.retrofitextension.call;

import android.net.Uri;

import com.mycode.base.retrofitextension.Initializer;
import com.mycode.base.retrofitextension.utility.ShaUtility;
import com.mycode.base.retrofitextension.utility.StringUtility;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyunghoon on 2019-05-20
 */
public class PreloadFileNameCreator {

    /**
     * memo. ts 등 매번 변화하는 파라미터는 key 로 사용할 수 없으므로, 본 변수에 정의해줍니다.
     * 2019-05-23 기준, 변경이 많은 파라미터를 OK HTTP Interceptor 에서 붙여주고 있고, 그 밖의 변경이 많은 파라미터는 아직 못찾아서 비어있습니다.
     */
    public static List<String> INVALID_PARAMETERS_FOR_KEY = new ArrayList<>();


    static String create(String url) {
        String key = makeKeyFrom(url);
        String encryptedKey = ShaUtility.md5(key);
        String fileName = StringUtility.format(".c_%s.tmp", encryptedKey);
        return fileName;
    }

    private static String makeKeyFrom(String url) {
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        if (!StringUtility.isNullOrEmpty(scheme)) {
            url = url.replace(scheme, "");
        }

        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (Exception e) {
            return "";
        }

        for (String param : INVALID_PARAMETERS_FOR_KEY) {
            removeParam(param + "=", url);
        }

        // User 별로 Preload file 을 관리하기 위해 Key 에 userId 를 포함시킴.
        String key = Initializer.getInstance().getUserUniqueId() + "_" + url.trim();
        return key;
    }

    private static String removeParam(String param, String url) {
        int idx = url.indexOf(param);
        if (idx > 0) {
            String prefix = url.substring(0, idx);
            String last = url.substring(idx);

            int end = last.indexOf("&");
            if (end > 0) {
                url = prefix + last.substring(end);
            } else {
                url = prefix;
            }
        }

        return url;
    }

}
