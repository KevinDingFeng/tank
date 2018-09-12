package com.shenghesun.tank.http.utils;

import java.io.IOException;
import java.io.InputStream;

public class StringInstreamUtil {

	public static String getInputString(InputStream instream){
		byte[] tmp = new byte[2048];
		StringBuilder bu = new StringBuilder();
		try {
			while ((instream.read(tmp)) != -1) {
				bu.append(new String(tmp));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		System.out.println(bu.toString());
		return bu.toString();
	}
}
