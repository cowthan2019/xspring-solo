package org.cp4j.core;


import org.apache.http.util.TextUtils;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import android.support.v7.app.NotificationCompat;


/**
 * common utils, like java.lang
 * @author cowthan
 *
 */
public class Lang {


	public static final long millis_for_1_second = 1000;
	public static final long millis_for_1_minute = 60 * millis_for_1_second;
	public static final long millis_for_1_hour = 60 * millis_for_1_minute;
	public static final long millis_for_1_day = 24 * millis_for_1_hour;
	public static final long millis_for_1_month = 30 * millis_for_1_day;
	public static final long millis_for_1_year = 12 * millis_for_1_month;


	// -----------------------------------------------------
	// 判空系列
	// -----------------------------------------------------
	public static boolean isEmpty(Collection<?> c) {
		return c == null || c.isEmpty();
	}

	public static boolean isNotEmpty(Collection<?> c) {
		return !isEmpty(c);
	}

	public static boolean isEmpty(Map<?, ?> c) {
		return c == null || c.isEmpty();
	}

	public static boolean isNotEmpty(Map<?, ?> c) {
		return !isEmpty(c);
	}

	public static <T> boolean isEmpty(T[] c) {
		return c == null || c.length == 0;
	}

	public static <T> boolean isNotEmpty(T[] c) {
		return !isEmpty(c);
	}

	public static boolean isEmpty(CharSequence c) {
		return c == null || c.toString().isEmpty();
	}

	public static boolean isNotEmpty(String c) {
		return !isEmpty(c);
	}

	public static boolean isNull(Object o) {
		return o == null;
	}

	public static boolean isNotNull(Object o) {
		return o != null;
	}

	// -----------------------------------------------------
	// 相等系列
	// -----------------------------------------------------
	public static <T> boolean isEquals(T o1, T o2) {
		if (o1 == null || o2 == null)
			return false;
		return o1.equals(o2);
	}

	public static boolean isEqualsIgnoreCase(String s1, String s2) {
		if (s1 == null || s2 == null) {
			return false;
		} else {
			return s1.equalsIgnoreCase(s2);
		}
	}

	public static <T> boolean isNotEquals(T o1, T o2) {
		if (o1 == null || o2 == null)
			return true;
		return !o1.equals(o2);
	}

	public static boolean isNotEqualsIgnoreCase(String s1, String s2) {
		if (s1 == null || s2 == null) {
			return true;
		} else {
			return !s1.equalsIgnoreCase(s2);
		}
	}

	// -----------------------------------------------------
	// 字符串系列
	// -----------------------------------------------------
	public static String snull(Object maybeNullOrEmpty, String replaceNull) {
		return (maybeNullOrEmpty == null || "".equals(maybeNullOrEmpty)) ? replaceNull : maybeNullOrEmpty.toString();
	}

	public static String snull(Object maybeNull) {
		return snull(maybeNull, "");
	}

	public static int sint(Integer maybeNull, int defaultValue){
		return maybeNull == null ? defaultValue : maybeNull;
	}

	public static int sint(Integer maybeNull){
		return sint(maybeNull, 0);
	}

	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	// -----------------------------------------------------
	// 集合数组长度系列
	// -----------------------------------------------------
	public static int count(Collection<?> c) {
		return c == null ? 0 : c.size();
	}

	public static int count(Map<?, ?> c) {
		return c == null ? 0 : c.size();
	}

	public static <T> int count(T[] c) {
		return c == null ? 0 : c.length;
	}

	public static <T> int count(String s) {
		return s == null ? 0 : s.length();
	}


	// -----------------------------------------------------
	// 类型转换系列
	// -----------------------------------------------------
	public static int toInt(String strInt) {
		try {
			return Integer.parseInt(strInt);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int toIntMaybeDouble(String maybeDouble) {
		try {
			return (int) Double.parseDouble(maybeDouble);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int toInt(String strInt, int defaultValue) {
		try {
			return Integer.parseInt(strInt);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static long toLong(String strInt) {
		try {
			return Long.parseLong(strInt);
		} catch (Exception e) {
			return 0;
		}
	}

	public static long toLong(String strInt, int defaultValue) {
		try {
			return Long.parseLong(strInt);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static double toDouble(String strInt) {
		try {
			return Double.parseDouble(strInt);
		} catch (Exception e) {
			return 0;
		}
	}

	public static double toDouble(String strInt, int defaultValue) {
		try {
			return Double.parseDouble(strInt);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static float toFloat(String str) {
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static float toFloat(String str, int defaultValue) {
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	// -----------------------------------------------------
	// 日期转换系列
	// -----------------------------------------------------
	public static String toDate(String pattern, String seconds) {
		return toDate(pattern, toInt(seconds));
	}

	public static String toDate(String pattern, long seconds) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		return sdf.format(new Date(seconds * 1000));
	}

	public static String toDate(String pattern, Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		return sdf.format(date);
	}

	public static long fromDate(String pattern, String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		try {
			return sdf.parse(dateStr).getTime();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 尝试把str的时间格式转为format的格式，通过new Date(str)来转，只能尽量转，失败则返回原来字符串
	 *
	 * @param str 类似Tue May 31 17:46:55 +0800 2011的字符串
	 * @param format
	 */
	public static String tryToDate(String str, String format) {
		try {
			Date date = new Date(str);
			DateFormat df = new SimpleDateFormat(format);
			String s = df.format(date);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		}
	}



	// -----------------------------------------------------
	// 集合系列
	// -----------------------------------------------------
	public static <T> ArrayList<T> newArrayList(T... t) {
		ArrayList<T> list = new ArrayList<T>();
		if (t == null || t.length == 0) {
			return list;
		} else {
			for (int i = 0; i < t.length; i++) {
				list.add(t[i]);
			}
		}
		return list;
	}

	public static <K, V> Map<K, V> newHashMap(Object... args) {
		Map<K, V> m = new HashMap<K, V>();

		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i += 2) {
				int ki = i;
				int vi = i + 1;
				if (ki < args.length && vi < args.length) {
					K k = (K)args[ki];
					V v = (V)args[vi];
					m.put(k, v);
				}
			}
		}
		return m;
	}

	public static <K> Set<K> newHashSet(K... args) {
		Set<K> set = new HashSet<>();

		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				set.add(args[i]);
			}
		}
		return set;
	}

	public static <T> T lastElement(List<T> list) {
		if (list == null || list.size() == 0)
			return null;
		return list.get(list.size() - 1);
	}

	public static <T> T lastElement(T[] list) {
		if (list == null || list.length == 0)
			return null;
		return list[list.length - 1];
	}

	public static <T> T firstElement(List<T> list) {
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	public static <T> T elementAt(List<T> list, int index, T defaultValue) {
		if (list == null || list.size() == 0)
			return defaultValue;
		if (index < 0 || index >= list.size())
			return defaultValue;
		return list.get(index);
	}

	public static <T> T elementAt(List<T> list, int index) {
		if (list == null || list.size() == 0)
			return null;
		if (index < 0 || index >= list.size())
			return null;
		return list.get(index);
	}

	public interface OnWalk<T> {

		/**
		 * @param index current index
		 * @param t current element
		 * @param total list.size()
		 */
		boolean process(int index, T t, int total);

	}

	public interface Func<IN, OUT> {
		OUT process(IN ele);
	}

	public static <OUT, IN> List<OUT> cast(List<IN> list, Func<IN, OUT> func) {
		if (Lang.isEmpty(list))
			return new ArrayList<>();
		List<OUT> res = new ArrayList<>();
		for (IN ele : list) {
			res.add(func.process(ele));
		}
		return res;
	}

	public static <T> void each(Collection<T> c, OnWalk<T> callback) {
		if (callback == null)
			return;
		if (Lang.isNotEmpty(c)) {
			int count = 0;
			for (T o : c) {
				callback.process(count, o, c.size());
				count++;
			}
		}
	}

	public static <K, V> void each(Map<K, V> c, OnWalk<Map.Entry<K, V>> callback) {
		if (callback == null)
			return;
		if (c == null || c.size() == 0)
			return;

		int count = 0;
		for (Map.Entry<K, V> entry : c.entrySet()) {
			callback.process(count, entry, c.size());
			count++;
		}
	}

	public static <T> Collection<T> combine(Collection<T> c1, Collection<T> c2) {
		if (c1 == null && c2 == null)
			return null;
		if (c1 == null)
			return c2;
		if (c2 == null)
			return c1;
		c1.addAll(c2);
		return c1;
	}

	public static  List<Object> combineIgnoreType(List<Object> c1, List<?> c2) {
		if (c1 == null && c2 == null)
			return null;
		if(c1 == null) c1 = new ArrayList<>();
		for (int i = 0; i < Lang.count(c2); i++) {
			c1.add(c2.get(i));
		}
		return c1;
	}

	public static <T> boolean contains(T[] array, T ele) {
		if (null == array || array.length == 0)
			return false;
		if (ele == null)
			return false;
		for (T e : array) {
			if (isEquals(e, ele))
				return true;
		}
		return false;
	}

	public static <T> boolean contains(List<T> array, T ele) {
		if (null == array || array.size() == 0)
			return false;
		if (ele == null)
			return false;
		return array.contains(ele);
	}

	public static <T> boolean contains(Set<T> array, T ele) {
		if (null == array || array.size() == 0)
			return false;
		if (ele == null)
			return false;
		return array.contains(ele);
	}

	public static <K, V> boolean containsKey(Map<K, V> map, K key) {
		if (null == map || map.size() == 0)
			return false;
		if (key == null)
			return false;
		return map.containsKey(key);
	}

	public static <K, V> boolean containsValue(Map<K, V> map, V value) {
		if (null == map || map.size() == 0)
			return false;
		if (value == null)
			return false;
		return map.containsValue(value);
	}


	// -----------------------------------------------------
	// String创建系列
	// -----------------------------------------------------
	public static String fromStream(InputStream inputStream) {
		String jsonStr = "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		boolean len = false;

		try {
			int len1;
			while ((len1 = inputStream.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, len1);
			}

			jsonStr = new String(out.toByteArray());
		} catch (IOException var6) {
			var6.printStackTrace();
		}

		return jsonStr;
	}


	public interface StringConverter<T> {
		String convert(T t);
	}

	public static <T> List<String> fromList(List<T> list, boolean ignoreNull,
									  StringConverter<T> converter) {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < count(list); ++i) {
			if (list.get(i) == null && ignoreNull) {
				continue;
			}
			result.add(converter.convert(list.get(i)));
		}
		return result;
	}

	public static <T> String fromList(List<T> list, String delemeter, boolean ignoreNull,
									  StringConverter<T> converter) {
		if (Lang.isEmpty(list)) {
			return "";
		} else {
			String res = "";

			for (int i = 0; i < list.size(); ++i) {
				if (!ignoreNull || list.get(i) != null) {
					res = res + Lang.snull(converter.convert(list.get(i)))
							+ (i == list.size() - 1 ? "" : delemeter);
				}
			}

			return res;
		}
	}

    public static <T> String fromList(List<T> list, String delemeter, boolean ignoreNull) {
        return fromList(list, delemeter, ignoreNull, new StringConverter<T>() {
            @Override
            public String convert(T t) {
                return t == null ? null : t.toString();
            }
        });
    }

	public static <T> String fromArray(T[] list, String delemeter, boolean ignoreNull) {
		if (Lang.isEmpty(list)) {
			return "";
		} else {
			String res = "";

			for (int i = 0; i < list.length; ++i) {
				if (!ignoreNull || list[i] != null) {
					res = res + Lang.snull(list[i]) + (i == list.length - 1 ? "" : delemeter);
				}
			}

			return res;
		}
	}

	public static <T> String fromSet(Set<T> set, String delemeter, boolean ignoreNull) {
		if (Lang.isEmpty(set)) {
			return "";
		} else {
			String res = "";
			int i = 0;
			Iterator var5 = set.iterator();

			while (true) {
				while (var5.hasNext()) {
					Object str = var5.next();
					if (ignoreNull && str == null) {
						++i;
					} else {
						res = res + Lang.snull(str) + (i == set.size() - 1 ? "" : delemeter);
						++i;
					}
				}

				return res;
			}
		}
	}

	public static String[] split(String s, String delemeter) {
		return s == null ? null : s.split(delemeter);
	}

	public static List<String> splitToList(String s, String delemeter, boolean ignoreEmpty) {
		if(s == null || "".equals(s)) return new ArrayList<>();
		String[] parts = s.split(delemeter);
		List<String> r = new ArrayList<>();
		for (int i = 0; i < Lang.count(parts); i++) {
			if(ignoreEmpty && Lang.isEmpty(parts[i])){
				continue;
			}else{
				r.add(parts[i]);
			}
		}
		return r;
	}
	public static Set<String> splitToSet(String s, String delemeter, boolean ignoreEmpty) {
		if(s == null || "".equals(s)) return new HashSet<>();
		String[] parts = s.split(delemeter);
		Set<String> r = new HashSet<>();
		for (int i = 0; i < Lang.count(parts); i++) {
			if(ignoreEmpty && Lang.isEmpty(parts[i])){
				continue;
			}else{
				r.add(parts[i]);
			}
		}
		return r;
	}

	public static List<String> splitToList(String s, String delemeter){
		if(Lang.isEmpty(s)) return new ArrayList<>();
		String[] parts = split(s, delemeter);
		List<String> list = new ArrayList<>();
		for (int i = 0; i < Lang.count(parts); i++) {
			list.add(parts[i]);
		}
		return list;
	}

	public static List<String> split(String s, int elementLength) {
		ArrayList list = new ArrayList();
		if (s == null) {
			return list;
		} else {
			if (s.length() <= elementLength) {
				list.add(s);
			} else {
				int start = 0;

				while (start < s.length()) {
					int end = start + elementLength;
					if (end > s.length()) {
						end = s.length();
					}

					String element = s.substring(start, end);
					start = end;
					list.add(element);
				}
			}

			return list;
		}
	}

	public static String trimStart(String s, String trim){
		if(s == null || trim == null) return s;
		if(s.startsWith(trim)){
			return trimStart(s.substring(trim.length()), trim);
		}else{
			return s;
		}
	}

	public static String trimEnd(String s, String trim){
		if(s == null || trim == null) return s;
		if(s.endsWith(trim)){
			return trimEnd(s.substring(0, s.length() - trim.length()), trim);
		}else{
			return s;
		}
	}

	public static String trim(String s, String trim){
		s = trimStart(s, trim);
		return trimEnd(s, trim);
	}



	// -----------------------------------------------------
	// path及uri系列
	// -----------------------------------------------------
	public static String pathToUri(String localPath) {
		if (Lang.isEmpty(localPath))
			return "";
		if (!localPath.startsWith("/"))
			return localPath;
		return "file://" + localPath;
	}





	//----
	public static String readThrowable(Throwable ex) {
		if(ex == null) return "没有传入有效Throwable对象";
		try {
			Writer info = new StringWriter();
			PrintWriter printWriter = new PrintWriter(info);
			ex.printStackTrace(printWriter);

			Throwable cause = ex.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}

			String result = info.toString();
			result = result.replace("\n", "<br/>").replace("\r\n", "<br/>").replace("\r", "<br/>");
			printWriter.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "read throwable faild";
		}

	}



	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}



	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	public static class date{

		private static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		/**
		 * 年月日[2015-07-28]
		 *
		 * @param timeInMills
		 * @return
		 */
		public static String getYmd(long timeInMills) {
			return ymd.format(new Date(timeInMills));
		}

		/**
		 * 是否是今天
		 *
		 * @param timeInMills
		 * @return
		 */
		public static boolean isToday(long timeInMills) {
			String dest = getYmd(timeInMills);
			String now = getYmd(Calendar.getInstance().getTimeInMillis());
			return dest.equals(now);
		}

		/**
		 * 是否是同一天
		 *
		 * @param aMills
		 * @param bMills
		 * @return
		 */
		public static boolean isSameDay(long aMills, long bMills) {
			String aDay = getYmd(aMills);
			String bDay = getYmd(bMills);
			return aDay.equals(bDay);
		}

		/**
		 * 获取年份
		 *
		 * @param mills
		 * @return
		 */
		public static int getYear(long mills) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(mills);
			return calendar.get(Calendar.YEAR);
		}

		/**
		 * 获取月份
		 *
		 * @param mills
		 * @return
		 */
		public static int getMonth(long mills) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(mills);
			return calendar.get(Calendar.MONTH) + 1;
		}


		/**
		 * 获取月份的天数
		 *
		 * @param mills
		 * @return
		 */
		public static int getDaysInMonth(long mills) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(mills);

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);

			switch (month) {
				case Calendar.JANUARY:
				case Calendar.MARCH:
				case Calendar.MAY:
				case Calendar.JULY:
				case Calendar.AUGUST:
				case Calendar.OCTOBER:
				case Calendar.DECEMBER:
					return 31;
				case Calendar.APRIL:
				case Calendar.JUNE:
				case Calendar.SEPTEMBER:
				case Calendar.NOVEMBER:
					return 30;
				case Calendar.FEBRUARY:
					return (year % 4 == 0) ? 29 : 28;
				default:
					throw new IllegalArgumentException("Invalid Month");
			}
		}


		/**
		 * 获取星期,0-周日,1-周一，2-周二，3-周三，4-周四，5-周五，6-周六
		 *
		 * @param mills
		 * @return
		 */
		public static int getWeek(long mills) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(mills);

			return calendar.get(Calendar.DAY_OF_WEEK) - 1;
		}

		/**
		 * 获取当月第一天的时间（毫秒值）
		 *
		 * @param mills
		 * @return
		 */
		public static long getFirstOfMonth(long mills) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(mills);
			calendar.set(Calendar.DAY_OF_MONTH, 1);

			return calendar.getTimeInMillis();
		}
	}

	public static class random{

		static Random _random = new Random(System.currentTimeMillis());

		public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		public static final String NUMBERS = "0123456789";
		public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		public static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";

		public static String getRandomNumbersAndLetters(int length) {
			return getRandom(NUMBERS_AND_LETTERS, length);
		}

		public static String getRandomNumbers(int length) {
			return getRandom(NUMBERS, length);
		}

		public static String getRandomLetters(int length) {
			return getRandom(LETTERS, length);
		}

		public static String getRandomCapitalLetters(int length) {
			return getRandom(CAPITAL_LETTERS, length);
		}

		public static String getRandomLowerCaseLetters(int length) {
			return getRandom(LOWER_CASE_LETTERS, length);
		}

		public static String getRandom(String source, int length) {
			return TextUtils.isEmpty(source) ? null : getRandom(source.toCharArray(), length);
		}

		public static String getRandom(char[] sourceChar, int length) {
			if (sourceChar == null || sourceChar.length == 0 || length < 0) {
				return null;
			}

			StringBuilder str = new StringBuilder(length);

			for (int i = 0; i < length; i++) {
				str.append(sourceChar[_random.nextInt(sourceChar.length)]);
			}
			return str.toString();
		}

		public static int randomInt(int max) {
			return randomInt(0, max);
		}

		public static int randomInt(int min, int max) {
			if (min > max) {
				return 0;
			}
			if (min == max) {
				return min;
			}
			return min + new Random().nextInt(max - min);
		}

	}



	public static class collection{
		public static <T> ArrayList<T> newArrayList(T...t){
			ArrayList<T> list = new ArrayList<T>();
			if(t == null || t.length == 0){
				return list;
			}else{
				for(int i = 0; i < t.length; i++){
					list.add(t[i]);
				}
			}
			return list;
		}

		public static <T> T lastElement(List<T> list){
			if(list == null || list.size() == 0) return null;
			return list.get(list.size() - 1);
		}

		public static <T> T firstElement(List<T> list){
			if(list == null || list.size() == 0) return null;
			return list.get(0);
		}

		/**
		 * delete traitors from ours
		 *
		 * @param <T>
		 * @param ours
		 * @param traitors
		 * @return
		 */
		public static <T> List<T> killTraitors(List<T> ours, List<T> traitors) {
			int len = ours.size();
			for (int i = 0; i < len; i++) {
				if (traitors.contains(ours.get(i))) {
					ours.remove(i);
				}
			}
			return ours;
		}

		public static <T> void each(Collection<T> c, OnWalk<T> callback) {
			if (callback == null)
				return;
			if (Lang.isNotEmpty(c)) {
				int count = 0;
				for (T o : c) {
					callback.process(count, o, c.size());
					count++;
				}
			}
		}
		public static <K, V> void each(Map<K, V> c, OnWalk<Map.Entry<K,V>> callback) {
			if (callback == null)
				return;
			if(c == null || c.size() == 0)
				return;

			int count = 0;
			for (Map.Entry<K, V> entry : c.entrySet()) {
				callback.process(count, entry, c.size());
				count++;
			}
		}

		public static <T> void each(T[] c, OnWalk<T> callback) {
			if (callback == null)
				return;
			if (Lang.isNotEmpty(c)) {
				int count = 0;
				for (T o : c) {
					callback.process(count, o, c.length);
					count++;
				}
			}
		}

		public static <T> void remove(Collection<T> c, OnWalk<T> callback) {
			if (callback == null)
				return;
			if(Lang.isEmpty(c))
				return;
			Iterator<T> it = c.iterator();
			int count = 0;
			while(it.hasNext()){
				boolean willBeDelete = callback.process(count, it.next(), c.size());
				count++;
				if(willBeDelete) it.remove();
			}
//			List<T> list = Lang.collection.clone(c);
//			if (Lang.isNotEmpty(list)) {
//				int count = 0;
//				for (T o : list) {
//					boolean willBeDelete = callback.process(count, o, c.size());
//					count++;
//					if(willBeDelete){
//						c.remove(o);
//					}
//				}
//			}
		}

		public static <K, V> void remove(Map<K, V> c, OnWalk<Map.Entry<K,V>> callback) {
			if (callback == null)
				return;
			if(Lang.isEmpty(c))
				return;
			int count = 0;
			for (Map.Entry<K, V> entry : c.entrySet()) {
				boolean willBeDeleted = callback.process(count, entry, c.size());
				count++;
				if(willBeDeleted) c.remove(entry.getKey());
			}
		}

		public static <T> List<T> clone(List<T> c){
			List<T> list = new ArrayList<T>();
			if (Lang.isNotEmpty(c)) {
				for (T o : c) {
					list.add(o);
				}
			}
			return list;
		}

		public static <T> List<T> clone(T[] c){
			List<T> list = new ArrayList<T>();
			if (Lang.isNotEmpty(c)) {
				for (T o : c) {
					list.add(o);
				}
			}
			return list;
		}


		public static <T> Collection<T> combine(Collection<T> c1,
												Collection<T> c2) {
			if (c1 == null && c2 == null)
				return null;
			if (c1 == null)
				return c2;
			if (c2 == null)
				return c1;
			c1.addAll(c2);
			return c1;
		}

		public static <K, V> Map<K, V> newHashMap(Object...args){
			Map<K, V> m = new HashMap<K, V>();

			if(args != null && args.length > 0){
				for(int i = 0; i < args.length; i+=2){
					int ki = i;
					int vi = i+1;
					if(ki < args.length && vi < args.length){
						K k = (K)args[ki];
						V v = (V)args[vi];
						m.put(k, v);
					}
				}
			}
			return m;
		}

		public static <K> Set<K> newHashSet(K...args){
			Set<K> set = new HashSet<>();

			if(args != null && args.length > 0){
				for(int i = 0; i < args.length; i++){
					set.add(args[i]);
				}
			}
			return set;
		}
	}


	public static void file_put_content(String file, String content){
		if(content == null) return;
		createFileIfNotExists(file);
		PrintWriter pw = null;
		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(file);
			pw = new PrintWriter(fs);
			pw.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(pw != null){
				pw.close();
			}
			if(fs != null){
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void file_append_content(String file, String content){
		if(content == null) return;
		createFileIfNotExists(file);
		PrintWriter pw = null;
		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(file, true);
			pw = new PrintWriter(fs);
			pw.append(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(pw != null){
				pw.close();
			}
			if(fs != null){
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void file_append_content(String file, byte[] content){
		if(content == null) return;
		createFileIfNotExists(file);
		PrintWriter pw = null;
		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(file, true);

			fs.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pw != null){
				pw.close();
			}
			if(fs != null){
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean createFileIfNotExists(String file){
		File f = new File(file);
		File dir = f.getParentFile();
		if(dir != null){
			if(!dir.exists() || !dir.isDirectory()){
				if(!dir.mkdirs()){
					return false;
				}
			}
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}else{
			return false;
		}
	}


	public static String file_get_content(String file){
		File f = new File(file);
		if(!f.exists() || !f.isFile()) return "";
		try {
			FileInputStream fs = new FileInputStream(f);
			byte[] buffer = new byte[1024];
			int len = 0;
			StringBuffer sb =new StringBuffer();
			while((len = fs.read(buffer)) != -1){
				sb.append(new String(buffer, 0, len));
			}
			fs.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static <T> List<T> diff(List<T> prelist, List<T> curlist) {
		List<T> diff = new ArrayList<T>();

		Map<T, Integer> map = new HashMap<>(curlist.size());
		for (T stu : curlist) {
			map.put(stu, 1);
		}
		for (T stu : prelist) {
			if (map.get(stu) != null) {
				map.put(stu, 2);
				continue;
			}
			diff.add(stu);
		}
		for (Map.Entry<T, Integer> entry : map.entrySet()) {
			if (entry.getValue() == 1) {
				diff.add(entry.getKey());
			}
		}
		return diff;
	}

	/**
	 * 获取curlist有，但prelist没有的
	 */
	public static <T> List<T> diffRight(List<T> prelist, List<T> curlist) {
		List<T> diff = new ArrayList<T>();

		Set<T> set = new HashSet<>(curlist.size());
		for (T stu : prelist) {
			set.add(stu);
		}
		for (T stu : curlist) {
			if(!set.contains(stu)) {
				diff.add(stu);
			}
		}
		return diff;
	}

	/**
	 * 获取prelist有，但curlist没有的
	 */
	public static <T> List<T> diffLeft(List<T> prelist, List<T> curlist) {
		return diffRight(curlist, prelist);
	}


	public static <T> T[] arrayOf(T...t){
		return t;
	}

	public static String getRepeatString(String s, int repeatCount, String seperator) {
		if (repeatCount == 0)
			return "";
		if (repeatCount == 1)
			return s;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < repeatCount; i++) {
			sb.append(s);
			if(i < repeatCount - 1){
				sb.append(seperator);
			}
		}
		return sb.toString();
	}

	public static  String[] toArray(List<String> list){
		String[] arr = new String[Lang.count(list)];
		for (int i = 0; i < Lang.count(list); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}


	public static <T> List<List<T>> splitList(List<T> list, int count){
		List<List<T>> result = new ArrayList<>();

		List<T> page = null;
		for (int i = 0; i < Lang.count(list); i++) {
			if(i % count == 0){
				page = new ArrayList<>();
				result.add(page);
			}
			page.add(list.get(i));
		}
		return result;
	}

	public interface FileConsumer{
		boolean consume(String path, boolean isDir);
	}

	public static void tranverseDir(String dirPath, FileConsumer fileConsumer){

		File dir = new File(dirPath);

		File[] list1 = dir.listFiles();
		for (File f1: list1){
			if(f1.isDirectory()){
				tranverseDir(f1.getAbsolutePath(), fileConsumer);
			}else{
				fileConsumer.consume(f1.getAbsolutePath(), false);
			}
		}
	}

	public static String urlencode(String s){
		try {
			return URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	public static String urldecode(String s){
		try {
			return URLDecoder.decode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}


	public static boolean isToday2(long millis){
		Date inputJudgeDate = new Date(millis);
		boolean flag = false;
		//获取当前系统时间
		long longDate = System.currentTimeMillis();
		Date nowDate = new Date(longDate);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(nowDate);
		String subDate = format.substring(0, 10);
		//定义每天的24h时间范围
		String beginTime = subDate + " 00:00:00";
		String endTime = subDate + " 23:59:59";
		Date paseBeginTime = null;
		Date paseEndTime = null;
		try {
			paseBeginTime = dateFormat.parse(beginTime);
			paseEndTime = dateFormat.parse(endTime);

		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		if(inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
			flag = true;
		}
		return flag;
	}

	public static boolean isToday(long millis){
		LocalDate today = LocalDate.now();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(millis));
		LocalDate local = LocalDate.of(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));

		return today.isEqual(local);
	}

	public static boolean isYestoday(long millis){
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(millis));
		LocalDate local = LocalDate.of(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));

		return yesterday.isEqual(local);
	}

	public static String getFileNameWithoutSuffix(String path){
		String fileName = new File(path).getName();
		if(fileName != null && fileName.contains(".")){
			return fileName.split("\\.")[0];
		}
		return Lang.snull(fileName, "");
	}

	public static String getFileSuffix(String path){
		String fileName = new File(path).getName();
		if(fileName != null && fileName.contains(".")){
			return fileName.split("\\.")[1];
		}
		return "";
	}
}
