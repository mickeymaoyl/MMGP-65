package nc.vo.mmgp.batch;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 携带错误日志信息的反馈值对象. <br>
 * 适用于前后台批量操作中，部分执行，部分执行失败的情况.如：</br> <li>停用、启用；  <li>批改
 * 
 * @author 
 * @created on 
 * @since 
 * 
 */
public class MMGPValueObjWithErrLog implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 5802775899042467722L;
	/**
	 * 
	 */
	private Set<Object> vos = null;
	private Map<Object, List<String>> value2ErrMap = null;

	public Map<Object, List<String>> getValue2ErrMap() {
		return value2ErrMap;
	}

	public void setValue2ErrMap(Map<Object, List<String>> value2ErrMap) {
		this.value2ErrMap = value2ErrMap;
	}

	public MMGPValueObjWithErrLog() {
		super();
		vos = new HashSet<Object>();
		value2ErrMap = new HashMap<Object, List<String>>();
	}

	public MMGPValueObjWithErrLog(Object[] vos) {
		super();
		this.vos = new HashSet<Object>();
		value2ErrMap = new HashMap<Object, List<String>>();
		setVos(vos);
	}

	/**
	 * 获取后台批量操作返回值
	 * 
	 * @return
	 */
	public Object[] getVos() {
		if (vos.size() == 0)
			return new Object[0];
		return vos.toArray((Object[]) Array.newInstance(vos.iterator().next()
				.getClass(), vos.size()));
	}

	/**
	 * 设置后台批量操作返回值
	 * 
	 * @param returnValue
	 */
	public void setVos(Object[] vos) {
		this.vos.clear();
		this.vos.addAll(Arrays.asList(vos));
	}

	public void addErrLogMessage(Object vo, String errmsg) {
		this.vos.remove(vo);
		List<String> errList = value2ErrMap.get(vo);
		if (errList == null) {
			errList = new ArrayList<String>();
			value2ErrMap.put(vo, errList);
		}
		errList.add(errmsg);
	}

}
