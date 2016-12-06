package nc.vo.train.saleoutdemo;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 *   �˴�����۵�������Ϣ
 * </p>
 *  ��������:2016-12-5
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class SaleOutBody extends SuperVO {
	
/**
*�ӱ�����
*/
public java.lang.String billbid;
/**
*�к�
*/
public java.lang.String rowno;
/**
*����
*/
public java.lang.String pk_material;
/**
*���϶�汾
*/
public java.lang.String pk_material_v;
/**
*����
*/
public nc.vo.pub.lang.UFDouble nnum;
/**
*����
*/
public nc.vo.pub.lang.UFDouble nprice;
/**
*���
*/
public nc.vo.pub.lang.UFDouble nmoney;
/**
*��Դ��������
*/
public java.lang.String vsrctype;
/**
*��Դ����ID
*/
public java.lang.String vsrcid;
/**
*��Դ���ݺ�
*/
public java.lang.String vsrccode;
/**
*��Դ���ݱ���ID
*/
public java.lang.String vsrcrowid;
/**
*�Զ�����1
*/
public java.lang.String vdef1;
/**
*�Զ�����2
*/
public java.lang.String vdef2;
/**
*�Զ�����3
*/
public java.lang.String vdef3;
/**
*�Զ�����4
*/
public java.lang.String vdef4;
/**
*�Զ�����5
*/
public java.lang.String vdef5;
/**
*�Զ�����6
*/
public java.lang.String vdef6;
/**
*�Զ�����7
*/
public java.lang.String vdef7;
/**
*�Զ�����8
*/
public java.lang.String vdef8;
/**
*�Զ�����9
*/
public java.lang.String vdef9;
/**
*�Զ�����10
*/
public java.lang.String vdef10;
/**
*�Զ�����11
*/
public java.lang.String vdef11;
/**
*�Զ�����12
*/
public java.lang.String vdef12;
/**
*�Զ�����13
*/
public java.lang.String vdef13;
/**
*�Զ�����14
*/
public java.lang.String vdef14;
/**
*�Զ�����15
*/
public java.lang.String vdef15;
/**
*�Զ�����16
*/
public java.lang.String vdef16;
/**
*�Զ�����17
*/
public java.lang.String vdef17;
/**
*�Զ�����18
*/
public java.lang.String vdef18;
/**
*�Զ�����19
*/
public java.lang.String vdef19;
/**
*�Զ�����20
*/
public java.lang.String vdef20;
/**
*�ϲ㵥������
*/
public String billid;
/**
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� billbid��Getter����.���������ӱ�����
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getBillbid() {
return this.billbid;
} 

/**
* ����billbid��Setter����.���������ӱ�����
* ��������:2016-12-5
* @param newBillbid java.lang.String
*/
public void setBillbid ( java.lang.String billbid) {
this.billbid=billbid;
} 
 
/**
* ���� rowno��Getter����.���������к�
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getRowno() {
return this.rowno;
} 

/**
* ����rowno��Setter����.���������к�
* ��������:2016-12-5
* @param newRowno java.lang.String
*/
public void setRowno ( java.lang.String rowno) {
this.rowno=rowno;
} 
 
/**
* ���� pk_material��Getter����.������������
*  ��������:2016-12-5
* @return nc.vo.bd.material.MaterialVersionVO
*/
public java.lang.String getPk_material() {
return this.pk_material;
} 

/**
* ����pk_material��Setter����.������������
* ��������:2016-12-5
* @param newPk_material nc.vo.bd.material.MaterialVersionVO
*/
public void setPk_material ( java.lang.String pk_material) {
this.pk_material=pk_material;
} 
 
/**
* ���� pk_material_v��Getter����.�����������϶�汾
*  ��������:2016-12-5
* @return nc.vo.bd.material.MaterialVO
*/
public java.lang.String getPk_material_v() {
return this.pk_material_v;
} 

/**
* ����pk_material_v��Setter����.�����������϶�汾
* ��������:2016-12-5
* @param newPk_material_v nc.vo.bd.material.MaterialVO
*/
public void setPk_material_v ( java.lang.String pk_material_v) {
this.pk_material_v=pk_material_v;
} 
 
/**
* ���� nnum��Getter����.������������
*  ��������:2016-12-5
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNnum() {
return this.nnum;
} 

/**
* ����nnum��Setter����.������������
* ��������:2016-12-5
* @param newNnum nc.vo.pub.lang.UFDouble
*/
public void setNnum ( nc.vo.pub.lang.UFDouble nnum) {
this.nnum=nnum;
} 
 
/**
* ���� nprice��Getter����.������������
*  ��������:2016-12-5
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNprice() {
return this.nprice;
} 

/**
* ����nprice��Setter����.������������
* ��������:2016-12-5
* @param newNprice nc.vo.pub.lang.UFDouble
*/
public void setNprice ( nc.vo.pub.lang.UFDouble nprice) {
this.nprice=nprice;
} 
 
/**
* ���� nmoney��Getter����.�����������
*  ��������:2016-12-5
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNmoney() {
return this.nmoney;
} 

/**
* ����nmoney��Setter����.�����������
* ��������:2016-12-5
* @param newNmoney nc.vo.pub.lang.UFDouble
*/
public void setNmoney ( nc.vo.pub.lang.UFDouble nmoney) {
this.nmoney=nmoney;
} 
 
/**
* ���� vsrctype��Getter����.����������Դ��������
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVsrctype() {
return this.vsrctype;
} 

/**
* ����vsrctype��Setter����.����������Դ��������
* ��������:2016-12-5
* @param newVsrctype java.lang.String
*/
public void setVsrctype ( java.lang.String vsrctype) {
this.vsrctype=vsrctype;
} 
 
/**
* ���� vsrcid��Getter����.����������Դ����ID
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVsrcid() {
return this.vsrcid;
} 

/**
* ����vsrcid��Setter����.����������Դ����ID
* ��������:2016-12-5
* @param newVsrcid java.lang.String
*/
public void setVsrcid ( java.lang.String vsrcid) {
this.vsrcid=vsrcid;
} 
 
/**
* ���� vsrccode��Getter����.����������Դ���ݺ�
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVsrccode() {
return this.vsrccode;
} 

/**
* ����vsrccode��Setter����.����������Դ���ݺ�
* ��������:2016-12-5
* @param newVsrccode java.lang.String
*/
public void setVsrccode ( java.lang.String vsrccode) {
this.vsrccode=vsrccode;
} 
 
/**
* ���� vsrcrowid��Getter����.����������Դ���ݱ���ID
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVsrcrowid() {
return this.vsrcrowid;
} 

/**
* ����vsrcrowid��Setter����.����������Դ���ݱ���ID
* ��������:2016-12-5
* @param newVsrcrowid java.lang.String
*/
public void setVsrcrowid ( java.lang.String vsrcrowid) {
this.vsrcrowid=vsrcrowid;
} 
 
/**
* ���� vdef1��Getter����.���������Զ�����1
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef1() {
return this.vdef1;
} 

/**
* ����vdef1��Setter����.���������Զ�����1
* ��������:2016-12-5
* @param newVdef1 java.lang.String
*/
public void setVdef1 ( java.lang.String vdef1) {
this.vdef1=vdef1;
} 
 
/**
* ���� vdef2��Getter����.���������Զ�����2
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef2() {
return this.vdef2;
} 

/**
* ����vdef2��Setter����.���������Զ�����2
* ��������:2016-12-5
* @param newVdef2 java.lang.String
*/
public void setVdef2 ( java.lang.String vdef2) {
this.vdef2=vdef2;
} 
 
/**
* ���� vdef3��Getter����.���������Զ�����3
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef3() {
return this.vdef3;
} 

/**
* ����vdef3��Setter����.���������Զ�����3
* ��������:2016-12-5
* @param newVdef3 java.lang.String
*/
public void setVdef3 ( java.lang.String vdef3) {
this.vdef3=vdef3;
} 
 
/**
* ���� vdef4��Getter����.���������Զ�����4
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef4() {
return this.vdef4;
} 

/**
* ����vdef4��Setter����.���������Զ�����4
* ��������:2016-12-5
* @param newVdef4 java.lang.String
*/
public void setVdef4 ( java.lang.String vdef4) {
this.vdef4=vdef4;
} 
 
/**
* ���� vdef5��Getter����.���������Զ�����5
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef5() {
return this.vdef5;
} 

/**
* ����vdef5��Setter����.���������Զ�����5
* ��������:2016-12-5
* @param newVdef5 java.lang.String
*/
public void setVdef5 ( java.lang.String vdef5) {
this.vdef5=vdef5;
} 
 
/**
* ���� vdef6��Getter����.���������Զ�����6
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef6() {
return this.vdef6;
} 

/**
* ����vdef6��Setter����.���������Զ�����6
* ��������:2016-12-5
* @param newVdef6 java.lang.String
*/
public void setVdef6 ( java.lang.String vdef6) {
this.vdef6=vdef6;
} 
 
/**
* ���� vdef7��Getter����.���������Զ�����7
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef7() {
return this.vdef7;
} 

/**
* ����vdef7��Setter����.���������Զ�����7
* ��������:2016-12-5
* @param newVdef7 java.lang.String
*/
public void setVdef7 ( java.lang.String vdef7) {
this.vdef7=vdef7;
} 
 
/**
* ���� vdef8��Getter����.���������Զ�����8
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef8() {
return this.vdef8;
} 

/**
* ����vdef8��Setter����.���������Զ�����8
* ��������:2016-12-5
* @param newVdef8 java.lang.String
*/
public void setVdef8 ( java.lang.String vdef8) {
this.vdef8=vdef8;
} 
 
/**
* ���� vdef9��Getter����.���������Զ�����9
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef9() {
return this.vdef9;
} 

/**
* ����vdef9��Setter����.���������Զ�����9
* ��������:2016-12-5
* @param newVdef9 java.lang.String
*/
public void setVdef9 ( java.lang.String vdef9) {
this.vdef9=vdef9;
} 
 
/**
* ���� vdef10��Getter����.���������Զ�����10
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef10() {
return this.vdef10;
} 

/**
* ����vdef10��Setter����.���������Զ�����10
* ��������:2016-12-5
* @param newVdef10 java.lang.String
*/
public void setVdef10 ( java.lang.String vdef10) {
this.vdef10=vdef10;
} 
 
/**
* ���� vdef11��Getter����.���������Զ�����11
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef11() {
return this.vdef11;
} 

/**
* ����vdef11��Setter����.���������Զ�����11
* ��������:2016-12-5
* @param newVdef11 java.lang.String
*/
public void setVdef11 ( java.lang.String vdef11) {
this.vdef11=vdef11;
} 
 
/**
* ���� vdef12��Getter����.���������Զ�����12
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef12() {
return this.vdef12;
} 

/**
* ����vdef12��Setter����.���������Զ�����12
* ��������:2016-12-5
* @param newVdef12 java.lang.String
*/
public void setVdef12 ( java.lang.String vdef12) {
this.vdef12=vdef12;
} 
 
/**
* ���� vdef13��Getter����.���������Զ�����13
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef13() {
return this.vdef13;
} 

/**
* ����vdef13��Setter����.���������Զ�����13
* ��������:2016-12-5
* @param newVdef13 java.lang.String
*/
public void setVdef13 ( java.lang.String vdef13) {
this.vdef13=vdef13;
} 
 
/**
* ���� vdef14��Getter����.���������Զ�����14
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef14() {
return this.vdef14;
} 

/**
* ����vdef14��Setter����.���������Զ�����14
* ��������:2016-12-5
* @param newVdef14 java.lang.String
*/
public void setVdef14 ( java.lang.String vdef14) {
this.vdef14=vdef14;
} 
 
/**
* ���� vdef15��Getter����.���������Զ�����15
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef15() {
return this.vdef15;
} 

/**
* ����vdef15��Setter����.���������Զ�����15
* ��������:2016-12-5
* @param newVdef15 java.lang.String
*/
public void setVdef15 ( java.lang.String vdef15) {
this.vdef15=vdef15;
} 
 
/**
* ���� vdef16��Getter����.���������Զ�����16
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef16() {
return this.vdef16;
} 

/**
* ����vdef16��Setter����.���������Զ�����16
* ��������:2016-12-5
* @param newVdef16 java.lang.String
*/
public void setVdef16 ( java.lang.String vdef16) {
this.vdef16=vdef16;
} 
 
/**
* ���� vdef17��Getter����.���������Զ�����17
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef17() {
return this.vdef17;
} 

/**
* ����vdef17��Setter����.���������Զ�����17
* ��������:2016-12-5
* @param newVdef17 java.lang.String
*/
public void setVdef17 ( java.lang.String vdef17) {
this.vdef17=vdef17;
} 
 
/**
* ���� vdef18��Getter����.���������Զ�����18
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef18() {
return this.vdef18;
} 

/**
* ����vdef18��Setter����.���������Զ�����18
* ��������:2016-12-5
* @param newVdef18 java.lang.String
*/
public void setVdef18 ( java.lang.String vdef18) {
this.vdef18=vdef18;
} 
 
/**
* ���� vdef19��Getter����.���������Զ�����19
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef19() {
return this.vdef19;
} 

/**
* ����vdef19��Setter����.���������Զ�����19
* ��������:2016-12-5
* @param newVdef19 java.lang.String
*/
public void setVdef19 ( java.lang.String vdef19) {
this.vdef19=vdef19;
} 
 
/**
* ���� vdef20��Getter����.���������Զ�����20
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef20() {
return this.vdef20;
} 

/**
* ����vdef20��Setter����.���������Զ�����20
* ��������:2016-12-5
* @param newVdef20 java.lang.String
*/
public void setVdef20 ( java.lang.String vdef20) {
this.vdef20=vdef20;
} 
 
/**
* ���� �����ϲ�������Getter����.���������ϲ�����
*  ��������:2016-12-5
* @return String
*/
public String getBillid(){
return this.billid;
}
/**
* ���������ϲ�������Setter����.���������ϲ�����
* ��������:2016-12-5
* @param newBillid String
*/
public void setBillid(String billid){
this.billid=billid;
} 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2016-12-5
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2016-12-5
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("train.SaleOutBody");
    }
   }
    