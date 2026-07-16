package com.springbootinit.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * SQL 工具
 *
 */
public class SqlUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField
     * @return
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }

    /**
     * 为 QueryWrapper 添加日期时间范围筛选（线程安全）
     *
     * @param wrapper   查询包装器
     * @param column    数据库列名，如 "createTime"、"updateTime"
     * @param startTime 开始时间字符串，格式 yyyy-MM-dd HH:mm:ss，为空则跳过
     * @param endTime   结束时间字符串，格式 yyyy-MM-dd HH:mm:ss，为空则跳过
     */
    public static <T> void applyDateTimeRange(QueryWrapper<T> wrapper, String column,
                                              String startTime, String endTime) {
        if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
            return;
        }
        if (StringUtils.isNotBlank(startTime)) {
            try {
                Date date = Date.from(LocalDateTime.parse(startTime, DATE_TIME_FORMATTER)
                        .atZone(ZoneId.systemDefault()).toInstant());
                wrapper.ge(column, date);
            } catch (Exception ignored) {
            }
        }
        if (StringUtils.isNotBlank(endTime)) {
            try {
                Date date = Date.from(LocalDateTime.parse(endTime, DATE_TIME_FORMATTER)
                        .atZone(ZoneId.systemDefault()).toInstant());
                wrapper.le(column, date);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 为 QueryWrapper 添加日期范围筛选（线程安全，只比较日期部分）
     *
     * @param wrapper   查询包装器
     * @param column    数据库列名，如 "startTime"、"endTime"
     * @param startTime 开始日期字符串，格式 yyyy-MM-dd，为空则跳过
     * @param endTime   结束日期字符串，格式 yyyy-MM-dd，为空则跳过
     */
    public static <T> void applyDateRange(QueryWrapper<T> wrapper, String column,
                                          String startTime, String endTime) {
        if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
            return;
        }
        if (StringUtils.isNotBlank(startTime)) {
            try {
                String dateStr = startTime.length() > 10 ? startTime.substring(0, 10) : startTime;
                Date date = Date.from(LocalDate.parse(dateStr, DATE_FORMATTER)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                wrapper.ge(column, date);
            } catch (Exception ignored) {
            }
        }
        if (StringUtils.isNotBlank(endTime)) {
            try {
                String dateStr = endTime.length() > 10 ? endTime.substring(0, 10) : endTime;
                Date date = Date.from(LocalDate.parse(dateStr, DATE_FORMATTER)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                wrapper.le(column, date);
            } catch (Exception ignored) {
            }
        }
    }
}
