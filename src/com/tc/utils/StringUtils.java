package com.tc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 此类封装了一些字符串的相关检测及操作
 * 
 * @author hgs
 *
 */
public class StringUtils {
	/**
	 * 用于检测content的字符长度是否在rangeMin到rangeMax之间
	 * 
	 * @param content
	 * @param rangeMin
	 * @param rangeMax
	 * @return
	 */
	public static boolean checkLong(String content, int rangeMin, int rangeMax) {
		int length = content.trim().length();
		if (length >= rangeMin && length <= rangeMax)
			return true;
		else
			return false;
	}

	/**
	 * 判断两字符串是否相同
	 * 
	 * @param content1
	 * @param content2
	 * @return
	 */
	public static boolean compare(String content1, String content2) {
		if (content1.equals(content2))
			return true;
		else
			return false;
	}

	/**
	 * 
	 */
	/**
	 * 正在表达式解析出对应的内容
	 * 
	 * @param content
	 *            数据
	 * @param regex
	 *            正则表达式
	 * @return
	 */
	public static String parse(String content, String regex) {
		StringBuilder sb = new StringBuilder();
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(content);
		while (matcher.find()) {
			String group = matcher.group();
			sb.append(group);
		}
		return sb.toString();
	}
	

	
}
