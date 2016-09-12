package com.tc.utils;
/**
 * 封装了 均值 和  方差
 * @author hgs
 *
 * @param <T>
 */
public class Result<T> {
	private T averageResult;
	private T varianceResult;

	public T getAverageResult() {
		return averageResult;
	}

	public void setAverageResult(T averageResult) {
		this.averageResult = averageResult;
	}

	public T getVarianceResult() {
		return varianceResult;
	}

	public void setVarianceResult(T varianceResult) {
		this.varianceResult = varianceResult;
	}

	@Override
	public String toString() {
		return "Result [averageResult=" + averageResult + ", varianceResult="
				+ varianceResult + "]";
	}

}
