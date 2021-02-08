package com.mycode.base.retrofitextension;

import android.content.Context;

import com.mycode.base.retrofitextension.call.PreloadFileNameCreator;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by kyunghoon on 2021-01-28
 * <p>
 * Preference 에서 Context 를 필요로 해서 임시 클래스를 만들었습니다.
 * 각 프로젝트 Application Class 에서 세팅해주시면 됩니다.
 */
public class Initializer {
    private static final String DEFAULT_USER_UNIQUE_ID = "NOT_SET_UNIQUE_ID";

    private Context mContext;
    private String mApiBaseUrl;

    private boolean mIsDebug;
    private int mVersionCode;
    private File mPreLoadFileFolder;
    private UserUniqueIdGetter mUserUniqueIdGetter;

    private static Initializer sInstance;
    private static final Object KEY = new Object();

    public static Initializer getInstance() {
        if (sInstance == null) {
            synchronized (KEY) {
                if (sInstance == null) {
                    sInstance = new Initializer();
                }
            }
        }
        return sInstance;
    }

    /**
     * 필수!
     *
     * @param context
     * @param apiBaseUrl
     */
    public void init(Context context,
                     String apiBaseUrl,
                     int versionCode,
                     boolean isDebug) {
        mContext = context;
        mApiBaseUrl = apiBaseUrl;
        mVersionCode = versionCode;
        mIsDebug = isDebug;
    }

    /**
     * @param preLoadFileFolder
     * @param userUniqueIdGetter      : 앱 실행 중 아이디가 바뀔 수 있습니다. 고려해서 넣어주세요. (ex : NLoginManager.getEffectiveId())
     * @param invalidParametersForKey : {@link com.mycode.base.retrofitextension.call.PreloadFileNameCreator#INVALID_PARAMETERS_FOR_KEY} 참고
     */
    public void initPreLoadSpec(@NotNull File preLoadFileFolder,
                                @Nullable UserUniqueIdGetter userUniqueIdGetter,
                                List<String> invalidParametersForKey) {
        this.mPreLoadFileFolder = preLoadFileFolder;
        this.mUserUniqueIdGetter = userUniqueIdGetter;
        if (invalidParametersForKey != null) {
            PreloadFileNameCreator.INVALID_PARAMETERS_FOR_KEY.addAll(invalidParametersForKey);
        }
    }

    public Context getContext() {
        return mContext;
    }

    public String getApiBaseUrl() {
        return mApiBaseUrl;
    }

    public int getVersionCode() {
        return mVersionCode;
    }

    public boolean isDebug() {
        return mIsDebug;
    }

    public String getUserUniqueId() {
        if (mUserUniqueIdGetter != null) {
            return mUserUniqueIdGetter.getUserUniqueId();
        }
        return DEFAULT_USER_UNIQUE_ID;
    }

    @Nullable
    public File getApiPreLoadFolder() {
        // ex. StorageFactory.getInstance().getPreferCacheDir(CacheStorageType.API_PRELOAD)
        return mPreLoadFileFolder;
    }

    public interface UserUniqueIdGetter {
        String getUserUniqueId();
    }

}
