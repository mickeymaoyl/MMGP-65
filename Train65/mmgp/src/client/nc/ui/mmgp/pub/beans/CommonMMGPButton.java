package nc.ui.mmgp.pub.beans;

import nc.ui.mmgp.base.IUIState;

/**
 * <b> 共有按钮创建 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-2-22
 * 
 * @author wangweiu
 * @deprecated by wangweiu :全部使用NCAction进行按钮显示
 * @see nc.ui.uif2.NCAction
 */
public class CommonMMGPButton {

    public static MMGPButtonObject createEditButton() {
        MMGPButtonObject bo = new MMGPButtonObject("修改", "修改", "修改"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_INIT, IUIState.STATE_CARD_BROWSE });
        return bo;
    }

    public static MMGPButtonObject createFileButton() {
        MMGPButtonObject bo = new MMGPButtonObject("文件", "文件", "文件"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_INIT, IUIState.STATE_CARD_BROWSE });
        return bo;
    }

    public static MMGPButtonObject createOpenButton() {
        MMGPButtonObject bo = new MMGPButtonObject("打开", "打开", "打开"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_INIT, IUIState.STATE_CARD_BROWSE });
        return bo;
    }

    public static MMGPButtonObject createDelButton() {

        MMGPButtonObject bo = new MMGPButtonObject("删除", "删除", "删除"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_INIT, IUIState.STATE_CARD_BROWSE, IUIState.STATE_MODIFY });
        return bo;
    }
    
    public static MMGPButtonObject createSaveButton() {
    	
    	MMGPButtonObject bo = new MMGPButtonObject("保存", "保存", "保存"); /*-=notranslate=-*/
    	bo.setOprateStatus(new int[]{IUIState.STATE_INIT, IUIState.STATE_CARD_BROWSE, IUIState.STATE_MODIFY });
    	return bo;
    }
    
    public static MMGPButtonObject createCircleButton() {
    	
    	MMGPButtonObject bo = new MMGPButtonObject("回路检查", "回路检查", "回路检查"); /*-=notranslate=-*/
    	bo.setOprateStatus(new int[]{IUIState.STATE_INIT, IUIState.STATE_CARD_BROWSE, IUIState.STATE_MODIFY });
    	return bo;
    }

    public static MMGPButtonObject createCancelButton() {
        MMGPButtonObject bo = new MMGPButtonObject("取消", "取消", "取消"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_MODIFY, IUIState.STATE_ADD });
        return bo;
    }

    public static MMGPButtonObject createCommitButton() {
        MMGPButtonObject bo = new MMGPButtonObject("提交", "提交", "提交"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_CARD_BROWSE });
        return bo;
    }

    public static MMGPButtonObject createUnCommitButton() {
        MMGPButtonObject bo = new MMGPButtonObject("取消提交", "取消提交", "取消提交"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_CARD_BROWSE });
        return bo;
    }

    public static MMGPButtonObject createExportButton() {
        MMGPButtonObject bo = new MMGPButtonObject("导出", "导出", "导出"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_CARD_BROWSE });
        return bo;
    }

    public static MMGPButtonObject createRefreshButton() {
        MMGPButtonObject bo = new MMGPButtonObject("刷新", "刷新", "刷新"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_CARD_BROWSE, IUIState.STATE_LIST });
        return bo;
    }

    public static MMGPButtonObject createQueryButton() {
        MMGPButtonObject bo = new MMGPButtonObject("查询", "查询", "查询"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_INIT, IUIState.STATE_CARD_BROWSE });
        return bo;
    }

    public static MMGPButtonObject createZoomOutButton() {
        MMGPButtonObject bo = new MMGPButtonObject("放大", "放大", "放大"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_CARD_BROWSE, IUIState.STATE_LIST });
        return bo;
    }

    public static MMGPButtonObject createZoomInButton() {
        MMGPButtonObject bo = new MMGPButtonObject("缩小", "缩小", "缩小"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_CARD_BROWSE, IUIState.STATE_LIST });
        return bo;
    }

    public static MMGPButtonObject createZoomResetButton() {
        MMGPButtonObject bo = new MMGPButtonObject("还原", "还原", "还原"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_CARD_BROWSE, IUIState.STATE_LIST });
        return bo;

    }

    public static MMGPButtonObject createViewButton() {
        MMGPButtonObject bo = new MMGPButtonObject("视图", "视图", "视图"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_MODIFY, IUIState.STATE_CARD_BROWSE, IUIState.STATE_LIST });
        return bo;
    }

    public static MMGPButtonObject createNewButton() {
        MMGPButtonObject bo = new MMGPButtonObject("新建", "新建", "新建"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_INIT, IUIState.STATE_CARD_BROWSE });
        return bo;
    }

    public static MMGPButtonObject createSealButton() {
        MMGPButtonObject bo = new MMGPButtonObject("封存", "封存", "封存"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_MODIFY, IUIState.STATE_CARD_BROWSE });
        return bo;
    }

    public static MMGPButtonObject createCancelSealButton() {
        MMGPButtonObject bo = new MMGPButtonObject("解封", "解封", "解封"); /*-=notranslate=-*/
        bo.setOprateStatus(new int[]{IUIState.STATE_MODIFY, IUIState.STATE_CARD_BROWSE });
        return bo;
    }

}
