package nc.ui.mmgp.flexgant.view.timelinerender;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import nc.ui.pubapp.gantt.timeline.render.AppTimelineObjectRenderer;


/**
 * 改变图元形状 只需继承drawShape与fillShape方法
 * @author wangfan3
 *
 */
public class MMGPAbstractTimeLineRender extends AppTimelineObjectRenderer{
	
	@Override
	protected void paintActivityContent(Graphics g) {
		int w = this.getWidth() - 1;
		int h = this.getHeight() - 1;
		Graphics2D g2d = (Graphics2D) g;
		if (this.isHighlighted()) {
			this.paintHighlightContent(g2d);
		} else if (this.isSelected()) {
			this.paintSelectedContent(g2d);
		} else {
			g2d.setPaint(new GradientPaint(0, 0, this.getActivityFillColor1(),
					0, h / 2, this.getActivityFillColor2()));
		}
		fillShape(w, h, g2d);
	}

	protected void fillShape(int w, int h, Graphics2D g2d) {
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

	protected void drawShape(int w, int h, Graphics2D g2d) {
		if (this.isRoundedCorners()) {
			g2d.drawRoundRect(1, 2, w - 1, h - 2, 4, 4);
		} else {
			g2d.drawRect(1, 2, w - 1, h - 2);
		}
	}
}
