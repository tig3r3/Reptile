package com.syntun.replace;

import java.util.LinkedList;

import com.syntun.webget.ResolveWebPage;

public class SRreplace implements replaceParameter {

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		String[] pArr = exeParam.split("-");
		String rpGroup = pArr[0];
		String rpStr = pArr[1];
		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();
		if (ResolveWebPage.replaceMaps.containsKey(rpGroup)) {
			if (ResolveWebPage.replaceMaps.get(rpGroup).get(rpStr) != null) {
				ll.add(new ParameInfo(ResolveWebPage.replaceMaps.get(rpGroup)
						.get(rpStr), ResolveWebPage.replaceMaps.get(rpGroup)
						.get(rpStr)));
			} else {
				ll.add(new ParameInfo(rpStr, rpStr));
			}
		} else {
			return null;
		}
		return ll;
	}
}
