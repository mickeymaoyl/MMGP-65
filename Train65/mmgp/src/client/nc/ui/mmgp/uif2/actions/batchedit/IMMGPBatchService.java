package nc.ui.mmgp.uif2.actions.batchedit;

import java.util.Map;

import nc.vo.pub.BusinessException;

public interface IMMGPBatchService {

    public Object[] batchUpdateData(Map<String, Object> attr_valueMap,
                                    MMGPBatchUpdateContext updateContext) throws BusinessException;

    public void afterUIRefresh(Map<String, Object> attr_valueMap,
                               MMGPBatchUpdateContext updateContext);
}
