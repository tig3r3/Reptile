package com.syntun.replace;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;

public class U2replace implements replaceParameter {

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();
		try {
			ll.add(new ParameInfo("pinpaiSearchU2",URLEncoder.encode(URLEncoder.encode(exeParam,"UTF-8"),"UTF-8")));
		} catch (UnsupportedEncodingException e) {
			ll=null;
		}
		return ll;
	}

}
