package nc.vo.mmgp.util;

/**
 * <b> ��Ҫ�������� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-6-20
 * 
 * @author Administrator
 */
public class MMLanguageUtil {
    /**
     * @param all
     * @param args
     * @return
     */
    public static final String getLangStr(final String all,
                                          String... args) {
        if (args == null || args.length == 0) {
            return all;
        }
        String newWord = all;
        for (int i = 0; i < args.length; i++) {
            newWord = newWord.replaceAll("\\{" + (i + 1) + "\\}", args[i]);
        }

        return newWord;
    }
}
