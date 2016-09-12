package com.tc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �����װ��һЩ�ַ�������ؼ�⼰����
 * 
 * @author hgs
 *
 */
public class StringUtils {
	/**
	 * ���ڼ��content���ַ������Ƿ���rangeMin��rangeMax֮��
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
	 * �ж����ַ����Ƿ���ͬ
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
	 * ���ڱ��ʽ��������Ӧ������
	 * 
	 * @param content
	 *            ����
	 * @param regex
	 *            ������ʽ
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
