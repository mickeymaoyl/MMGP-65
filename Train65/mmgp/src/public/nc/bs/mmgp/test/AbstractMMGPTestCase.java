package nc.bs.mmgp.test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Properties;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.common.UserExit;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.core.service.IFwLogin;
import nc.bs.framework.test.AbstractTestCase;
import nc.itf.mmgp.ITestCase;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.util.RbacUserPwdUtil;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 测试用例抽象层，与平台隔离。 由于平台增加了安全特性，造成原来的TestCase运行出错。
 * </p>
 * 
 * @since: 6.3 创建日期:Mar 4, 2013
 * @author:wangweir
 */
public abstract class AbstractMMGPTestCase extends AbstractTestCase {

    protected Properties props = new Properties();

    /**
     * 
     */
    private static final String USER_CODE = "test_yonyou_test";

    /**
     * 
     */
    private static final String DEFAULT_PWD = "1";

    /**
     * 
     */
    private String userPK;

    @Override
    protected void setUp() throws Exception {

        RuntimeEnv.getInstance().setProperty("CLIENT_COMMUNICATOR", "nc.bs.framework.comn.cli.JavaURLCommunicator");
        this.props.setProperty("CLIENT_COMMUNICATOR", "nc.bs.framework.comn.cli.JavaURLCommunicator");

        RuntimeEnv.getInstance().setRunningInServer(false);

        String baseURL = "http://" + getHost() + ":" + getPort();

        RuntimeEnv.getInstance().setProperty("SERVICEDISPATCH_URL", baseURL + "/ServiceDispatcherServlet");
        this.props.setProperty("SERVICEDISPATCH_URL", baseURL + "/ServiceDispatcherServlet");
        RuntimeEnv.getInstance().setProperty("CLIENT_COMMUNICATOR", "nc.bs.framework.comn.cli.JavaURLCommunicator");
        this.props.setProperty("CLIENT_COMMUNICATOR", "nc.bs.framework.comn.cli.JavaURLCommunicator");
        UserExit.getInstance().setBizCenterCode(getBizCenterCode());
        // SocketAddress addr = new InetSocketAddress(getHost(), Integer.parseInt(getPort()));
        //
        // Socket socket = new Socket();
        // try {
        // socket.connect(addr, 1000);
        // System.setProperty("nc.bs.logging.default.level", "ERROR");
        // ((IFwLogin) NCLocator.getInstance().lookup(IFwLogin.class)).login("1", "1", null);
        // } catch (Exception e) {
        // } finally {
        // System.clearProperty("nc.bs.logging.default.level");
        // try {
        // socket.close();
        // } catch (IOException e) {
        // }
        // }

        UserVO user = initOneUser();
        initToken(user);
    }

    /**
     * @param user
     */
    private void initToken(UserVO user) {
        IFwLogin loginService = NCLocator.getInstance().lookup(IFwLogin.class);
        byte[] token = loginService.login(user.getUser_code(), DEFAULT_PWD, null);
        NetStreamContext.setToken(token);
        try {
            NCLocator.getInstance().lookup(IVOPersistence.class).deleteByPK(UserVO.class, userPK);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     * @throws BusinessException
     */
    private UserVO initOneUser() throws BusinessException {
        UserVO user = new UserVO();
        user.setUser_code(USER_CODE);
        user.setUser_code_q(USER_CODE);
        user.setUser_name(USER_CODE);
        user.setUser_type(1);
        user.setAbledate(new UFDate());

        InvocationInfoProxy.getInstance().setUserCode(USER_CODE);

        userPK = NCLocator.getInstance().lookup(IVOPersistence.class).insertVO(user);
        user = (UserVO) NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByPK(UserVO.class, userPK);
        String codecPwdByMD5 = RbacUserPwdUtil.getEncodedPassword(user, DEFAULT_PWD);
        user.setUser_password(codecPwdByMD5);
        user.setStatus(VOStatus.UPDATED);

        NCLocator.getInstance().lookup(IVOPersistence.class).updateVO(user);
        return user;
    }

    // @Override
    protected void tearDown() throws Exception {
        // super.tearDown();
        NetStreamContext.resetAll();
    }

    protected String getBizCenterCode() {
        return System.getProperty("nc.systemid", "0001");
    }

    protected String getHost() {
        return System.getProperty("nc.host", "localhost");
    }

    protected String getPort() {
        return System.getProperty("nc.port", "80");
    }

    public NCLocator getLocator() {
        return NCLocator.getInstance(this.props);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#runTest()
     */
    @Override
    protected void runTest() throws Throwable {
        assertNotNull(this.getName()); // Some VMs crash when calling
        // getMethod(null,null);
        Method runMethod = null;
        try {
            // use getMethod to get all public inherited
            // methods. getDeclaredMethods returns all
            // methods of this class but excludes the
            // inherited ones.
            runMethod = getClass().getMethod(this.getName(), (Class[]) null);
        } catch (NoSuchMethodException e) {
            fail("Method \"" + this.getName() + "\" not found");
        }
        if (!Modifier.isPublic(runMethod.getModifiers())) {
            fail("Method \"" + this.getName() + "\" should be public");
        }

        NCLocator
            .getInstance()
            .lookup(ITestCase.class)
            .runTestCaseWithRemoteCall(this.getClass().getName(), this.getName());
        // runMethod.invoke(this, (Object[])new Class[0]);}
    }

}
