package com.tc.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * ��װ����/İ���˲���ʱ����ʶ����ȡ���������
 * 
 * @author hgs
 *
 */
public class RecognizeUserData {
	private List<Coords> upSlidingCoordsList;// �ϻ��������꼯
	private List<Coords> viewPagerCoordsList;// �󻬶������꼯
	private int betweenTime[];
	private int reactionV;// ��Ӧ�ٶ�
	private int thumbLong;
	private float upSlidingDownSize;// �ϻ�ʱdown�Ӵ����
	private float leftSlidingDownSize;// ��ʱdown�Ӵ����
	private float touchMaxSize;// touch�Ӵ����
	private String gesture = "";
	private int count;

	public int getThumbLong() {
		return thumbLong;
	}

	public void setThumbLong(int thumbLong) {
		this.thumbLong = thumbLong;
	}

	public String getGesture() {
		return gesture;
	}

	public void setGesture(String gesture) {
		this.gesture = gesture;
	}

	// private Coords upSlidingDownCoords;// �ϻ�����ʱ������
	// private Coords upSlidingUpCoords;// �ϻ�̧��ʱ������
	// private Coords leftSlidingDownCoords;// �ϻ�����ʱ������
	// private Coords leftSlidingUpCoords;// �ϻ�̧��ʱ������
	// �ϻ�����ʱ������
	public Coords getUpSlidingDownCoords() {
		return upSlidingCoordsList.get(0);
	}

	// �ϻ�̧��ʱ������
	public Coords getUpSlidingUpCoords() {
		return upSlidingCoordsList.get(upSlidingCoordsList.size() - 1);
	}

	// �ϻ�����ʱ������
	public Coords getLeftSlidingDownCoords() {
		return viewPagerCoordsList.get(0);
	}

	// �ϻ�̧��ʱ������
	public Coords getLeftSlidingUpCoords() {
		return viewPagerCoordsList.get(viewPagerCoordsList.size() - 1);
	}

	// ��ʼ��
	public void init() {
		upSlidingCoordsList = new ArrayList<Coords>();// �ϻ��������꼯
		viewPagerCoordsList = new ArrayList<Coords>();// �󻬶������꼯
		betweenTime = new int[2];
		reactionV = 0;// ��Ӧ�ٶ�
		upSlidingDownSize = 0;// �ϻ�ʱdown�Ӵ����
		leftSlidingDownSize = 0;// ��ʱdown�Ӵ����
		touchMaxSize = 0;// touch�Ӵ����
		count = 0;
	}

	public List<Coords> getUpSlidingCoordsList() {
		return upSlidingCoordsList;
	}

	public void setUpSlidingCoordsList(List<Coords> upSlidingCoordsList) {
		this.upSlidingCoordsList = upSlidingCoordsList;
	}

	public List<Coords> getViewPagerCoordsList() {
		return viewPagerCoordsList;
	}

	public void setViewPagerCoordsList(List<Coords> viewPagerCoordsList) {
		this.viewPagerCoordsList = viewPagerCoordsList;
	}

	public int[] getBetweenTime() {
		return betweenTime;
	}

	public void setBetweenTime(int[] betweenTime) {
		this.betweenTime = betweenTime;
	}

	public int getReactionV() {
		return reactionV;
	}

	public void setReactionV(int reactionV) {
		this.reactionV = reactionV;
	}

	public float getUpSlidingDownSize() {
		return upSlidingDownSize;
	}

	public void setUpSlidingDownSize(float upSlidingDownSize) {
		this.upSlidingDownSize = upSlidingDownSize;
	}

	public float getLeftSlidingDownSize() {
		return leftSlidingDownSize;
	}

	public void setLeftSlidingDownSize(float leftSlidingDownSize) {
		this.leftSlidingDownSize = leftSlidingDownSize;
	}

	public float getTouchMaxSize() {
		return touchMaxSize;
	}

	public void setTouchMaxSize(float touchMaxSize) {
		this.touchMaxSize = touchMaxSize;
	}

	public void addBetweenTime(long time) {

		betweenTime[count] = (int) time;
		count++;
	}

	/**
	 * �ϻ����󻬵�ƽ��ʱ��
	 * 
	 * @return
	 */
	public int getAvgBetweenTime() {
		int avgTime = (betweenTime[0] + betweenTime[1]) / 2;
		return avgTime;
	}
}
