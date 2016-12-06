package nc.vo.mmgp.util;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2011-6-20
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
