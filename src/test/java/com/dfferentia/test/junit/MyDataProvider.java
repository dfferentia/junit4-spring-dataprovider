package com.dfferentia.test.junit;

import java.util.ArrayList;
import java.util.List;

public class MyDataProvider {
	public static List<Object[]> sample() {
		List<Object[]> sample = new ArrayList<Object[]>();
		sample.add(new Object[] { "hello", "world" });
		sample.add(new Object[] { "hello2", "world2" });
		sample.add(new Object[] { "hello3", "world3" });
		return sample;
	}
}
