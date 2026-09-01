package com.gm.ace.common.convert;

import com.gm.ace.common.constant.AceConst;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

/**
 * 通用类型转换器，供 MapStruct Mapper 通过 {@code uses = BaseConverter.class} 引用
 * <p>
 * MapStruct 未内置 Date / LocalDateTime / LocalDate / YearMonth 之间的互转，故集中在此实现。
 * 全部按 {@link AceConst#TIME_ZONE} 转换，避免隐式依赖 JVM 默认时区。
 *
 * @author guoym
 */
public interface BaseConverter {

    /** Date → LocalDateTime */
    static LocalDateTime dateToLocalDateTime(Date date) {
        return date == null ? null : date.toInstant().atZone(AceConst.TIME_ZONE.toZoneId()).toLocalDateTime();
    }

    /** Date → LocalDate */
    static LocalDate dateToLocalDate(Date date) {
        return date == null ? null : date.toInstant().atZone(AceConst.TIME_ZONE.toZoneId()).toLocalDate();
    }

    /** LocalDate → Date */
    static Date localDateToDate(LocalDate localDate) {
        return localDate == null ? null : Date.from(localDate.atStartOfDay(AceConst.TIME_ZONE.toZoneId()).toInstant());
    }

    /** LocalDateTime → Date */
    static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Date.from(localDateTime.atZone(AceConst.TIME_ZONE.toZoneId()).toInstant());
    }

    /** String → YearMonth（MapStruct 无内置转换） */
    static YearMonth stringToYearMonth(String yearMonth) {
        return (yearMonth == null || yearMonth.isBlank()) ? null : YearMonth.parse(yearMonth);
    }

    /** YearMonth → String */
    static String yearMonthToString(YearMonth yearMonth) {
        return yearMonth == null ? null : yearMonth.toString();
    }

    /** LocalDateTime → 毫秒时间戳 */
    static Long localDateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
