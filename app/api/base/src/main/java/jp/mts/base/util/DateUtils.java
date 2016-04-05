package jp.mts.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {

	public static String format(Date date, String pattern) {
		if(date == null) return "";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static String prettyYmd(Date date) {
		return format(date, "yyyy/MM/dd (E)");
	}
}
