package nc.itf.mmgp;

import nc.vo.pub.BusinessException;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ͨ��Զ�̵���ִ�в�������,��������������Ժ�̨����ʱ�Ҳ�������Դ
 * </p>
 * ��������:2011-12-27
 * 
 * @author:wangweir
 */
public interface ITestCase {

    /**
     * ͨ��Զ�̵���ִ�в�������,��������������Ժ�̨����ʱ�Ҳ�������Դ
     * 
     * @param className
     *        ������������
     * @param methodName
     *        ��������������
     * @throws BusinessException
     *         �쳣
     */
    void runTestCaseWithRemoteCall(String className,
                                   String methodName) throws BusinessException;
}
