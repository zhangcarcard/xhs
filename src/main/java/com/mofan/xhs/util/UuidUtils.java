package com.mofan.xhs.util;

import java.util.UUID;

/**
 * 
 * @author zk
 *
 */
public class UuidUtils {
	private UuidUtils() {
		throw new AssertionError("No com.mofan.xhs.util.util.UuidUtils instances for you!");
	}

	/**
	 * 生成UUID.
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}
}
