package nc.ui.mmgp.flexgant.view.treetable.userdefitem;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.ui.mmgp.flexgant.model.MMGPGantChartModel;
import nc.ui.mmgp.flexgant.pub.IGantTableData;
import nc.ui.uif2.editor.UserdefQueryParam;
import nc.ui.uif2.userdefitem.UserDefItemContainer;
import nc.vo.bd.userdefrule.UserdefitemVO;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMCollectionUtil;

/**
 * @Description: TODO
 *               <p>
 *               参照UserdefitemContainerListPreparator
 *               </p>
 * @data:2014-6-4下午3:23:25
 * @author: tangxya
 */
public class MMGPGantUserdefitemContainerPreparator implements IGantTableData {

	private UserDefItemContainer container;
	private List<UserdefQueryParam> params; // UserDefItemContainer参数中定义的值(规则编码rulecode或元数据实体全名spacename.entityname)

	private IMMGPUserDefGantItemsProcessor processor;

	@Override
	public void prepareGantTableData(MMGPGantChartModel model) {
		if (MMCollectionUtil.isNotEmpty(getParams())) {
			for (UserdefQueryParam param : getParams()) {
				UserdefitemVO[] items = null;
				String ruleCode = param.getRulecode();
				if (StringUtils.isNotEmpty(ruleCode)) {
					items = getContainer().getUserdefitemVOsByUserdefruleCode(
							ruleCode);
				} else {
					items = getContainer().getUserdefitemVOsByMDClassFullName(
							param.getMdfullname());
				}
				if (MMArrayUtil.isEmpty(items)) {
					continue;
				}
				this.getProcessor().resetItems(model, param.getPrefix(), items);
			}

		}

	}

	public UserDefItemContainer getContainer() {
		return container;
	}

	public void setContainer(UserDefItemContainer container) {
		this.container = container;
	}

	public List<UserdefQueryParam> getParams() {
		return params;
	}

	public void setParams(List<UserdefQueryParam> params) {
		this.params = params;
	}

	public IMMGPUserDefGantItemsProcessor getProcessor() {
		if(processor==null){
			processor=new MMGPDefaultUserDefGantItemsProcessor();
		}
		return processor;
	}

	public void setProcessor(IMMGPUserDefGantItemsProcessor processor) {
		this.processor = processor;
	}

}
