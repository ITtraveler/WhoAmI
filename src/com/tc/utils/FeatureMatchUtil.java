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
 * ��ȡİ���û���������洢���û���������ƥ��,�õ���Ӧ��ƥ������ ÿһ���һ���ķ�ֵ�����շ�ֵ�ۼӵķ�ʽ���ó�����ϵ�һ��
 * ��νһ��ָreportĿ¼�µ�һ�ݱ��浥 �ܷ�Ϊ110��
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
	 * �����ݿ��д�ŵ��û����ݶ����ڴ�
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
	 * �Ա��浥����ƥ�䡣
	 */
	public void reportMatchFilter() {
		/**
		 * ��ǰʶ���û�����������
		 */
		Coords coords1 = rud.getLeftSlidingDownCoords();// ��down��
		int hdx = coords1.getX();
		int hdy = coords1.getY();
		Coords coords2 = rud.getUpSlidingDownCoords();// �ϻ�down��
		int vdx = coords2.getX();
		int vdy = coords2.getY();
		Coords coords3 = rud.getLeftSlidingUpCoords();// ��up��
		int hux = coords3.getX();
		int huy = coords3.getY();
		Coords coords4 = rud.getUpSlidingUpCoords();// �ϻ�up��
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
		/** �û����� */
		int size = userList.size();
		System.out.println("ts:" + ts + "userSize:" + size);
		/** ����һһƥ�䣬�ó�ÿ���û��ĳɼ� */
		for (int i = 0; i < size; i++) {
			// scoreList.add(0);// ��ʼ��һ���ɼ�
			int location = i;
			User curUser = userList.get(location);
			// ��ȡ��ǰƥ�����û��ı��浥����
			String content = FileUtils.readFile(FileUtils.REPORTPATH,
					FileUtils.TAG_NAME1, curUser.getName()).trim();
			// ���ҽ���content��Ϊ�ս���
			if (!content.equals("") && !content.isEmpty()) {
				/** ������ */
				int i0[] = getIntMM(content, "avgVDCX:\\d+\\s*varVDCX:\\d+");// ��ֱ��������x��
				int i1[] = getIntMM(content, "avgVUCX:\\d+\\s*varVUCX:\\d+");// ��ֱ����̧��x��
				int i2[] = getIntMM(content, "avgVDCY:\\d+\\s*varVDCY:\\d+");// ��ֱ��������y��
				int i3[] = getIntMM(content, "avgVUCY:\\d+\\s*varVUCY:\\d+");// ��ֱ����̧��y��
				int i4[] = getIntMM(content, "avgHDCX:\\d+\\s*varHDCX:\\d+");// �󻬲�������x��
				int i5[] = getIntMM(content, "avgHUCX:\\d+\\s*varHUCX:\\d+");// �󻬲���̧��x��
				int i6[] = getIntMM(content, "avgHDCY:\\d+\\s*varHDCY:\\d+");// �󻬲�������y��
				int i7[] = getIntMM(content, "avgHUCY:\\d+\\s*varHUCY:\\d+");// �󻬲���̧��y��

				int i8[] = getIntMM(content, "avgTime:\\d+\\s*varTime:\\d+");// ����ʱ��
				int i9[] = getIntMM(content,
						"avgReaction:\\d+\\s*varReaction:\\d+");// ��Ӧʱ��
				int i10[] = getIntMM(content,
						"avgThumbLong:\\d+\\s*varThumbLong:\\d+");// Ĵָ����

				float f1[] = getFloatMM(content,
						"avgMHS:\\d.\\d+\\s*varMHS:\\d.\\d+");// �󻬽Ӵ����
				float f2[] = getFloatMM(content,
						"avgMVS:\\d.\\d+\\s*varMVS:\\d.\\d+");// �ϻ��Ӵ����
				float f3[] = getFloatMM(content,
						"avgTS:\\d.\\d+\\s*varTS:\\d.\\d+");// �Ӵ����
				// ���ݼ�
				int[][] intData = { i0, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10 };// int���û�����
				float[][] floatData = { f1, f2, f3 };// float���û�����
				// ��Ӧ������
				int intScore[] = { 5, 5, 5, 5, 5, 5, 5, 5, 10, 10, 10 };
				int floatScore[] = { 5, 5, 10 };
				System.out.println("intDataSize:" + intData.length);
				/** intData�������͵ķ���ͳ�� */
				for (int id = 0; id < intData.length; id++) {
					System.out.println("fffffffffff");
					int mmIV[] = intData[id];
					totalScore(curUser, intValues[id], mmIV, intScore[id]);
				}
				/** floatData�������͵ķ���ͳ�� */
				for (int id = 0; id < floatData.length; id++) {
					float mmIV[] = floatData[id];
					totalScore(curUser, floatValues[id], mmIV, floatScore[id]);
				}
				matchGesture(curUser, content, gesture, 20);

			}
		}
	}

	// �������ж� ��ֵ20
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
	 *            ���ֵ����Сֵ������
	 * @param score
	 *            ����ɼ�
	 * @param value
	 *            Ҫ���бȽϵ�ֵ
	 * @param location
	 *            Ϊ�ڼ�������
	 */
	private void totalScore(User user, int value, int mmV[], int score) {
		int max = mmV[0];
		int min = mmV[1];
		System.out.println("value:" + value + "max:" + max + "min:" + min);
		if (value <= max && value >= min) {// �����������
			System.out.println("score:" + score);
			// int curScore = scoreList.get(location) + score;// ��Ӧ�����ۼӳɼ�
			// scoreList.set(location, curScore);// ���³ɼ�

			user.setScore(user.getScore() + score);// �û��ɼ��ۼ�
		}
	}

	private void totalScore(User user, float value, float mmV[], int score) {
		float max = mmV[0];
		float min = mmV[1];
		System.out.println("value:" + value + "max:" + max + "min:" + min);
		if (value <= max && value >= min) {// �����������
			// int curScore = scoreList.get(location) + score;// ��Ӧ�����ۼӳɼ�
			// scoreList.set(location, curScore);// ���³ɼ�
			user.setScore(user.getScore() + score);// �û��ɼ��ۼ�
		}
	}

	// �õ����ֵ����Сֵ
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

	// �õ����ֵ����Сֵ
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

	// �õ�ƥ��Ľ��ֵ(int)
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

	// �õ�ƥ��Ľ��ֵ(float)
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

	// ƥ��� avg �� var
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
	 * ��������ƥ�����,���ɼ��Ӵ�С����
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

		//ɸѡ����
		List<User> list = new ArrayList<User>();
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getScore() >= 55)// ���ڵ���45�ּ���list
				list.add(userList.get(i));
		}

		return list;
	}

}
