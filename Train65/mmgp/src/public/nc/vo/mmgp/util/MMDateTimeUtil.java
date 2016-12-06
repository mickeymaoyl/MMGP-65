package nc.vo.mmgp.util;

import java.util.Calendar;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFTime;

/**
 * <b> ����ʱ����㹫���� </b>
 * <p>
 * ���ɶ����ں�ʱ����㹦�ܵ���
 * </p>
 * ��������:2011-3-4
 * 
 * @author ������
 */
public final class MMDateTimeUtil {
	/**
	 * ��ʼ������
	 */
	public MMDateTimeUtil() {

	}

	/**
	 * ���Ϊ�շ���Ĭ��ֵ
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
	 * ��Calendar������ַ���,��ʽΪYYYY
	 * 
	 * @param caFromCalendar
	 *            Calendar
	 * @return String
	 */
	public static String getYearFromCalendar(Calendar caFromCalendar) {
		return Integer.toString(caFromCalendar.get(Calendar.YEAR));
	}

	/**
	 * ��Calendar������ַ���,��ʽΪMM
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
	 * ��Calendar������ַ���,��ʽΪDD
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
	 * ��Calendar���ʱ���ַ���,��ʽΪHH24:MI:SS
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
	 * ��Calendar���24��ȫСʱ�ַ���,��ʽΪSS��������λʱǰ��0
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
	 * ��Calendar���ȫ�����ַ���,��ʽΪMI��������λʱǰ��0
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
	 * ��Calendar���ȫ���ַ���,��ʽΪSS��������λʱǰ��0
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
	 * ��Calendar���Сʱ�ͷ����ַ���,��ʽΪHH24:MI
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
	 * UFDateTime�����ӼӼ�������
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
	 * UFDateTime��Сʱ�Ӽ�������(24Сʱ��)
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
	 * UFDateTime����Ӽ�������
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
	 * UFDateTime���ռӼ�������
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
	 * UFDateTime���¼Ӽ�������
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
	 * UFDateTime����Ӽ�������
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
	 * ��UFDateTime���24��ȫСʱ�ַ���,��ʽΪSS��������λʱǰ��0
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
	 * ��UFDateTime���ȫ�����ַ���,��ʽΪMI��������λʱǰ��0
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
	 * ��UFDateTime���ȫ���ַ���,��ʽΪSS��������λʱǰ��0
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
	 * ��UFDateTime���Сʱ�ͷ����ַ���,��ʽΪHH24:MI
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
