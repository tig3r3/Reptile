package com.syntun.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class GetChannel {
	public static void main(String[] args) throws Exception {
		// write test 覆盖原来的数据
		FileChannel fc = new FileOutputStream("D:/test.txt").getChannel();
		fc.write(ByteBuffer.wrap("test".getBytes()));
		fc.close();
		// 可以追加到上面写入后的文件，不能追加到原来文件中有内容的文件
		fc = new RandomAccessFile("D:/test.txt", "rw").getChannel();
		fc.position(fc.size());
		fc.write(ByteBuffer.wrap("set11".getBytes()));
		fc.close();
		// 读文件
		fc = new FileInputStream("D:/test.txt").getChannel();
		ByteBuffer buff = ByteBuffer.allocate(1024);
		fc.read(buff);
		buff.flip();
		while (buff.hasRemaining()) {
			System.out.println((char) buff.get());
		}

	}
}
