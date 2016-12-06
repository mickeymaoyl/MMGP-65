package nc.itf.mmgp.linkquery;

import java.util.Map;

import nc.vo.mmgp.linkquery.LinkQueryBillNode;
import nc.vo.mmgp.linkquery.LinkQueryParamVO;
import nc.vo.mmgp.util.MMGPMapList;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * <b> 单据追溯服务类 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since: 创建日期:2014-10-29
 * @author:liwsh
 */
public interface IMMGPLinkQueryService {

    /**
     * 根据单据类型和单据表体id等，查询对应的来源单据表体（目的单据类型指定哪一个类型表体）<br\>
     * 比如，采购入库单->采购到货单->采购订单->请购单->作业。 我们需要根据采购入库单的表体id，找到这行对应的作业表体信息<br\>
     * 我们就可以传入由采购入库单表体id和采购入库单单据类型构造的paramvos和作业的单据类型作为的destBillType<br\>
     * 如果我们要查对应的采购订单是哪一行，destBillType换成采购订单的单据类型即可。
     * 
     * @param paramvos
     *        单据类型与单据表体id信息
     * @param destBillType
     *        目的单据类型
     * @param vsrcbidkeyMap
     *        来源单据表体key信息，key->单据类型 value->来源单据表体id key。 <br\>
     *        由于每一张单据的表体字段来源单据表体id不都是vsrcbid，而且无法根据元数据确定这个字段的key.<br\>
     *        如果从目前的单据回溯到目的单据中间有的单据表体该字段不是vsrcbid，请通过这个map传入。
     * @return key-> 单据表体id， value->目的单据类型对应的来源单据表体VO
     * @throws BusinessException
     *         业务异常
     */
    public Map<String, SuperVO> querySrcBodyVO(LinkQueryParamVO[] paramvos,
                                               String destBillType,
                                               Map<String, String> vsrcbidkeyMap) throws BusinessException;

    /**
     * 根据单据类型和表体ID查询指定单据类型的下游单据<br/>
     * 目前只有PMRP用到该接口，为了防止构建树错误，只指定了本版本用到的下游单据类型。其余没有用到的单据类型暂时不参与构建。相关类型见BP类
     * 
     * @param paramvos
     *        单据类型与单据表体id信息
     * @param destBillType
     *        目的单据类型
     * @param vsrcbidkeyMap
     *        来源单据表体key信息，key->单据类型 value->来源单据表体id key。 <br\>
     *        由于每一张单据的表体字段来源单据表体id不都是vsrcbid，而且无法根据元数据确定这个字段的key.<br\>
     *        如果从目前的单据回溯到目的单据中间有的单据表体该字段不是vsrcbid，请通过这个map传入。
     * @return key-> 单据表体id,如果表体ID没有则为表头ID， value->目的单据类型对应表体ID，如果没有则为表头ID
     * @throws BusinessException
     */
    public MMGPMapList<String, String> queryForwardBodyVOs(LinkQueryParamVO[] paramvos,
                                                           String destBillType,
                                                           Map<String, String> vsrcbidkeyMap)
            throws BusinessException;

    /**
     * 查询下游所有单据（运算范围内的)
     * 
     * @param paramvos
     *        单据类型与单据表体id信息
     * @param vsrcbidkeyMap
     *        来源单据表体key信息，key->单据类型 value->来源单据表体id key。 <br\>
     *        由于每一张单据的表体字段来源单据表体id不都是vsrcbid，而且无法根据元数据确定这个字段的key.<br\>
     *        如果从目前的单据回溯到目的单据中间有的单据表体该字段不是vsrcbid，请通过这个map传入。
     * @return key-> 单据表体id,如果表体ID没有则为表头ID， value->下游单据属性node节点
     * @throws BusinessException
     */
    public Map<String, LinkQueryBillNode> queryForwardBodyVOs(LinkQueryParamVO[] paramvos,
                                                                      Map<String, String> vsrcbidkeyMap)
            throws BusinessException;

}
