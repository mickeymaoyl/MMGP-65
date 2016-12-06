package nc.ui.mmgp.uif2.query;

import java.util.ArrayList;
import java.util.List;

import nc.itf.org.IOrgConst;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.query2.DefaultQueryConditionDLG;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.refregion.CommonTwoLayerListener;
import nc.ui.querytemplate.filtereditor.FilterEditorWrapper;
import nc.ui.querytemplate.filtereditor.IFilterEditor;
import nc.ui.querytemplate.value.IFieldValueElement;
import nc.vo.mmgp.util.MMCollectionUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

/**
 */

@SuppressWarnings("deprecation")
public class MMGPOrgFilter extends CommonTwoLayerListener {

	// // ɾ�������ղ���
	// UFBoolean isstorcDelete;

	private String defaultWhere = null;

	// һֱ��ʾ����+��֯����
	private UFBoolean isAlwaysShowAllData;

	// ��֯�ֶ�
	private String pk_orgCode = "pk_org";

	// �����ֶ�pk�ֶ���
	private String ref_pkField;

	public MMGPOrgFilter(DefaultQueryConditionDLG dlg, String ref_pkField) {
		this(new QueryConditionDLGDelegator(dlg), ref_pkField);
	}

	public MMGPOrgFilter(QueryConditionDLGDelegator dlg, String ref_pkField) {
		super(dlg);
		this.ref_pkField = ref_pkField;
	}

	public void addEditorListener() {
		this.setFatherPath(this.pk_orgCode);
		this.setChildPath(this.ref_pkField);
		// ע��༭�¼�
		this.qryCondDLGDelegator.registerCriteriaEditorListener(this);
	}

	@Override
	public void setChildRefRegion(List<IFieldValueElement> fatherValues,
			IFilterEditor editor) {
		List<String> diffValues = new ArrayList<String>();
		for (IFieldValueElement fve : fatherValues) {
			if (diffValues.contains(fve.getSqlString())) {
				continue;
			}

			diffValues.add(fve.getSqlString());
		}

		FilterEditorWrapper wrapper = new FilterEditorWrapper(editor);
		UIRefPane refPane = (UIRefPane) wrapper
				.getFieldValueElemEditorComponent();

		// �Ƿ�һֱ��ʾ����+��֯����
		if (this.isAlwaysShowAllData != null
				&& this.isAlwaysShowAllData.booleanValue()) {
			refPane.setMultiCorpRef(true);
			return;
		}

		// ���֮ǰ��ֵ
		refPane.getRefModel().clearData();
		// �������֯Ψһ����������֯���ˣ������ſɼ�
		if (diffValues.size() > 0) {
			if (MMCollectionUtil.isNotEmpty(diffValues)
					&& MMCollectionUtil.isNotEmpty(fatherValues)) {
				AbstractRefModel model = refPane.getRefModel();
				if (defaultWhere == null) {
					defaultWhere = model.getWherePart();
					if (defaultWhere == null) {
						defaultWhere = " 1 = 1 ";
					}
				}
				model.setAddEnvWherePart(false);
				SqlBuilder sql = new SqlBuilder();

				if (diffValues.size() == 1) {
					String pkOrgString = diffValues.get(0);
					if (pkOrgString.toLowerCase().indexOf("select ") == -1) {
						sql.append(pk_orgCode,
								diffValues.toArray(new String[0]));
					} else {
						sql.append(pk_orgCode + " in (" + pkOrgString + ")");
					}
				} else {
					sql.append(pk_orgCode, diffValues.toArray(new String[0]));
				}
				String newWherePart = " (" + sql.toString() + " or ("
						+ pk_orgCode + "='"
						+ AppContext.getInstance().getPkGroup() + "') or "
						+ pk_orgCode + "='" + IOrgConst.GLOBEORG + "')";
				if (MMStringUtil.isNotEmpty(defaultWhere)) {
					model.setWherePart(defaultWhere + " and " + newWherePart);
				} else {
					model.setWherePart(newWherePart);
				}
			}
		}

		refPane.setMultiCorpRef(false);
		// new
		// FilterSupplierRefUtils(refPane).filtByDeleteFlag(this.isstorcDelete);
	}

	public void setIsAlwaysShowAllData(UFBoolean isAlwaysShowAllData) {
		this.isAlwaysShowAllData = isAlwaysShowAllData;
	}

	// public void setIsstorcDelete(UFBoolean isstorcDelete) {
	// this.isstorcDelete = isstorcDelete;
	// }

	public void setPk_orgCode(String pkOrgCode) {
		this.pk_orgCode = pkOrgCode;
	}

}
