package nc.ui.mmgp.pub.util;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIToggleButton;

public class MMButtonUtil {
	/**
	 * 
	 * @param iconKey
	 *            图形标识（简称）
	 * @param toolTip
	 *            hint提示
	 * @param iconConfigPath
	 *            图形配置文件的路径
	 * @return
	 */
	public static JButton createUIButton(String iconKey, String toolTip,
			String iconConfigPath) {
		UIButton button = new UIButton();
		button.setPreferredSize(new Dimension(20, 20));
		setButtonIconAndToolTip(button, iconKey, toolTip, iconConfigPath);
		return button;
	}

	/**
	 * @param iconKey
	 * @param button
	 */
	private static void setButtonIconAndToolTip(AbstractButton button,
			String iconKey, String toolTip, String iconConfigPath) {
		IconService iconService = new IconService(iconConfigPath);
		if (iconConfigPath == null) {
			iconService = new IconService();
		} else {
			iconService = new IconService(iconConfigPath);
		}

		URL url = iconService.getIcon(iconKey);
		button.setIcon(new ImageIcon(url));
		button.setToolTipText(toolTip);
	}

	/**
	 * 
	 * @param iconKey
	 *            图形标识（简称）
	 * @param toolTip
	 *            hint提示
	 * @param iconConfigPath
	 *            图形配置文件的路径
	 * @return
	 */
	public static JToggleButton createToggleButton(String iconKey,
			String toolTip, String iconConfigPath) {
		UIToggleButton button = new UIToggleButton();
		button.setPreferredSize(new Dimension(20, 20));
		setButtonIconAndToolTip(button, iconKey, toolTip, iconConfigPath);
		return button;
	}
}
