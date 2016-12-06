package nc.ui.mmgp.pub.beans.digraph.renderer;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import nc.ui.pub.beans.UILabel;

public class ImageLabel extends UILabel {

    /**
     * 
     */
    private static final long serialVersionUID = -8331968821179502225L;

    @Override
    protected void paintComponent(Graphics g) {
        // super.paintComponent(g);
        if (getIcon() == null || !(getIcon() instanceof ImageIcon)) {
            super.paintComponent(g);
            return;
        }
        g.drawImage(((ImageIcon) getIcon()).getImage(), 0, 0, getWidth(), getHeight(), null);

    }
}
