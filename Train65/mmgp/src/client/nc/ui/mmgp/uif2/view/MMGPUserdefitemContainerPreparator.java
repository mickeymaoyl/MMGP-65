package nc.ui.mmgp.uif2.view;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.bill.BillData;
import nc.ui.uif2.editor.TemplateContainer;
import nc.ui.uif2.editor.UserdefQueryParam;
import nc.ui.uif2.editor.UserdefitemContainerPreparator;
import nc.ui.uif2.userdefitem.QueryParam;
import nc.vo.pub.bill.BillTabVO;

/**
 * <b> </b>
 * @author chenleif
 * @date 2013-4-19
 * @description
 */
public class MMGPUserdefitemContainerPreparator extends UserdefitemContainerPreparator {
	private TemplateContainer templateContainer;
	private boolean isSingleBody = false;
	private String nodekey="";
	
	@Override
	public void setParams(List<UserdefQueryParam> params) {
		super.setParams(params);
	}
	
	@Override
	public void prepareBillData(BillData billData) {
		String mdfullname = getMdfullName();
		for (UserdefQueryParam param : getParams()) {
			param.setMdfullname(mdfullname);
		}
		List<QueryParam> queryParamList = new ArrayList<QueryParam>();
		List<UserdefQueryParam> bodyDefQueryParamList = getBodyDefQueryParamList();
		for (UserdefQueryParam bodyDefParam : bodyDefQueryParamList) {
			QueryParam param = new QueryParam();
			param.setMdfullname(bodyDefParam.getMdfullname());
			param.setRulecode(bodyDefParam.getRulecode());
			queryParamList.add(param);
		}
//		
//		if (getIsSingleBody()) {
//			setParams(bodyDefQueryParamList);
//			getContainer().setParams(queryParamList);
//		}else {
//			
//		}
		getParams().addAll(bodyDefQueryParamList);
		getContainer().getParams().addAll(queryParamList);
		
		super.prepareBillData(billData);
	}

	private String getMdfullName() {
		return getContainer().getParams().get(0).getMdfullname();
	}
	
	
	private List<UserdefQueryParam> getBodyDefQueryParamList() {
		List<UserdefQueryParam> paramList = new ArrayList<UserdefQueryParam>();
		BillTabVO[] billTabVOs = getTemplateContainer().getTemplate(nodekey).getHeadVO().getStructvo().getBillTabVOs();
		for (BillTabVO tabVO : billTabVOs) {
			if(tabVO.getPos() == 1){
				UserdefQueryParam param = new UserdefQueryParam();
				param.setPos(1);
				//如果是单表体，则前缀是vdef，否则是vbdef
				if (getIsSingleBody()) {
					param.setPrefix("vdef");
				}else {
					param.setPrefix("vbdef");
				}
//				param.setPrefix("vbdef");
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
