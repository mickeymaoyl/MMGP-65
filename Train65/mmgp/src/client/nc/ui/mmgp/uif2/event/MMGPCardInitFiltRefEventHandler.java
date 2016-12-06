package nc.ui.mmgp.uif2.event;

import java.util.List;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.editor.IBillCardPanelEditor;
import nc.vo.pub.lang.UFBoolean;

/** 建议使用pubapp的事件注册方式 */
@Deprecated
public class MMGPCardInitFiltRefEventHandler implements IAppEventHandler<AppEvent> {

	private static final int ITEMKEY = 0;

	private static final int WHERESQL = 1;

	private static final int BODY = 2;

	private static final int TABLECODE = 3;

	private IBillCardPanelEditor editor;

	private boolean isAddWhereSql = false;

	private List<String> refInfo;

	public IBillCardPanelEditor getEditor() {
		return editor;
	}

	public void setEditor(IBillCardPanelEditor editor) {
		this.editor = editor;
	}

	@Override
	public void handleAppEvent(AppEvent event) {

		UIRefPane refPanel = null;
		boolean isbody = false;
		for (int i = 0; i < getRefInfo().size(); i++) {
			String[] refInfo = getRefInfo().get(i).split(":");

			if (refInfo.length < 2) {
				throw new IllegalArgumentException(NCLangRes.getInstance().getStrByID("mmgp001_0", "0mmgp001-0091", null, new String[]{getRefInfo().get(i)})/*条件[{0}]需要有冒号(:)分隔!*/);
			}
			// 表头
			else if (refInfo.length == 2) {
				refPanel = (UIRefPane) getEditor().getBillCardPanel()
						.getHeadItem(refInfo[ITEMKEY]).getComponent();
			}

			else if (refInfo.length > 2) {
				isbody = UFBoolean.valueOf(refInfo[BODY]).booleanValue();
				// 表体
				if (isbody) {
					// 未指定页签
					if (refInfo.length == 3) {
						refPanel = (UIRefPane) getEditor().getBillCardPanel()
								.getBodyItem(refInfo[ITEMKEY]).getComponent();
					}
					// 指定页签
					else {
						refPanel = (UIRefPane) getEditor()
								.getBillCardPanel()
								.getBodyItem(refInfo[TABLECODE],
										refInfo[ITEMKEY]).getComponent();
					}

				} else {
					refPanel = (UIRefPane) getEditor().getBillCardPanel()
							.getHeadItem(refInfo[ITEMKEY]).getComponent();
				}

			}
			if (getIsAddWhereSql()) {
				refPanel.getRefModel()
						.addWherePart(" and " + refInfo[WHERESQL]);
			} else {
				refPanel.setWhereString(refInfo[WHERESQL]);
			}

		}

	}

	public void setRefInfo(List<String> refInfo) {
		this.refInfo = refInfo;
	}

	public List<String> getRefInfo() {
		return refInfo;
	}

	public boolean getIsAddWhereSql() {
		return isAddWhereSql;
	}

	public void setIsAddWhereSql(boolean isAddWhereSql) {
		this.isAddWhereSql = isAddWhereSql;
	}

}
