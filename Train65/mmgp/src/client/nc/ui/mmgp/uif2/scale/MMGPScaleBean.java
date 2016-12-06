package nc.ui.mmgp.uif2.scale;

import java.util.List;

import nc.vo.pubapp.scale.PosEnum;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Jan 10, 2014
 * @author wangweir
 */
public class MMGPScaleBean {

    /**
     * 精度字段
     */
    private List<String> scalekeys;

    /**
     * 精度字段所在页签
     */
    private String scaletabcode;

    /**
     * 精度字段所在位置
     */
    private PosEnum scalepos;

    /**
     * 控制精度字段（汇率时使用，代表源币币种）
     */
    private String refkey;

    /**
     * 控制精度字段所在页签（汇率时使用，代表源币币种）
     */
    private String reftabcode;

    /**
     * 控制精度字段所在位置（汇率时使用，代表源币币种）
     */
    private PosEnum refpos;
    
    /**
     * 控制精度字段（汇率时使用，代表目的币种）
     */
    private String refkey2;

    /**
     * 控制精度字段所在页签（汇率时使用，代表目的币种）
     */
    private String reftabcode2;

    /**
     * 控制精度字段所在位置（汇率时使用，代表目的币种）
     */
    private PosEnum refpos2;
    
    /**
     * 控制精度字段（汇率时使用，代表财务组织）
     */
    private String refkey3;

    /**
     * 控制精度字段所在页签（汇率时使用，代表财务组织）
     */
    private String reftabcode3;

    /**
     * 控制精度字段所在位置（汇率时使用，代表财务组织）
     */
    private PosEnum refpos3;

    /**
     * 精度类型
     */
    private ScaleBeanType type;

    /**
     * @return the refkey
     */
    public String getRefkey() {
        return refkey;
    }

    /**
     * @param refkey
     *        the refkey to set
     */
    public void setRefkey(String refkey) {
        this.refkey = refkey;
    }

    /**
     * @return the reftabcode
     */
    public String getReftabcode() {
        return reftabcode;
    }

    /**
     * @param reftabcode
     *        the reftabcode to set
     */
    public void setReftabcode(String reftabcode) {
        this.reftabcode = reftabcode;
    }

    /**
     * @return the refpos
     */
    public PosEnum getRefpos() {
        return refpos;
    }

    /**
     * @param refpos
     *        the refpos to set
     */
    public void setRefpos(PosEnum refpos) {
        this.refpos = refpos;
    }

    /**
     * @return the type
     */
    public ScaleBeanType getType() {
        return type;
    }

    /**
     * @param type
     *        the type to set
     */
    public void setType(ScaleBeanType type) {
        this.type = type;
    }

    /**
     * @return the scaletabcode
     */
    public String getScaletabcode() {
        return scaletabcode; 
    }

    /**
     * @param scaletabcode
     *        the scaletabcode to set
     */
    public void setScaletabcode(String scaletabcode) {
        this.scaletabcode = scaletabcode;
    }

    /**
     * @return the scalepos
     */
    public PosEnum getScalepos() {
        return scalepos;
    }

    /**
     * @param scalepos
     *        the scalepos to set
     */
    public void setScalepos(PosEnum scalepos) {
        this.scalepos = scalepos;
    }

    /**
     * @return the scalekeys
     */
    public List<String> getScalekeys() {
        return scalekeys;
    }

    /**
     * @param scalekeys
     *        the scalekeys to set
     */
    public void setScalekeys(List<String> scalekeys) {
        this.scalekeys = scalekeys;
    }

	public String getRefkey2() {
		return refkey2;
	}

	public void setRefkey2(String refkey2) {
		this.refkey2 = refkey2;
	}

	public String getReftabcode2() {
		return reftabcode2;
	}

	public void setReftabcode2(String reftabcode2) {
		this.reftabcode2 = reftabcode2;
	}

	public PosEnum getRefpos2() {
		return refpos2;
	}

	public void setRefpos2(PosEnum refpos2) {
		this.refpos2 = refpos2;
	}

	public String getRefkey3() {
		return refkey3;
	}

	public void setRefkey3(String refkey3) {
		this.refkey3 = refkey3;
	}

	public String getReftabcode3() {
		return reftabcode3;
	}

	public void setReftabcode3(String reftabcode3) {
		this.reftabcode3 = reftabcode3;
	}

	public PosEnum getRefpos3() {
		return refpos3;
	}

	public void setRefpos3(PosEnum refpos3) {
		this.refpos3 = refpos3;
	}

}
