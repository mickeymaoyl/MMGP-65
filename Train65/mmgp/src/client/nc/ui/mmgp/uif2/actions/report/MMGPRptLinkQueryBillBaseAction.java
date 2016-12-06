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
 * <b> 报表联查单据基本类 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 2014-1-13
 * @author dongchx
 */
public abstract class MMGPRptLinkQueryBillBaseAction implements ITraceDataOperator, TraceDataInterface {

    /**
     * 按钮编码
     */
    private String actionCode;

    /**
     * 按钮名称
     */
    private String actionName;

    /**
     * 节点编码,要联查的节点
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
     * 初始化操作
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
     * 动作前处理
     * 
     * @param container
     * @param param
     */
    protected void doActionBefore(final Container container,
                                  final TraceDataParam param) {

    }

    /**
     * 打开单据节点
     * 
     * @param data
     *        打开节点所需要的数据，如主键pk等
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
     * 是空数据
     * 
     * @param param
     * @return
     */
    private boolean isNullData(TraceDataParam param) {
        return param == null || param.getRowData() == null;
    }

    /**
     * 获得联查操作所需要的数据
     * 
     * @param param
     * @return
     */
    protected Object getInitData(TraceDataParam param) {
        // 取得选中行数据集
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
     * 按钮编码
     */
    public String getActionCode() {
        return actionCode;
    }

    /**
     * 按钮编码
     */
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    /**
     * 按钮名称
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * 按钮名称
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * 节点编码
     */
    public String getNodeCode() {
        return nodeCode;
    }

    /**
     * 节点编码
     */
    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

}
