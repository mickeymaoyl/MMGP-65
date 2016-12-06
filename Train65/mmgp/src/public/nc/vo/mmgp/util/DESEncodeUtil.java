package nc.vo.mmgp.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * �ӽ��ܹ�����
 */
public class DESEncodeUtil {

    private static final String DES = "DES";

    /**
     * ����
     *
     * @param src
     *        ����Դ
     * @param key
     *        ��Կ�����ȱ�����8�ı���
     * @return ���ؼ��ܺ������
     */
    public static byte[] encrypt(byte[] src,
                                 byte[] key) {
        if (key.length == 0) {
            throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0075")/*@res "��Կ���Ȳ���Ϊ0"*/);
        }
        if (key.length % 8 != 0) {
            throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0076")/*@res "��Կ���ȱ�����8�ı���"*/);
        }
        // DES�㷨Ҫ����һ�������ε������Դ
        try {
            SecureRandom sr = new SecureRandom();
            // ��ԭʼ�ܳ����ݴ���DESKeySpec����
            DESKeySpec dks = new DESKeySpec(key);
            // ����һ���ܳ׹�����Ȼ��������DESKeySpecת����
            // һ��SecretKey����
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher����ʵ����ɼ��ܲ���
            Cipher cipher = Cipher.getInstance(DES);
            // ���ܳ׳�ʼ��Cipher����
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // ���ڣ���ȡ���ݲ�����
            // ��ʽִ�м��ܲ���
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ����
     *
     * @param src
     *        ����Դ
     * @param key
     *        ��Կ�����ȱ�����8�ı���
     * @return ���ؽ��ܺ��ԭʼ����
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src,
                                 byte[] key) {
        try {
            // DES�㷨Ҫ����һ�������ε������Դ
            SecureRandom sr = new SecureRandom();
            // ��ԭʼ�ܳ����ݴ���һ��DESKeySpec����
            DESKeySpec dks = new DESKeySpec(key);
            // ����һ���ܳ׹�����Ȼ��������DESKeySpec����ת����
            // һ��SecretKey����
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher����ʵ����ɽ��ܲ���
            Cipher cipher = Cipher.getInstance(DES);
            // ���ܳ׳�ʼ��Cipher����
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // ���ڣ���ȡ���ݲ�����
            // ��ʽִ�н��ܲ���
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ���ݽ���
     *
     * @param data
     * @param key
     *        ��Կ
     * @return
     * @throws Exception
     */
    public static final String decrypt(String data,
                                       String key) {
        return new String(decrypt(hex2byte(data.getBytes()), key.getBytes()));
    }

    /**
     * ���ݼ���
     *
     * @param data
     * @param key
     *        ��Կ
     * @return
     * @throws Exception
     */
    public static final String encrypt(String data,
                                       String key) {
        if (data != null) try {
            return byte2hex(encrypt(data.getBytes(), key.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * ������ת�ַ���
     *
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

}