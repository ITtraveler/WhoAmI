package com.tc.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * ��DataAnalysisUtils�����������ݣ����н�һ�������õ�������浥��
 * 
 * @author hgs
 *
 */
public class GenerateAnalysisResult {
	private DataAnalysisUtils dau;
	private String username;

	public GenerateAnalysisResult(String username) {
		dau = new DataAnalysisUtils(username);
		this.username = username;
		result();
	}

	public GenerateAnalysisResult() {
	}

	/**
	 * ��������ȡ�����ݣ������ۺ��Է������õ� ��ֵ ���
	 */
	public void result() {
		StringBuilder sb = new StringBuilder();

		// ����ʱ��
		int times[] = dau.getBetweenTime();
		dealInt(times, sb, "avgTime", "varTime");

		// ���һ��Ӵ����
		float mHs[] = dau.getMotionH();
		dealFloat(mHs, sb, "avgMHS", "varMHS");

		// ���»��Ӵ����
		float mVs[] = dau.getMotionV();
		dealFloat(mVs, sb, "avgMVS", "varMVS");

		// ��Ӧ�ٶ�
		int reaction[] = dau.getReaction();
		dealInt(reaction, sb, "avgReaction", "varReaction");

		// touch���
		float touchSize[] = dau.getTouchSize();
		dealFloat(touchSize, sb, "avgTS", "varTS");

		// h down Coords
		int hdown[][] = dau.getHDownPoint();
		int hdownLenght = hdown.length;

		int hdownCoordX[] = new int[hdownLenght];
		int hdownCoordY[] = new int[hdownLenght];
		for (int i = 0; i < hdownLenght; i++) {
			hdownCoordX[i] = hdown[i][0];
			hdownCoordY[i] = hdown[i][1];
		}
		dealInt(hdownCoordX, sb, "avgHDCX", "varHDCX");
		dealInt(hdownCoordY, sb, "avgHDCY", "varHDCY");

		// v down Coords
		int vdown[][] = dau.getVDownPoint();
		int vdownLenght = vdown.length;

		int vdownCoordX[] = new int[vdownLenght];
		int vdownCoordY[] = new int[vdownLenght];
		for (int i = 0; i < vdownLenght; i++) {
			vdownCoordX[i] = vdown[i][0];
			vdownCoordY[i] = vdown[i][1];
		}
		dealInt(vdownCoordX, sb, "avgVDCX", "varVDCX");
		dealInt(vdownCoordY, sb, "avgVDCY", "varVDCY");

		// h up Coords
		int hup[][] = dau.getHUpPoint();
		int hupLenght = hup.length;

		int hupCoordX[] = new int[hupLenght];
		int hupCoordY[] = new int[hupLenght];
		for (int i = 0; i < hupLenght; i++) {
			hupCoordX[i] = hup[i][0];
			hupCoordY[i] = hup[i][1];
		}
		dealInt(hupCoordX, sb, "avgHUCX", "varHUCX");
		dealInt(hupCoordY, sb, "avgHUCY", "varHUCY");

		// v up Coords
		int vup[][] = dau.getVUpPoint();
		int vupLenght = vup.length;
		int vupCoordX[] = new int[vupLenght];
		int vupCoordY[] = new int[vupLenght];
		for (int i = 0; i < vupLenght; i++) {
			vupCoordX[i] = vup[i][0];
			vupCoordY[i] = vup[i][1];
		}

		int avg = averageInt(vupCoordY);
		int var = varianceInt(vupCoordY);
		dealInt(vupCoordX, sb, "avgVUCX", "varVUCX");
		dealInt(vupCoordY, sb, "avgVUCY", "varVUCY");
		// �����꼯
		List<ArrayList<Coords>> mhc = dau.getHMovePoint();
		for (int i = 0; i < mhc.size(); i++) {
			StringBuilder sb2 = new StringBuilder();
			int l = mhc.get(i).size();
			for (int j = 0; j < l; j++) {
				sb2.append("h" + i + ":" + mhc.get(i).get(j).toString());
			}
			sb.append(sb2 + "\n");
		}

		// �ϻ����꼯
		List<ArrayList<Coords>> mvc = dau.getVMovePoint();
		for (int i = 0; i < mvc.size(); i++) {
			StringBuilder sb2 = new StringBuilder();
			int l = mvc.get(i).size();
			for (int j = 0; j < l; j++) {
				sb2.append("v" + i + ":" + mvc.get(i).get(j).toString());
			}
			sb.append(sb2 + "\n");
		}

		// thumbLong
		int[] thumbLongs = dau.getThumbLong();
		dealInt(thumbLongs, sb, "avgThumbLong", "varThumbLong");

		// �õ�����X��������Ӧֵ
		float[] gXs = dau.getGesture();
		dealGDate(sb, gXs);
		// if(gXs[DataAnalysisUtils.LU] < -2)
		System.out.println(gXs.length);
		// ��sb�е�����д���ڴ���ȥ
		generateReport(sb.toString(), username);
		// System.out.println(sb.toString());
	}

	/** ����ж����Ƶ����ݴ��� */
	public void dealGDate(StringBuilder sb, float[] gXs) {
		int LU = DataAnalysisUtils.LU;
		int RU = DataAnalysisUtils.RU;
		int LD = DataAnalysisUtils.LD;
		int RD = DataAnalysisUtils.RD;
		int DIVISION = 2;
		if (gXs[LU] > DIVISION || gXs[RU] > DIVISION || gXs[RD] > DIVISION) {
			sb.append("left\n");
		} else if (gXs[LU] < -DIVISION || gXs[RU] < -DIVISION
				|| gXs[LD] < -DIVISION) {
			sb.append("right\n");
		} else {
			sb.append("double\n");
		}
	}

	// ����int��ֵ������
	private void dealInt(int values[], StringBuilder sb, String avgName,
			String varName) {
		Result<Integer> r = new Result<Integer>();
		int avgResult = averageInt(values);
		int varResult = varianceInt(values);
		r.setAverageResult(avgResult);
		r.setVarianceResult(varResult);
		sb.append(avgName + ":" + r.getAverageResult() + "	" + varName + ":"
				+ r.getVarianceResult() + "\n");
	}

	// ����float��ֵ������
	private void dealFloat(float values[], StringBuilder sb, String avgName,
			String varName) {
		Result<Float> r = new Result<Float>();
		float avgResult = averageFloat(values);
		float varResult = varianceFloat(values);
		r.setAverageResult(avgResult);
		r.setVarianceResult(varResult);
		sb.append(avgName + ":" + r.getAverageResult() + "	" + varName + ":"
				+ r.getVarianceResult() + "\n");
	}

	/**
	 * ���浥����
	 * 
	 * @param content
	 * @param username
	 */
	private void generateReport(String content, String username) {
		FileUtils.writeFile(FileUtils.REPORTPATH, FileUtils.TAG_NAME1, content,
				username);
	}

	/**
	 * ƽ��ֵ
	 * 
	 * @param values
	 *            ֵ��
	 * @return
	 */
	public int averageInt(int values[]) {
		int lenght = values.length;
		int total = 0;
		for (int i = 0; i < lenght; i++) {
			total += values[i];
		}
		int avg = total / lenght;
		return avg;
	}

	public float averageFloat(float values[]) {
		int lenght = values.length;
		float total = 0;
		for (int i = 0; i < lenght; i++) {
			total += values[i];
		}
		float avg = total / lenght;
		return avg;
	}

	/**
	 * int���ͷ���
	 * 
	 * @param values
	 *            ����
	 * @param avg
	 *            ƽ��ֵ
	 * @return
	 */
	public int varianceInt(int values[]) {
		int lenght = values.length;
		int avg = averageInt(values);
		int total = 0;
		for (int i = 0; i < lenght; i++) {
			total += Math.pow(values[i] - avg, 2);
		}
		int result = (int) Math.sqrt((total / lenght));
		return result;
	}

	/**
	 * float���ͷ���
	 * 
	 * @param values
	 *            ����
	 * @param avg
	 *            ƽ��ֵ
	 * @return
	 */
	public float varianceFloat(float values[]) {
		int lenght = values.length;
		float avg = averageFloat(values);
		float total = 0;
		for (int i = 0; i < lenght; i++) {
			total += Math.pow(values[i] - avg, 2);
		}
		float result = (float) Math.sqrt((total / lenght));
		return result;
	}
	// public
}
