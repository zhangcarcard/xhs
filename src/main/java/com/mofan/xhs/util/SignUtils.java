package com.mofan.xhs.util;

import com.mofan.xhs.constant.XhsConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

/**
 * 
 * @author zk
 *
 */
public class SignUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignUtils.class);
	private static final String DEFAULT_CHARSET_NAME = "UTF-8";

	private SignUtils() {
		throw new AssertionError("No com.mofan.xhs.util.util.SignUtils instances for you!");
	}

	/**
	 * url签名.
	 * @param queryString
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String sign(String queryString, String key) {
		try {
			String[] arr = queryString.split("&");
			Arrays.sort(arr);
			StringBuilder contentBuilder = new StringBuilder(512);
			for (String p : arr) {
				contentBuilder.append(URLDecoder.decode(p, DEFAULT_CHARSET_NAME));
			}

			String content = contentBuilder.toString();
			content = URLEncoder.encode(content, DEFAULT_CHARSET_NAME);
			content = content.replaceAll("%5F", "_");
			content = content.replaceAll("%20", "+");

			byte[] bs = content.getBytes(DEFAULT_CHARSET_NAME);
			int bslen = bs.length;
			StringBuilder tmpBuilder = new StringBuilder(bslen);
			while (tmpBuilder.length() < bslen) {
				tmpBuilder.append(key);
			}

			String tmp = tmpBuilder.substring(0, bslen);
			byte[] tmpbs = tmp.getBytes(DEFAULT_CHARSET_NAME);

			StringBuilder builder = new StringBuilder(bslen);
			for (int i = 0; i < bslen; ++i) {
				builder.append(Integer.toString(bs[i] ^ tmpbs[i]));
			}

			String fnl = builder.toString();
			String ret = MD5Utils.stringMD5(MD5Utils.stringMD5(fnl).toLowerCase() + key).toLowerCase();
			return ret;
		} catch (Exception e) {
			LOGGER.error("URL签名时出错", e);
			throw new RuntimeException("URL签名时出错");
		}
	}
}
