package nc.ui.mmgp.uif2.ref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.model.MaterialMultiVersionRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.bd.material.IMaterialEnumConst;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 物料参照过滤
 * 
 */
public class MMFilterMaterialRefUtils {
	/**
	 * 设备
	 */
	private UFBoolean bAsset;

	/**
	 * 价格折扣
	 */
	private UFBoolean bDiscount;

	/**
	 * 批次管理
	 */
	private UFBoolean wholemanaflag = UFBoolean.FALSE;

	public UFBoolean getWholemanaflag() {
		return this.wholemanaflag;
	}

	public void setWholemanaflag(UFBoolean wholemanaflag) {
		this.wholemanaflag = wholemanaflag;
	}

	/**
	 * 制造件或委外件
	 */
	protected UFBoolean isMaOrOut = UFBoolean.FALSE;

	public UFBoolean getIsMaOrOut() {
		return this.isMaOrOut;
	}

	public void setIsMaOrOut(UFBoolean isMaOrOut) {
		this.isMaOrOut = isMaOrOut;
	}

	/**
	 * 费用类
	 */
	private UFBoolean bFee;

	/**
	 * 删除，参照不到
	 */
	private UFBoolean isMarterialDelete;

	/**
	 * 是否封存
	 */
	private UFBoolean isSealedShow;

	/**
	 * 按计价方式过滤物料
	 */
	private Integer[] marterialJjfs;

	/**
	 * 参照Pane
	 */
	private UIRefPane pane;

	/**
	 * 组织
	 */
	private String pk_org;

	/**
	 * 是否显示封存
	 */
	private UFBoolean setpartsflag;

	/**
	 * 是否制造件
	 */
	protected UFBoolean isMarterialMark;

	private final SqlBuilder sql = new SqlBuilder();

	/**
	 * 构造函数
	 * 
	 * @param panel
	 *            参照Pane
	 */
	public MMFilterMaterialRefUtils(UIRefPane panel) {
		this.pane = panel;
	}

	/**
	 * 方法功能描述：获得资产类物料
	 * 
	 * @param b
	 *            <p>
	 * @author wuweiping
	 * @time 2010-5-7 下午01:59:18
	 */
	public void fetchByAssetMaterial() {
		String sqlwhere = " materialmgt= 2";
		this.filterRef(sqlwhere);
	}

	/**
	 * 根据标识过滤
	 */
	public void filtByOtherFlag() {
		SqlBuilder where = new SqlBuilder();
		// 费用
		if (this.bFee != null) {
			where.append(" fee", this.bFee.toString());
		}

		// 折扣
		if (this.bDiscount != null) {
			if (where.toString().length() > 0) {
				where.append(" and ");
			}
			where.append(" discountflag", this.bDiscount.toString());
		}

		// 批次管理
		// if (this.getWholemanaflag() != null) {
		// if (where.toString().length() > 0) {
		// where.append(" and ");
		// }
		//
		// // where.append(" pk_material in ( select pk_material from ");
		// // where.append(" bd_materialstock k  ");
		// // where.append(" where ");
		// // where.append(" k.wholemanaflag ",
		// this.getWholemanaflag().toString());
		// // where.append(" and k.pk_org ", this.pk_org);
		// // where.append(")");
		//
		// where.append(" wholemanaflag ", this.getWholemanaflag().toString());
		// }

		// 物料管理模式(周转材料: 1, 设备: 2)
		if (this.bAsset != null) {
			if (where.toString().length() > 0) {
				where.append(" and ");
			}
			where.append(" materialmgt", 2);
		}

		if (this.isSealedShow != null && this.isSealedShow.booleanValue()) {
			if (where.toString().length() > 0) {
				where.append(" or ");
			}
			where.append(" materialstate", 1);
		}

		if (this.setpartsflag != null) {
			if (where.toString().length() > 0) {
				where.append(" and ");
			}
			where.append("setpartsflag", this.setpartsflag.toString());
		}
		// 删除参照不到
		if (this.isMarterialDelete != null
				&& this.isMarterialDelete.booleanValue()) {
			if (where.toString().length() > 0) {
				where.append(" and ");
			}
			where.append(" dr", 0);
		}
		// 是制造件
		if (this.isMarterialMark != null && this.isMarterialMark.booleanValue()
				&& this.pk_org != null) {

			where.append(" pk_material in ( select pk_material from ");
			where.append(" bd_materialstock k  ");
			where.append(" where ");
			where.append(" k.martype ", IMaterialEnumConst.MATERTYPE_MR);
			where.append(" and k.pk_org ", this.pk_org);
			where.append(")");

		}

		// 是委外件
		if (this.getIsMaOrOut() != null && this.getIsMaOrOut().booleanValue()
				&& this.pk_org != null) {
			if (this.isMarterialMark != null
					&& this.isMarterialMark.booleanValue()) {
				where.append(" or ");
			}
			where.append(" pk_material in ( select pk_material from ");
			where.append(" bd_materialstock k  ");
			where.append(" where ");
			where.append(" k.martype ", IMaterialEnumConst.MATERTYPE_OT);
			where.append(" and k.pk_org ", this.pk_org);
			where.append(")");

		}

		if (this.marterialJjfs != null && this.pk_org != null) {
			where.append(" pk_material in ( select pk_material from ");
			where.append(" bd_materialcost c,bd_materialcostmod m ");
			where.append(" where c.pk_materialcost = m.pk_materialcost ");
			where.append(" and m.costmode ", this.marterialJjfs);
			where.append(" and c.pk_org ", this.pk_org);
			where.append(")");
		}

		if (StringUtils.isNotEmpty(where.toString())) {
			this.filterRef(where.toString());
		}
	}

	/**
	 * 方法功能描述：按照封存过滤物料 <b>参数说明</b>
	 * 
	 * @param b
	 *            是否封存
	 *            <p>
	 * @author
	 * @time 2010-3-16 下午01:59:18
	 */
	public void filterIsSealedShow(UFBoolean b) {
		String sqlwhere = "";
		if (!b.booleanValue()) {
			sqlwhere = " enablestate = 2 ";
		}
		this.filterRef(sqlwhere);
	}

	/**
	 * 根据集团过滤
	 * 
	 * @param pk_group
	 *            集团主键
	 */
	public void filterItemRefByGroup(String pk_group) {
		if (this.pane == null || this.pane.getRefModel() == null) {
			return;
		}
		this.pane.getRefModel().setPk_group(pk_group);
	}

	/**
	 * 根据组织过滤
	 * 
	 * @param pPk_org
	 *            组织主键
	 */
	public void filterItemRefByOrg(String pPk_org) {
		if (this.pane == null || this.pane.getRefModel() == null) {
			return;
		}
		this.pane.getRefModel().setPk_org(pPk_org);
	}

	/**
	 * 方法功能描述：成套件过滤物料信息
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param b
	 *            是否成套件
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-5-12 下午02:41:16
	 */
	public void filterItemRefBySetpartsflag(UFBoolean b) {

		String sqlwhere = " setpartsflag= '" + b.toString() + "' ";
		this.filterRef(sqlwhere);
	}

	/**
	 * 方法功能描述：依据成本域，计价方式过滤相应的物料分类
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pk_costregion
	 *            <p>
	 *            成本域主键
	 * @param pk_costmode
	 *            <p>
	 *            计价方式
	 * @author wangjianf
	 * @time 2010-3-1 上午10:27:35
	 */
	public void filterMaterialClassRefByPks(String pk_costregion, int costmode) {
		SqlBuilder sql1 = new SqlBuilder();
		sql1.append("pk_marbasclass in (");
		sql1.append("select pk_marbasclass from bd_material ");
		sql1.append("where pk_material in ( select c.pk_material from ");
		sql1.append("bd_materialcost c,bd_materialcostmod m ");
		sql1.append(" where c.pk_materialcost = m.pk_materialcost ");
		sql1.append(" and c.pk_org ", pk_costregion);
		sql1.append("and ");
		sql1.append("m.costmode ", costmode);
		sql1.append("))");

		this.filterRef(sql1.toString());
	}

	/**
	 * 方法功能描述：依据成本域，计价方式过滤相应的物料版本
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pk_costregion
	 *            <p>
	 *            成本域主键
	 * @param pk_costmode
	 *            <p>
	 *            计价方式
	 * @author wangjianf
	 * @time 2010-3-1 上午10:27:35
	 */
	public void filterMaterialRefByPks(String pk_costregion, int costmode) {
		SqlBuilder sql1 = new SqlBuilder();
		sql1.append("pk_material in ( select m.pk_material from ");
		sql1.append("bd_materialcost c,bd_materialcostmod m ");
		sql1.append(" where c.pk_materialcost = m.pk_materialcost ");
		sql1.append(" and c.pk_org ", pk_costregion);
		sql1.append("and ");
		sql1.append("m.costmode ", costmode);
		sql1.append(")");

		this.filterRef(sql1.toString());
	}

	/**
	 * 方法功能描述：根据采购结算成本要素过滤物料。
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pk_finaceorg
	 *            采购结算成本
	 *            <p>
	 * @since 6.0
	 * @author zhyhang
	 * @time 2010-6-14 下午10:38:07
	 */
	public void filterRefByCostfactor(String pk_finaceorg) {
		SqlBuilder sb = new SqlBuilder();
		sb.append("pk_source in ");
		sb.append(" (select pk_srcmaterial from po_costfactor,po_costfactor_b");
		sb.append(" where po_costfactor.pk_costfactor=po_costfactor_b.pk_costfactor");
		sb.append(" and po_costfactor.dr=0 and po_costfactor_b.dr=0");
		sb.append(" and bentercost='Y'");
		sb.append(" and ");
		sb.append("pk_org", pk_finaceorg);
		sb.append(")");
		this.filterRef(sb.toString());
	}

	/**
	 * 方法功能描述：
	 * <p>
	 * 根据物料的生产信息成本对象属性过滤 <b>参数说明</b>
	 * 
	 * @param sfcbdx
	 *            成本对象属性
	 * @since 6.0
	 * @author 于晓龙
	 * @time 2011-01-20 下午03:25:15
	 */
	public void filterRefByCostObj(UFBoolean sfcbdx) {
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in (select pk_material from bd_materialprod where ");
		if (sfcbdx.booleanValue()) {
			sqlwhere.append("sfcbdx", sfcbdx);
		} else {
			sqlwhere.append("sfcbdx <> 'Y'");
		}
		sqlwhere.append(")");
		this.filterRef(sqlwhere.toString());
	}

	/**
	 * 方法功能描述：依据成本域过滤相应的物料版本
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pk_costregion
	 *            <p>
	 *            成本域主键
	 * @author 皮之兵
	 * @time 2010-3-1 上午10:27:35
	 */
	public void filterRefByCostRegion(String pk_costregion) {
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in (select pk_material from bd_materialcost where ");
		sqlwhere.append("pk_org", pk_costregion);
		sqlwhere.append(")");

		this.filterRef(sqlwhere.toString());
	}

	/**
	 * 按折扣过滤存货
	 * 
	 * @param b
	 *            true:为折扣类存货 false:非折扣类存货
	 */
	public void filterRefByDiscountflag(UFBoolean b) {
		String sqlwhere = " discountflag= '" + b.toString() + "' ";
		this.filterRef(sqlwhere);
	}

	/**
	 * 方法功能描述：按费用类存货进行过滤（对应5系列的劳务类存货） <b>参数说明</b>
	 * 
	 * @param b
	 *            是否费用类存货
	 *            <p>
	 * @author liyu1
	 * @time 2010-3-16 下午01:59:18
	 */
	public void filterRefByFeeflag(UFBoolean b) {
		String sqlwhere = " fee= '" + b.toString() + "' ";
		this.filterRef(sqlwhere);
	}

	/**
	 * 方法功能描述：根据费用或者折扣过滤物料
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pBFee
	 *            是否费用
	 * @param pBDiscount
	 *            是否折扣
	 *            <p>
	 * @since 6.0
	 * @author tianft
	 * @time 2010-3-17 上午09:46:29
	 */
	public void filterRefByFeeOrDiscount(UFBoolean pBFee, UFBoolean pBDiscount) {
		if (pBFee == null && pBDiscount == null) {
			this.filterRef("");
			return;
		}
		StringBuffer sqlwhere = new StringBuffer();
		sqlwhere.append("(");
		// 费用
		if (pBFee != null) {
			sqlwhere.append(" fee= '").append(pBFee.toString()).append("' ");
		}
		// 折扣
		if (pBDiscount != null) {
			if (!"(".equals(sqlwhere.toString())) {
				sqlwhere.append(" or ");
			}
			sqlwhere.append(" discountflag= '").append(pBDiscount.toString())
					.append("' ");
		}

		if ("(".equals(sqlwhere.toString())) {
			return;
		}

		sqlwhere.append(")");
		this.filterRef(sqlwhere.toString());

	}

	/**
	 * 方法功能描述：根据费用,折扣,虚项过滤物料
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pBFee
	 *            是否费用
	 * @param pBDiscount
	 *            是否折扣
	 * @param 是否虚项
	 *            <p>
	 * @since 6.0
	 * @author tianft
	 * @time 2010-3-17 上午09:46:29
	 */
	public void filterRefByFeeOrDiscount(UFBoolean pBFee, UFBoolean pBDiscount,
			UFBoolean isvirtual) {
		if (pBFee == null && pBDiscount == null) {
			this.filterRef("");
			return;
		}
		StringBuffer sqlwhere = new StringBuffer();
		sqlwhere.append("(");
		// 费用
		if (pBFee != null) {
			sqlwhere.append(" isnull(fee,'N') = '").append(pBFee.toString())
					.append("' ");
		}
		// 折扣
		if (pBDiscount != null) {
			if (!"(".equals(sqlwhere.toString())) {
				sqlwhere.append(" and ");
			}
			sqlwhere.append(" isnull(discountflag,'N') = '")
					.append(pBDiscount.toString()).append("' ");
		}
		if ("(".equals(sqlwhere.toString())) {
			return;
		}

		sqlwhere.append(")");

		if (isvirtual != null) {
			sqlwhere.append(" and  ");
			sqlwhere.append(" pk_material in ( select pk_material from ");
			sqlwhere.append(" bd_materialplan k  ");
			sqlwhere.append(" where ");
			sqlwhere.append(" isnull(k.isvirtual,'N') = '");

			sqlwhere.append(isvirtual);
			sqlwhere.append("' and k.pk_org = '");
			sqlwhere.append(this.pk_org);
			sqlwhere.append("')");
		}
		this.filterRef(sqlwhere.toString());

	}

	/**
	 * 方法功能描述：根据物料基本分类过滤物料
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pk_marBasClass
	 *            物料基本分类主键
	 *            <p>
	 * @since 6.0
	 * @author wanghuid
	 * @time 2010-6-24 下午02:10:40
	 */
	public void filterRefByMarBasClass(String pk_marBasClass) {
		SqlBuilder sb = new SqlBuilder();
		if (StringUtils.isBlank(pk_marBasClass)) {
			this.filterRef("");
		} else {
			sb.append("pk_marbasclass", pk_marBasClass);
			this.filterRef(sb.toString());
		}
	}

	/**
	 * 方法功能描述：
	 * <p>
	 * 根据物料类型过滤 <b>参数说明</b>
	 * <p>
	 * 
	 * @param 物料类型
	 * @since 6.0
	 * @author chennn
	 * @time 2010-10-12 下午04:57:16
	 */
	public void filterRefByMartype(String marType) {
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in  ");
		sqlwhere.append(" ( select pk_material from bd_materialstock where ");
		sqlwhere.append(" martype ", marType);
		sqlwhere.append(" ) ");
		this.filterRef(sqlwhere.toString());
	}

	/**
	 * 方法功能描述：按照物料OID过滤
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param oid
	 *            物料OID
	 *            <p>
	 * @since 6.0
	 * @author liuzy
	 * @time 2010-4-1 下午04:50:14
	 */
	public void filterRefByMaterialOID(String oid) {
		StringBuffer sqlWhere = new StringBuffer();
		sqlWhere.append(" pk_source = '");
		sqlWhere.append(oid);
		sqlWhere.append("'");
	}

	/**
	 * 方法功能描述：按照物料OID过滤。
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param oids
	 *            物料OID数组 这里不考虑临时表，因此个数不要太多
	 *            <p>
	 * @since 6.0
	 * @author zhyhang
	 * @time 2010-6-14 下午09:50:33
	 */
	public void filterRefByOIDNotIn(String[] oids) {
		if (ArrayUtils.isEmpty(oids)) {
			return;
		}
		SqlBuilder sb = new SqlBuilder();
		sb.appendNot("pk_source", oids);
		this.filterRef(sb.toString());
	}

	/**
	 * 根据物料的OID过滤物料参照
	 * 
	 * @param cmaterialoids
	 *            物料OID数组
	 */
	public void filterRefByOIDs(String[] cmaterialoids) {
		if (ArrayUtils.isEmpty(cmaterialoids)) {
			return;
		}
		SqlBuilder sb = new SqlBuilder();
		sb.append("pk_source", cmaterialoids);
		this.filterRef(sb.toString());
	}

	/**
	 * 根据库存组织页签的计划方法过滤物料
	 * 
	 * @param planMethod
	 *            计划方法 IMaterialEnumConst.PLANPROP_ZD 或者
	 *            IMaterialEnumConst.PLANPROP_DX
	 */
	public void filterRefByPlanMethod(String planMethod) {
		SqlBuilder sqlwhere = new SqlBuilder();

		sqlwhere.append(" pk_material in (select pk_material from bd_materialplan ");
		sqlwhere.append(" where planprop ='" + planMethod + "') ");

		this.filterRef(sqlwhere.toString());
	}

	/**
	 * 根据计划页签的计划方法和组织过滤物料
	 * 
	 * @param planMethod
	 *            计划方法主键
	 * @param pk_org
	 *            组织主键
	 */
	public void filterRefByPlanMethod(String planMethod, String pk_org1) {
		SqlBuilder sqlwhere = new SqlBuilder();

		sqlwhere.append(" pk_material in (select pk_material from bd_materialplan ");
		sqlwhere.append(" where planprop ='" + planMethod + "' and pk_org='"
				+ pk_org1 + "') ");

		this.filterRef(sqlwhere.toString());
	}

	/**
	 * 提供给关键料驱动
	 * 
	 * @param pk_org1
	 */
	public void filterRefByKmdMethod(String pk_org1) {
		SqlBuilder sqlwhere = new SqlBuilder();

		sqlwhere.append(" pk_material in ( select pk_material from ");
		sqlwhere.append(" bd_materialprod t where t.primaryflag='Y'");
		if (!StringUtil.isEmptyWithTrim(pk_org1)) {
			sqlwhere.append(" and t.pk_org='" + pk_org1 + "'");
		}
		sqlwhere.append(")");

		this.filterRef(sqlwhere.toString());
	}

	/**
	 * 方法功能描述：
	 * <p>
	 * 根据物料的生产信息的所属工厂过滤 <b>参数说明</b>
	 * 
	 * @param pPk_org
	 *            组织主键
	 *            <p>
	 * @since 6.0
	 * @author chennn
	 * @time 2010-10-12 下午04:02:15
	 */
	public void filterRefByProduceOrg(String pPk_org) {
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in (select pk_material from bd_materialprod where ");
		sqlwhere.append("pk_org", pPk_org);
		sqlwhere.append(")");
		this.filterRef(sqlwhere.toString());

	}

	/**
	 * 方法功能描述：
	 * <p>
	 * 根据物料的生产信息生产模式过滤 <b>参数说明</b>
	 * 
	 * @param pro_mode
	 *            生产信息生产模式
	 *            <p>
	 * @since 6.3
	 * @author lijjl
	 * @time 2012-11-22 下午04:02:15
	 */
	public void filterRefByProduceMode(String pro_mode) {
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in (select pk_material from bd_materialprod where ");
		sqlwhere.append("prodmode", pro_mode);
		sqlwhere.append(")");
		this.filterRef(sqlwhere.toString());

	}

	/**
	 * 根据生产信息生产模式和组织过滤
	 * 
	 * @param pro_mode
	 *            生产模式 :1流程，2离散
	 * @param pk_org
	 *            组织
	 */
	public void filterRefByProduceMode2(String pro_mode, String pk_org) {
		this.filterItemRefByOrg(pk_org);
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in (select pk_material from bd_materialprod mprod where ");
		sqlwhere.append("mprod.prodmode", pro_mode);
		sqlwhere.append(" and ");
		sqlwhere.append(" mprod.pk_org ", pk_org);
		sqlwhere.append(")");
		this.filterRef(sqlwhere.toString());
	}

	/**
	 * 根据生产信息生产模式（流程）和组织过滤
	 * 
	 * @param pk_org
	 *            组织
	 */
	public void filterRefByProdMode(String pk_org) {
		this.filterRefByProduceMode2("1",pk_org);
	}

	/**
	 * 根据生产模式和组织过滤（批量）
	 * 
	 * @param pro_mode
	 *            生产模式数组
	 * @param pk_org
	 *            组织
	 */
	public void filterRefByProduceMode3(String[] pro_mode, String pk_org) {
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in (select pk_material from bd_materialprod mprod where ");
		sqlwhere.append("mprod.prodmode", pro_mode);
		sqlwhere.append(" and ");
		sqlwhere.append(" mprod.pk_org ", pk_org);
		sqlwhere.append(")");
		this.filterRef(sqlwhere.toString());
	}

	public UFBoolean getIsSealedShow() {
		return this.isSealedShow;
	}

	public void setbAsset(UFBoolean bAsset) {
		this.bAsset = bAsset;
	}

	public void setbDiscount(UFBoolean bDiscount) {
		this.bDiscount = bDiscount;
	}

	public void setbFee(UFBoolean bFee) {
		this.bFee = bFee;
	}

	public void setIsMarterialDelete(UFBoolean isMarterialDelete) {
		this.isMarterialDelete = isMarterialDelete;
	}

	/**
	 * 方法功能描述：依据仓库过滤相应的物料
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pk_storc
	 *            仓库主键
	 *            <p>
	 * @author liyu1
	 * @time 2010-1-22 下午04:27:35
	 */
	public void filterRefByStorc(String pk_storc) {
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in (select pk_material from bd_materialwarh where ");
		sqlwhere.append("pk_stordoc", pk_storc);
		sqlwhere.append(")");
		this.filterRef(sqlwhere.toString());
	}

	public void setIsSealedShow(UFBoolean isSealedShow) {
		this.isSealedShow = isSealedShow;
	}

	public void setMarterialJjfs(Integer[] marterialJjfs) {
		this.marterialJjfs = marterialJjfs;
	}

	public UFBoolean getIsMarterialMark() {
		return this.isMarterialMark;
	}

	public void setIsMarterialMark(UFBoolean isMarterialMark) {
		this.isMarterialMark = isMarterialMark;
	}

	/**
	 * 方法功能描述：设置物料参照是否允许多选物料
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param multiSelectedEnabled
	 *            true，允许多选；false，不允许多选
	 *            <p>
	 * @since 6.0
	 * @author duy
	 * @time 2010-3-30 下午08:11:44
	 */
	public void setMultiSelectedEnabled(boolean multiSelectedEnabled) {
		if (this.pane == null || this.pane.getRefModel() == null) {
			return;
		}
		AbstractRefModel model = this.pane.getRefModel();
		if (model instanceof MaterialMultiVersionRefModel) {
			this.pane.setMultiSelectedEnabled(multiSelectedEnabled);
		}
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public void setSetpartsflag(UFBoolean setpartsflag) {
		this.setpartsflag = setpartsflag;
	}

	/**
	 * 参照过滤
	 * 
	 * @param sqlwhere
	 *            过滤SQL
	 */
	private void filterRef(String sqlwhere) {
		if (this.pane == null || this.pane.getRefModel() == null) {
			return;
		}
		if (this.sql != null && this.sql.toString().length() > 0) {
			this.sql.append(" and ");

		}
		if (this.sql != null) {
			this.sql.append(sqlwhere);
			this.pane.setWhereString(this.sql.toString());
		}
	}

}
