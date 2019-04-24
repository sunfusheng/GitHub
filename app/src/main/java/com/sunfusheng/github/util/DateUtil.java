package com.sunfusheng.github.util;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sunfusheng on 2017/1/17.
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {
    public static final long SECOND = 1000;
    public static final long MINUTE_2_SECOND = SECOND * 60;
    public static final long HOUR_2_SECOND = MINUTE_2_SECOND * 60;
    public static final long DAY_2_SECOND = HOUR_2_SECOND * 24;
    public static final long WEEK_2_SECOND = DAY_2_SECOND * 7;
    public static final long MONTH_2_SECOND = DAY_2_SECOND * 31;
    public static final long YEAR_2_SECOND = DAY_2_SECOND * 365;

    public static final long BEIJING_SECOND = HOUR_2_SECOND * 8;

    public static Date formatDate2Date(String dateStr, String pattern) {
        java.text.DateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String formatDate2String(String dateStr, String pattern) {
        java.text.DateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.format(dateFormat.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(new Date());
    }

    public static long getMilliSeconds(String dateStr, String pattern) {
        return formatDate2Date(dateStr, pattern).getTime();
    }

    public static String formatDate(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        if (isInTheSameDay(calendar, Calendar.getInstance())) {
            return DateFormat.format("今天 kk:mm", calendar).toString();
        } else if (isDateBefore(milliseconds, 1)) {
            return "昨天";
        } else if (isThisYear(calendar, Calendar.getInstance())) {
            return DateFormat.format(FORMAT.format(FORMAT.Md), calendar).toString();
        } else {
            return DateFormat.format(FORMAT.format(FORMAT.yyyyMMdd), calendar).toString();
        }
    }

    public static String formatTimeAgo(long milliSeconds, boolean isEnglish) {
        long year = milliSeconds / YEAR_2_SECOND;
        milliSeconds %= YEAR_2_SECOND;
        long month = milliSeconds / MONTH_2_SECOND;
        milliSeconds %= MONTH_2_SECOND;
        long day = milliSeconds / DAY_2_SECOND;
        milliSeconds %= DAY_2_SECOND;
        long hour = milliSeconds / HOUR_2_SECOND;
        milliSeconds %= HOUR_2_SECOND;
        long minute = milliSeconds / MINUTE_2_SECOND;

        if (year > 0) {
            return year + (isEnglish ? " years ago" : "年前");
        } else if (month > 0) {
            return month + (isEnglish ? " months ago" : "月前");
        } else if (day > 0) {
            return day + (isEnglish ? " days ago" : "天前");
        } else if (hour > 0) {
            return hour + (isEnglish ? " hours ago" : "小时前");
        } else if (minute > 0) {
            return minute + (isEnglish ? " minutes ago" : "分钟前");
        }
        return milliSeconds / SECOND + (isEnglish ? " seconds ago" : "秒前");
    }

    public static boolean isThisYear(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    public static boolean isInTheSameDay(long dayFirst, long daySecond) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(dayFirst);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(daySecond);
        return isInTheSameDay(c1, c2);
    }

    public static boolean isInTheSameDay(Calendar c1, Calendar c2) {
        return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    public static boolean isDateBefore(long time, int before) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -before);
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        now.clear();
        now.set(year, month, day);
        return now.getTimeInMillis() <= time;
    }

    public enum FORMAT {
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

        public static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal();

        FORMAT() {
        }

        public SimpleDateFormat getFormat() {
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) threadLocal.get();
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

        public static String format(DateUtil.FORMAT format) {
            switch (format) {
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
