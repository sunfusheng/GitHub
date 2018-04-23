package com.sunfusheng.github.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunfusheng on 2017/1/17.
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    public static final String FORMAT = "yyyy-MM-dd";

    // 把日期转为字符串
    public static String convertDate2String(Date date) {
        DateFormat df = new SimpleDateFormat(FORMAT);
        return df.format(date);
    }

    // 把字符串转为日期
    public static Date convertString2Date(String dateStr) {
        DateFormat df = new SimpleDateFormat(FORMAT);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    // 把字符串转为日期
    public static String convertString2String(String dateStr) {
        DateFormat df = new SimpleDateFormat(FORMAT);
        try {
            return df.format(df.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static enum TL_FORMAT {
        AHMM,
        YESTERDAY {
            public String format(long time) {
                return super.format(time);
            }
        },
        YESTERDAYA {
            public String format(long time) {
                return super.format(time);
            }
        },
        MD,
        MD2,
        MMDDYYYY,
        MMDDYYYY4,
        WEEK,
        MDHM,
        YYYYMd,
        Md,
        HHmm,
        HHmmss,
        yyyyMMddHHmm,
        yyyyMMddHHmm2,
        yyyyMMddHHmmss,
        yyyyMMddHHmmss2,
        MMddHHmm,
        MMddHHmmss,
        yyyyMMdd;

        private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal();

        private TL_FORMAT() {
        }

        private SimpleDateFormat getFormat() {
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat)threadLocal.get();
            if (null == simpleDateFormat) {
                simpleDateFormat = new SimpleDateFormat(format(this));
                threadLocal.set(simpleDateFormat);
            }

            simpleDateFormat.applyPattern(format(this));
            return simpleDateFormat;
        }

        public String format(long time) {
            return this.getFormat().format(new Date(time));
        }

        private static String format(DateUtil.TL_FORMAT tlFormat) {
            switch(tlFormat) {
                case AHMM:
                    return "a h:mm";
                case YESTERDAY:
                    return "'昨天'";
                case YESTERDAYA:
                    return "'昨天' a";
                case MD:
                    return "M/d";
                case MD2:
                    return "M/d";
                case MMDDYYYY:
                    return "MM/dd/yyyy";
                case MMDDYYYY4:
                    return "yyyy年M月d日 a hh:mm";
                case WEEK:
                    return "EEEE";
                case MDHM:
                    return "M/d HH:mm";
                case YYYYMd:
                    return "yyyy年M月d日";
                case Md:
                    return "M月d日";
                case HHmm:
                    return "HH:mm";
                case HHmmss:
                    return "HH:mm:ss";
                case yyyyMMddHHmm:
                    return "yyyy年MM月dd日 kk:mm";
                case yyyyMMddHHmmss:
                    return "yyyy年MM月dd日 HH:mm:ss";
                case yyyyMMddHHmmss2:
                    return "yyyy-MM-dd HH:mm:ss";
                case yyyyMMddHHmm2:
                    return "yyyy-MM-dd HH:mm";
                case MMddHHmm:
                    return "MM-dd HH:mm";
                case MMddHHmmss:
                    return "MM-dd HH:mm:ss";
                case yyyyMMdd:
                    return "yyyy-MM-dd";
                default:
                    return "";
            }
        }
    }
}
