package com.mycode.base.retrofitextension.call;

import android.util.Log;

import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by kyunghoon on 2019-05-16
 */
class PreloadFileUtility {
    private static final String TAG = "PreloadFileUtility";

    static void deleteIfTooOld(long expireTimeMillis, File file) {
        if (file == null || !file.exists()) {
            return;
        }

        long howLong = System.currentTimeMillis() - file.lastModified();
        if (howLong > expireTimeMillis) {
            file.delete();
        }
    }

    static String readTextFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        FileReader fileReader = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        try {
            fileReader = new FileReader(filePath);
            reader = new BufferedReader(fileReader);

            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return builder.toString();
    }

    static void appendTextToFile(String text, File file, boolean append) {
        if (text == null || text.isEmpty()) {
            Log.d(TAG, "### appendTextToFile : first parameter text is null");
            return;
        }

        if (!makeFileIfNotExist(file)) {
            Log.d(TAG, "### appendTextToFile : makeFileIfNotExist was failed");
            return;
        }

        FileOutputStream output = null;
        try {
            byte data[] = text.getBytes();
            output = new FileOutputStream(file, append);
            output.write(data);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    static boolean hasValidParentFolder(File file) {
        File parent = file.getParentFile();
        return parent != null && parent.exists() && parent.isDirectory();
    }

    static boolean makeFileIfNotExist(File file) {
        if (file.exists()) {
            return true;
        }

        if (!hasValidParentFolder(file)) {
            Log.d(TAG, "### makeFileIfNotExist : invalid Parent Folder");
            return false;
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
        return true;
    }


    static boolean deleteIfExist(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        return file.delete();
    }

    static void cleanDirectory(File directory) {
        if (directory == null || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    cleanDirectory(file);
                    file.delete();
                } else {
                    file.delete();
                }
            }
        }
    }

    /**
     * 오래된 것이 앞으로 정렬됨
     */
    static void sortByOldFile(List files) {
        if (CollectionUtils.isEmpty(files)) {
            return;
        }

        Collections.sort(files, (File o1, File o2) -> {
            if (o1.lastModified() < o2.lastModified()) {
                return -1;
            } else if (o1.lastModified() > o2.lastModified()) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    static void deleteOldFileIfTooMany(File directory, int maxNumberOfFiles) {
        if (directory == null || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null && files.length > maxNumberOfFiles) {
            List<File> fileList = new ArrayList<>(Arrays.asList(files));
            sortByOldFile(fileList);
            while (fileList.size() > maxNumberOfFiles) {
                deleteIfExist(fileList.get(0));
                fileList.remove(0);
            }
        }
    }

}
