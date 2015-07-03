package jp.mts.authaccess.test.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {
	
	public static Date dateTime(String value){
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		try {
			return format.parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
	}
}