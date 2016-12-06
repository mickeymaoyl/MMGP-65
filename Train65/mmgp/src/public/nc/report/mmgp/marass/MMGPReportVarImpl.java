package nc.report.mmgp.marass;

import nc.bs.pubapp.report.function.ReportVarImpl;

import com.ufida.dataset.IContext;

/**
 * 报表自定义项处理
 * 
 * @author yinyxa
 */
public class MMGPReportVarImpl extends ReportVarImpl {

    @Override
    public Object getVarsValues(IContext context,
                                String paraKey) {
        // 处理物料辅助属性
        if (paraKey.startsWith(MMGPReportVarImpl.REP_VAR_VFREECAPTION)) {
            return new MMGPMarAssistantCaption().processDef(paraKey);
        }
        // 处理自定义项
        if (paraKey.startsWith(MMGPReportVarImpl.REP_VAR_DEFCAPTION)) {
            return new MMGPUserDefCaption().processDef(paraKey);
        }
        return null;
    }

}
