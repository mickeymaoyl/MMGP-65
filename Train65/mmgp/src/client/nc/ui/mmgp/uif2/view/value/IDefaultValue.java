package nc.ui.mmgp.uif2.view.value;

import java.util.Map;

/**
 * 给前台界面新增时设置默认值接口
 * 
 * @author zhumh
 *
 */
public interface IDefaultValue {

	/**
	 * 获得表头或表尾的属性/值对
	 * @return Map<String(属性), Object(值)>
	 */
	Map<String, Object> getHeadTailValue();
	
}
