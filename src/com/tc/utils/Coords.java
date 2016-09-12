package com.tc.utils;
/**
 * XY×ø±ê·â×°Àà
 * @author hgs
 *
 */
public class Coords {

	private int X;
	private int Y;

	public Coords() {
	}

	public Coords(int x, int y) {
		super();
		X = x;
		Y = y;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	@Override
	public String toString() {
		return "(X=" + X + ",Y=" + Y + ")";
	}

}
