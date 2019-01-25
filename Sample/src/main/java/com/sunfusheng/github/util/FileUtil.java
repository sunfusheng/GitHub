package com.sunfusheng.github.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author by sunfusheng on 2019/1/25
 */
public class FileUtil {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static String convertFileToString(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader bf = null;
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, UTF_8);
            bf = new BufferedReader(isr);
            String readLine = "";
            StringBuilder sb = new StringBuilder();
            while ((readLine = bf.readLine()) != null) {
                sb.append(readLine.trim());
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(fis);
            IoUtil.close(isr);
            IoUtil.close(bf);
        }
        return null;
    }

}
