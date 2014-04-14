package com.dfferentia.junit;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A JUnit Runner which extends the functionality of SpringJUnit4ClassRunner
 * with @DataProvider annotation.
 * 
 * @author Saurabh Raje
 * @email saurabh.raje@gmail.com
 * 
 * @created 02-Apr-2014
 */
public class SpringRunner extends SpringJUnit4ClassRunner {
	/**
	 * Shared cache of all the DataProvider's
	 */
	private static ConcurrentHashMap<String, Collection<Object[]>> dataProviderCache = new ConcurrentHashMap<String, Collection<Object[]>>();

	private final Method getFilteredChildren;
	private boolean filteredChildrenModified;

	public SpringRunner(Class<?> klass) throws InitializationError {
		super(klass);
		try {
			this.getFilteredChildren = ParentRunner.class.getDeclaredMethod("getFilteredChildren");
			this.getFilteredChildren.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException("need access to ParentRunner.fFilteredChildren", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Description getDescription() {
		if (!filteredChildrenModified) {
			try {
				List<FrameworkMethod> list = (List<FrameworkMethod>) getFilteredChildren.invoke(this);
				// ForEach FrameworkMethod which is annotated by @DataProvider;
				// replace FrameworkMethod with FrameworkMethodCollection
				for (int i = 0; i < list.size(); ++i) {
					FrameworkMethod method = list.get(i);
					DataProvider dataProvider = method.getAnnotation(DataProvider.class);
					if (dataProvider != null) {
						method = new FrameworkMethodCollection(method.getMethod(), parameters(dataProvider),
								dataProvider.index());
					}
					list.set(i, method);
				}
				filteredChildrenModified = true;
			} catch (Exception e) {
				throw new RuntimeException("need access to ParentRunner.fFilteredChildren", e);
			}
		}
		return super.getDescription();
	}

	@Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		if (method instanceof FrameworkMethodCollection) {
			FrameworkMethodCollection frameworkMethodCollection = (FrameworkMethodCollection) method;
			for (FrameworkMethodParams frameworkMethod : frameworkMethodCollection.getFrameworkMethods()) {
				super.runChild(frameworkMethod, notifier);
			}
		} else {
			super.runChild(method, notifier);
		}
	}

	@Override
	protected Description describeChild(FrameworkMethod method) {
		if (method instanceof FrameworkMethodCollection) {
			FrameworkMethodCollection frameworkMethodCollection = (FrameworkMethodCollection) method;
			Description description = Description.createSuiteDescription(testName(method), method.getAnnotations());
			for (FrameworkMethodParams frameworkMethod : frameworkMethodCollection.getFrameworkMethods()) {
				description.addChild(describeChild(frameworkMethod));
			}
			return description;
		} else if (method instanceof FrameworkMethodParams) {
			FrameworkMethodParams frameworkMethod = (FrameworkMethodParams) method;
			StringBuilder sb = new StringBuilder();
			sb.append(testName(method));
			frameworkMethod.display(sb);
			return Description.createTestDescription(getTestClass().getJavaClass(), sb.toString(),
					method.getAnnotations());
		}
		return super.describeChild(method);
	}

	@SuppressWarnings("unchecked")
	protected Collection<Object[]> parameters(DataProvider dataProvider) {
		final String key = dataProvider.clazz().getName() + "#" + dataProvider.method();
		try {
			Collection<Object[]> collection = dataProviderCache.get(key);
			if (collection == null) {
				collection = (Collection<Object[]>) dataProvider.clazz().getMethod(dataProvider.method()).invoke(null);
				dataProviderCache.put(key, collection);
			}
			return collection;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void validateInstanceMethods(List<Throwable> errors) {
		// TODO: JUNIT4 does not allow test methods with arguments which is a
		// necessity for the @DataProvider. Hence, this HACK to get it out of
		// the way. In any case it should not come in the way of advanced JUNIT4
		// users.
	}
}
