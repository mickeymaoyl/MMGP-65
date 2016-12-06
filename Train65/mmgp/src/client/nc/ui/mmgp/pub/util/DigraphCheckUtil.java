package nc.ui.mmgp.pub.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * <b> ����ͼ��鹤�� </b>
 * <p>
 * ��ϸ��������
 * </p>
 * ��������:2011-2-25
 *
 * @author wangweiu
 */
public class DigraphCheckUtil {
	/**
	 * ȡ�óɻ��Ľڵ���Ϣ
	 *
	 * @param source
	 * @return ��״�ڵ�λ��
	 */
	public static int[] circleModel(List<List<Boolean>> source) {
		if (source == null || source.isEmpty()) {
			return null;
		}
		List<List<Boolean>> cloneAdjMat = cloneAdjMat(source);
		List<Integer> froms = head(source);
		if (froms.isEmpty()) {
			throw new RuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0001")/*@res "��ͼû����ǰ���Ķ��㣬Ϊһ����·."*/);
		}

		List<Integer> allPathList = new ArrayList<Integer>();

		for (Integer v : froms) {
			Stack<Integer> statck = new Stack<Integer>();
			allPathList.add(v);
			statck.push(v);
			while (!statck.isEmpty()) {
				Integer next = findNext(statck.lastElement(), cloneAdjMat);
				if (next < 0) {
					// ȥ�����Ӵ˶���ı�;
					Integer now = statck.pop();
					if (!statck.isEmpty()) {
						deleteEdge(statck.lastElement(), now, cloneAdjMat);
					}
				} else if (statck.contains(next)) {
					int[] result = { statck.lastElement(), next };
					return result;
				} else if (!statck.contains(next)) {
					allPathList.add(next);
					statck.push(next);
				}
			}
		}

		if (allPathList.size() < cloneAdjMat.size()) {
			List<Integer> cirList = new ArrayList<Integer>();
			for (int i = 0; i < cloneAdjMat.size() - 1; i++) {
				if (!allPathList.contains(i)) {
					cirList.add(i);
				}
			}
			int[] result = { cirList.get(0), cirList.get(1) };
			return result;
		}
		return null;
	}

	/**
	 * @param lastElement
	 * @param now
	 * @param cloneAdjMat
	 */
	private static void deleteEdge(Integer lastElement, Integer now,
			List<List<Boolean>> cloneAdjMat) {
		cloneAdjMat.get(lastElement).set(now, false);

	}

	private static Integer findNext(Integer fromV, List<List<Boolean>> source) {
		for (int i = 0; i < source.size(); i++) { // ��
			if (source.get(fromV).get(i)) {
				return i;
			}
		}

		return -1;
	}

	private static List<Integer> head(List<List<Boolean>> source) {
		List<Integer> lstV = new ArrayList<Integer>();

		for (int x = 0; x < source.size(); x++) { // ��
			int row = x;
			for (int y = 0; y < source.size(); y++) { // ��
				if (source.get(y).get(x)) {
					row = -1;
					break;
				}
			}
			if (row >= 0) {
				lstV.add(row);
			}
		}

		return lstV;

	}

	private static List<List<Boolean>> cloneAdjMat(List<List<Boolean>> source) {
		List<List<Boolean>> cloned = new ArrayList<List<Boolean>>();
		for (List<Boolean> row : source) {
			List<Boolean> cloneRow = new ArrayList<Boolean>();
			for (Boolean cell : row) {
				cloneRow.add(cell);
			}
			cloned.add(cloneRow);
		}
		return cloned;
	}
}