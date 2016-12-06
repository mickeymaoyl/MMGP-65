package nc.ui.mmgp.uif2.scale;

import java.util.List;

import nc.ui.pub.bill.IBillItem;
import nc.vo.pubapp.scale.PosEnum;

/**
 * 【数量】精度配置
 * <p>
 * 数量要根据其计量单位指定的小数位数设置精度
 * 
 * @author wangweiu
 * @deprecated 最初设计时只能支持数量精度。由于后续需求增加，使用MMGPScaleBean代替。
 * @since MMGP1.1
 */
public class MMGPNumScaleBean extends MMGPScaleBean {


	// 数量item的key
	private List<String> numkeys;
	private int pos = IBillItem.HEAD;
	// 多子表有效
	private String tabcode;
	// 单位item的key
	private String unitkey = "pk_measdoc";
	private int unitpos = IBillItem.HEAD;
	// 多子表有效
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
