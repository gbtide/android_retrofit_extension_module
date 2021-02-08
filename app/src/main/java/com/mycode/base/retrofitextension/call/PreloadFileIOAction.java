package com.mycode.base.retrofitextension.call;

import android.util.Log;

import com.mycode.base.retrofitextension.GsonInstance;
import com.mycode.base.retrofitextension.Initializer;

import java.io.File;

import androidx.annotation.Nullable;
import retrofit2.Response;

/**
 * Created by kyunghoon on 2019-05-16
 */
class PreloadFileIOAction<T> {
    private static final String TAG = "PreloadFileIOAction";

    private static final long CACHE_FILE_EXPIRE_TIME = 1000L * 60L * 60L * 24L * 15L;    // 15일
    private static final int MAX_NUMBER_OF_CACHE_FILES = 100;

    private String mUrl;
    private Class<?> mRawType;
    private boolean mCannotAccessPreloadFolder = false;
    private boolean mIsFileloadDisabled = false;

    PreloadFileIOAction(String url, Class<?> rawType, boolean disableFileload) {
        this.mUrl = url;
        this.mRawType = rawType;
        this.mIsFileloadDisabled = disableFileload;

        checkValidation();
    }

    private void checkValidation() {
        try {
            File folder = getPreloadFolder();
            if (folder != null) {
                Log.d(TAG, "###N preload folder path is : " + folder.getAbsolutePath());
                mCannotAccessPreloadFolder = false;
                return;
            }
        } catch (Throwable e) {
        }
        Log.d(TAG, "###N failed to get preload folder!");
        mCannotAccessPreloadFolder = true;
    }

    @Nullable
    public Response<T> load() {
        if (mCannotAccessPreloadFolder) {
            return null;
        }

        if (mIsFileloadDisabled) {
            return null;
        }

        Response<T> response = null;
        try {
            Log.d(TAG, "###N start preload : " + mUrl);

            // 1. 앱 업데이트 시 (or 데이터 삭제 시) 기존 파일들 모두 제거
            int currentVersionCode = Initializer.getInstance().getVersionCode();
            int savedVersionCode = PreloadPreference.getInstance().getVersionCode();
            if (currentVersionCode != savedVersionCode) {
                PreloadFileUtility.cleanDirectory(getPreloadFolder());
                PreloadPreference.getInstance().saveVersionCode(currentVersionCode);
            }

            // 2. 파일에서 가져오되, 너무 오래된 파일은 제거
            String filePath = getFilePath(mUrl);
            PreloadFileUtility.deleteIfTooOld(CACHE_FILE_EXPIRE_TIME, new File(filePath));
            String responseString = PreloadFileUtility.readTextFromFile(filePath);
            if (responseString == null) {
                Log.d(TAG, "###N preload file is empty");
                return null;
            }

            T body = (T) GsonInstance.get().fromJson(responseString, mRawType);
            response = Response.success(body);

        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }

        return response;
    }

    public void save(Response<T> response) {
        if (mCannotAccessPreloadFolder) {
            return;
        }

        try {
            Log.d(TAG, "###N save Response for preload : " + mUrl);
            String responseText = GsonInstance.get().toJson(response.body());

            // 1. 네트워크에서 받은 response 파일에 저장
            File file = new File(getFilePath(mUrl));
            PreloadFileUtility.deleteIfExist(file);
            PreloadFileUtility.makeFileIfNotExist(file);
            PreloadFileUtility.appendTextToFile(responseText, file, false);

            // 2. 너무 파일이 많이 저장되어있으면 가장 오래된 파일을 제거합니다.
            PreloadFileUtility.deleteOldFileIfTooMany(file.getParentFile(), MAX_NUMBER_OF_CACHE_FILES);

        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void saveAsync(Response<T> response) {
        new Thread(() -> {
            save(response);
        }).start();
    }

    @Nullable
    private File getPreloadFolder() {
        return Initializer.getInstance().getApiPreLoadFolder();
    }

    private String getFilePath(String url) {
        return getPreloadFolder().getAbsolutePath() + "/" + PreloadFileNameCreator.create(url);
    }


}
