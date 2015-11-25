package com.syntun.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.codec.binary.Base64;

public class ZipTools {
	
	public static void main(String[] args) {
		System.out.println(unzip("UEsDBBQACAAIAA9QZEcAAAAAAAAAAAAAAAABAAAAMH1WS28jRRD+L3020Yxjjx+3JM4iS3HWxIbLag/tmbbdpKd76Ie9VmRp4cAB7e4BDrkgIW4rwQEJAdFy4M/EK/IvqO4ez4xjh0ucrq6u5/dVzQ1KyBQbpgciIQx1b+DM6ILIVe+5PWFJcD9B3TAMwiCoFbcnSSKJUqiL7u+++/jHX6i8Gl2bAc68KWcadV9YQ5IuMLskr3QPr1B3ipkiNZQJpfGMgJ3NP788vP6+GxwFwflg1G3CLyrun0lCijfq2vSeDkOvMlAN1i/XNiSlKceaCm49vLm9//Dr5u4b0FImjt1DLQ1xmhpTNgRXY5opn3ssxTIZe3M1NKcJ6ceCqyKOL+eq0ylOKeU0xlKPjMwkVf4R/BeTHlUZwytvqeGEKZarIY1zey9eemElxblY9oQJw5FesUNiMxtKkQ6t+b3b+pmQnEhrvLyr5gtFgr89I8LQWsmVbBWEmTByzsiC8HoQNlwTK7K+Jmlhco7Z9Pl0uiOzIZzoVKhsTuRuZFd0NtdXJBZpSnhCkhNAVqmgSeYK9rgvM6zJEudgjOeYz8iFiF1HP/VX9uIrYwGbY2KboxPu5rdXBZwQh/IkGYtTs7oUSziCohOcQTftcQ0CyiF9Lba0ADAkpKwujcdCY/aZwVxTDRGErabDaSm5QfV61IqisBW0Wj7mHWV9wICHcrg+EPn/aEM40BIHjSuigH9PMRm4RfX5qwzzZAy0BIZYFltU7PTUSUaepfAONCW2ZR3ZphLMdkF4CBVzrHr2hfBELKUDMaGM+B75xCzLBmC0HBDWep9PxYES2pRGgiXbx5mPBB138uGx9XlBVT6FUmG4tg4vaEq1S8jLoFQanuejAh5PsCIuNc9b1I99ojUUYw5YORMmE/zSpL4qUENH4LARNVrNehRGQVCtVl4lVG8fRTa24iIvfb29L/aew6rUSY5riNnwK+A+PCmz0ml7W5A8HS5kCmW21HMwL4gIp20mQbvd7rQ6xy4TuNEGAFgHpE3P3KDLMlF5qsEeO3jhPW7e/r757duPf76//9vOacMvxOxUAvgGJJ0QWai7CwBX9gXNdoXjyaIQwXxf73N8f4CkzrgbPjkPoMcLYE0x2O1kY35aH1o2EMijAB9z0VerhmC9GWvyqGn9UhV7hxMTXxNtqQezP3Z8zVcR8ucqK4EUHliFN0lmHpP3dz9ufnq3efMBbYXFQ3CuYKXoU8xh8D9jeAb6WmSPFx0cVzBqU99gFDYaURQAVhvhsYWHFsuto7fvHm7f//vz7cPXP+QXpbMgsEMGsDSzuxdmrqWnz9UvC7tWoMaM2JHxCQTHyXJ41udQXcZg+uuSsIWNc44nlU0nYbj7r4i1Xx/DUnXI8NOLjWa+sdatlqtTMhWSAF0rS4AATC3lfcjFEXWj5j5+FMwXu6p6WOP8wSMNt0X8pHVLCvqwTcbHZCU+qLr/TBhvmbKrt8O9RtSEvjQbnnsH9lb1LURhFGz8og8O5GSUU9aCXMwo/xx0cvpPzAoQvWfZWUns55NdI2q0k+p/UEsHCGaxgap5BAAALAoAAA==;"));
	}
	
	/**
	 * 使用zip进行压缩
	 * @param str
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static final String zip(String str) {
		if (str == null)
			return null;
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = new String(new Base64().encode(compressed));
		} catch (IOException e) {
			compressed = null;
		} finally {
			if (zout != null) {
				try {
					zout.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 * 
	 * @param compressed
	 *            压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = new Base64().decode(compressedStr.getBytes());
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}
}
