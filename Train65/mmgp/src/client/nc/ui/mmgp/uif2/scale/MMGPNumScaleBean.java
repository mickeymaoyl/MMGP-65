package nc.ui.mmgp.uif2.scale;

import java.util.List;

import nc.ui.pub.bill.IBillItem;
import nc.vo.pubapp.scale.PosEnum;

/**
 * ����������������
 * <p>
 * ����Ҫ�����������λָ����С��λ�����þ���
 * 
 * @author wangweiu
 * @deprecated ������ʱֻ��֧���������ȡ����ں����������ӣ�ʹ��MMGPScaleBean���档
 * @since MMGP1.1
 */
public class MMGPNumScaleBean extends MMGPScaleBean {


	// ����item��key
	private List<String> numkeys;
	private int pos = IBillItem.HEAD;
	// ���ӱ���Ч
	private String tabcode;
	// ��λitem��key
	private String unitkey = "pk_measdoc";
	private int unitpos = IBillItem.HEAD;
	// ���ӱ���Ч
	private String unittabcode;
	
    public MMGPNumScaleBean() {
        this.setType(ScaleBeanType.NumAssNum);
        this.setPos(IBillItem.HEAD);
        this.setUnitkey("pk_measdoc");
        this.setUnitpos(IBillItem.HEAD);
    }
	public List<String> getNumkeys() {
		return numkeys;
	}

    public void setNumkeys(List<String> numkeys) {
        this.numkeys = numkeys;
        this.setScalekeys(numkeys);
    }

	public int getPos() {
		return pos;
	}

    public void setPos(int pos) {
        this.pos = pos;
        this.setScalepos(PosEnum.valueOf(pos));
    }

	public String getTabcode() {
		return tabcode;
	}

    public void setTabcode(String tabcode) {
        this.tabcode = tabcode;
        this.setScaletabcode(tabcode);
    }

	public String getUnitkey() {
		return unitkey;
	}

	public void setUnitkey(String unitkey) {
		this.unitkey = unitkey;
		this.setRefkey(unitkey);
	}

	public int getUnitpos() {
		return unitpos;
	}

	public void setUnitpos(int unitpos) {
		this.unitpos = unitpos;
		this.setRefpos(PosEnum.valueOf(unitpos));
	}

	public String getUnittabcode() {
		return unittabcode;
	}

    public void setUnittabcode(String unittabcode) {
        this.unittabcode = unittabcode;
        this.setReftabcode(unittabcode);
    }

}
