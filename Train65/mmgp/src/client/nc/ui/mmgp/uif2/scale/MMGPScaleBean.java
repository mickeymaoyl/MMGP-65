package nc.ui.mmgp.uif2.scale;

import java.util.List;

import nc.vo.pubapp.scale.PosEnum;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * 
 * @since �������� Jan 10, 2014
 * @author wangweir
 */
public class MMGPScaleBean {

    /**
     * �����ֶ�
     */
    private List<String> scalekeys;

    /**
     * �����ֶ�����ҳǩ
     */
    private String scaletabcode;

    /**
     * �����ֶ�����λ��
     */
    private PosEnum scalepos;

    /**
     * ���ƾ����ֶΣ�����ʱʹ�ã�����Դ�ұ��֣�
     */
    private String refkey;

    /**
     * ���ƾ����ֶ�����ҳǩ������ʱʹ�ã�����Դ�ұ��֣�
     */
    private String reftabcode;

    /**
     * ���ƾ����ֶ�����λ�ã�����ʱʹ�ã�����Դ�ұ��֣�
     */
    private PosEnum refpos;
    
    /**
     * ���ƾ����ֶΣ�����ʱʹ�ã�����Ŀ�ı��֣�
     */
    private String refkey2;

    /**
     * ���ƾ����ֶ�����ҳǩ������ʱʹ�ã�����Ŀ�ı��֣�
     */
    private String reftabcode2;

    /**
     * ���ƾ����ֶ�����λ�ã�����ʱʹ�ã�����Ŀ�ı��֣�
     */
    private PosEnum refpos2;
    
    /**
     * ���ƾ����ֶΣ�����ʱʹ�ã����������֯��
     */
    private String refkey3;

    /**
     * ���ƾ����ֶ�����ҳǩ������ʱʹ�ã����������֯��
     */
    private String reftabcode3;

    /**
     * ���ƾ����ֶ�����λ�ã�����ʱʹ�ã����������֯��
     */
    private PosEnum refpos3;

    /**
     * ��������
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
