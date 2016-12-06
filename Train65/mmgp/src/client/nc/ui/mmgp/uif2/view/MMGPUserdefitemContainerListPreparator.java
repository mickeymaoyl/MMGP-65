package nc.ui.mmgp.uif2.view;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.uif2.editor.TemplateContainer;
import nc.ui.uif2.editor.UserdefQueryParam;
import nc.ui.uif2.editor.UserdefitemContainerListPreparator;
import nc.ui.uif2.userdefitem.QueryParam;
import nc.vo.pub.bill.BillTabVO;

/**
 * <b> </b>
 * @author chenleif
 * @date 2013-4-18
 * @description
 */
public class MMGPUserdefitemContainerListPreparator extends UserdefitemContainerListPreparator{
	private TemplateContainer templateContainer;
	private boolean isSingleBody = false;
	private String nodekey="";
	
	@Override
	public void setParams(List<UserdefQueryParam> params) {
		super.setParams(params);
	}
	
	
	
	@Override
	public void prepareBillListData(BillListData bld) {
		String mdfullname = getMdfullName();
		for (UserdefQueryParam param : getParams()) {
			param.setMdfullname(mdfullname);
		}
		List<QueryParam> queryParamList = new ArrayList<QueryParam>();
		List<UserdefQueryParam> bodyDefQueryParamList = getBodyDefQueryParamList();
		for (UserdefQueryParam bodyDefQueryParam : bodyDefQueryParamList) {
			QueryParam param = new QueryParam();
			param.setMdfullname(bodyDefQueryParam.getMdfullname());
			param.setRulecode(bodyDefQueryParam.getRulecode());
			queryParamList.add(param);
		}
		if (getIsSingleBody()) {
			setParams(bodyDefQueryParamList);
			getContainer().setParams(queryParamList);
		}else {
			getParams().addAll(bodyDefQueryParamList);
			getContainer().getParams().addAll(queryParamList);
			
		}
		
		super.prepareBillListData(bld);
	}

	private String getMdfullName() {
		return getContainer().getParams().get(0).getMdfullname();
	}
	
	
	private List<UserdefQueryParam> getBodyDefQueryParamList() {
		List<UserdefQueryParam> paramList = new ArrayList<UserdefQueryParam>();
		BillTabVO[] billTabVOs = getTemplateContainer().getTemplate(nodekey).getHeadVO().getStructvo().getBillTabVOs();
		for (BillTabVO tabVO : billTabVOs) {
			if(tabVO.getPos() == BillItem.BODY){
				UserdefQueryParam param = new UserdefQueryParam();
				param.setPos(1);
				//如果是单表体，则前缀是vdef，否则是vbdef
				if (getIsSingleBody()) {
					param.setPrefix("vdef");
				}else {
					param.setPrefix("vbdef");
				}
				param.setTabcode(tabVO.getTabcode());
				param.setMdfullname(tabVO.getMetadataclass());
				paramList.add(param);
			}
			
		}
		return paramList;
	}

	public TemplateContainer getTemplateContainer() {
		return templateContainer;
	}
	public void setTemplateContainer(TemplateContainer templateContainer) {
		this.templateContainer = templateContainer;
	}



	public boolean getIsSingleBody() {
		return isSingleBody;
	}



	public void setIsSingleBody(boolean isSingleBody) {
		this.isSingleBody = isSingleBody;
	}



    public String getNodekey() {
        return nodekey;
    }



    public void setNodekey(String nodekey) {
        this.nodekey = nodekey;
    }
}
