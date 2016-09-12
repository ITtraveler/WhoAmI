package com.tc.whoami;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.tc.utils.Coords;
import com.tc.utils.DataAnalysisUtils;
import com.tc.utils.FeatureMatchUtil;
import com.tc.utils.GenerateAnalysisResult;

public class MyTest extends AndroidTestCase {

	public MyTest() {
	}

	/*
	 * public void test() { // DataAnalysisUtils dau = new
	 * DataAnalysisUtils("test"); // int[] result = dau.getReaction(); // for
	 * (int i = 0; i < result.length; i++) { //
	 * System.out.println("reaction:"+result[i]); // }
	 * 
	 * }
	 * 
	 * public void testGenerate() throws ClassNotFoundException,
	 * InstantiationException, IllegalAccessException { //
	 * GenerateAnalysisResult gar = new GenerateAnalysisResult("test"); //
	 * gar.result(); Coords coords = (Coords)
	 * Class.forName("com.tc.utils.Coords").newInstance(); coords.setX(123);
	 * coords.setY(321); System.out.println("coords"+coords.toString());
	 * 
	 * }
	 */

	public void testFeatureMatchUtil() {
//		FeatureMatchUtil fmu = new FeatureMatchUtil(getContext());
//		fmu.reportMatchFilter();
//		DataAnalysisUtils d = new DataAnalysisUtils("qqq");
//		d.getThumbLong();
//		d.getGesture();
		GenerateAnalysisResult g = new GenerateAnalysisResult("test0");
		
	}
}
