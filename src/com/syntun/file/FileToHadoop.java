package com.syntun.file;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import com.syntun.webget.UrlSupervise;

/**
 * 基于hadoop1.0上传文件
 */
public class FileToHadoop {

	public static String dataStr = "data";

	public FileToHadoop() {
	}

	public boolean fileToDb(ContentFile contentFile) {
		return filetoHadoop(contentFile.getFileName(),
				contentFile.getFileName());
	}

	public boolean filetoHadoop(String localFile, String upFile) {
		if (UrlSupervise.isFirst == 0) {
			return true;
		}
		// return true;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					localFile));
			Configuration conf = new Configuration();
			conf.set("fs.default.name", "hdfs://namenode20:9000");
			conf.set("mapred.job.tracker", "namenode20:9001");
			upFile = "/"
					+ dataStr
					+ upFile.substring(upFile.indexOf("/2014"), upFile.length());
			FileSystem fs = FileSystem.get(URI.create(upFile), conf);
			OutputStream out = fs.create(new Path(upFile));
			IOUtils.copyBytes(in, out, 4068, true);
			System.out.println("success");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		FileToHadoop fh = new FileToHadoop();
		fh.filetoHadoop("D:/tuo/new221.txt", "/data/testtuo/test.txt");
	}

}
