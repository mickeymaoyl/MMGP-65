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
 * <b> ϵͳ������ </b>
 * <p>
 * ����ClassLoader��usr_paths
 * </p>
 * ��������:2011-6-20
 *
 * @version 1.1
 * @author wangweiu
 */
public class MMSystemUtil {

    /**
     * ���usr_paths ����
     *
     * @param s
     *        path����
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
     * ��ȡ����·��
     *
     * @return ����·��
     */
    public static String getCachePath() {
        return System.getProperty("client.code.dir", System.getProperty("user.home"));
    }

    /**
     * ���ر��ػ��ļ���ͼ��
     *
     * @return ���ػ��ļ���ͼ��
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
     * ���ر��ػ��ļ�ͼ��
     *
     * @param fileName
     *        �ļ�����
     * @return ���ػ��ļ�ͼ��
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
     * ��ȡ���ؼ��������
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
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0078")/*@res "��ȡ��������������쳣! "*/);
        }
        return pcName;
    }

    /**
     * ��ȡIP��ַ
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
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0079")/*@res "��ȡ���������ip�쳣! "*/);
        }
        return ip;
    }

    /**
     * ��ȡ�ļ��ı��ػ�ͼ��
     *
     * @param f
     *        �ļ�
     * @return �ļ��ı��ػ�ͼ��
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
     * �Ƿ�Ϊ����̬����
     *
     * @return ������̬�����Ƿ���true
     */
    public static boolean isDevelopMode() {
        return MMStringUtil.isNotEmpty(System.getProperty("nc.runMode"));
    }

    public static void assertTrue(boolean mustBeTrue) {
        assertTrue(mustBeTrue, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0080")/*@res "����ʧ��!"*/);
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