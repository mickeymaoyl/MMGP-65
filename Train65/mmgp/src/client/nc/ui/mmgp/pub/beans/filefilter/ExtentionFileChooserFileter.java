package nc.ui.mmgp.pub.beans.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * <b> 根据扩展名过滤文件 </b>
 * <p>
 * 1. 文件夹/后缀名符合规则，返回true
 * 2. 后缀名为空/后缀名不符合规则，返回false
 * </p>
 * 创建日期:2011-3-26
 * 
 * @author wangweiu
 */
public class ExtentionFileChooserFileter extends FileFilter {

	/**
	 * 文件后缀名
	 */
	private String extension = null;

	/**
	 * 描述
	 */
	private String description = null;

	/**
	 * 构造方法
	 * 
	 * @param inExtention
	 *            后缀名
	 */
	public ExtentionFileChooserFileter(String inExtention) {
		extension = inExtention;
	}

	/**
	 * 构造方法
	 * 
	 * @param inExtention
	 *            后缀名
	 * @param inDescription
	 *            描述
	 */
	public ExtentionFileChooserFileter(String inExtention, String inDescription) {
		extension = inExtention;
		description = inDescription;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		if (extension == null) {
			return false;
		}
		if (f.getAbsoluteFile().getName().toLowerCase().endsWith("." + extension.toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * 简要说明
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		if (description == null) {
			return extension.toUpperCase();
		}
		return description;
	}

	public String getExtension() {
		return extension;
	}

}
