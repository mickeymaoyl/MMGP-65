package nc.vo.mmgp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;

import nc.bs.logging.Logger;
import nc.vo.mmgp.exception.MMGPAssertException;
import nc.vo.pub.BusinessException;
import sun.awt.shell.ShellFolder;

/**
 * <b> 系统工具类 </b>
 * <p>
 * 增加ClassLoader的usr_paths
 * </p>
 * 创建日期:2011-6-20
 *
 * @version 1.1
 * @author wangweiu
 */
public class MMSystemUtil {

    /**
     * 添加usr_paths 变量
     *
     * @param s
     *        path变量
     */
    public static void addUserPath(String s) {
        try {
            // test on jdk5 and jdk6
            Field field = ClassLoader.class.getDeclaredField("usr_paths");
            field.setAccessible(true);
            String[] paths = (String[]) field.get(null);
            for (int i = 0; i < paths.length; i++) {
                if (s.equals(paths[i])) {
                    return;
                }
            }
            String[] tmp = new String[paths.length + 1];
            System.arraycopy(paths, 0, tmp, 0, paths.length);
            tmp[paths.length] = s;
            field.set(null, tmp);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取缓存路径
     *
     * @return 缓存路径
     */
    public static String getCachePath() {
        return System.getProperty("client.code.dir", System.getProperty("user.home"));
    }

    /**
     * 返回本地化文件夹图标
     *
     * @return 本地化文件夹图标
     */
    public static ImageIcon getLocalFolderIcon() {
        File f = new File(MMSystemUtil.getCachePath());
        if (!f.exists()) {
            boolean result = f.mkdirs();
            if (!result) {
                Logger.debug("mkdirs failed");
            }
        }
        return getImageIcon(f);
    }

    /**
     * 返回本地化文件图标
     *
     * @param fileName
     *        文件名称
     * @return 本地化文件图标
     */
    public static ImageIcon getLocalFileIcon(String fileName) {
        File folder = new File(MMSystemUtil.getCachePath() + "\\mescache\\medoc");
        boolean result = folder.mkdirs();
        if (!result) {
            Logger.debug("mkdirs failed");
        }
        File f = new File(folder, fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return getImageIcon(f);
    }

    /**
     * 获取本地计算机名称
     *
     * @return String
     * @throws BusinessException
     */
    public static String getLocalPcName() throws BusinessException {
        String pcName = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            pcName = addr.getHostName();
        } catch (UnknownHostException e) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0078")/*@res "获取计算机本地名称异常! "*/);
        }
        return pcName;
    }

    /**
     * 获取IP地址
     *
     * @return String
     * @throws BusinessException
     */
    public static String getLocalPcIP() throws BusinessException {
        String ip = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0079")/*@res "获取计算机本地ip异常! "*/);
        }
        return ip;
    }

    /**
     * 获取文件的本地化图标
     *
     * @param f
     *        文件
     * @return 文件的本地化图标
     */
    public static ImageIcon getImageIcon(File f) {
        try {
            ShellFolder shellFolder = ShellFolder.getShellFolder(f);
            ImageIcon bigIcon = new ImageIcon(shellFolder.getIcon(true));
            return bigIcon;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 是否为开发态环境
     *
     * @return 当开发态环境是返回true
     */
    public static boolean isDevelopMode() {
        return MMStringUtil.isNotEmpty(System.getProperty("nc.runMode"));
    }

    public static void assertTrue(boolean mustBeTrue) {
        assertTrue(mustBeTrue, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0080")/*@res "断言失败!"*/);
    }

    public static void assertTrue(boolean mustBeTrue,
                                  String errorMsg) {
        if (isDevelopMode()) {
            if (!mustBeTrue) {
                throw new MMGPAssertException(errorMsg);
            }
        }
    }

    public static void assertFalse(boolean mustBeTrue) {
        assertTrue(!mustBeTrue);
    }

    public static void assertFalse(boolean mustBeTrue,
                                   String errorMsg) {
        assertTrue(!mustBeTrue, errorMsg);
    }
}