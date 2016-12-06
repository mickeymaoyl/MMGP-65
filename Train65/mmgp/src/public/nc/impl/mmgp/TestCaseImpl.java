package nc.impl.mmgp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import nc.bs.logging.Logger;
import nc.itf.mmgp.ITestCase;
import nc.vo.pub.BusinessException;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 通过远程调用执行测试用例,避免测试用例测试后台方法时找不到数据源
 * </p>
 * 创建日期:2011-12-27
 * 
 * @author:wangweir
 */
public class TestCaseImpl implements ITestCase {

	/**
	 * 简要说明
	 * 
	 * @see nc.itf.cm.ITestCase#runTestCaseWithRemoteCall(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
    public void runTestCaseWithRemoteCall(String className, String methodName)
			throws BusinessException {
		Method runMethod = null;
		@SuppressWarnings("rawtypes")
		Class clazz = null;
		try {
			clazz = Class.forName(className);
			runMethod = clazz.getMethod(methodName, (Class[]) null);
		} catch (ClassNotFoundException e) {
			Logger.error("create class object failed : " + className, e);
			throw new BusinessException("create class object failed : "
					+ className, e);
		} catch (SecurityException e) {
			Logger.error("Method \"" + methodName + "\" not found", e);
			throw new BusinessException("Method \"" + methodName
					+ "\" not found", e);
		} catch (NoSuchMethodException e) {
			Logger.error("Method \"" + methodName + "\" not found", e);
			throw new BusinessException("Method \"" + methodName
					+ "\" not found", e);
		}

		if (!Modifier.isPublic(runMethod.getModifiers())) {
			Logger.error("Method \"" + methodName + "\" should be public");
			throw new BusinessException("Method \"" + methodName
					+ "\" should be public");
		}

		try {
			runMethod.invoke(clazz.newInstance(), (Object[]) new Class[0]);
		} catch (IllegalArgumentException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		} catch (InstantiationException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		}
	}

}
