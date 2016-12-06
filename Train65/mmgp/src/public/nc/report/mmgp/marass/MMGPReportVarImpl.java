package nc.report.mmgp.marass;

import nc.bs.pubapp.report.function.ReportVarImpl;

import com.ufida.dataset.IContext;

/**
 * �����Զ������
 * 
 * @author yinyxa
 */
public class MMGPReportVarImpl extends ReportVarImpl {

    @Override
    public Object getVarsValues(IContext context,
                                String paraKey) {
        // �������ϸ�������
        if (paraKey.startsWith(MMGPReportVarImpl.REP_VAR_VFREECAPTION)) {
            return new MMGPMarAssistantCaption().processDef(paraKey);
        }
        // �����Զ�����
        if (paraKey.startsWith(MMGPReportVarImpl.REP_VAR_DEFCAPTION)) {
            return new MMGPUserDefCaption().processDef(paraKey);
        }
        return null;
    }

}
