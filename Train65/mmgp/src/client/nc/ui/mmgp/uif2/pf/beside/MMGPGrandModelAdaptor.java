package nc.ui.mmgp.uif2.pf.beside;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

import nc.ui.pubapp.uif2app.components.grand.model.MainGrandModel;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.ui.uif2.model.ModelDataDescriptor;
import nc.vo.uif2.LoginContext;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Sep 12, 2013
 * @author wangweir
 */
public class MMGPGrandModelAdaptor extends AbstractAppModel {

    private MainGrandModel mainGrandModel;

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractAppModel#add(java.lang.Object)
     */
    @Override
    public Object add(Object object) throws Exception {
        return this.getMainGrandModel().getMainModel().add(object);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractAppModel#update(java.lang.Object)
     */
    @Override
    public Object update(Object object) throws Exception {
        return this.getMainGrandModel().getMainModel().update(object);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractAppModel#delete()
     */
    @Override
    public void delete() throws Exception {
        this.getMainGrandModel().getMainModel().delete();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#getSelectedData()
     */
    @Override
    public Object getSelectedData() {
        return this.getMainGrandModel().getSelectedData();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#initModel(java.lang.Object)
     */
    @Override
    public void initModel(Object data) {
        this.getMainGrandModel().getMainModel().initModel(data);
    }

    /**
     * @return the mainGrandModel
     */
    public MainGrandModel getMainGrandModel() {
        return mainGrandModel;
    }

    /**
     * @param mainGrandModel
     *        the mainGrandModel to set
     */
    public void setMainGrandModel(MainGrandModel mainGrandModel) {
        this.mainGrandModel = mainGrandModel;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractAppModel#directlyUpdate(java.lang.Object)
     */
    @Override
    public void directlyUpdate(Object object) {
        this.getMainGrandModel().getMainModel().directlyUpdate(object);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractAppModel#directlyAdd(java.lang.Object)
     */
    @Override
    public void directlyAdd(Object object) {
        this.getMainGrandModel().getMainModel().directlyAdd(object);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#addAppEventListener(nc.ui.uif2.AppEventListener)
     */
    @Override
    public void addAppEventListener(AppEventListener l) {
        this.getMainGrandModel().getMainModel().addAppEventListener(l);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#removeAppEventListener(nc.ui.uif2.AppEventListener)
     */
    @Override
    public void removeAppEventListener(AppEventListener l) {
        this.getMainGrandModel().getMainModel().removeAppEventListener(l);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#fireEvent(nc.ui.uif2.AppEvent)
     */
    @Override
    public void fireEvent(AppEvent event) {
        this.getMainGrandModel().getMainModel().fireEvent(event);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#getAppEventListener(java.lang.Class)
     */
    @Override
    public <T extends EventListener> T[] getAppEventListener(Class<T> listenerClazz) {
        return this.getMainGrandModel().getMainModel().getAppEventListener(listenerClazz);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#getUiState()
     */
    @Override
    public UIState getUiState() {
        return this.getMainGrandModel().getMainModel().getUiState();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#setUiState(nc.ui.uif2.UIState)
     */
    @Override
    public void setUiState(UIState uiState) {
        this.getMainGrandModel().getMainModel().setUiState(uiState);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#getListeners()
     */
    @Override
    protected EventListenerList getListeners() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#getContext()
     */
    @Override
    public LoginContext getContext() {
        return this.getMainGrandModel().getMainModel().getContext();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#setContext(nc.vo.uif2.LoginContext)
     */
    @Override
    public void setContext(LoginContext context) {
        this.getMainGrandModel().getMainModel().setContext(context);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#startBusy()
     */
    @Override
    public void startBusy() {
        this.getMainGrandModel().getMainModel().startBusy();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#stopBusy()
     */
    @Override
    public void stopBusy() {
        this.getMainGrandModel().getMainModel().stopBusy();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#initModel(java.lang.Object, nc.ui.uif2.model.ModelDataDescriptor)
     */
    @Override
    public void initModel(Object data,
                          ModelDataDescriptor descriptor) {
        this.getMainGrandModel().getMainModel().initModel(data, descriptor);
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#getCurrentDataDescriptor()
     */
    @Override
    public ModelDataDescriptor getCurrentDataDescriptor() {
        return this.getMainGrandModel().getMainModel().getCurrentDataDescriptor();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.uif2.model.AbstractUIAppModel#setDataCount(java.lang.Object)
     */
    @Override
    protected void setDataCount(Object data) {
        throw new UnsupportedOperationException(getClass().getName());
    }

}
