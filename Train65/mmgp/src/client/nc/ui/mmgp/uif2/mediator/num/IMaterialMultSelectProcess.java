package nc.ui.mmgp.uif2.mediator.num;

import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ���������������ϲ��ն�ѡ��ص��ӿڡ����ڴ������ϲ��ն��к����߼�
 * </p>
 * 
 * @since �������� Jun 21, 2013
 * @author wangweir
 */
public interface IMaterialMultSelectProcess {

    /**
     * �������ϲ��ն�ѡ������
     * 
     * @param e
     *        CardBodyAfterEditEvent
     * @param changedRows
     *        ���ն��д�����к�
     */
    void afterMaterialMultSelected(CardBodyAfterEditEvent e,
                                   int[] changedRows);
}
