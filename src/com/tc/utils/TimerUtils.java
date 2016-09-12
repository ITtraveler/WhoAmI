package com.tc.utils;

import java.util.Date;

/**
 * 时间计时器，通过开始到结束，得到期间的时间
 * 
 * @author hgs
 *
 */
public class TimerUtils {
	private long time;
	private long mStartTime = 0;
	private long mStopTime = 0;
	private boolean haveStart = false;

	public void startRecord() {
		if (!haveStart) {
			mStartTime = System.currentTimeMillis();
			haveStart = true;
		}
	}

	public void stopRecord() {
		if (haveStart) {
			mStopTime = System.currentTimeMillis();
			haveStart = false;
		}

	}

	public long getBetweenTime() {
		long between = mStopTime - mStartTime;
		return between;
	}
}
