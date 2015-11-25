package com.syntun.tools;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import com.syntun.wetget.annotation.SetProperty;

/**
 * 
 */
public class SetDefaultProperty {
	/**
	 * 需要遍历的包列表
	 */
	private static List<String> packageList = new ArrayList<String>();
	/**
	 * 配置文件路径
	 */
	private static final String PROPERTY_FILE_NAME = "./conf.Properties";
	/**
	 * 
	 */
	private static Properties p;
	/**
	 * 配置文件写入
	 */
	private static FileWriter fw;

	private static boolean isMysqlLocal = false;

	private static String localIp;

	static {
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			boolean isfined = false;
			while (netInterfaces.hasMoreElements() && !isfined) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ei = ni.getInetAddresses();
				while (ei.hasMoreElements()) {
					InetAddress ia = ei.nextElement();
					String[] localhostIpArr = ia.getHostAddress().split("\\.");
					if (localhostIpArr.length == 4
							&& (localhostIpArr[0] + localhostIpArr[1])
									.equals("192168")) {
						localIp = ia.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("获取本地ip失败");
		}
	}

	/**
	 * 获取属性值
	 * 
	 * @param PropertyName
	 * @return
	 */
	public static synchronized String getProperty(String PropertyName)
			throws Exception {
		if (p == null) {
			throw new Exception("系统配置文件未加载");
		}
		return p.getProperty(PropertyName);
	}

	public static boolean getIsMysqlLocal() {
		if (p == null) {
			try {
				throw new Exception("系统配置文件未加载");
			} catch (Exception e) {
				System.err.println("配置文件未加载");
				System.exit(0);
			}
		}
		return isMysqlLocal;
	}

	/**
	 * 获取本地ip
	 * 
	 * @return
	 */
	public static String getLocalIp() {
		return localIp;
	}

	/**
	 * 为列表赋值
	 * 
	 * @throws IOException
	 */
	private static void setProperty() throws IOException {
		p = new Properties();
		FileInputStream fi = new FileInputStream(PROPERTY_FILE_NAME);
		p.load(fi);
		fi.close();
		// 遍历包中的类
		Map<Class<?>, SetProperty> classPoropertyMap = getPropertyClass(packageList);
		boolean isLoadSuccess = true;
		fw = new FileWriter(PROPERTY_FILE_NAME);
		for (Iterator<Entry<Class<?>, SetProperty>> iter = classPoropertyMap
				.entrySet().iterator(); iter.hasNext();) {
			Entry<Class<?>, SetProperty> e = iter.next();
			Class<?> pClass = e.getKey();
			SetProperty sp = e.getValue();
			String[] varNames = sp.propertyVars();
			fw.write("#" + sp.propertyComment() + "\n");
			for (int i = 0; i < varNames.length; i++) {
				String value = setClassPropertyVal(pClass, varNames[i]);
				if (value == null) {
					isLoadSuccess = false;
					value = "";
				}
				fw.write(pClass.getName() + "." + varNames[i] + "=" + value
						+ "\n");
				System.out.println(pClass.getName() + "." + varNames[i] + "="
						+ value);
			}
		}
		fw.close();
		fw = null;
		if (!isLoadSuccess) {
			throw new IOException("配置文件加载失败！");
		}
	}

	/**
	 * 为类需要设置的变量进行赋值
	 */
	private static String setClassPropertyVal(Class<?> pClass,
			String propertyVarName) {
		String valueStr = null;
		try {
			Field pf = pClass.getDeclaredField(propertyVarName);
			pf.setAccessible(true);
			String pName = pClass.getName() + "." + propertyVarName;
			if (p.containsKey(pName)) {
				Class<?> typeClass = getClass(pf.getType());
				Constructor<?> con = typeClass.getConstructor(String.class);
				Object value = con.newInstance(p.get(pName));
				pf.set(null, value);
			} else {
				if (pf.get(null) == null)
					System.err.println(pName + "：没有默认值，也未在配置文件中进行配置");
			}
			valueStr = pf.get(null) != null ? pf.get(null).toString() : null;
		} catch (Exception e) {
			System.out.println(propertyVarName);
			e.printStackTrace();
			System.exit(0);
		}
		return valueStr;
	}

	/**
	 * 获取参数类型class
	 * 
	 * @return
	 */
	private static Class<?> getClass(Class<?> sClass) {
		String className = sClass.getName();
		Class<?> c = null;
		if (className == "int")
			c = Integer.class;
		else if (className == "short")
			c = Short.class;
		else if (className == "long")
			c = Long.class;
		else if (className == "double")
			c = Double.class;
		else if (className == "float")
			c = Float.class;
		else if (className == "boolean")
			c = Boolean.class;
		else if (className == "byte")
			c = Byte.class;
		else if (className == "boolean")
			c = Boolean.class;
		else if (className == "java.util.Date")
			c = java.util.Date.class;
		else if (className == "java.lang.String")
			c = sClass;
		return c;
	}

	/**
	 * 或取所有需要设置的类
	 * 
	 * @param packageList
	 * @return
	 */
	private static Map<Class<?>, SetProperty> getPropertyClass(
			List<String> packageList) {
		Map<Class<?>, SetProperty> classPoropertyMap = new HashMap<Class<?>, SetProperty>();
		List<Class<?>> classList = GetAllClass.getAllClassList();
		for (int j = 0; j < classList.size(); j++) {
			try {
				Class<?> packClass = classList.get(j);
				SetProperty a = packClass.getAnnotation(SetProperty.class);
				if (a != null) {
					classPoropertyMap.put(packClass, a);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return classPoropertyMap;
	}

	public static void loadProperty() {
		try {
			SetDefaultProperty.setProperty();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		// 本程序是否和数据库在同一主机
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			boolean isfined = false;
			String mysqlHost = SetDefaultProperty.getProperty(
					"com.syntun.tools.ConnectSql.sqlHost").trim();
			while (netInterfaces.hasMoreElements() && !isfined) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ei = ni.getInetAddresses();
				while (ei.hasMoreElements()) {
					InetAddress ia = ei.nextElement();
					if (ia.getHostAddress().equals(mysqlHost)) {
						isfined = true;
						isMysqlLocal = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		loadProperty();

	}

}
