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
 * ���ϲ��չ���
 * 
 */
public class MMFilterMaterialRefUtils {
	/**
	 * �豸
	 */
	private UFBoolean bAsset;

	/**
	 * �۸��ۿ�
	 */
	private UFBoolean bDiscount;

	/**
	 * ���ι���
	 */
	private UFBoolean wholemanaflag = UFBoolean.FALSE;

	public UFBoolean getWholemanaflag() {
		return this.wholemanaflag;
	}

	public void setWholemanaflag(UFBoolean wholemanaflag) {
		this.wholemanaflag = wholemanaflag;
	}

	/**
	 * �������ί���
	 */
	protected UFBoolean isMaOrOut = UFBoolean.FALSE;

	public UFBoolean getIsMaOrOut() {
		return this.isMaOrOut;
	}

	public void setIsMaOrOut(UFBoolean isMaOrOut) {
		this.isMaOrOut = isMaOrOut;
	}

	/**
	 * ������
	 */
	private UFBoolean bFee;

	/**
	 * ɾ�������ղ���
	 */
	private UFBoolean isMarterialDelete;

	/**
	 * �Ƿ���
	 */
	private UFBoolean isSealedShow;

	/**
	 * ���Ƽ۷�ʽ��������
	 */
	private Integer[] marterialJjfs;

	/**
	 * ����Pane
	 */
	private UIRefPane pane;

	/**
	 * ��֯
	 */
	private String pk_org;

	/**
	 * �Ƿ���ʾ���
	 */
	private UFBoolean setpartsflag;

	/**
	 * �Ƿ������
	 */
	protected UFBoolean isMarterialMark;

	private final SqlBuilder sql = new SqlBuilder();

	/**
	 * ���캯��
	 * 
	 * @param panel
	 *            ����Pane
	 */
	public MMFilterMaterialRefUtils(UIRefPane panel) {
		this.pane = panel;
	}

	/**
	 * ������������������ʲ�������
	 * 
	 * @param b
	 *            <p>
	 * @author wuweiping
	 * @time 2010-5-7 ����01:59:18
	 */
	public void fetchByAssetMaterial() {
		String sqlwhere = " materialmgt= 2";
		this.filterRef(sqlwhere);
	}

	/**
	 * ���ݱ�ʶ����
	 */
	public void filtByOtherFlag() {
		SqlBuilder where = new SqlBuilder();
		// ����
		if (this.bFee != null) {
			where.append(" fee", this.bFee.toString());
		}

		// �ۿ�
		if (this.bDiscount != null) {
			if (where.toString().length() > 0) {
				where.append(" and ");
			}
			where.append(" discountflag", this.bDiscount.toString());
		}

		// ���ι���
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

		// ���Ϲ���ģʽ(��ת����: 1, �豸: 2)
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
		// ɾ�����ղ���
		if (this.isMarterialDelete != null
				&& this.isMarterialDelete.booleanValue()) {
			if (where.toString().length() > 0) {
				where.append(" and ");
			}
			where.append(" dr", 0);
		}
		// �������
		if (this.isMarterialMark != null && this.isMarterialMark.booleanValue()
				&& this.pk_org != null) {

			where.append(" pk_material in ( select pk_material from ");
			where.append(" bd_materialstock k  ");
			where.append(" where ");
			where.append(" k.martype ", IMaterialEnumConst.MATERTYPE_MR);
			where.append(" and k.pk_org ", this.pk_org);
			where.append(")");

		}

		// ��ί���
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
	 * �����������������շ��������� <b>����˵��</b>
	 * 
	 * @param b
	 *            �Ƿ���
	 *            <p>
	 * @author
	 * @time 2010-3-16 ����01:59:18
	 */
	public void filterIsSealedShow(UFBoolean b) {
		String sqlwhere = "";
		if (!b.booleanValue()) {
			sqlwhere = " enablestate = 2 ";
		}
		this.filterRef(sqlwhere);
	}

	/**
	 * ���ݼ��Ź���
	 * 
	 * @param pk_group
	 *            ��������
	 */
	public void filterItemRefByGroup(String pk_group) {
		if (this.pane == null || this.pane.getRefModel() == null) {
			return;
		}
		this.pane.getRefModel().setPk_group(pk_group);
	}

	/**
	 * ������֯����
	 * 
	 * @param pPk_org
	 *            ��֯����
	 */
	public void filterItemRefByOrg(String pPk_org) {
		if (this.pane == null || this.pane.getRefModel() == null) {
			return;
		}
		this.pane.getRefModel().setPk_org(pPk_org);
	}

	/**
	 * �����������������׼�����������Ϣ
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param b
	 *            �Ƿ���׼�
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-5-12 ����02:41:16
	 */
	public void filterItemRefBySetpartsflag(UFBoolean b) {

		String sqlwhere = " setpartsflag= '" + b.toString() + "' ";
		this.filterRef(sqlwhere);
	}

	/**
	 * �����������������ݳɱ��򣬼Ƽ۷�ʽ������Ӧ�����Ϸ���
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pk_costregion
	 *            <p>
	 *            �ɱ�������
	 * @param pk_costmode
	 *            <p>
	 *            �Ƽ۷�ʽ
	 * @author wangjianf
	 * @time 2010-3-1 ����10:27:35
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
	 * �����������������ݳɱ��򣬼Ƽ۷�ʽ������Ӧ�����ϰ汾
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pk_costregion
	 *            <p>
	 *            �ɱ�������
	 * @param pk_costmode
	 *            <p>
	 *            �Ƽ۷�ʽ
	 * @author wangjianf
	 * @time 2010-3-1 ����10:27:35
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
	 * �����������������ݲɹ�����ɱ�Ҫ�ع������ϡ�
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pk_finaceorg
	 *            �ɹ�����ɱ�
	 *            <p>
	 * @since 6.0
	 * @author zhyhang
	 * @time 2010-6-14 ����10:38:07
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
	 * ��������������
	 * <p>
	 * �������ϵ�������Ϣ�ɱ��������Թ��� <b>����˵��</b>
	 * 
	 * @param sfcbdx
	 *            �ɱ���������
	 * @since 6.0
	 * @author ������
	 * @time 2011-01-20 ����03:25:15
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
	 * �����������������ݳɱ��������Ӧ�����ϰ汾
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pk_costregion
	 *            <p>
	 *            �ɱ�������
	 * @author Ƥ֮��
	 * @time 2010-3-1 ����10:27:35
	 */
	public void filterRefByCostRegion(String pk_costregion) {
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in (select pk_material from bd_materialcost where ");
		sqlwhere.append("pk_org", pk_costregion);
		sqlwhere.append(")");

		this.filterRef(sqlwhere.toString());
	}

	/**
	 * ���ۿ۹��˴��
	 * 
	 * @param b
	 *            true:Ϊ�ۿ����� false:���ۿ�����
	 */
	public void filterRefByDiscountflag(UFBoolean b) {
		String sqlwhere = " discountflag= '" + b.toString() + "' ";
		this.filterRef(sqlwhere);
	}

	/**
	 * ���������������������������й��ˣ���Ӧ5ϵ�е����������� <b>����˵��</b>
	 * 
	 * @param b
	 *            �Ƿ��������
	 *            <p>
	 * @author liyu1
	 * @time 2010-3-16 ����01:59:18
	 */
	public void filterRefByFeeflag(UFBoolean b) {
		String sqlwhere = " fee= '" + b.toString() + "' ";
		this.filterRef(sqlwhere);
	}

	/**
	 * �����������������ݷ��û����ۿ۹�������
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pBFee
	 *            �Ƿ����
	 * @param pBDiscount
	 *            �Ƿ��ۿ�
	 *            <p>
	 * @since 6.0
	 * @author tianft
	 * @time 2010-3-17 ����09:46:29
	 */
	public void filterRefByFeeOrDiscount(UFBoolean pBFee, UFBoolean pBDiscount) {
		if (pBFee == null && pBDiscount == null) {
			this.filterRef("");
			return;
		}
		StringBuffer sqlwhere = new StringBuffer();
		sqlwhere.append("(");
		// ����
		if (pBFee != null) {
			sqlwhere.append(" fee= '").append(pBFee.toString()).append("' ");
		}
		// �ۿ�
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
	 * �����������������ݷ���,�ۿ�,�����������
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pBFee
	 *            �Ƿ����
	 * @param pBDiscount
	 *            �Ƿ��ۿ�
	 * @param �Ƿ�����
	 *            <p>
	 * @since 6.0
	 * @author tianft
	 * @time 2010-3-17 ����09:46:29
	 */
	public void filterRefByFeeOrDiscount(UFBoolean pBFee, UFBoolean pBDiscount,
			UFBoolean isvirtual) {
		if (pBFee == null && pBDiscount == null) {
			this.filterRef("");
			return;
		}
		StringBuffer sqlwhere = new StringBuffer();
		sqlwhere.append("(");
		// ����
		if (pBFee != null) {
			sqlwhere.append(" isnull(fee,'N') = '").append(pBFee.toString())
					.append("' ");
		}
		// �ۿ�
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
	 * ���������������������ϻ��������������
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pk_marBasClass
	 *            ���ϻ�����������
	 *            <p>
	 * @since 6.0
	 * @author wanghuid
	 * @time 2010-6-24 ����02:10:40
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
	 * ��������������
	 * <p>
	 * �����������͹��� <b>����˵��</b>
	 * <p>
	 * 
	 * @param ��������
	 * @since 6.0
	 * @author chennn
	 * @time 2010-10-12 ����04:57:16
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
	 * ����������������������OID����
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param oid
	 *            ����OID
	 *            <p>
	 * @since 6.0
	 * @author liuzy
	 * @time 2010-4-1 ����04:50:14
	 */
	public void filterRefByMaterialOID(String oid) {
		StringBuffer sqlWhere = new StringBuffer();
		sqlWhere.append(" pk_source = '");
		sqlWhere.append(oid);
		sqlWhere.append("'");
	}

	/**
	 * ����������������������OID���ˡ�
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param oids
	 *            ����OID���� ���ﲻ������ʱ����˸�����Ҫ̫��
	 *            <p>
	 * @since 6.0
	 * @author zhyhang
	 * @time 2010-6-14 ����09:50:33
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
	 * �������ϵ�OID�������ϲ���
	 * 
	 * @param cmaterialoids
	 *            ����OID����
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
	 * ���ݿ����֯ҳǩ�ļƻ�������������
	 * 
	 * @param planMethod
	 *            �ƻ����� IMaterialEnumConst.PLANPROP_ZD ����
	 *            IMaterialEnumConst.PLANPROP_DX
	 */
	public void filterRefByPlanMethod(String planMethod) {
		SqlBuilder sqlwhere = new SqlBuilder();

		sqlwhere.append(" pk_material in (select pk_material from bd_materialplan ");
		sqlwhere.append(" where planprop ='" + planMethod + "') ");

		this.filterRef(sqlwhere.toString());
	}

	/**
	 * ���ݼƻ�ҳǩ�ļƻ���������֯��������
	 * 
	 * @param planMethod
	 *            �ƻ���������
	 * @param pk_org
	 *            ��֯����
	 */
	public void filterRefByPlanMethod(String planMethod, String pk_org1) {
		SqlBuilder sqlwhere = new SqlBuilder();

		sqlwhere.append(" pk_material in (select pk_material from bd_materialplan ");
		sqlwhere.append(" where planprop ='" + planMethod + "' and pk_org='"
				+ pk_org1 + "') ");

		this.filterRef(sqlwhere.toString());
	}

	/**
	 * �ṩ���ؼ�������
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
	 * ��������������
	 * <p>
	 * �������ϵ�������Ϣ�������������� <b>����˵��</b>
	 * 
	 * @param pPk_org
	 *            ��֯����
	 *            <p>
	 * @since 6.0
	 * @author chennn
	 * @time 2010-10-12 ����04:02:15
	 */
	public void filterRefByProduceOrg(String pPk_org) {
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in (select pk_material from bd_materialprod where ");
		sqlwhere.append("pk_org", pPk_org);
		sqlwhere.append(")");
		this.filterRef(sqlwhere.toString());

	}

	/**
	 * ��������������
	 * <p>
	 * �������ϵ�������Ϣ����ģʽ���� <b>����˵��</b>
	 * 
	 * @param pro_mode
	 *            ������Ϣ����ģʽ
	 *            <p>
	 * @since 6.3
	 * @author lijjl
	 * @time 2012-11-22 ����04:02:15
	 */
	public void filterRefByProduceMode(String pro_mode) {
		SqlBuilder sqlwhere = new SqlBuilder();
		sqlwhere.append(" pk_material in (select pk_material from bd_materialprod where ");
		sqlwhere.append("prodmode", pro_mode);
		sqlwhere.append(")");
		this.filterRef(sqlwhere.toString());

	}

	/**
	 * ����������Ϣ����ģʽ����֯����
	 * 
	 * @param pro_mode
	 *            ����ģʽ :1���̣�2��ɢ
	 * @param pk_org
	 *            ��֯
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
	 * ����������Ϣ����ģʽ�����̣�����֯����
	 * 
	 * @param pk_org
	 *            ��֯
	 */
	public void filterRefByProdMode(String pk_org) {
		this.filterRefByProduceMode2("1",pk_org);
	}

	/**
	 * ��������ģʽ����֯���ˣ�������
	 * 
	 * @param pro_mode
	 *            ����ģʽ����
	 * @param pk_org
	 *            ��֯
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
	 * �����������������ݲֿ������Ӧ������
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pk_storc
	 *            �ֿ�����
	 *            <p>
	 * @author liyu1
	 * @time 2010-1-22 ����04:27:35
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
	 * ���������������������ϲ����Ƿ������ѡ����
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param multiSelectedEnabled
	 *            true�������ѡ��false���������ѡ
	 *            <p>
	 * @since 6.0
	 * @author duy
	 * @time 2010-3-30 ����08:11:44
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
	 * ���չ���
	 * 
	 * @param sqlwhere
	 *            ����SQL
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
