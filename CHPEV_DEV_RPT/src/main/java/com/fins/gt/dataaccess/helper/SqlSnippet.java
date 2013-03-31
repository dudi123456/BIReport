package com.fins.gt.dataaccess.helper;

import java.util.Map;

@SuppressWarnings({ "rawtypes" })
public class SqlSnippet {

	public String snippet;
	public String condition;
	public String content;
	public int start;
	public int end;

	public String type;

	public String condition_before;
	public String condition_c;
	public String condition_after;

	public Object value;

	public boolean need = false;

	public SqlSnippet() {

	}

	public String toString() {
		StringBuffer bufs = new StringBuffer();
		bufs.append("===== ").append(this.getClass().getName())
				.append(" =====\n");
		bufs.append("snippet : ").append(snippet).append("\n");
		bufs.append("condition : ").append(condition).append("\n");
		bufs.append("condition_ex : ").append(condition_before)
				.append(condition_c).append(condition_after).append("\n");
		bufs.append("content : ").append(content).append("\n");
		bufs.append("start : ").append(start).append("\n");
		bufs.append("end : ").append(end).append("\n");
		return bufs.toString();
	}

	public void init() {
		if (condition.indexOf(DataAccessConstants.NOTEQUAL_S) > 0) {
			condition_c = DataAccessConstants.NOTEQUAL_S;
		} else if (condition.indexOf(DataAccessConstants.LESSEQUAL_S) > 0) {
			condition_c = DataAccessConstants.LESSEQUAL_S;
		} else if (condition.indexOf(DataAccessConstants.GREATEQUAL_S) > 0) {
			condition_c = DataAccessConstants.GREATEQUAL_S;
		} else if (condition.indexOf(DataAccessConstants.EQUAL_S) > 0) {
			condition_c = DataAccessConstants.EQUAL_S;
		} else if (condition.indexOf(DataAccessConstants.LESS_S) > 0) {
			condition_c = DataAccessConstants.LESS_S;
		} else if (condition.indexOf(DataAccessConstants.GREAT_S) > 0) {
			condition_c = DataAccessConstants.GREAT_S;
		}

		String[] ba = condition.split(condition_c);
		condition_before = ba[0];
		condition_after = ba[1];

	}

	public boolean check(Map map) {
		String type = "string";
		value = map.get(condition_before);

		String beforeS = value == null ? null : String.valueOf(value);
		String afterS = condition_after;

		long beforeL = -1;
		long afterL = -1;

		if (condition_after.indexOf("'") < 0
				&& condition_after.indexOf("\"") < 0) {
			try {
				afterL = new Long(condition_after).longValue();
				type = "number";
			} catch (Exception e) {
				type = "string";
			}
		}
		if ("number".equalsIgnoreCase(type)) {
			try {
				beforeL = new Long(beforeS).longValue();
				type = "number";
			} catch (Exception e) {
				type = "string";
			}
		}

		boolean need = false;
		this.type = type;

		if ("number".equalsIgnoreCase(type)) {
			if (condition.indexOf(DataAccessConstants.NOTEQUAL_S) > 0) {
				need = DataAccessUtils.isNotEquals(beforeL, afterL);
			} else if (condition.indexOf(DataAccessConstants.LESSEQUAL_S) > 0) {
				need = DataAccessUtils.isLessThenE(beforeL, afterL);
			} else if (condition.indexOf(DataAccessConstants.GREATEQUAL_S) > 0) {
				need = DataAccessUtils.isGreatThenE(beforeL, afterL);
			} else if (condition.indexOf(DataAccessConstants.EQUAL_S) > 0) {
				need = DataAccessUtils.isEquals(beforeL, afterL);
			} else if (condition.indexOf(DataAccessConstants.LESS_S) > 0) {
				need = DataAccessUtils.isLessThen(beforeL, afterL);
			} else if (condition.indexOf(DataAccessConstants.GREAT_S) > 0) {
				need = DataAccessUtils.isGreatThen(beforeL, afterL);
			}
		} else {
			if (condition.indexOf(DataAccessConstants.NOTEQUAL_S) > 0) {
				if (afterS.equalsIgnoreCase(DataAccessConstants.C_EMPTY)) {
					need = DataAccessUtils.isNotEmpty(beforeS);
				} else {
					if (afterS.equalsIgnoreCase(DataAccessConstants.C_NULL)) {
						afterS = null;
					}
					need = DataAccessUtils.isNotEquals(beforeS, afterS);
				}
			} else if (condition.indexOf(DataAccessConstants.LESSEQUAL_S) > 0) {
				need = DataAccessUtils.isLessThenE(beforeS, afterS);
			} else if (condition.indexOf(DataAccessConstants.GREATEQUAL_S) > 0) {
				need = DataAccessUtils.isGreatThenE(beforeS, afterS);
			} else if (condition.indexOf(DataAccessConstants.EQUAL_S) > 0) {
				if (afterS.equalsIgnoreCase(DataAccessConstants.C_EMPTY)) {
					need = DataAccessUtils.isEmpty(beforeS);
				} else {
					if (afterS.equalsIgnoreCase(DataAccessConstants.C_NULL)) {
						afterS = null;
					}
					need = DataAccessUtils.isEquals(beforeS, afterS);
				}
			} else if (condition.indexOf(DataAccessConstants.LESS_S) > 0) {
				need = DataAccessUtils.isLessThen(beforeS, afterS);
			} else if (condition.indexOf(DataAccessConstants.GREAT_S) > 0) {
				need = DataAccessUtils.isGreatThen(beforeS, afterS);
			}
		}
		this.need = need;
		return need;
	}
}
