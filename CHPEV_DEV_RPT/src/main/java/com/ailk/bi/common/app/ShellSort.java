package com.ailk.bi.common.app;

import java.util.Comparator;

/**
 * @author hailong.wang 定义排序，本排序是按照希尔排序进行
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ShellSort {
	private static final boolean DEBUGGING = false;

	/**
	 * 将对象数组按照字符进行升序排序，返回其升序排列后的具体位置
	 * 
	 * @param userArray
	 *            需要进行排序的一维数组
	 * @return 返回按照升序排序后的实际数据的具体位置
	 */
	public static String[] sortStr(Object[] userArray, Comparator delegate) {
		ShellSort s = new ShellSort();
		if (userArray == null)
			return null;
		s.userArray = userArray;
		s.delegate = delegate;
		String[] strQuence = new String[s.userArray.length];
		for (int i = 0; i < s.userArray.length; i++) {
			strQuence[i] = Integer.toString(i);
		}
		if (s.isAlreadySorted())
			return strQuence;
		// ShellSort proper
		// h is the separation between items we compare.
		int h = 1;
		while (h < userArray.length) {
			h = 3 * h + 1;
		}

		while (h > 0) {
			h = (h - 1) / 3;
			for (int i = h; i < userArray.length; ++i) {
				Object item = userArray[i];
				String tempQuence = strQuence[i];
				int j = 0;
				for (j = i - h; j >= 0
						&& delegate.compare(item, userArray[j]) < 0; j -= h) {
					userArray[j + h] = userArray[j];
					strQuence[j + h] = strQuence[j];
				} // end inner for
				userArray[j + h] = item;
				strQuence[j + h] = tempQuence;
			} // end outer for
		} // end while

		if (DEBUGGING) {
			if (!s.isAlreadySorted())
				System.out.println("Sort failed");
		}
		return strQuence;
	} // end sort

	/**
	 * 将对象数组按照数值进行升序排序，返回其升序排列后的具体位置
	 * 
	 * @param userArray
	 *            需要进行排序的一维数组
	 * @return 返回按照升序排序后的实际数据的具体位置
	 */
	public static String[] sortDouble(Object[] userArray) {
		ShellSort s = new ShellSort();
		if (userArray == null)
			return null;
		s.userArray = userArray;
		String[] strQuence = new String[s.userArray.length];
		for (int i = 0; i < s.userArray.length; i++) {
			strQuence[i] = Integer.toString(i);
		}
		// 如果不需要排序，那么返回正序列
		if (s.isDoubleSorted())
			return strQuence;
		// ShellSort proper
		// h is the separation between items we compare.
		int h = 1;
		while (h < userArray.length) {
			h = 3 * h + 1;
		}

		while (h > 0) {
			h = (h - 1) / 3;
			for (int i = h; i < userArray.length; ++i) {
				Object item = userArray[i];
				String tempQuence = strQuence[i];
				int j = 0;
				for (j = i - h; j >= 0 && intcompare(item, userArray[j]) < 0; j -= h) {
					userArray[j + h] = userArray[j];
					strQuence[j + h] = strQuence[j];
				} // end inner for
				userArray[j + h] = item;
				strQuence[j + h] = tempQuence;
			} // end outer for
		} // end while

		if (DEBUGGING) {
			if (!s.isDoubleSorted())
				System.out.println("Sort failed");
		}
		return strQuence;
	} // end sort

	// check if user's array is already sorted
	private boolean isDoubleSorted() {
		for (int i = 1; i < userArray.length; i++) {
			if (intcompare(userArray[i], userArray[i - 1]) < 0)
				return false;
		}
		return true;
	} // end isAlreadySorted

	/**
	 * 比较两个实数的大小
	 * 
	 * @param a
	 *            Object
	 * @param b
	 *            Object
	 * @return int
	 */
	public static int intcompare(Object a, Object b) {
		if (a == null || b == null)
			return 0;
		try {
			double int_a = Double.parseDouble((String) a);
			double int_b = Double.parseDouble((String) b);
			if (int_a >= int_b)
				return 1;
			else
				return -1;
		} catch (NullPointerException e) {
			System.out.println("NullPointerException==>" + e.toString());
			return 0;
		} catch (NumberFormatException ne) {
			System.out.println("NumberFormatException==>" + ne.toString());
			return 0;
		}
	}

	/**
	 * 将对象数组按照数值形式进行排序
	 * 
	 * @param userArray
	 *            需要进行排序的对象数组
	 */
	public static void sort(Object[] userArray) {
		ShellSort s = new ShellSort();
		if (userArray == null)
			return;
		s.userArray = userArray;
		if (s.isDoubleSorted())
			return;
		// ShellSort proper
		// h is the separation between items we compare.
		int h = 1;
		while (h < userArray.length) {
			h = 3 * h + 1;
		}

		while (h > 0) {
			h = (h - 1) / 3;
			for (int i = h; i < userArray.length; ++i) {
				System.out.println("userArray.length" + userArray.length);
				Object item = userArray[i];
				int j = 0;
				for (j = i - h; j >= 0 && intcompare(item, userArray[j]) < 0; j -= h) {
					userArray[j + h] = userArray[j];
				} // end inner for
				userArray[j + h] = item;
			} // end outer for
		} // end while

		if (DEBUGGING) {
			if (!s.isDoubleSorted())
				System.out.println("Sort failed");
		}
	} // end sort

	/**
	 * 对于对象数组按照字符串进行排序
	 * 
	 * @param userArray
	 *            需要进行排序的对象数组
	 * @param delegate
	 * 
	 */
	public static void sort(Object[] userArray, Comparator delegate) {
		ShellSort s = new ShellSort();
		if (userArray == null)
			return;
		s.userArray = userArray;
		s.delegate = delegate;
		if (s.isAlreadySorted())
			return;
		// ShellSort proper
		// h is the separation between items we compare.
		int h = 1;
		while (h < userArray.length) {
			h = 3 * h + 1;
		}

		while (h > 0) {
			h = (h - 1) / 3;
			for (int i = h; i < userArray.length; ++i) {
				Object item = userArray[i];
				int j = 0;
				for (j = i - h; j >= 0
						&& delegate.compare(item, userArray[j]) < 0; j -= h) {
					userArray[j + h] = userArray[j];
				} // end inner for
				userArray[j + h] = item;
			} // end outer for
		} // end while

		if (DEBUGGING) {
			if (!s.isAlreadySorted())
				System.out.println("Sort failed");
		}
	} // end sort

	// callback object we are passed that has
	// a compare(Object a, Object b) method.
	private Comparator delegate;

	// pointer to the array of user's objects we are sorting
	private Object[] userArray;

	// check if user's array is already sorted
	private boolean isAlreadySorted() {
		for (int i = 1; i < userArray.length; i++) {
			try {
				if (delegate.compare(userArray[i], userArray[i - 1]) < 0)
					return false;
			} catch (NumberFormatException e) {
				System.out.println("NumberFormatException==>" + e.toString());
				return false;
			}
		}
		return true;
	} // end isAlreadySorted

} // end class ShellSort

