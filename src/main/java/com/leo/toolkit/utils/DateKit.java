package com.leo.toolkit.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class DateKit {

    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    public static void main(String[] args) {
        Date date = pareDate("2022-12-31 12:59");
        System.out.println(date);
    }

    /**
     * 输出startTime（含）和endTime（含）之间的所有日期
     *
     * @param startTime 开始时间 如 2022-01-01
     * @param endTime   结束时间 如 2022-03-01
     * @return List<String> startTime（含）和endTime（不含）之间的所有日期，输出日期格式为 yyyy-MM-dd
     */
    public static List<String> getDateList(String startTime, String endTime) {
        try {
            List<String> dateList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startTime);// 定义起始日期
            Date endDate = sdf.parse(endTime);// 定义结束日期
            Calendar dd = Calendar.getInstance();// 定义日期实例
            dd.setTime(startDate);// 设置日期起始时间
            while (dd.getTime().compareTo(endDate) <= 0) {
                dateList.add(sdf.format(dd.getTime()));
                dd.add(Calendar.DATE, 1);
            }
            return dateList;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static Date pareDate(String date) {
        try {
            return DateUtils.parseDate(date, parsePatterns);
        } catch (Exception e) {
            log.error("parse date failed: {}", date);
            return null;
        }
    }

}
