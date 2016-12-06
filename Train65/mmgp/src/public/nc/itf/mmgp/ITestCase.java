package nc.itf.mmgp;

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
public interface ITestCase {

    /**
     * 通过远程调用执行测试用例,避免测试用例测试后台方法时找不到数据源
     * 
     * @param className
     *        测试用例类名
     * @param methodName
     *        测试用例方法名
     * @throws BusinessException
     *         异常
     */
    void runTestCaseWithRemoteCall(String className,
                                   String methodName) throws BusinessException;
}
