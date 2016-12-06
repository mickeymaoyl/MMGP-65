package nc.ui.mmgp.uif2.mediator.num;

import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 主辅计量表体物料参照多选后回调接口。用于处理物料参照多行后处理逻辑
 * </p>
 * 
 * @since 创建日期 Jun 21, 2013
 * @author wangweir
 */
public interface IMaterialMultSelectProcess {

    /**
     * 表体物料参照多选后处理类
     * 
     * @param e
     *        CardBodyAfterEditEvent
     * @param changedRows
     *        参照多行处理的行号
     */
    void afterMaterialMultSelected(CardBodyAfterEditEvent e,
                                   int[] changedRows);
}
