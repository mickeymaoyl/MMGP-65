package nc.ui.mmgp.flexgant.view.timelinerender;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;





import nc.ui.pubapp.gantt.timeline.render.AppGantActivityObjectRenderer;
import nc.ui.pubapp.gantt.ui.util.AppGanttDataFormatUtil;

import com.dlsc.flexgantt.swing.layer.LayerColor;

/**
 * 甘特图右侧时间线Render   需要显示类似完成百分比在时间线上用此类
 * 改变图元形状 只需继承drawShape与fillShape方法
 * @author wangfan3
 *
 */
public class MMGPAbstractActiveTimeLineRender extends AppGantActivityObjectRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3589696134172681250L;

	/* (non-Javadoc)
	 * @see nc.ui.pubapp.gantt.model.AppGantActivityObjectRenderer#paintActivityContent(java.awt.Graphics)
	 */
	@Override
	protected void paintActivityContent(Graphics g) {
	    int w = this.getWidth() - 1;
        int h = getHeight() - 1;
        Graphics2D g2d = (Graphics2D) g;
        if (this.isHighlighted()) {
            this.paintHighlightContent(g2d);
        } else if (this.isSelected()) {
            this.paintSelectedContent(g2d);
        } else {
            g2d.setPaint(new GradientPaint(0, 0, this.getActivityFillColor1(),
                    0, h / 2, this.getActivityFillColor2()));
        }
        
        fillShape(h, w, g2d);
	    

		Color fillColor1Old = getActivityFillColor1();
		Color fillColor2Old = getActivityFillColor2();
		double percentComplete = getActivityObject().getPercentageComplete();
		String p = AppGanttDataFormatUtil.formatNumberType(percentComplete);
		String billno = (String) getActivityObject().getPopupTitleObject();
		int finishedLength = (int) (getWidth() / 100d * percentComplete);
		int unfinishedLength = ((int) getWidth()) - finishedLength;
		setActivityFillColor1(new Color(203, 218, 245));
		setActivityFillColor2(new Color(153, 183, 236));
		Shape clip = g.getClip();
		g.clipRect(0, 0, finishedLength, getHeight());
		
		if (this.isHighlighted()) {
			this.paintHighlightContent(g2d);
		} else if (this.isSelected()) {
			this.paintSelectedContent(g2d);
		} else {
			g2d.setPaint(new GradientPaint(0, 0, this.getActivityFillColor1(),
					0, h / 2, this.getActivityFillColor2()));
		}
		
		fillShape(h, w, g2d);
		
		setActivityFillColor1(fillColor1Old);
		setActivityFillColor2(fillColor2Old);
		g.setClip(clip);
		if (finishedLength < getWidth() - 6 || isRoundedCorners()) {
			if (billno != null) {
				int billnoWidth = getBillnoWidth(billno);
				g.setColor(LayerColor.TEXTCOLOR);
				g.setFont(new Font("Arial", Font.PLAIN, 12));
				if (isShowIcon() == false) {
					if (billnoWidth < finishedLength) {
						g.drawString(billno, 8, 14);
						drawPercentage(unfinishedLength, p, finishedLength, g);
					} else {
						g.drawString(billno, finishedLength + 7, 14);
					}
				} else {
					if (billnoWidth < finishedLength) {
						g.drawString(billno, 20, 14);
						drawPercentage(unfinishedLength, p, finishedLength, g);
					} else {
						g.drawString(billno, finishedLength + 20, 14);
					}
				}
			} else {
				g.setColor(LayerColor.TEXTCOLOR);
				g.setFont(new Font("Arial", Font.PLAIN, 12));
				drawPercentage(unfinishedLength, p, finishedLength, g);
			}
		}
	}

	protected void fillShape(int h, int w, Graphics2D g2d) {
		if (this.isRoundedCorners()) {
			g2d.fillRoundRect(1, 2, w, h - 1, 4, 4);
			if (this.isGlossy()) {
//				g2d.setColor(new Color(255, 255, 255, 100));
				g2d.fillRoundRect(1, 2, w, h / 2 - 1, 4, 4);
			}
		} else {
			g2d.fillRect(1, 2, w - 1, h - 2);
			if (this.isGlossy()) {
				g2d.setColor(new Color(255, 255, 255, 100));
				g2d.fillRect(1, 2, w - 1, h / 2 - 2);
			}
		}
	}

	// 获得单据号占的长度
	private int getBillnoWidth(String billno) {
		return 8 * billno.length();
	}

	// 描绘百分比事件线的处理
	protected void drawPercentage(int unfinishedLength, String p,
			int finishedLength, Graphics g) {
		int percentageLength = getBillnoWidth(String.valueOf(p));
		if (unfinishedLength > percentageLength + 7) {
			g.drawString(p + "%", finishedLength + 7, 14);
		} else {
			if (finishedLength < percentageLength - 7) {
				g.drawString(p + "%", 0, 14);
			} else {
				g.drawString(p + "%", finishedLength - percentageLength - 7, 14);
			}
		}
	}

	protected void paintComponent(Graphics g) {
		long timeNow = this.getTimelineObjectLayer().getGanttChart()
				.getEventline().getTimeNow();
		int x = this.getTimelineObjectLayer().getGanttChart().getDateline()
				.getTimeLocation(timeNow);
		if (this.isEvent()) {
			this.paintEvent(g);
			if (this.isVisualizingPastTime() && x > this.getBounds().x) {
				Shape clip = g.getClip();
				g.clipRect(0, 0, x - this.getBounds().x, this.getHeight() * 2);
				Color fill1 = this.getEventFillColor1();
				Color fill2 = this.getEventFillColor2();
				this.setEventLineColor(this.getPastLineColor());
				this.setEventFillColor1(this.getPastFillColor1());
				this.setEventFillColor2(this.getPastFillColor2());
				this.paintEvent(g);
				this.setEventFillColor1(fill1);
				this.setEventFillColor2(fill2);
				g.setClip(clip);
			}
		} else if (this.isParent()) {
			this.paintParent(g);
		} else {
			this.paintActivityContent(g);
			if (this.isVisualizingPastTime() && x > this.getBounds().x) {
				Shape clip = g.getClip();
				g.clipRect(0, 0, x - this.getBounds().x, this.getHeight() * 2);
				Color fill1 = this.getActivityFillColor1();
				Color fill2 = this.getActivityFillColor2();
				this.setActivityLineColor(this.getPastLineColor());
				this.setActivityFillColor1(this.getPastFillColor1());
				this.setActivityFillColor2(this.getPastFillColor2());
				this.paintActivityContent(g);
				this.setActivityFillColor1(fill1);
				this.setActivityFillColor2(fill2);
				g.setClip(clip);
			}
			this.paintActivityBorder(g);
			if (this.isFocus()) {
				this.paintActivityFocus(g);
			}
		}
	}

	@Override
	protected void paintActivityBorder(Graphics g) {
		int w = this.getWidth() - 1;
		int h = this.getHeight() - 1;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (this.isHighlighted()) {
			g2d.setColor(this.getHighlightLineColor());
		} else if (this.isFocus()) {
			this.setFocusLineColor(getActivityLineColor());
			g2d.setColor(this.getFocusLineColor());
			g2d.setStroke(new BasicStroke(2.0f));
		} else if (this.isSelected()) {
			g2d.setStroke(new BasicStroke(2.0f));
			g2d.setColor(this.getSelectionLineColor());
		} else {
			g2d.setColor(this.getActivityLineColor());
		}
		
		drawShape(w, h, g2d);
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_DEFAULT);
	}
	
	protected void drawShape(int w, int h, Graphics2D g2d){
		if (this.isRoundedCorners()) {
			drawRoundedShape(w, h, g2d);
		} else {
			drawNoRoundedShape(w, h, g2d);
		}
	}
	
	protected void drawRoundedShape(int w, int h, Graphics2D g2d) {
		 g2d.drawRoundRect(1, 2, w - 1, h - 2, 4, 4);
	}

	protected void drawNoRoundedShape(int w, int h, Graphics2D g2d) {
		g2d.drawRect(1, 2, w - 1, h - 2);
	}
}
