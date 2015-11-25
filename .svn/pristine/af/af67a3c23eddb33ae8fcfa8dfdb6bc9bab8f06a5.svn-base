package wgdata_tmall;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringEscapeUtils;

import com.syntun.putdata.InsertData;


public class sort_product_list extends InsertData {
	public static void main(String[] args){
		String s="%E7%BA%A2%E8%B1%86%E5%AE%98%E6%96%B9%E6%97%97%E8%88%B0%E5%BA%97";
		System.out.println(new sort_product_list().replaceISO(s));
//		System.out.println(Utf8URLdecode(s));
	}
	public String replaceISO(String colValue) {
		colValue=Utf8URLdecode(colValue).trim();
		colValue=new InsertData().replaceUnicode(colValue);
		colValue= StringEscapeUtils.unescapeHtml(StringEscapeUtils.unescapeHtml(colValue));//转译html编码
		colValue=colValue.replace("&amp;", "")
				         .replace(";", "");
		return colValue;
	}

	/*
	 * 转化%E6%97%97%E8%88%B0%E5%BA%97这种类型的编码
	 */
	public static String Utf8URLdecode(String text) {
		String result = "";
		int p = 0;
		if (text != null && text.length() > 0) {
			text = text.toLowerCase();
			p = text.indexOf("%e");
			if (p == -1)
				return text;
			while (p != -1) {
				result += text.substring(0, p);
				text = text.substring(p, text.length());
				if (text == "" || text.length() < 9)
					return result;
				result += CodeToWord(text.substring(0, 9));
				text = text.substring(9, text.length());
				p = text.indexOf("%e");
			}
		}
		return result + text;
	}
	/**
	 * utf8URL编码转字符
	 * 
	 * @param text
	 * @return
	 */
	private static String CodeToWord(String text) {
		String result;
		if (Utf8codeCheck(text)) {
			byte[] code = new byte[3];
			code[0] = (byte) (Integer.parseInt(text.substring(1, 3), 16) - 256);
			code[1] = (byte) (Integer.parseInt(text.substring(4, 6), 16) - 256);
			code[2] = (byte) (Integer.parseInt(text.substring(7, 9), 16) - 256);
			try {
				result = new String(code, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				result = null;
			}
		} else {
			result = text;
		}
		return result;
	}
	/**
	 * 编码是否有效
	 * 
	 * @param text
	 * @return
	 */
	private static boolean Utf8codeCheck(String text) {
		String sign = "";
		if (text.startsWith("%e"))
			for (int i = 0, p = 0; p != -1; i++) {
				p = text.indexOf("%", p);
				if (p != -1)
					p++;
				sign += p;
			}
		return sign.equals("147-1");
	}

}
