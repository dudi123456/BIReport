package com.ailk.bi.common.app;

/**
 * <p>Title: iBusiness</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author 
 * @version 1.0
 */

/**
 * This class is just a helper class to make it handy to print out debug
 * statements
 */
public final class Debug {

	public static final boolean debuggingOn = false;

	public static void myAssert(boolean condition) {
		if (!condition) {
			println("Assert Failed: ");
			throw new IllegalArgumentException();
		}
	}

	public static void print(String msg) {
		if (debuggingOn) {
			com.ailk.bi.common.app.Debug.print(msg);
		}
	}

	public static void println(String msg) {
		if (debuggingOn) {
			com.ailk.bi.common.app.Debug.println(msg);
		}
	}

	public static void print(Exception e, String msg) {
		print((Throwable) e, msg);
	}

	public static void print(Exception e) {
		print(e, null);
	}

	public static void print(Throwable t, String msg) {
		if (debuggingOn) {
			com.ailk.bi.common.app.Debug
					.println("Received throwable with Message: "
							+ t.getMessage());
			if (msg != null)
				com.ailk.bi.common.app.Debug.print(msg);
			t.printStackTrace();
		}
	}

	public static void print(Throwable t) {
		print(t, null);
	}
}
