package com.tc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;

/**
 * 提取陌生用户特征，与存储的用户特征进行匹配,得到相应的匹配结果。 每一项都有一定的分值，按照分值累加的方式，得出最符合的一项
 * 所谓一项指report目录下的一份报告单 总分为110分
 * 
 * @author hgs
 *
 */
public class FeatureMatchUtil {
	private RecognizeUserData rud;
	private List<User> userList = new ArrayList<User>();
	DatabaseUtils dbUtils;

	public FeatureMatchUtil(Context context) {
		this.dbUtils = new DatabaseUtils(context);
		initUserList();
	}

	public FeatureMatchUtil(RecognizeUserData rud, Context context) {
		this.rud = rud;
		this.dbUtils = new DatabaseUtils(context);
		initUserList();
	}

	/**
	 * 将数据库中存放的用户数据读入内存
	 */
	private void initUserList() {
		dbUtils.openDatabase();
		Cursor c = dbUtils.query("select username,password from user");
		while (c.moveToNext()) {
			User user = new User(c.getString(0), c.getString(1));
			userList.add(user);
		}
		dbUtils.closeDB();
	}

	/**
	 * 对报告单进行匹配。
	 */
	public void reportMatchFilter() {
		/**
		 * 当前识别用户的特征数据
		 */
		Coords coords1 = rud.getLeftSlidingDownCoords();// 左滑down点
		int hdx = coords1.getX();
		int hdy = coords1.getY();
		Coords coords2 = rud.getUpSlidingDownCoords();// 上滑down点
		int vdx = coords2.getX();
		int vdy = coords2.getY();
		Coords coords3 = rud.getLeftSlidingUpCoords();// 左滑up点
		int hux = coords3.getX();
		int huy = coords3.getY();
		Coords coords4 = rud.getUpSlidingUpCoords();// 上滑up点
		int vux = coords4.getX();
		int vuy = coords4.getY();
		int avgTime = rud.getAvgBetweenTime();
		int avgReaction = rud.getReactionV();
		int thumbLong = rud.getThumbLong();
		float MHS = rud.getLeftSlidingDownSize();
		float MVS = rud.getUpSlidingDownSize();
		float ts = rud.getTouchMaxSize();
		String gesture = rud.getGesture().trim();
		System.out.println("gesture:" + gesture.length());
		int[] intValues = { vdx, vux, vdy, vuy, hdx, hux, hdy, huy, avgTime,
				avgReaction, thumbLong };
		float[] floatValues = { MHS, MVS, ts };
		/** 用户数量 */
		int size = userList.size();
		System.out.println("ts:" + ts + "userSize:" + size);
		/** 进行一一匹配，得出每个用户的成绩 */
		for (int i = 0; i < size; i++) {
			// scoreList.add(0);// 初始化一个成绩
			int location = i;
			User curUser = userList.get(location);
			// 读取当前匹配中用户的报告单内容
			String content = FileUtils.readFile(FileUtils.REPORTPATH,
					FileUtils.TAG_NAME1, curUser.getName()).trim();
			// 当且仅当content不为空进行
			if (!content.equals("") && !content.isEmpty()) {
				/** 触摸点 */
				int i0[] = getIntMM(content, "avgVDCX:\\d+\\s*varVDCX:\\d+");// 垂直操作点下x轴
				int i1[] = getIntMM(content, "avgVUCX:\\d+\\s*varVUCX:\\d+");// 垂直操作抬起x轴
				int i2[] = getIntMM(content, "avgVDCY:\\d+\\s*varVDCY:\\d+");// 垂直操作点下y轴
				int i3[] = getIntMM(content, "avgVUCY:\\d+\\s*varVUCY:\\d+");// 垂直操作抬起y轴
				int i4[] = getIntMM(content, "avgHDCX:\\d+\\s*varHDCX:\\d+");// 左滑操作点下x轴
				int i5[] = getIntMM(content, "avgHUCX:\\d+\\s*varHUCX:\\d+");// 左滑操作抬起x轴
				int i6[] = getIntMM(content, "avgHDCY:\\d+\\s*varHDCY:\\d+");// 左滑操作点下y轴
				int i7[] = getIntMM(content, "avgHUCY:\\d+\\s*varHUCY:\\d+");// 左滑操作抬起y轴

				int i8[] = getIntMM(content, "avgTime:\\d+\\s*varTime:\\d+");// 滑动时间
				int i9[] = getIntMM(content,
						"avgReaction:\\d+\\s*varReaction:\\d+");// 反应时间
				int i10[] = getIntMM(content,
						"avgThumbLong:\\d+\\s*varThumbLong:\\d+");// 拇指长度

				float f1[] = getFloatMM(content,
						"avgMHS:\\d.\\d+\\s*varMHS:\\d.\\d+");// 左滑接触面积
				float f2[] = getFloatMM(content,
						"avgMVS:\\d.\\d+\\s*varMVS:\\d.\\d+");// 上滑接触面积
				float f3[] = getFloatMM(content,
						"avgTS:\\d.\\d+\\s*varTS:\\d.\\d+");// 接触面积
				// 数据集
				int[][] intData = { i0, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10 };// int型用户数据
				float[][] floatData = { f1, f2, f3 };// float型用户数据
				// 对应分数集
				int intScore[] = { 5, 5, 5, 5, 5, 5, 5, 5, 10, 10, 10 };
				int floatScore[] = { 5, 5, 10 };
				System.out.println("intDataSize:" + intData.length);
				/** intData数据类型的分数统计 */
				for (int id = 0; id < intData.length; id++) {
					System.out.println("fffffffffff");
					int mmIV[] = intData[id];
					totalScore(curUser, intValues[id], mmIV, intScore[id]);
				}
				/** floatData数据类型的分数统计 */
				for (int id = 0; id < floatData.length; id++) {
					float mmIV[] = floatData[id];
					totalScore(curUser, floatValues[id], mmIV, floatScore[id]);
				}
				matchGesture(curUser, content, gesture, 20);

			}
		}
	}

	// 惯用手判断 分值20
	private void matchGesture(User user, String content, String gesture,
			int score) {
		Pattern p = Pattern.compile("(left|double|right)");
		Matcher matcher = p.matcher(content);
		String curGesture = "";
		if (matcher.find()) {
			curGesture = matcher.group();
		}
		if (curGesture.equals(gesture)) {
			System.out.println(score);
			user.setScore(user.getScore() + score);
		}
	}

	/**
	 * 
	 * @param mmV
	 *            最大值和最小值的数组
	 * @param score
	 *            此项成绩
	 * @param value
	 *            要进行比较的值
	 * @param location
	 *            为第几个报告
	 */
	private void totalScore(User user, int value, int mmV[], int score) {
		int max = mmV[0];
		int min = mmV[1];
		System.out.println("value:" + value + "max:" + max + "min:" + min);
		if (value <= max && value >= min) {// 如果满足条件
			System.out.println("score:" + score);
			// int curScore = scoreList.get(location) + score;// 对应报告累加成绩
			// scoreList.set(location, curScore);// 更新成绩

			user.setScore(user.getScore() + score);// 用户成绩累加
		}
	}

	private void totalScore(User user, float value, float mmV[], int score) {
		float max = mmV[0];
		float min = mmV[1];
		System.out.println("value:" + value + "max:" + max + "min:" + min);
		if (value <= max && value >= min) {// 如果满足条件
			// int curScore = scoreList.get(location) + score;// 对应报告累加成绩
			// scoreList.set(location, curScore);// 更新成绩
			user.setScore(user.getScore() + score);// 用户成绩累加
		}
	}

	// 得到最大值和最小值
	private int[] getIntMM(String content, String regex) {
		Result<Integer> value = getResultIntValue(content, regex);
		int avg = value.getAverageResult();
		int range = value.getVarianceResult();
		// Log.i("avg", "" + avg);
		// Log.i("range", "" + range);
		int[] result = new int[2];
		int max = avg + range;
		int min = avg - range;
		result[0] = max;
		result[1] = min;
		return result;
	}

	// 得到最大值和最小值
	private float[] getFloatMM(String content, String regex) {
		Result<Float> value = getResultFloatValue(content, regex);
		float avg = value.getAverageResult();
		float range = value.getVarianceResult();
		// Log.i("avg", "" + avg);
		// Log.i("range", "" + range);
		float[] result = new float[2];
		float max = avg + range;
		float min = avg - range;
		result[0] = max;
		result[1] = min;
		return result;
	}

	// 得到匹配的结果值(int)
	private Result<Integer> getResultIntValue(String content, String regex) {
		String s = StringUtils.parse(content, regex);
		String r[] = parse(s, "\\d+");
		// System.out.println(s + "  length:" + r.length);
		if (r != null && r.length >= 0) {
			Result<Integer> result = new Result<Integer>();
			result.setAverageResult(Integer.valueOf(r[0]));
			result.setVarianceResult(Integer.valueOf(r[1]));
			return result;
		}
		return null;
	}

	// 得到匹配的结果值(float)
	private Result<Float> getResultFloatValue(String content, String regex) {
		String s = StringUtils.parse(content, regex);
		String r[] = parse(s, "\\d.\\d+");
		// System.out.println(s + "  length:" + r.length);
		if (r != null && r.length >= 0) {
			Result<Float> result = new Result<Float>();
			result.setAverageResult(Float.valueOf(r[0]));
			result.setVarianceResult(Float.valueOf(r[1]));
			return result;
		}
		return null;
	}

	// 匹配出 avg 和 var
	private String[] parse(String content, String regex) {
		String[] result;
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(content);
		// System.out.println("count:" + matcher.groupCount());
		result = new String[2];
		int count = 0;
		while (matcher.find()) {
			String group = matcher.group();
			result[count] = group;
			count++;
		}
		return result;
	}

	/**
	 * 返回最优匹配对象,按成绩从大到小排序
	 * 
	 * @return
	 */
	public List<User> getOptimalResult() {

		Collections.sort(userList, new Comparator<User>() {
			@Override
			public int compare(User lhs, User rhs) {
				if (lhs.getScore() < rhs.getScore())
					return 1;
				else if (lhs.getScore() > rhs.getScore())
					return -1;
				else
					return 0;
			}
		});

		//筛选处理
		List<User> list = new ArrayList<User>();
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getScore() >= 55)// 大于等于45分加入list
				list.add(userList.get(i));
		}

		return list;
	}

}
