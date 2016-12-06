package nc.vo.mmgp.util;

import java.util.Calendar;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFTime;

/**
 * <b> 日期时间计算公共类 </b>
 * <p>
 * 集成对日期和时间计算功能的类
 * </p>
 * 创建日期:2011-3-4
 * 
 * @author 代国勇
 */
public final class MMDateTimeUtil {
	/**
	 * 初始化方法
	 */
	public MMDateTimeUtil() {

	}

	/**
	 * 如果为空返回默认值
	 * 
	 * @param dt
	 * @param defaultDt
	 * @return
	 */
	public static UFDateTime isNull(UFDateTime dt, UFDateTime defaultDt) {
		if (dt == null) {
			return defaultDt;
		}
		return dt;
	}

	/**
	 * 从Calendar获得年字符串,格式为YYYY
	 * 
	 * @param caFromCalendar
	 *            Calendar
	 * @return String
	 */
	public static String getYearFromCalendar(Calendar caFromCalendar) {
		return Integer.toString(caFromCalendar.get(Calendar.YEAR));
	}

	/**
	 * 从Calendar获得月字符串,格式为MM
	 * 
	 * @param caFromCalendar
	 *            Calendar
	 * @return String
	 */
	public static String getMonthFromCalendar(Calendar caFromCalendar) {
		String sTmp = null;

		sTmp = Integer.toString(caFromCalendar.get(Calendar.MONTH));
		if (sTmp.length() < 2) {
			sTmp = "0" + sTmp;
		}
		return sTmp;
	}

	/**
	 * 从Calendar获得日字符串,格式为DD
	 * 
	 * @param caFromCalendar
	 *            Calendar
	 * @return String
	 */
	public static String getDayFromCalendar(Calendar caFromCalendar) {
		String sTmp = null;

		sTmp = Integer.toString(caFromCalendar.get(Calendar.DATE));
		if (sTmp.length() < 2) {
			sTmp = "0" + sTmp;
		}
		return sTmp;
	}

	/**
	 * 从Calendar获得时间字符串,格式为HH24:MI:SS
	 * 
	 * @param caFromCalendar
	 *            Calendar
	 * @return String
	 */
	public static String getTimeFromCalendar(Calendar caFromCalendar) {
		return getHour24FullFromCalendar(caFromCalendar) + ":"
				+ getMinuteFullFromCalendar(caFromCalendar) + ":"
				+ getSecondFullFromCalendar(caFromCalendar);
	}

	/**
	 * 从Calendar获得24制全小时字符串,格式为SS，不足两位时前补0
	 * 
	 * @param caFromCalendar
	 *            Calendar
	 * @return String
	 */
	public static String getHour24FullFromCalendar(Calendar caFromCalendar) {
		String sTmp = null;
		String sRet = "";

		sTmp = Integer.toString(caFromCalendar.get(Calendar.HOUR_OF_DAY));
		if (sTmp.length() < 2) {
			sTmp = "0" + sTmp;
		}
		sRet += sTmp;

		return sRet;
	}

	/**
	 * 从Calendar获得全分钟字符串,格式为MI，不足两位时前补0
	 * 
	 * @param caFromCalendar
	 *            Calendar
	 * @return String
	 */
	public static String getMinuteFullFromCalendar(Calendar caFromCalendar) {
		String sTmp = null;
		String sRet = "";

		sTmp = Integer.toString(caFromCalendar.get(Calendar.MINUTE));
		if (sTmp.length() < 2) {
			sTmp = "0" + sTmp;
		}
		sRet += sTmp;

		return sRet;
	}

	/**
	 * 从Calendar获得全秒字符串,格式为SS，不足两位时前补0
	 * 
	 * @param caFromCalendar
	 *            Calendar
	 * @return String
	 */
	public static String getSecondFullFromCalendar(Calendar caFromCalendar) {
		String sTmp = null;
		String sRet = "";

		sTmp = Integer.toString(caFromCalendar.get(Calendar.SECOND));
		if (sTmp.length() < 2) {
			sTmp = "0" + sTmp;
		}
		sRet += sTmp;

		return sRet;
	}

	/**
	 * 从Calendar获得小时和分钟字符串,格式为HH24:MI
	 * 
	 * @param caFromCalendar
	 *            Calendar
	 * @return String
	 */
	public static String getHour24AndMinuteFromCalendar(Calendar caFromCalendar) {
		return getHour24FullFromCalendar(caFromCalendar) + ":"
				+ getMinuteFullFromCalendar(caFromCalendar);
	}

	/**
	 * UFDateTime按分钟加减法操作
	 * 
	 * @param dateTime
	 *            UFDateTime
	 * @param toAddd
	 *            int
	 * @return UFDateTime
	 */
	public static UFDateTime ufDateTimeAddMinites(UFDateTime dateTime,
			int toAddd) {
		Calendar caTmp = Calendar.getInstance();
		caTmp.setTimeInMillis(dateTime.getMillis());
		caTmp.add(Calendar.MINUTE, toAddd);
		return new UFDateTime(caTmp.getTime());
	}

	/**
	 * UFDateTime按小时加减法操作(24小时制)
	 * 
	 * @param dateTime
	 *            UFDateTime
	 * @param toAddd
	 *            int
	 * @return UFDateTime
	 */
	public static UFDateTime ufDateTimeAddHours(UFDateTime dateTime, int toAddd) {
		Calendar caTmp = Calendar.getInstance();
		caTmp.setTimeInMillis(dateTime.getMillis());
		caTmp.add(Calendar.HOUR_OF_DAY, toAddd);
		return new UFDateTime(caTmp.getTime());
	}

	/**
	 * UFDateTime按秒加减法操作
	 * 
	 * @param dateTime
	 *            UFDateTime
	 * @param toAddd
	 *            int
	 * @return UFDateTime
	 */
	public static UFDateTime ufDateTimeAddSeconds(UFDateTime dateTime,
			int toAddd) {
		Calendar caTmp = Calendar.getInstance();
		caTmp.setTimeInMillis(dateTime.getMillis());
		caTmp.add(Calendar.SECOND, toAddd);
		return new UFDateTime(caTmp.getTime());
	}

	/**
	 * UFDateTime按日加减法操作
	 * 
	 * @param dateTime
	 *            UFDateTime
	 * @param toAddd
	 *            int
	 * @return UFDateTime
	 */
	public static UFDateTime ufDateTimeAddDays(UFDateTime dateTime, int toAddd) {
		Calendar caTmp = Calendar.getInstance();
		caTmp.setTimeInMillis(dateTime.getMillis());
		caTmp.add(Calendar.DAY_OF_MONTH, toAddd);
		return new UFDateTime(caTmp.getTime());
	}

	public static UFDate ufDateAddDays(UFDate date, int toAddd) {
		return ufDateTimeAddDays(new UFDateTime(date, new UFTime("00:00:00")),
				toAddd).getDate();
	}

	/**
	 * UFDateTime按月加减法操作
	 * 
	 * @param dateTime
	 *            UFDateTime
	 * @param toAddd
	 *            int
	 * @return UFDateTime
	 */
	public static UFDateTime ufDateTimeAddMonths(UFDateTime dateTime, int toAddd) {
		Calendar caTmp = Calendar.getInstance();
		caTmp.setTimeInMillis(dateTime.getMillis());
		caTmp.add(Calendar.MONTH, toAddd);
		return new UFDateTime(caTmp.getTime());
	}

	/**
	 * UFDateTime按年加减法操作
	 * 
	 * @param dateTime
	 *            UFDateTime
	 * @param toAddd
	 *            int
	 * @return UFDateTime
	 */
	public static UFDateTime ufDateTimeAddYears(UFDateTime dateTime, int toAddd) {
		Calendar caTmp = Calendar.getInstance();
		caTmp.setTimeInMillis(dateTime.getMillis());
		caTmp.add(Calendar.YEAR, toAddd);
		return new UFDateTime(caTmp.getTime());
	}

	/**
	 * 从UFDateTime获得24制全小时字符串,格式为SS，不足两位时前补0
	 * 
	 * @param dateTime
	 *            UFDateTime
	 * @return String
	 */
	public static String getHour24Full(UFDateTime dateTime) {
		String sTmp = null;

		sTmp = Integer.toString(dateTime.getHour());
		if (sTmp.length() < 2) {
			sTmp = "0" + sTmp;
		}

		return sTmp;
	}

	/**
	 * 从UFDateTime获得全分钟字符串,格式为MI，不足两位时前补0
	 * 
	 * @param dateTime
	 *            UFDateTime
	 * @return String
	 */
	public static String getMinuteFull(UFDateTime dateTime) {
		String sTmp = null;

		sTmp = Integer.toString(dateTime.getMinute());
		if (sTmp.length() < 2) {
			sTmp = "0" + sTmp;
		}

		return sTmp;
	}

	/**
	 * 从UFDateTime获得全秒字符串,格式为SS，不足两位时前补0
	 * 
	 * @param dateTime
	 *            UFDateTime
	 * @return String
	 */
	public static String getSecondFull(UFDateTime dateTime) {
		String sTmp = null;

		sTmp = Integer.toString(dateTime.getSecond());
		if (sTmp.length() < 2) {
			sTmp = "0" + sTmp;
		}

		return sTmp;
	}

	/**
	 * 从UFDateTime获得小时和分钟字符串,格式为HH24:MI
	 * 
	 * @param dateTime
	 *            UFDateTime
	 * @return String
	 */
	public static String getHour24AndMinute(UFDateTime dateTime) {
		return getHour24Full(dateTime) + ":" + getMinuteFull(dateTime);
	}

	public static UFDateTime min(UFDateTime... datetimes) {
		if (MMArrayUtil.isEmpty(datetimes)) {
			return null;
		}
		UFDateTime min = datetimes[0];
		for (int i = 1; i < datetimes.length; i++) {
			if (datetimes[i].compareTo(min) < 0) {
				min = datetimes[i];
			}
		}
		return min;
	}

	public static UFDateTime max(UFDateTime... datetimes) {
		if (MMArrayUtil.isEmpty(datetimes)) {
			return null;
		}
		UFDateTime max = datetimes[0];
		for (int i = 1; i < datetimes.length; i++) {
			if (datetimes[i].compareTo(max) > 0) {
				max = datetimes[i];
			}
		}
		return max;
	}
}
