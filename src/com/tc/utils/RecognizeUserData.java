package com.tc.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装本人/陌生人操作时进行识别，提取的相关数据
 * 
 * @author hgs
 *
 */
public class RecognizeUserData {
	private List<Coords> upSlidingCoordsList;// 上滑动作坐标集
	private List<Coords> viewPagerCoordsList;// 左滑动作坐标集
	private int betweenTime[];
	private int reactionV;// 反应速度
	private int thumbLong;
	private float upSlidingDownSize;// 上滑时down接触面积
	private float leftSlidingDownSize;// 左滑时down接触面积
	private float touchMaxSize;// touch接触面积
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

	// private Coords upSlidingDownCoords;// 上滑按下时的坐标
	// private Coords upSlidingUpCoords;// 上滑抬起时的坐标
	// private Coords leftSlidingDownCoords;// 上滑按下时的坐标
	// private Coords leftSlidingUpCoords;// 上滑抬起时的坐标
	// 上滑按下时的坐标
	public Coords getUpSlidingDownCoords() {
		return upSlidingCoordsList.get(0);
	}

	// 上滑抬起时的坐标
	public Coords getUpSlidingUpCoords() {
		return upSlidingCoordsList.get(upSlidingCoordsList.size() - 1);
	}

	// 上滑按下时的坐标
	public Coords getLeftSlidingDownCoords() {
		return viewPagerCoordsList.get(0);
	}

	// 上滑抬起时的坐标
	public Coords getLeftSlidingUpCoords() {
		return viewPagerCoordsList.get(viewPagerCoordsList.size() - 1);
	}

	// 初始化
	public void init() {
		upSlidingCoordsList = new ArrayList<Coords>();// 上滑动作坐标集
		viewPagerCoordsList = new ArrayList<Coords>();// 左滑动作坐标集
		betweenTime = new int[2];
		reactionV = 0;// 反应速度
		upSlidingDownSize = 0;// 上滑时down接触面积
		leftSlidingDownSize = 0;// 左滑时down接触面积
		touchMaxSize = 0;// touch接触面积
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
	 * 上滑与左滑的平均时间
	 * 
	 * @return
	 */
	public int getAvgBetweenTime() {
		int avgTime = (betweenTime[0] + betweenTime[1]) / 2;
		return avgTime;
	}
}
