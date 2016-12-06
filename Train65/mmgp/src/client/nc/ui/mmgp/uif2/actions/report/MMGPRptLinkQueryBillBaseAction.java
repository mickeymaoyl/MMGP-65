package nc.ui.mmgp.uif2.actions.report;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.uap.bbd.func.IFuncRegisterQueryService;
import nc.pub.smart.data.IRowData;
import nc.pub.smart.tracedata.ITraceDataOperator;
import nc.pub.smart.tracedata.TraceDataInterface;
import nc.pub.smart.tracedata.TraceDataParam;
import nc.ui.pub.linkoperate.ILinkType;
import nc.vo.pub.BusinessException;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.sm.funcreg.FuncRegisterVO;

import com.ufida.report.free.userdef.DefaultMenu;
import com.ufida.report.free.userdef.IMenuActionInfo;

/**
 * <b> �������鵥�ݻ����� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� 2014-1-13
 * @author dongchx
 */
public abstract class MMGPRptLinkQueryBillBaseAction implements ITraceDataOperator, TraceDataInterface {

    /**
     * ��ť����
     */
    private String actionCode;

    /**
     * ��ť����
     */
    private String actionName;

    /**
     * �ڵ����,Ҫ����Ľڵ�
     */
    private String nodeCode;

    public MMGPRptLinkQueryBillBaseAction() {
        this.init();
    }

    public MMGPRptLinkQueryBillBaseAction(String actionCode,
                                          String actionName,
                                          String nodeCode) {
        this.init();
        this.actionCode = actionCode;
        this.actionName = actionName;
        this.nodeCode = nodeCode;
    }

    /**
     * <p>
     * ��ʼ������
     * </p>
     */
    protected void init() {

    }

    @Override
    public void traceData(Container container,
                          TraceDataParam param) {
        if (this.isNullData(param)) {
            return;
        }
        this.doActionBefore(container, param);
        Object data = this.getInitData(param);
        if (data == null) {
            return;
        }
        this.openNodeDialog(data);
    }

    /**
     * ����ǰ����
     * 
     * @param container
     * @param param
     */
    protected void doActionBefore(final Container container,
                                  final TraceDataParam param) {

    }

    /**
     * �򿪵��ݽڵ�
     * 
     * @param data
     *        �򿪽ڵ�����Ҫ�����ݣ�������pk��
     */
    protected void openNodeDialog(Object data) {
        FuncletInitData initData = new FuncletInitData();
        initData.setInitType(ILinkType.LINK_TYPE_QUERY);
        initData.setInitData(data);
        FuncRegisterVO funRegVo = null;
        try {
            IFuncRegisterQueryService register =
                    (IFuncRegisterQueryService) NCLocator.getInstance().lookup(IFuncRegisterQueryService.class);
            funRegVo = register.queryFunctionByCode(this.getNodeCode());
        } catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        Dimension size = new Dimension();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        size.setSize(screenSize.getWidth() * 0.9, screenSize.getHeight() * 0.9);
        FuncletWindowLauncher.openFuncNodeDialog(null, funRegVo, initData, null, false, false, size);
    }

    /**
     * �ǿ�����
     * 
     * @param param
     * @return
     */
    private boolean isNullData(TraceDataParam param) {
        return param == null || param.getRowData() == null;
    }

    /**
     * ��������������Ҫ������
     * 
     * @param param
     * @return
     */
    protected Object getInitData(TraceDataParam param) {
        // ȡ��ѡ�������ݼ�
        IRowData rowData = param.getRowData();
        return this.dealLinkQuery(rowData);
    }

    /**
     * @param rowData
     * @return
     */
    protected Object dealLinkQuery(IRowData rowData) {
        String[] pks = this.getLinkBillPks(rowData);
        if (pks == null || pks.length == 0) {
            return null;
        }
        List<DefaultLinkData> linkDatas = new ArrayList<DefaultLinkData>();
        for (String pk : pks) {
            DefaultLinkData linkData = new DefaultLinkData();
            final String[] billIDs = {pk };
            linkData.setBillIDs(billIDs);
            linkDatas.add(linkData);
        }
        return linkDatas.toArray(new DefaultLinkData[0]);
    }

    /**
     * @param rowData
     * @return
     */
    protected abstract String[] getLinkBillPks(IRowData rowData);

    @Override
    public IMenuActionInfo getMenuItemInfo() {
        DefaultMenu action = new DefaultMenu(this.getActionCode(), this.getActionName());
        action.setToolTipText(this.getActionName());
        return action;
    }

    @Override
    public ITraceDataOperator[] provideTraceDataOperator() {
        return new ITraceDataOperator[]{this };
    }

    @Override
    public Action[] ctreateExtensionActions() {
        return null;
    }

    /**
     * ��ť����
     */
    public String getActionCode() {
        return actionCode;
    }

    /**
     * ��ť����
     */
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    /**
     * ��ť����
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * ��ť����
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * �ڵ����
     */
    public String getNodeCode() {
        return nodeCode;
    }

    /**
     * �ڵ����
     */
    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

}
