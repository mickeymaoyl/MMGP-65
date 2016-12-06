package nc.ui.mmgp.pub.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.swing.ImageIcon;

import nc.bs.logging.Logger;

public class IconService {

	public static final String DEFAULTICONCONFIG = "nc/ui/mes/MES.res";

	private String iconConfig;

	private static final Properties RES_PROPERTIES = new Properties();

	public IconService(String configFile) {
		iconConfig = configFile;
		loadProperty();
	}

	public IconService() {
		iconConfig = DEFAULTICONCONFIG;
		loadProperty();
	}

	private void loadProperty() {
		// 获取图片资源
		InputStream is = IconService.class.getClassLoader()
				.getResourceAsStream(iconConfig);
		try {
			RES_PROPERTIES.load(is);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	/**
	 * 根据名字返回相关图片的url
	 * 
	 * @param name
	 * @return
	 */

	public URL getIcon(String key) {

		String name = RES_PROPERTIES.getProperty(key);
		if (name != null) {
			URL url = IconService.class.getClassLoader().getResource(name);
			return url;
		}
		return null;

	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public ImageIcon getImageIcon(String key) {

		String name = RES_PROPERTIES.getProperty(key);
		if (name != null) {
			URL url = IconService.class.getClassLoader().getResource(name);
			if (url != null) {
				return new ImageIcon(url);
			}
		}
		return null;
	}
}
