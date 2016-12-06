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
 
public class SaleOut extends SuperVO {
	
/**
*��������
*/
public java.lang.String billid;
/**
*����
*/
public java.lang.String pk_group;
/**
*��֯
*/
public java.lang.String pk_org;
/**
*��֯�汾
*/
public java.lang.String pk_org_v;
/**
*��������
*/
public java.lang.String vbilltypecode;
/**
*ҵ������
*/
public java.lang.String busitype;
/**
*���ݺ�
*/
public java.lang.String vbillcode;
/**
*��������
*/
public UFDate dbilldate;
/**
*�Ƶ���
*/
public java.lang.String vbillmaker;
/**
*�Ƶ�ʱ��
*/
public UFDateTime dmakedate;
/**
*������
*/
public java.lang.String creator;
/**
*����ʱ��
*/
public UFDateTime creationtime;
/**
*�޸���
*/
public java.lang.String modifier;
/**
*�޸�ʱ��
*/
public UFDateTime modifiedtime;
/**
*����޸�ʱ��
*/
public UFDateTime lastmaketime;
/**
*������
*/
public java.lang.String approver;
/**
*����״̬
*/
public java.lang.Integer approvestatus;
/**
*��������
*/
public java.lang.String approvenote;
/**
*����ʱ��
*/
public UFDateTime approvedate;
/**
*��Դ��������
*/
public java.lang.String srcbilltype;
/**
*��Դ����ID
*/
public java.lang.String srcbillid;
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
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� billid��Getter����.����������������
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getBillid() {
return this.billid;
} 

/**
* ����billid��Setter����.����������������
* ��������:2016-12-5
* @param newBillid java.lang.String
*/
public void setBillid ( java.lang.String billid) {
this.billid=billid;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2016-12-5
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2016-12-5
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2016-12-5
* @return nc.vo.org.SalesOrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2016-12-5
* @param newPk_org nc.vo.org.SalesOrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2016-12-5
* @return nc.vo.vorg.SalesOrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2016-12-5
* @param newPk_org_v nc.vo.vorg.SalesOrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� vbilltypecode��Getter����.����������������
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVbilltypecode() {
return this.vbilltypecode;
} 

/**
* ����vbilltypecode��Setter����.����������������
* ��������:2016-12-5
* @param newVbilltypecode java.lang.String
*/
public void setVbilltypecode ( java.lang.String vbilltypecode) {
this.vbilltypecode=vbilltypecode;
} 
 
/**
* ���� busitype��Getter����.��������ҵ������
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* ����busitype��Setter����.��������ҵ������
* ��������:2016-12-5
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* ���� vbillcode��Getter����.�����������ݺ�
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVbillcode() {
return this.vbillcode;
} 

/**
* ����vbillcode��Setter����.�����������ݺ�
* ��������:2016-12-5
* @param newVbillcode java.lang.String
*/
public void setVbillcode ( java.lang.String vbillcode) {
this.vbillcode=vbillcode;
} 
 
/**
* ���� dbilldate��Getter����.����������������
*  ��������:2016-12-5
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getDbilldate() {
return this.dbilldate;
} 

/**
* ����dbilldate��Setter����.����������������
* ��������:2016-12-5
* @param newDbilldate nc.vo.pub.lang.UFDate
*/
public void setDbilldate ( UFDate dbilldate) {
this.dbilldate=dbilldate;
} 
 
/**
* ���� vbillmaker��Getter����.���������Ƶ���
*  ��������:2016-12-5
* @return nc.vo.sm.UserVO
*/
public java.lang.String getVbillmaker() {
return this.vbillmaker;
} 

/**
* ����vbillmaker��Setter����.���������Ƶ���
* ��������:2016-12-5
* @param newVbillmaker nc.vo.sm.UserVO
*/
public void setVbillmaker ( java.lang.String vbillmaker) {
this.vbillmaker=vbillmaker;
} 
 
/**
* ���� dmakedate��Getter����.���������Ƶ�ʱ��
*  ��������:2016-12-5
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getDmakedate() {
return this.dmakedate;
} 

/**
* ����dmakedate��Setter����.���������Ƶ�ʱ��
* ��������:2016-12-5
* @param newDmakedate nc.vo.pub.lang.UFDateTime
*/
public void setDmakedate ( UFDateTime dmakedate) {
this.dmakedate=dmakedate;
} 
 
/**
* ���� creator��Getter����.��������������
*  ��������:2016-12-5
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* ����creator��Setter����.��������������
* ��������:2016-12-5
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* ���� creationtime��Getter����.������������ʱ��
*  ��������:2016-12-5
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* ����creationtime��Setter����.������������ʱ��
* ��������:2016-12-5
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* ���� modifier��Getter����.���������޸���
*  ��������:2016-12-5
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* ����modifier��Setter����.���������޸���
* ��������:2016-12-5
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* ���� modifiedtime��Getter����.���������޸�ʱ��
*  ��������:2016-12-5
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* ����modifiedtime��Setter����.���������޸�ʱ��
* ��������:2016-12-5
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* ���� lastmaketime��Getter����.������������޸�ʱ��
*  ��������:2016-12-5
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getLastmaketime() {
return this.lastmaketime;
} 

/**
* ����lastmaketime��Setter����.������������޸�ʱ��
* ��������:2016-12-5
* @param newLastmaketime nc.vo.pub.lang.UFDateTime
*/
public void setLastmaketime ( UFDateTime lastmaketime) {
this.lastmaketime=lastmaketime;
} 
 
/**
* ���� approver��Getter����.��������������
*  ��������:2016-12-5
* @return nc.vo.sm.UserVO
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* ����approver��Setter����.��������������
* ��������:2016-12-5
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* ���� approvestatus��Getter����.������������״̬
*  ��������:2016-12-5
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* ����approvestatus��Setter����.������������״̬
* ��������:2016-12-5
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* ���� approvenote��Getter����.����������������
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* ����approvenote��Setter����.����������������
* ��������:2016-12-5
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* ���� approvedate��Getter����.������������ʱ��
*  ��������:2016-12-5
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* ����approvedate��Setter����.������������ʱ��
* ��������:2016-12-5
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* ���� srcbilltype��Getter����.����������Դ��������
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* ����srcbilltype��Setter����.����������Դ��������
* ��������:2016-12-5
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* ���� srcbillid��Getter����.����������Դ����ID
*  ��������:2016-12-5
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* ����srcbillid��Setter����.����������Դ����ID
* ��������:2016-12-5
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
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
    return VOMetaFactory.getInstance().getVOMeta("train.SaleOut");
    }
   }
    