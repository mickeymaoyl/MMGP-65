package nc.vo.train.saleoutdemo;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加累的描述信息
 * </p>
 *  创建日期:2016-12-5
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class SaleOutBody extends SuperVO {
	
/**
*子表主键
*/
public java.lang.String billbid;
/**
*行号
*/
public java.lang.String rowno;
/**
*物料
*/
public java.lang.String pk_material;
/**
*物料多版本
*/
public java.lang.String pk_material_v;
/**
*数量
*/
public nc.vo.pub.lang.UFDouble nnum;
/**
*单价
*/
public nc.vo.pub.lang.UFDouble nprice;
/**
*金额
*/
public nc.vo.pub.lang.UFDouble nmoney;
/**
*来源单据类型
*/
public java.lang.String vsrctype;
/**
*来源单据ID
*/
public java.lang.String vsrcid;
/**
*来源单据号
*/
public java.lang.String vsrccode;
/**
*来源单据表体ID
*/
public java.lang.String vsrcrowid;
/**
*自定义项1
*/
public java.lang.String vdef1;
/**
*自定义项2
*/
public java.lang.String vdef2;
/**
*自定义项3
*/
public java.lang.String vdef3;
/**
*自定义项4
*/
public java.lang.String vdef4;
/**
*自定义项5
*/
public java.lang.String vdef5;
/**
*自定义项6
*/
public java.lang.String vdef6;
/**
*自定义项7
*/
public java.lang.String vdef7;
/**
*自定义项8
*/
public java.lang.String vdef8;
/**
*自定义项9
*/
public java.lang.String vdef9;
/**
*自定义项10
*/
public java.lang.String vdef10;
/**
*自定义项11
*/
public java.lang.String vdef11;
/**
*自定义项12
*/
public java.lang.String vdef12;
/**
*自定义项13
*/
public java.lang.String vdef13;
/**
*自定义项14
*/
public java.lang.String vdef14;
/**
*自定义项15
*/
public java.lang.String vdef15;
/**
*自定义项16
*/
public java.lang.String vdef16;
/**
*自定义项17
*/
public java.lang.String vdef17;
/**
*自定义项18
*/
public java.lang.String vdef18;
/**
*自定义项19
*/
public java.lang.String vdef19;
/**
*自定义项20
*/
public java.lang.String vdef20;
/**
*上层单据主键
*/
public String billid;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 billbid的Getter方法.属性名：子表主键
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getBillbid() {
return this.billbid;
} 

/**
* 属性billbid的Setter方法.属性名：子表主键
* 创建日期:2016-12-5
* @param newBillbid java.lang.String
*/
public void setBillbid ( java.lang.String billbid) {
this.billbid=billbid;
} 
 
/**
* 属性 rowno的Getter方法.属性名：行号
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getRowno() {
return this.rowno;
} 

/**
* 属性rowno的Setter方法.属性名：行号
* 创建日期:2016-12-5
* @param newRowno java.lang.String
*/
public void setRowno ( java.lang.String rowno) {
this.rowno=rowno;
} 
 
/**
* 属性 pk_material的Getter方法.属性名：物料
*  创建日期:2016-12-5
* @return nc.vo.bd.material.MaterialVersionVO
*/
public java.lang.String getPk_material() {
return this.pk_material;
} 

/**
* 属性pk_material的Setter方法.属性名：物料
* 创建日期:2016-12-5
* @param newPk_material nc.vo.bd.material.MaterialVersionVO
*/
public void setPk_material ( java.lang.String pk_material) {
this.pk_material=pk_material;
} 
 
/**
* 属性 pk_material_v的Getter方法.属性名：物料多版本
*  创建日期:2016-12-5
* @return nc.vo.bd.material.MaterialVO
*/
public java.lang.String getPk_material_v() {
return this.pk_material_v;
} 

/**
* 属性pk_material_v的Setter方法.属性名：物料多版本
* 创建日期:2016-12-5
* @param newPk_material_v nc.vo.bd.material.MaterialVO
*/
public void setPk_material_v ( java.lang.String pk_material_v) {
this.pk_material_v=pk_material_v;
} 
 
/**
* 属性 nnum的Getter方法.属性名：数量
*  创建日期:2016-12-5
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNnum() {
return this.nnum;
} 

/**
* 属性nnum的Setter方法.属性名：数量
* 创建日期:2016-12-5
* @param newNnum nc.vo.pub.lang.UFDouble
*/
public void setNnum ( nc.vo.pub.lang.UFDouble nnum) {
this.nnum=nnum;
} 
 
/**
* 属性 nprice的Getter方法.属性名：单价
*  创建日期:2016-12-5
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNprice() {
return this.nprice;
} 

/**
* 属性nprice的Setter方法.属性名：单价
* 创建日期:2016-12-5
* @param newNprice nc.vo.pub.lang.UFDouble
*/
public void setNprice ( nc.vo.pub.lang.UFDouble nprice) {
this.nprice=nprice;
} 
 
/**
* 属性 nmoney的Getter方法.属性名：金额
*  创建日期:2016-12-5
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNmoney() {
return this.nmoney;
} 

/**
* 属性nmoney的Setter方法.属性名：金额
* 创建日期:2016-12-5
* @param newNmoney nc.vo.pub.lang.UFDouble
*/
public void setNmoney ( nc.vo.pub.lang.UFDouble nmoney) {
this.nmoney=nmoney;
} 
 
/**
* 属性 vsrctype的Getter方法.属性名：来源单据类型
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVsrctype() {
return this.vsrctype;
} 

/**
* 属性vsrctype的Setter方法.属性名：来源单据类型
* 创建日期:2016-12-5
* @param newVsrctype java.lang.String
*/
public void setVsrctype ( java.lang.String vsrctype) {
this.vsrctype=vsrctype;
} 
 
/**
* 属性 vsrcid的Getter方法.属性名：来源单据ID
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVsrcid() {
return this.vsrcid;
} 

/**
* 属性vsrcid的Setter方法.属性名：来源单据ID
* 创建日期:2016-12-5
* @param newVsrcid java.lang.String
*/
public void setVsrcid ( java.lang.String vsrcid) {
this.vsrcid=vsrcid;
} 
 
/**
* 属性 vsrccode的Getter方法.属性名：来源单据号
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVsrccode() {
return this.vsrccode;
} 

/**
* 属性vsrccode的Setter方法.属性名：来源单据号
* 创建日期:2016-12-5
* @param newVsrccode java.lang.String
*/
public void setVsrccode ( java.lang.String vsrccode) {
this.vsrccode=vsrccode;
} 
 
/**
* 属性 vsrcrowid的Getter方法.属性名：来源单据表体ID
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVsrcrowid() {
return this.vsrcrowid;
} 

/**
* 属性vsrcrowid的Setter方法.属性名：来源单据表体ID
* 创建日期:2016-12-5
* @param newVsrcrowid java.lang.String
*/
public void setVsrcrowid ( java.lang.String vsrcrowid) {
this.vsrcrowid=vsrcrowid;
} 
 
/**
* 属性 vdef1的Getter方法.属性名：自定义项1
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef1() {
return this.vdef1;
} 

/**
* 属性vdef1的Setter方法.属性名：自定义项1
* 创建日期:2016-12-5
* @param newVdef1 java.lang.String
*/
public void setVdef1 ( java.lang.String vdef1) {
this.vdef1=vdef1;
} 
 
/**
* 属性 vdef2的Getter方法.属性名：自定义项2
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef2() {
return this.vdef2;
} 

/**
* 属性vdef2的Setter方法.属性名：自定义项2
* 创建日期:2016-12-5
* @param newVdef2 java.lang.String
*/
public void setVdef2 ( java.lang.String vdef2) {
this.vdef2=vdef2;
} 
 
/**
* 属性 vdef3的Getter方法.属性名：自定义项3
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef3() {
return this.vdef3;
} 

/**
* 属性vdef3的Setter方法.属性名：自定义项3
* 创建日期:2016-12-5
* @param newVdef3 java.lang.String
*/
public void setVdef3 ( java.lang.String vdef3) {
this.vdef3=vdef3;
} 
 
/**
* 属性 vdef4的Getter方法.属性名：自定义项4
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef4() {
return this.vdef4;
} 

/**
* 属性vdef4的Setter方法.属性名：自定义项4
* 创建日期:2016-12-5
* @param newVdef4 java.lang.String
*/
public void setVdef4 ( java.lang.String vdef4) {
this.vdef4=vdef4;
} 
 
/**
* 属性 vdef5的Getter方法.属性名：自定义项5
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef5() {
return this.vdef5;
} 

/**
* 属性vdef5的Setter方法.属性名：自定义项5
* 创建日期:2016-12-5
* @param newVdef5 java.lang.String
*/
public void setVdef5 ( java.lang.String vdef5) {
this.vdef5=vdef5;
} 
 
/**
* 属性 vdef6的Getter方法.属性名：自定义项6
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef6() {
return this.vdef6;
} 

/**
* 属性vdef6的Setter方法.属性名：自定义项6
* 创建日期:2016-12-5
* @param newVdef6 java.lang.String
*/
public void setVdef6 ( java.lang.String vdef6) {
this.vdef6=vdef6;
} 
 
/**
* 属性 vdef7的Getter方法.属性名：自定义项7
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef7() {
return this.vdef7;
} 

/**
* 属性vdef7的Setter方法.属性名：自定义项7
* 创建日期:2016-12-5
* @param newVdef7 java.lang.String
*/
public void setVdef7 ( java.lang.String vdef7) {
this.vdef7=vdef7;
} 
 
/**
* 属性 vdef8的Getter方法.属性名：自定义项8
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef8() {
return this.vdef8;
} 

/**
* 属性vdef8的Setter方法.属性名：自定义项8
* 创建日期:2016-12-5
* @param newVdef8 java.lang.String
*/
public void setVdef8 ( java.lang.String vdef8) {
this.vdef8=vdef8;
} 
 
/**
* 属性 vdef9的Getter方法.属性名：自定义项9
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef9() {
return this.vdef9;
} 

/**
* 属性vdef9的Setter方法.属性名：自定义项9
* 创建日期:2016-12-5
* @param newVdef9 java.lang.String
*/
public void setVdef9 ( java.lang.String vdef9) {
this.vdef9=vdef9;
} 
 
/**
* 属性 vdef10的Getter方法.属性名：自定义项10
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef10() {
return this.vdef10;
} 

/**
* 属性vdef10的Setter方法.属性名：自定义项10
* 创建日期:2016-12-5
* @param newVdef10 java.lang.String
*/
public void setVdef10 ( java.lang.String vdef10) {
this.vdef10=vdef10;
} 
 
/**
* 属性 vdef11的Getter方法.属性名：自定义项11
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef11() {
return this.vdef11;
} 

/**
* 属性vdef11的Setter方法.属性名：自定义项11
* 创建日期:2016-12-5
* @param newVdef11 java.lang.String
*/
public void setVdef11 ( java.lang.String vdef11) {
this.vdef11=vdef11;
} 
 
/**
* 属性 vdef12的Getter方法.属性名：自定义项12
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef12() {
return this.vdef12;
} 

/**
* 属性vdef12的Setter方法.属性名：自定义项12
* 创建日期:2016-12-5
* @param newVdef12 java.lang.String
*/
public void setVdef12 ( java.lang.String vdef12) {
this.vdef12=vdef12;
} 
 
/**
* 属性 vdef13的Getter方法.属性名：自定义项13
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef13() {
return this.vdef13;
} 

/**
* 属性vdef13的Setter方法.属性名：自定义项13
* 创建日期:2016-12-5
* @param newVdef13 java.lang.String
*/
public void setVdef13 ( java.lang.String vdef13) {
this.vdef13=vdef13;
} 
 
/**
* 属性 vdef14的Getter方法.属性名：自定义项14
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef14() {
return this.vdef14;
} 

/**
* 属性vdef14的Setter方法.属性名：自定义项14
* 创建日期:2016-12-5
* @param newVdef14 java.lang.String
*/
public void setVdef14 ( java.lang.String vdef14) {
this.vdef14=vdef14;
} 
 
/**
* 属性 vdef15的Getter方法.属性名：自定义项15
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef15() {
return this.vdef15;
} 

/**
* 属性vdef15的Setter方法.属性名：自定义项15
* 创建日期:2016-12-5
* @param newVdef15 java.lang.String
*/
public void setVdef15 ( java.lang.String vdef15) {
this.vdef15=vdef15;
} 
 
/**
* 属性 vdef16的Getter方法.属性名：自定义项16
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef16() {
return this.vdef16;
} 

/**
* 属性vdef16的Setter方法.属性名：自定义项16
* 创建日期:2016-12-5
* @param newVdef16 java.lang.String
*/
public void setVdef16 ( java.lang.String vdef16) {
this.vdef16=vdef16;
} 
 
/**
* 属性 vdef17的Getter方法.属性名：自定义项17
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef17() {
return this.vdef17;
} 

/**
* 属性vdef17的Setter方法.属性名：自定义项17
* 创建日期:2016-12-5
* @param newVdef17 java.lang.String
*/
public void setVdef17 ( java.lang.String vdef17) {
this.vdef17=vdef17;
} 
 
/**
* 属性 vdef18的Getter方法.属性名：自定义项18
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef18() {
return this.vdef18;
} 

/**
* 属性vdef18的Setter方法.属性名：自定义项18
* 创建日期:2016-12-5
* @param newVdef18 java.lang.String
*/
public void setVdef18 ( java.lang.String vdef18) {
this.vdef18=vdef18;
} 
 
/**
* 属性 vdef19的Getter方法.属性名：自定义项19
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef19() {
return this.vdef19;
} 

/**
* 属性vdef19的Setter方法.属性名：自定义项19
* 创建日期:2016-12-5
* @param newVdef19 java.lang.String
*/
public void setVdef19 ( java.lang.String vdef19) {
this.vdef19=vdef19;
} 
 
/**
* 属性 vdef20的Getter方法.属性名：自定义项20
*  创建日期:2016-12-5
* @return java.lang.String
*/
public java.lang.String getVdef20() {
return this.vdef20;
} 

/**
* 属性vdef20的Setter方法.属性名：自定义项20
* 创建日期:2016-12-5
* @param newVdef20 java.lang.String
*/
public void setVdef20 ( java.lang.String vdef20) {
this.vdef20=vdef20;
} 
 
/**
* 属性 生成上层主键的Getter方法.属性名：上层主键
*  创建日期:2016-12-5
* @return String
*/
public String getBillid(){
return this.billid;
}
/**
* 属性生成上层主键的Setter方法.属性名：上层主键
* 创建日期:2016-12-5
* @param newBillid String
*/
public void setBillid(String billid){
this.billid=billid;
} 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2016-12-5
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2016-12-5
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
    