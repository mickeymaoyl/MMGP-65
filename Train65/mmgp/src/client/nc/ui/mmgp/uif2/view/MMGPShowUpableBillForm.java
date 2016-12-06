package nc.ui.mmgp.uif2.view;

import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import nc.md.model.IBusinessEntity;
import nc.md.model.access.javamap.IBeanStyle;
import nc.md.model.access.javamap.NCBeanStyle;
import nc.md.model.access.javamap.POJOStyle;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.mmgp.uif2.view.value.IDefaultValue;
import nc.ui.mmgp.uif2.view.value.MMGPCardPanelDefaultValueSetter;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.model.IAppModelEx;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.pubapp.uif2app.view.util.RefMoreSelectedUtils;
import nc.ui.pubapp.uif2app.view.value.BillCardPanelMetaDataValueAdapter;
import nc.ui.pubapp.uif2app.view.value.BillCardPartiallyRefreshValueAdapter;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.ui.uif2.model.AppEventConst;
import nc.vo.pub.bill.BillTabVO;

public class MMGPShowUpableBillForm extends ShowUpableBillForm {
	private static final long serialVersionUID = -4880360020628899693L;
	private MMGPCardPanelDefaultValueSetter defaultValueSetter;
	private IDefaultValue defaultValue;
	private List<MultiRefConfig> multiSelectRefConfigs;

	@Override
	public void initUI() {
		super.initUI();
		/**
		 * ��ԭ����componentValueManager �и����⣺<br>
		 * �޸�ʱ ɾ���������ǲ���ʹ��<br>
		 * ԭ������<br>
		 * Object nc.ui.pubapp.uif2app.view.value.
		 * BillCardPanelMetaDataFullValueAdapter.getValue() <br>
		 * ������ ��ñ������� ʹ����getBodyValueVOs������65�У�,ȡ���ǽ����ϵ�ֵ��������ɾ���� <br>
		 * ISuperVO[] body = (ISuperVO[]) panel.getBillData().getBodyValueVOs(<br>
		 * tabVO.getTabcode(), bodyClassName);<br>
		 */
		// componentValueManager = new BillCardPanelMetaDataValueAdapter();
		// componentValueManager.setComponent(billCardPanel);
		defaultValueSetter = new MMGPCardPanelDefaultValueSetter();
		// default listener
		if (multiSelectRefConfigs != null && multiSelectRefConfigs.size() > 0) {
			IAppModelEx modelEx = (IAppModelEx) getModel();
			for (final MultiRefConfig refConfig : multiSelectRefConfigs) {
				setRefPaneMulti(refConfig);
				IAppEventHandler<CardBodyAfterEditEvent> listener = createMultiProcessListener(refConfig);
				modelEx.addAppEventListener(CardBodyAfterEditEvent.class,
						listener);
			}
		}
		BillTabVO[] tabVos = getBillCardPanel().getBillData().getAllTabVos();
		for (BillTabVO tabVO : tabVos) {
			if (tabVO.getPos() != BillItem.BODY) {
				continue;
			}
			if (tabVO.getBasetab() != null) {
				BillTabVO tempVO = getBillCardPanel().getBillData().getTabVO(
						IBillItem.BODY, tabVO.getBasetab());
				if (tempVO != null) {
					tabVO = tempVO;
				}
			}
			this.getBillCardPanel().setBodyAutoAddLine(tabVO.getTabcode(),
					this.isAutoAddLine());
		}
		this.setRequestFocus(true);
	}

	protected void setRefPaneMulti(MultiRefConfig refConfig) {
		JComponent comp;
		if (refConfig.getTableCode() == null) {
			comp = getBillCardPanel().getBodyItem(refConfig.getItemKey())
					.getComponent();
		} else {
			comp = getBillCardPanel().getBodyItem(refConfig.getTableCode(),
					refConfig.getItemKey()).getComponent();
		}
		UIRefPane refPane = (UIRefPane) comp;
		refPane.setMultiSelectedEnabled(true);
	}

	protected IAppEventHandler<CardBodyAfterEditEvent> createMultiProcessListener(
			final MultiRefConfig refConfig) {
		IAppEventHandler<CardBodyAfterEditEvent> listener = new IAppEventHandler<CardBodyAfterEditEvent>() {

			@Override
			public void handleAppEvent(CardBodyAfterEditEvent event) {
				if (refConfig.getTableCode() != null
						&& !refConfig.getTableCode().equals(
								event.getTableCode())) {
					return;
				}
				if (!event.getKey().equals(refConfig.getItemKey())) {
					return;
				}
				RefMoreSelectedUtils utils = new RefMoreSelectedUtils(
						event.getBillCardPanel());
				utils.refMoreSelected(event.getRow(), event.getKey(),
						refConfig.isAddLine());
			}

		};
		return listener;
	}

	@Override
	protected void setDefaultValue() {
		defaultValueSetter.setDefaultValue(getModel().getContext(),
				getBillCardPanel());
		if (defaultValue == null) {
			return;
		}
		Map<String, Object> keyValues = defaultValue.getHeadTailValue();
		for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
			setHeadTailValue(entry.getKey(), entry.getValue());
		}
		// ִ�б༭��ʽ
		getBillCardPanel().execHeadTailEditFormulas();
	}

	protected void setHeadTailValue(String key, Object value) {
		BillItem item = getBillCardPanel().getHeadTailItem(key);
		if (item != null) {
			Object defaultValue = item.getDefaultValue();
			if (defaultValue == null) {
				item.setValue(value);
				this.getBillCardPanel().getBillData().loadEditHeadRelation(key);
			}
		}
	}

	@Override
	public void setModel(AbstractAppModel model) {
		super.setModel(model);
		// ����Լ�������componentValueManager���򲻹�
		if (!BillCardPartiallyRefreshValueAdapter.class
				.equals(componentValueManager.getClass())) {
			return;
		}
		// ������ͷ�����
		IBusinessEntity entity = (IBusinessEntity) MMGPMetaUtils.getIBean(model
				.getContext());
		IBeanStyle beanStype = entity.getBeanStyle();
		if (beanStype instanceof NCBeanStyle || beanStype instanceof POJOStyle) {
			componentValueManager = new BillCardPanelMetaDataValueAdapter();
		}
	}

	@Override
	public void handleEvent(AppEvent event) {
		super.handleEvent(event);
		if (AppEventConst.SHOW_EDITOR.equals(event.getType())) {
			showMeUp();
		}
        if (OrgChangedEvent.class.getName().equals(event.getType())) {
            if (this.getModel().getUiState() == UIState.ADD) {
                OrgChangedEvent orgChangedEvent = (OrgChangedEvent) event;
                String pk_org = orgChangedEvent.getNewPkOrg();
                getModel().getContext().setPk_org(pk_org);
                /* Oct 15, 2013 wangweir ��֯�л�ʱ����addNew Begin */
                this.addNew();
                // getBillCardPanel().addNew();
                // setDefaultValue();
                /* Oct 15, 2013 wangweir End */
            }
        }
	}

	public List<MultiRefConfig> getMultiSelectRefConfigs() {
		return multiSelectRefConfigs;
	}

	public void setMultiSelectRefConfigs(
			List<MultiRefConfig> multiSelectRefConfigs) {
		this.multiSelectRefConfigs = multiSelectRefConfigs;
	}

	public void setDefaultValue(IDefaultValue defaultValue) {
		this.defaultValue = defaultValue;
	}

}
