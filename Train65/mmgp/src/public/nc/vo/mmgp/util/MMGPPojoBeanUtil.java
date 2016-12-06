package nc.vo.mmgp.util;

import java.lang.reflect.Method;
import java.util.List;

import nc.vo.pub.BeanHelper;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pub.lang.UFTime;
/***
 * 
 * Pojo转换工具类
 * @author xiegg
 *
 */
public final class MMGPPojoBeanUtil {
	/**
	 * 将VO对象转换成pojo对象
	 * @param <E>
	 * @param pojoClazz
	 * @param superVO
	 * @return
	 * @throws Exception
	 */
	public static <E> E superVO2Pojo(final Class<E> pojoClazz, final ISuperVO superVO)
			throws Exception {
		if (superVO == null) {
			return null;
		}
		if (pojoClazz == null) {
			return null;
		}
		
		E pojo = (E) pojoClazz.newInstance();

		List<String> list = BeanHelper.getPropertys(pojo);

		if (list == null || list.isEmpty()) {
			return null;
		}

		for (String att : list) {
			Class<?> type = getAttributeNameType(att, pojo);
			Method setmethod = BeanHelper.getSetMethod(pojo, att);
			Object value = getBeanValue(type, superVO.getAttributeValue(att)) ;
			if(value == null){
				continue ;
			}
			setmethod.invoke(pojo,value);
		}

		return pojo;
	}
	
	/***
	 * 将pojo对象转换成VO
	 * @param <E>
	 * @param superVOClazz
	 * @param pojoBean
	 * @return
	 * @throws Exception
	 */
	public static <E extends ISuperVO> E pojo2SuperVO(final Class<E> superVOClazz,final Object pojoBean)throws Exception{
		if(superVOClazz == null || pojoBean == null){
			return null;
		}
		
		E superVO = (E) superVOClazz.newInstance();
		
		List<String> list = BeanHelper.getPropertys(superVO) ;
		
		if(list == null || list.isEmpty()){
			return null ;
		}
		
		for(String att : list){							
			
			Class<?> type = getAttributeNameType(att,superVO) ;
			Method getmethod = BeanHelper.getGetMethod(pojoBean, att) ;
			
			if(getmethod == null){
				continue ;
			}
			
			Object value = getBeanValue(type, getmethod.invoke(pojoBean)) ;
			
			if(value == null){
				continue ;
			}
				
			superVO.setAttributeValue(att, value) ;
/*			Method setmethod = BeanHelper.getSetMethod(superVO, att) ;
			if(setmethod == null){
				continue ;
			}
			setmethod.invoke(getBeanValue(type,value)) ;*/
		}
		
		return superVO ;
	}

	private static Object getBeanValue(Class<?> valuetype, Object value)
			throws Exception {
		if (UFDouble.class.equals(valuetype) || double.class.equals(valuetype)) {
			return getUFDouble(value);
		} else if (Integer.class.equals(valuetype)
				|| int.class.equals(valuetype)) {
			return getInteger(value);
		} else if (UFDate.class.equals(valuetype)) {
			return getUFDate(value);
		} else if (UFDateTime.class.equals(valuetype)) {
			return getUFDateTime( value);
		} else if (UFBoolean.class.equals(valuetype)
				|| boolean.class.equals(valuetype)) {
			return getUFBoolean(value);
		} else if (java.lang.String.class.equals(valuetype)) {
			return getString( value);
		}else if (UFTime.class.equals(valuetype)) {
			return getUFTime(value);
		}else if (UFLiteralDate.class.equals(valuetype)){
			return getUFLiteralDate(value);
		}
		return null;
	}
	
	public static UFLiteralDate getUFLiteralDate(Object value) throws Exception {
		if (value == null) {
			return null;
		}
		return new UFLiteralDate(String.valueOf(value));
	}

	public static UFDate getUFDate(Object value) throws Exception {
		if (value == null) {
			return null;
		}
		return new UFDate(String.valueOf(value));
	}

	public static UFBoolean getUFBoolean(Object value)
			throws Exception {
		if (value == null) {
			return null;
		}
		return new UFBoolean(String.valueOf(value));

	}

	public static UFDateTime getUFDateTime(Object value)
			throws Exception {
		if (value == null) {
			return null;
		}
		return new UFDateTime(String.valueOf(value));
	}

	public static UFTime getUFTime(Object value)
			throws Exception {
		if (value == null) {
			return null;
		}
		return new UFTime(String.valueOf(value));
	}
	
	public static UFDouble getUFDouble(Object value)
			throws Exception {
		if (value == null) {
			return null;
		}
		return new UFDouble(String.valueOf(value));

	}

	public static Integer getInteger(Object value)
			throws Exception {
		if (value == null) {
			return null;
		}

		return Integer.valueOf(String.valueOf(value)) ;


	}

	public static String getString(Object value)
			throws Exception {
		if (value == null) {
			return null;
		}
		return String.valueOf(value) ;
	
	}


	private static Class<?> getAttributeNameType(String attributeName, Object vo) {
		if (attributeName == null || "".equals(attributeName) || vo == null) {
			return null;
		}

		Method[] methods = BeanHelper.getInstance().getAllGetMethod(
				vo.getClass(), new String[] { attributeName });

		if (methods == null || methods.length != 1 || methods[0] == null) {
			return null;
		}
		return methods[0].getReturnType();
	}
}
