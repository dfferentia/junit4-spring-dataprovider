package com.dfferentia.test.junit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.dfferentia.junit.DataProvider;
import com.dfferentia.junit.SpringRunner;

@RunWith(SpringRunner.class)
// You can use any annotation that is valid with SpringJUnit4ClassRunner
public class DataProviderTest {
	@Test
	@DataProvider(clazz = MyDataProvider.class, method = "sample")
	public void test(String s1, String s2) {
		Assert.assertTrue("pass for all s1=^hello", s1.startsWith("hello"));
		Assert.assertTrue("fail for all s2=world3", !s2.equals("world3"));
	}

	@Test
	@DataProvider(clazz = MyDataProvider.class, method = "sample", index = true)
	public void testWithIndex(int index, String s1, String s2) {
		Assert.assertTrue("pass for all s1=^hello", s1.startsWith("hello"));
		Assert.assertTrue("fail for all s2=world3", !s2.equals("world3"));
	}
}
