package jp.mts.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public final class DateUtils {

	public static String format(Date date, String pattern) {
		if(date == null) return null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static String prettyYmd(Date date) {
		return format(date, "yyyy/MM/dd (E)");
	}

	public static Date parse(String property, String pattern) {
		if(StringUtils.isEmpty(property)) return null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setLenient(false);
		try {
			return format.parse(property);
		} catch (ParseException e) {
			return null;
		}
	}
}
