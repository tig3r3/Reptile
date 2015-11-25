package wg_all;

import com.syntun.putdata.InsertData;

public class sort_name_list extends InsertData{
	public String replaceDecode(String value) {
		return decode(value);
	}
	
	
	
	/**
     * 转化\u4e00
     * @param in
     * @return
     */
    public static String decode(CharSequence in) {
        StringBuilder out = new StringBuilder();
        char aChar;
        for (int i = 0, n = in.length(); i < n; i++) {
            aChar = in.charAt(i);
            if (aChar == '\\') {
                aChar = in.charAt(++i);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int inner = 0; inner < 4; inner++) {
                        aChar = in.charAt(++i);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed \\uxxxx encoding.");
                        }
                    }
                    out.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    out.append(aChar);
                }
            } else {
                out.append((char) aChar);
            }
        }
        return out.toString();
    }
}
