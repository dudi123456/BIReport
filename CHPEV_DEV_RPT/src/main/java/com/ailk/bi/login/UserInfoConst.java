package com.ailk.bi.login;

public class UserInfoConst {
	private UserInfoConst() {
	}

	public static enum USER_STATE {
		LOCKED(99), NORMAL(0), ONE_ERR(1), TWO_ERR(2);
		private final int state;

		private USER_STATE(int state) {
			this.state = state;
		}

		public int getState() {
			return state;
		}
	}
}
