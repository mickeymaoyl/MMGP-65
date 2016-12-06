package nc.ui.mmgp.uif2.view.treetable.util;

import java.util.Comparator;

import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * @Description: TODO
 *               <p>
 *               innercode�����������
 *                1
 *                	1.1
 *                	1.2
 *                2 
 *                	2.1
 *                		2.1.1
 *                	2.2
 *               </p>
 * @data:2014-5-15����11:24:58
 * @author: lisyd
 */
public class MMGPInnerCodeComparator implements
		Comparator<CircularlyAccessibleValueObject> {

	private static final String INNERCODE = "innercode";

	@Override
	public int compare(CircularlyAccessibleValueObject arg0,
			CircularlyAccessibleValueObject arg1) {
		String o1 = (String) arg0.getAttributeValue(INNERCODE);
		String o2 = (String) arg1.getAttributeValue(INNERCODE);

		/**
		 * o1 = "1.2" �� o1s = ["1","2"]; o2 = "1" �� o2s = ["1"]
		 */
		String[] o1s = o1.split("\\.");
		String[] o2s = o2.split("\\.");

		// ȡ�������鳤����Сֵ
		int minLen = Math.min(o1s.length, o2s.length);

		for (int i = 0; i < minLen; i++) {
			int m = Integer.parseInt(o1s[i]);
			int n = Integer.parseInt(o2s[i]);

			if (m == n) {// 1��1.1 ��һ��Ѷ����
				continue;
			} else if (m > n) {// 1.3��1.2.1 �ڶ���ѭ��
				return 1;
			} else {// 1.2.1��1.3 �ڶ���ѭ��
				return -1;
			}
		}

		// Ĭ���������
		int res = 0;// 1.2��1.2

		if (o1s.length < o2s.length) {// 1.2��1.2.1
			res = -1;

		} else if (o1s.length > o2s.length) {// 1.2.1��1.2
			res = 1;

		}
		return res;

	}
}
