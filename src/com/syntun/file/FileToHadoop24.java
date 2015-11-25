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

import com.syntun.webget.ResolveWebPage;
import com.syntun.webget.UrlSupervise;

/**
 * 基于hadoop2.4的插入类
 * 
 * @author tuo
 * 
 */
public class FileToHadoop24 {
	public boolean fileToDb(ContentFile contentFile) {
		return filetoHadoop(contentFile.getFileName(),
				contentFile.getFileName());
	}

	public boolean filetoHadoop(String localFile, String upFile) {
		if (UrlSupervise.isFirst == 0 || UrlSupervise.isUpload == 0) {
			return true;
		}
		// return true;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					localFile));
			Configuration conf = new Configuration();
			conf.set("fs.defaultFS", "hdfs://myhadoop");
			conf.set("dfs.nameservices", "myhadoop");
			conf.set("dfs.ha.namenodes.myhadoop", "nn1,nn2");
			conf.set("dfs.namenode.rpc-address.myhadoop.nn1",
					"namenode30.shadoop.com:8020");
			conf.set("dfs.namenode.rpc-address.myhadoop.nn2",
					"namenode31.shadoop.com:8020");
			conf.set("dfs.client.failover.proxy.provider.myhadoop",
					"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

			// conf.set("fs.default.name", "hdfs://namenode30.shadoop.com");
			// conf.set("mapred.job.tracker", "http://namenode30.shadoop.com");
			upFile = "/"
					+ ResolveWebPage.upFile
					+ localFile.substring(localFile.indexOf("/20"),
							localFile.length());
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
		ResolveWebPage.upFile = "test1";
		FileToHadoop24 fh = new FileToHadoop24();
		fh.filetoHadoop(
				"C:/Users/tuo/Desktop/2014-08-11-11/200_550_a3e8697d-bf1e-440a-8dc4-c8d6f3965438103_0.txt",
				"/test1/t.txt");
	}

}
