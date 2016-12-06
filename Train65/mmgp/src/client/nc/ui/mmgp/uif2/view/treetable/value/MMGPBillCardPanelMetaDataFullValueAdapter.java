package nc.ui.mmgp.uif2.view.treetable.value;

import java.util.HashMap;

import nc.bs.logging.Logger;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.md.model.impl.MDBean;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.uif2.editor.value.AbstractComponentValueAdapter;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.meta.entity.vo.PseudoColumnAttribute;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: TODO
 *               <p>
 *               详细功能描述
 *               </p>
 * @data:2014-5-15上午11:22:39
 * @author: tangxya
 */
public class MMGPBillCardPanelMetaDataFullValueAdapter extends
		AbstractComponentValueAdapter {

	private boolean useFakeRowNO = true;

	@SuppressWarnings("unchecked")
	@Override
	public Object getValue() {
		BillCardPanel panel = (BillCardPanel) this.getComponent();
		IBill bill = null;
		try {
			BillTempletVO billTempletVO = panel.getBillData()
					.getBillTempletVO();
			bill = (IBill) NCObject.newInstanceWithKeyValues(
					billTempletVO.getHeadVO().getBillMetaDataBusinessEntity(),
					new HashMap<Object, Object>()).getContainmentObject();
			String headClassName = this
					.getClassNameByMetadataclass(billTempletVO.getHeadVO()
							.getMetadataclass());
			ISuperVO head = (ISuperVO) panel.getBillData().getHeaderValueVO(
					headClassName);
			head.setStatus(panel.getBillData().getBillstatus());
			bill.setParent(head);
			BillTabVO[] tabVOs = panel.getBillData().getBillTabVOsByPosition(
					Integer.valueOf(IBillItem.BODY));
			if (tabVOs == null) {
				return bill;
			}
			for (BillTabVO tabVO : tabVOs) {
				if (!StringUtils.isBlank(tabVO.getBasetab())) {
					continue;
				}

				if (tabVO.getMetadataclass() != null) {
					String bodyClassName = this
							.getClassNameByMetadataclass(tabVO
									.getMetadataclass());
					ISuperVO[] body = (ISuperVO[]) panel.getBillData()
							.getBodyValueVOs(tabVO.getTabcode(), bodyClassName);

					// !!!!!!!!!!!!!!!!!!!!!!改了获得表体的方法!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					// ISuperVO[] body = (ISuperVO[])
					// panel.getBodyPanel().getTableModel().getBodyValueVOs(bodyClassName);

					bill.setChildren((Class<? extends ISuperVO>) Class
							.forName(this.getClassNameByMetadataclass(tabVO
									.getMetadataclass())), body);
				}
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		if (this.isUseFakeRowNO()) {
			this.setFakeRowNO(bill);
		}
		return bill;

	}

	public void setFakeRowNO(Object value) {
		if (value instanceof AggregatedValueObject) {
			if (value instanceof AbstractBill) {
				AbstractBill bill = (AbstractBill) value;
				IVOMeta[] voMetas = bill.getMetaData().getChildren();
				if (voMetas == null) {
					return;
				}
				// 处理多子表
				for (IVOMeta voMeta : voMetas) {
					ISuperVO[] vos = bill.getChildren(voMeta);
					if (vos != null) {
						for (int i = 0; i < vos.length; i++) {
							vos[i].setAttributeValue(
									PseudoColumnAttribute.PSEUDOCOLUMN,
									Integer.valueOf(i));
						}
					}
				}
			} else {
				CircularlyAccessibleValueObject[] childrenVO = ((AggregatedValueObject) value)
						.getChildrenVO();
				if (childrenVO != null) {
					for (int i = 0; i < childrenVO.length; i++) {
						childrenVO[i].setAttributeValue(
								PseudoColumnAttribute.PSEUDOCOLUMN,
								Integer.valueOf(i));
					}
				}
			}
		}

	}

	public void setUseFakeRowNO(boolean useFakeRowNO) {
		this.useFakeRowNO = useFakeRowNO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object object) {

		Logger.debug("全部刷新");

		BillCardPanel panel = (BillCardPanel) this.getComponent();
		if (object == null) {
			Logger.debug("BillCardPanelVOValueGetter填充空值");
			//2014-08-26 lisyd 没有清除表头数据，正确的应该是下面的处理方法
//			panel.getBodyPanel().getTableModel().clearBodyData();
			panel.getBillData().clearViewData();
			return;
		}

		AbstractBill bill = (AbstractBill) object;
		panel.getBillData().setHeaderValueVO(
				(CircularlyAccessibleValueObject) bill.getParent());
		panel.getBillData().loadLoadHeadRelation();
		BillTabVO[] tabVOs = panel.getBillData().getBillTabVOsByPosition(
				Integer.valueOf(IBillItem.BODY));
		if (tabVOs != null) {
			for (BillTabVO tabVO : tabVOs) {
				if (!StringUtils.isBlank(tabVO.getBasetab())) {
					continue;
				}
				try {
					if (tabVO.getMetadataclass() != null
							&& !tabVO.getMetadataclass().equals("")) {
						String bodyClassName = this
								.getClassNameByMetadataclass(tabVO
										.getMetadataclass());
						panel.getBillData()
								.setBodyValueVO(
										tabVO.getTabcode(),
										(CircularlyAccessibleValueObject[]) bill
												.getChildren((Class<? extends ISuperVO>) Class
														.forName(bodyClassName)));
						panel.getBillModel(tabVO.getTabcode())
								.loadLoadRelationItemValue();
					}
				} catch (Exception e) {
					// 日志异常
					ExceptionUtils.wrappException(e);
				}
			}
		}
		panel.getBillData().updateValue();
		// 手动执行表头表尾的item项的显示公式
		// panel.execHeadTailLoadFormulas();
	}

	protected boolean isUseFakeRowNO() {
		return this.useFakeRowNO;
	}

	private String getClassNameByMetadataclass(String metadataclass)
			throws MetaDataException {
		IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullName(
				metadataclass);
		String className = ((MDBean) bean).getFullClassName();
		return className;
	}

}
