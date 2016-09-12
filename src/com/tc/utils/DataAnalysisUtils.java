package com.tc.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

/**
 * 数据分析提取工具，从获取的数据中解析出，各个部分的数据。
 * 
 * @author hgs
 *
 */
public class DataAnalysisUtils {
	public static int LU = 0;
	public static int RU = 1;
	public static int LD = 2;
	public static int RD = 3;
	
	DatabaseUtils dbUtils;
	private String content = null;

	public DataAnalysisUtils(Context context, String username) {
		dbUtils = new DatabaseUtils(context);
		loadContent(username);
	}

	public DataAnalysisUtils(String username) {
		loadContent(username);
	}

	/**
	 * 将数据加载进内存中
	 * 
	 * @param username
	 *            以用户名作为所产生数据文档名的唯一标识之一
	 */
	private void loadContent(String username) {
		content = FileUtils.readFile(FileUtils.generatePath(0),
				FileUtils.TAG_NAME0, username);
		System.out.println(content);
	}

	/**
	 * 获取的到动作左滑的接触面积值
	 * 
	 * @return
	 */
	public float[] getMotionH() {
		String s = parse(content, "horizontal:\\d*\\.\\d*");
		String ss[] = s.split("horizontal:");
		return parseFloatValue(ss);
	}

	/**
	 * 获取的到动作上滑的接触面积值
	 * 
	 * @return
	 */
	public float[] getMotionV() {
		String s = parse(content, "vertical:\\d*\\.\\d*");
		String ss[] = s.split("vertical:");
		return parseFloatValue(ss);
	}

	/**
	 * 获取得到动作滑动的时间
	 * 
	 * @return
	 */
	public int[] getBetweenTime() {
		String s = parse(content, "betweenTime:\\d*");
		String ss[] = s.split("betweenTime:");
		return parseIntValue(ss);
	}

	/**
	 * 得到反应速度
	 * 
	 * @return
	 */
	public int[] getReaction() {
		String s = parse(content, "v:\\d*");
		System.out.println("v:" + s);
		String ss[] = s.split("v:");
		return parseIntValue(ss);
	}

	/**
	 * 得到拇指长
	 */
	public int[] getThumbLong() {
		String s = parse(content, "long\\d:\\d*").trim();
		String ss[] = s.split("long\\d:");
		return parseIntValue(ss);
	}
	/**
	 * 得到touch面积
	 * 
	 * @return
	 */
	public float[] getTouchSize() {
		String s = parse(content, "maxSize:\\d.\\d*");
		// System.out.println("touchsize:" + s);
		String ss[] = s.split("maxSize:");
		return parseFloatValue(ss);
	}

	/**
	 * 得到手的姿势判断值 得到重力感应X轴四个位置（lu、ru、ld、rd）的值,注意顺序
	 */
	public float[] getGesture() {
		String s = parse(content, "[lurd]+X:[-]*\\d*.\\d*");
		String ss[] = s.split("[lurd]+X:");
		return parseFloatValue(ss);
	}

	
	//解析处理
	private int[] parseIntValue(String[] ss) {
		int results[] = new int[ss.length - 1];
		for (int i = 1; i < ss.length; i++) {//之所以从一开始由于，继续是默认第0位为""导致错误
			// System.out.println(ss[i].trim());
			results[i - 1] = Integer.valueOf(ss[i].trim());
		}
		return results;
	}

	private float[] parseFloatValue(String[] s) {
		float results[] = new float[s.length - 1];
		for (int i = 1; i < s.length; i++) {
			// System.out.println(ss[i].trim());
			results[i - 1] = Float.valueOf(s[i].trim());
		}
		return results;
	}

	/**
	 * 得到左右滑动按下的点
	 * 
	 * @return
	 */
	public int[][] getHDownPoint() {
		String s = parse(content, "hdown\\s*x:\\d*\\.\\d*\\s*y:\\d*\\.\\d*");
		return parseXY(s);
	}

	/**
	 * 得到左右滑动抬起的点
	 * 
	 * @return
	 */
	public int[][] getHUpPoint() {
		String s = parse(content, "hup\\s*x:\\d*\\.\\d*\\s*y:\\d*\\.\\d*");
		return parseXY(s);
	}

	/**
	 * 解析左右滑动的坐标集
	 * 
	 * @return
	 */
	public List<ArrayList<Coords>> getHMovePoint() {
		List<ArrayList<Coords>> coords = new ArrayList<ArrayList<Coords>>();
		for (int i = 0; i < 4; i++) {
			String s = parse(content, "hmove" + i
					+ "\\sx:\\d*.\\d*\\s*y:\\d*.\\d*");
			// System.out.println(s);
			int[][] xy = parseXY(s);
			ArrayList<Coords> coordsList = new ArrayList<Coords>();
			for (int ii = 0; ii < xy.length; ii++) {
				int x = xy[ii][0];
				int y = xy[ii][1];
				// System.out.println(x + "," + y);
				coordsList.add(new Coords(x, y));
			}
			coords.add(coordsList);
		}
		return coords;
	}

	/**
	 * 得到上下滑动按下的点
	 * 
	 * @return
	 */
	public int[][] getVDownPoint() {
		String s = parse(content, "vdown\\s*x:\\d*\\.\\d*\\s*y:\\d*\\.\\d*");
		return parseXY(s);
	}

	/**
	 * 得到上下滑动抬起的点
	 * 
	 * @return
	 */
	public int[][] getVUpPoint() {
		String s = parse(content, "vup\\s*x:\\d*\\.\\d*\\s*y:\\d*\\.\\d*");
		return parseXY(s);
	}

	/**
	 * 解析上下滑动的坐标集
	 * 
	 * @return
	 */
	public List<ArrayList<Coords>> getVMovePoint() {
		List<ArrayList<Coords>> coords = new ArrayList<ArrayList<Coords>>();
		for (int i = 0; i < 3; i++) {
			String s = parse(content, "vmove" + i
					+ "\\sx:\\d*.\\d*\\s*y:\\d*.\\d*");
			// System.out.println(s);
			int[][] xy = parseXY(s);
			ArrayList<Coords> coordsList = new ArrayList<Coords>();
			for (int ii = 0; ii < xy.length; ii++) {
				int x = xy[ii][0];
				int y = xy[ii][1];
				// System.out.println(x + "," + y);
				coordsList.add(new Coords(x, y));
			}
			coords.add(coordsList);
		}
		return coords;
	}





	/**
	 * 坐标解析器
	 * 
	 * @param s
	 * @return
	 */
	private int[][] parseXY(String s) {
		String sX = parse(s, "x:\\d*");
		String sY = parse(s, "y:\\d*");
		String X[] = sX.split("x:");
		String Y[] = sY.split("y:");
		int results[][] = new int[X.length - 1][2];
		for (int i = 1; i < X.length; i++) {
			results[i - 1][0] = Integer.valueOf(X[i].trim());
			results[i - 1][1] = Integer.valueOf(Y[i].trim());
			// System.out.println(results[i-1][0] + "," + results[i-1][1]);
		}
		return results;
	}

	/**
	 * 正在表达式解析出对应的内容
	 * 
	 * @param content
	 * @param regex
	 * @return
	 */
	public String parse(String content, String regex) {
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
