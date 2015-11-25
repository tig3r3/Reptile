package com.syntun.replace;

import java.util.LinkedList;

public class RDreplace implements  replaceParameter{

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		exeParam = exeParam.replace("==", "%253D%253D");
		LinkedList<ParameInfo> list = new LinkedList<ParameInfo>();
		ParameInfo parameInfo = new ParameInfo(exeParam, exeParam);
		list.add(parameInfo);
		return list;
	}

}
