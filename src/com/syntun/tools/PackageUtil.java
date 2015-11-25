package com.syntun.tools;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.jar.JarEntry;  
import java.util.jar.JarInputStream; 

public class PackageUtil {

	private static String[] CLASS_PATH_PROP = { "java.class.path", "java.ext.dirs","sun.boot.class.path" };  
	private static List<File> CLASS_PATH_ARRAY = getClassPath();
	 public static List<Class<?>> getClasses(String pckgName) throws ClassNotFoundException, IOException { 
		 ArrayList<Class<?>> classes = new ArrayList<Class<?>>(); 
	      String rPath = pckgName.replace('.', '/') + "/";  
	      try {  
	          for (File classPath : CLASS_PATH_ARRAY) {
	            if(!classPath.exists()) continue;
	            if (classPath.isDirectory()) {
	              File dir = new File(classPath, rPath);  
	              if(!dir.exists()) continue;  
	              for (File file : dir.listFiles()) {  
	                  if (file.isFile()) {  
	                    String clsName = file.getName();  
	                    clsName = pckgName+"." +clsName.substring(0, clsName.length() - 6);
//	                    System.out.println(clsName);
	                    classes.add(Class.forName(clsName));  
	                  }  
	              }  
	            } else {  
	              FileInputStream fis = new FileInputStream(classPath);  
	              JarInputStream jis = new JarInputStream(fis, false);  
	              JarEntry e = null;  
	              while ((e = jis.getNextJarEntry()) != null) {  
	                  String eName = e.getName();  
	                  if (eName.startsWith(rPath) && !eName.endsWith("/")) {
	                	  classes.add(Class.forName(eName.replace('/', '.').substring(0,eName.length()-6)));  
	                  }  
	                  jis.closeEntry();  
	              }  
	              jis.close();  
	            }  
	          }  
	      } catch (Exception e) {  
	          throw new RuntimeException(e);  
	      }  
	 
	      return classes; 
	 }
	 
	 private static List<File> getClassPath() {  
	      List<File> ret = new ArrayList<File>();  
	      String delim = ":";  
	      if (System.getProperty("os.name").indexOf("Windows") !=-1)  
	          delim = ";";  
	      for (String pro : CLASS_PATH_PROP) {  
	          String[] pathes = System.getProperty(pro).split(delim);  
	          for (String path : pathes)  
	            ret.add(new File(path));  
	      }  
	      return ret;  
	    }
}
