/* 
 * CreateDate 2016-7-18
 *
 * Email ：darkidiot@icloud.com 
 * School：CUIT 
 * Copyright For darkidiot
 */
package org.darkidiot.frame;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * 
 * @author darkidiot
 * @version 1.0
 */
public class Util {
	/** 字符编码 */
	private static final String CHARSET = "UTF-8";

	/** 格式化日期 */
	public static String format(Date date) {
		if (date == null) {
			return null;
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	/**
	 * 取得匹配的字符串
	 * 
	 * @author DarkIdiot
	 * @param input
	 *            字符串
	 * @param regex
	 *            正则表达式
	 * @param group
	 *            正则分组1-9
	 * @return List 匹配的字符串
	 */
	public static List<String> matchs(String input, String regex, int group) {
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(input);
		List<String> matches = new ArrayList<String>();
		while (match.find()) {
			matches.add(match.group(group));
		}
		return matches;
	}

	/**
	 * 首字母大写
	 * 
	 * @author DarkIdiot
	 * @param value
	 *            对象值
	 * @return String 字符串
	 */
	public static String firstCharUpperCase(String value) {
		if (isValid(value)) {
			return value.substring(0, 1).toUpperCase() + value.substring(1);
		}
		return value;
	}

	/**
	 * 首字母小写
	 * 
	 * @author DarkIdiot
	 * @param value
	 *            对象值
	 * @return String 字符串
	 */
	public static String firstCharLowerCase(String value) {
		if (isValid(value)) {
			return value.substring(0, 1).toLowerCase() + value.substring(1);
		}
		return value;
	}

	/**
	 * 转换格式 CUST_INFO_ID - > custInfoId
	 * 
	 * @author DarkIdiot
	 * @param name
	 * @return
	 */
	public static String toFieldName(String name) {
		if (name == null) {
			return null;
		}
		String field = name.toLowerCase();
		String[] values = field.split("\\_");
		StringBuffer b = new StringBuffer(name.length());
		for (int i = 0; i < values.length; i++) {
			if (i == 0)
				b.append(values[i]);
			else
				b.append(firstCharUpperCase(values[i]));
		}
		return b.toString();
	}

	/**
	 * 连接字符串
	 * 
	 * @author DarkIdiot
	 * @param list
	 *            列表
	 * @param split
	 *            分隔符
	 * @return 字符串
	 */
	public static String join(List<String> list, String split) {
		if (list == null)
			return null;
		String[] array = list.toArray(new String[] {});
		StringBuilder s = new StringBuilder(128);
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				s.append(split);
			}
			s.append(array[i]);
		}
		return s.toString();
	}
	
	/**
	 * 判断字符串是否为Null或trim后长度为0
	 * 
	 * @author DarkIdiot
	 * @param validate
	 * @return
	 */
	public static boolean isEmpty(Object value) {
		if (value == null)
			return true;
		return value.toString().trim().isEmpty();
	}

	/**
	 * 判断对象是否有效
	 * 
	 * @author DarkIdiot
	 * @param value
	 * @return
	 */
	public static boolean isValid(Object value) {
		return !isEmpty(value);
	}

	/**
	 * 读取文件
	 * 
	 * @author DarkIdiot
	 * @param file
	 *            文件
	 * @return 文件内容
	 */
	public static String read(String file) throws Exception {
		InputStream in = null;
		InputStreamReader reader = null;
		try {
			in = CodeHelper.class.getResourceAsStream(file);
			reader = new InputStreamReader(in, CHARSET);
			StringWriter writer = new StringWriter();
			int len = -1;
			char[] buffer = new char[128];
			while ((len = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, len);
			}
			writer.flush();
			return writer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	/**
	 * 读取文件
	 * 
	 * @author DarkIdiot
	 * @param file
	 *            文件
	 * @return 文件内容
	 */
	public static String read(File file) throws Exception {
		InputStream in = null;
		InputStreamReader reader = null;
		try {
			in = new FileInputStream(file);
			reader = new InputStreamReader(in, CHARSET);
			StringWriter writer = new StringWriter();
			int len = -1;
			char[] buffer = new char[128];
			while ((len = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, len);
			}
			writer.flush();
			return writer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	/**
	 * 取得异常信息
	 * 
	 * @author DarkIdiot
	 * @param e
	 * @return
	 */
	public static String getStack(Exception e) {
		Writer writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}
}