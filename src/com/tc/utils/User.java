package com.tc.utils;

public class User {
	private String name;
	private String psw;
	private int score;

	public User() {

	}

	public User(String name, String psw, int score) {
		super();
		this.name = name;
		this.psw = psw;
		this.score = score;
	}

	public User(String name, String psw) {
		super();
		this.name = name;
		this.psw = psw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
