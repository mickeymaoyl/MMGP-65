package nc.ui.mmgp.uif2.ref.gridtree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import nc.bs.logging.Logger;

import nc.ui.bd.ref.ExTreeNode;
import nc.ui.bd.ref.ITreeCellRendererIconPolicy;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UITree;
import nc.uitheme.ui.ThemeResourceCenter;
import nc.vo.jcom.lang.StringUtil;

/**
 * @author wangfan3
 */
public class MMGPRefTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 1L;

    public static int CHECKBOXSIZE = 20;

    private ITreeCellRendererIconPolicy iconPolicy = null;

    private Object curTreeNode = null;

    private MMGPTreePanel treeComponent;

    private CheckBoxLabel checkBox = new CheckBoxLabel();

    private JPanel panel = new JPanel();

    private MMGPTreeGridNodeListenner l;

    public MMGPRefTreeCellRenderer(MMGPTreePanel treeComp,
                                   MMGPTreeGridRefModel refTreeModel) {
        iconPolicy = refTreeModel.getTreeIconPolicy();
        try {
            this.treeComponent = treeComp;
            panel.setOpaque(false);
            this.setOpaque(false);
            checkBox.setOpaque(false);
            panel.setLayout(new BorderLayout(0, 0));
            panel.add(this, BorderLayout.CENTER);
            if (treeComp.isShowCheckBoxes()) {
                checkBox.setSize(CHECKBOXSIZE, CHECKBOXSIZE);
                checkBox.setPreferredSize(new Dimension(CHECKBOXSIZE, CHECKBOXSIZE));
                panel.add(checkBox, BorderLayout.BEFORE_LINE_BEGINS);
            }
            registerTreeListener(treeComp.getTree());
            setTreeNodeRenderListener(new MMGPTreeGridNodeListenner(treeComp));
            treeComp.getTree().addKeyListener(getTreeNodeRenderListener());
            treeComp.getTree().addMouseListener(getTreeNodeRenderListener());

        } catch (Exception ex) {
            Logger.debug(ex);
        }
    }

    private void registerTreeListener(UITree tree) {
        KeyListener kl = null;
        ArrayList toRemove = new ArrayList();
        for (int i = 0; i < tree.getKeyListeners().length; i++) {
            kl = tree.getKeyListeners()[i];
            if (kl instanceof MMGPTreeGridNodeListenner) toRemove.add(kl);
        }
        for (int i = 0; i < toRemove.size(); i++)
            tree.removeKeyListener((KeyListener) toRemove.get(i));

        MouseListener ml = null;
        toRemove.clear();
        for (int i = 0; i < tree.getMouseListeners().length; i++) {
            ml = tree.getMouseListeners()[i];
            if (ml instanceof MMGPTreeGridNodeListenner) toRemove.add(ml);
        }
        for (int i = 0; i < toRemove.size(); i++)
            tree.removeMouseListener((MouseListener) toRemove.get(i));
    }

    private boolean isShowIcon() {
        if (treeComponent != null && treeComponent.isShowCheckBoxes()) {
            if (curTreeNode instanceof ExTreeNode) {
                if (isSelectedEnabled(curTreeNode, ((ExTreeNode) curTreeNode).isLeaf())) {
                    return false;
                } else {
                    // if (iconPolicy != null) return true;
                    // return false;
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public Icon getLeafIcon() {
        if (!isShowIcon()) return null;
        if (iconPolicy != null && !StringUtil.isEmpty(iconPolicy.getLeafIcon(curTreeNode))) {
            Icon icon = ThemeResourceCenter.getInstance().getImage(iconPolicy.getLeafIcon(curTreeNode));
            if (icon != null) {
                return icon;
            }
        }
        if (curTreeNode instanceof ExTreeNode) {
            if (!((ExTreeNode) curTreeNode).isCanSelected() || ((ExTreeNode) curTreeNode).getParent() == null) {
                return ThemeResourceCenter.getInstance().getImage("themeres/control/tree/folder_open.png");
            } else {
                return null;
            }
        }
        return ThemeResourceCenter.getInstance().getImage("themeres/control/tree/point.png");
    }

    @Override
    public Icon getClosedIcon() {
        if (!isShowIcon()) return null;
        if (iconPolicy != null && !StringUtil.isEmpty(iconPolicy.getClosedIcon(curTreeNode))) {
            Icon icon = ThemeResourceCenter.getInstance().getImage(iconPolicy.getClosedIcon(curTreeNode));
            if (icon != null) {
                return icon;
            }
        }
        return ThemeResourceCenter.getInstance().getImage("themeres/control/tree/folder.png");
    }

    @Override
    public Icon getOpenIcon() {
        if (!isShowIcon()) {
            return null;
        }
        if (iconPolicy != null && !StringUtil.isEmpty(iconPolicy.getOpenIcon(curTreeNode))) {
            Icon icon = ThemeResourceCenter.getInstance().getImage(iconPolicy.getOpenIcon(curTreeNode));
            if (icon != null) {
                return icon;
            }
        }
        return ThemeResourceCenter.getInstance().getImage("themeres/control/tree/folder_open.png");
    }

    private boolean isSelectedEnabled(Object value,
                                      boolean leaf) {
        if (((DefaultMutableTreeNode) value).getParent() == null) return false;
        if (!leaf) {
            if (!treeComponent.getRefUI().getRefUIConfig().isNotLeafSelectedEnabled()) return false;
        }
        if (!(value instanceof ExTreeNode && ((ExTreeNode) value).isCanSelected())) return false;

        if (!(value instanceof ExTreeNode && ((ExTreeNode) value).isMainClass())) return false;
        return true;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {
        curTreeNode = value;

        if (treeComponent == null) {
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }

        checkBox.setEnabled(treeComponent.isEnabled());

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (treeComponent.isShowCheckBoxes()) {

            checkBox.setSelected(treeComponent.getCheckedNodes().contains(node));

            if (!isSelectedEnabled(value, leaf)) {
                checkBox.setVisible(false);
            } else {
                checkBox.setVisible(true);
            }
            if (value instanceof ExTreeNode) {
                if (!((ExTreeNode) value).isCanSelected() || ((ExTreeNode) value).getParent() == null) {
                    checkBox.setVisible(false);
                }
            }
        }

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (!tree.hasFocus()) {
            // Ê÷Ê§È¥½¹µãÊ±
            this.setBackgroundSelectionColor(new Color(100, 100, 100));
        } else {
            this.setBackgroundSelectionColor(new Color(53, 103, 153));
        }

        if (!isSelectedEnabled(value, leaf)) {
            if (hasFocus) {

            } else {
                // this.setForeground(new Color(102, 102, 102));
            }
        } else {
            this.setEnabled(true);
        }
        if (curTreeNode instanceof ExTreeNode) {
            if (!((ExTreeNode) curTreeNode).isCanSelected() && !hasFocus) {
                this.setForeground(Color.RED);
            }
        }
        panel.revalidate();
        panel.repaint();
        return panel;
    }

    private void setTreeNodeRenderListener(MMGPTreeGridNodeListenner l) {
        this.l = l;
    }

    public MMGPTreeGridNodeListenner getTreeNodeRenderListener() {
        return l;
    }

    class CheckBoxLabel extends UICheckBox {

        private static final long serialVersionUID = 1L;

        public void setSelected(boolean sel) {
            super.setSelected(sel);
            repaint();
        }

    }
}
