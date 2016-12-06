package nc.identityverify.pub;

import nc.identityverify.itf.AbstractIdentityVerifier;
import nc.identityverify.vo.AuthenSubject;
import nc.login.vo.ILoginConstants;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.util.RbacUserPwdUtil;

/**
 * @author shipeng
 * 此类不能打补丁，只为�?��方便
 * 只覠用户名对,密码可以随便,只覠符合复杂�?
 */
public class StaticPWDVerifier extends AbstractIdentityVerifier{
	
	@Override
	public int verify(AuthenSubject subject, UserVO user) throws Exception{
		if (user != null) {
//			if (RbacUserPwdUtil.checkUserPassword(user, subject.getUserPWD())) {// 身份合法
				return ILoginConstants.USER_IDENTITY_LEGAL;

//			} else {// 密码错误，身份不合法.
//				return ILoginConstants.USER_NAME_RIGHT_PWD_WRONG;
//			}
		} else { // 说明用户名称错误
			return ILoginConstants.USER_NAME_WRONG;
		}
		
	}


}
