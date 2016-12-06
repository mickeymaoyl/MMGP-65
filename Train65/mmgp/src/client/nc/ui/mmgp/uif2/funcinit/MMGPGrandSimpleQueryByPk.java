package nc.ui.mmgp.uif2.funcinit;

import nc.bs.framework.common.NCLocator;
import nc.itf.mmgp.IMMGPGrandQueryBillService;
import nc.itf.pubapp.pub.smart.IQueryBillService;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener;
import nc.ui.pubapp.uif2app.model.SimpleQueryByPk;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.uif2.LoginContext;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Jun 27, 2013
 * @author wangweir
 */
public class MMGPGrandSimpleQueryByPk extends SimpleQueryByPk {

    private LoginContext context;

    private Class< ? > voClass;

    private IQueryBillService queryBillService;

    /**
     * 
     */
    public MMGPGrandSimpleQueryByPk() {
        super(null);
    }

    /**
     * @param plistener
     */
    public MMGPGrandSimpleQueryByPk(DefaultFuncNodeInitDataListener plistener) {
        super(plistener);
    }

    @Override
    public Object queryByPk(String pk) {
        try {
            Class< ? > voClass = getVoClass();
            return getQueryBillService().queryBill(voClass, pk);
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }

    @Override
    public Object queryByPkWithFunCode(String pk,
                                       String nodeCode) {
        try {
            Class< ? > voClass = getVoClass();
            return getQueryBillService().queryBill(voClass, pk, nodeCode);
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }

    protected IQueryBillService getQueryBillService() {
        if (this.queryBillService == null) {
            queryBillService = NCLocator.getInstance().lookup(IMMGPGrandQueryBillService.class);
        }
        return queryBillService;
    }

    protected String getClazzName() {
        return MMGPMetaUtils.getClassFullName(getContext());
    }

    @Override
    public Object[] queryByPk(String[] pk) {
        try {
            Class< ? > voClass = getVoClass();
            return getQueryBillService().queryBills(voClass, pk);
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }

    protected Class< ? > getVoClass() throws ClassNotFoundException {
        if (this.voClass == null) {
            voClass = Class.forName(getClazzName());
        }
        return voClass;
    }

    @Override
    public Object[] queryByPksWithFunCode(String[] pks,
                                          String nodeCode) {
        try {
            Class< ? > voClass = getVoClass();
            return getQueryBillService().queryBills(voClass, pks, nodeCode);
        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }

    /**
     * @return the context
     */
    public LoginContext getContext() {
        return context;
    }

    /**
     * @param context
     *        the context to set
     */
    public void setContext(LoginContext context) {
        this.context = context;
    }
}
