package com.syntun.file;

import java.io.File;

public class LocalFileToHadoop {
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("args: localPath updatePath likePath");
			System.exit(0);
		}
		String dataStr = args[1];
		File f = new File(args[0]);
		String upFile = "/" + dataStr + "/";
		FileToHadoop fh = new FileToHadoop();
		for (File f1 : f.listFiles())
			if (f1.listFiles() != null)
				for (File f2 : f1.listFiles()) {
					System.out.println(f2.getPath() + "~~~~~`" + args[1]);
					if (!f2.getPath().contains(args[2])) {
						continue;
					}
					String localPath = f2.getPath();
					String dateStr = localPath.split("/")[4];
					System.out.println(localPath + "~~~~~`" + upFile + dateStr);
					fh.filetoHadoop(localPath, upFile + dateStr + "/"
							+ localPath.split("/")[5]);
					System.out.println("上传成功：" + localPath);
				}
	}
}