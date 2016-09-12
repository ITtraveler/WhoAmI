package com.tc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import android.os.Environment;

/**
 * 此类封装了sdCard目标文件的操作
 * 
 * @author hgs
 *
 */
public class FileUtils {
	public static final String TAG_NAME0 = "behaviour_";
	public static final String TAG_NAME1 = "report_";
	public static final String FOLDER = "/WhoAmI";
	public static final String CHILDFOLDER1 = "/behaviour";
	public static final String CHILDFOLDER2 = "/report";
	public static final String BEHAVIOURPATH = FOLDER + CHILDFOLDER1;
	public static final String REPORTPATH = FOLDER + CHILDFOLDER2;
	public static final int PATHID_0 = 0;
	public static final int PATHID_1 = 1;

	public static boolean existsCheck = false;

	/**
	 * 将内容写到文件中
	 * 
	 * @param relativePath
	 *            相对路径可以由 generatePath得到
	 * @param content
	 * @param fileName
	 */
	public static void writeFile(String relativePath, String tagName,
			String content, String username) {
		// 判断是否存在SD卡
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd卡目录
			File sdCardir = Environment.getExternalStorageDirectory();
			try {
				System.out.println(sdCardir.getAbsolutePath());
				File file = new File(sdCardir.getCanonicalPath() + relativePath);
				// 判断文件是否存在，不存在则创建此文件
				if (!existsCheck)
					if (!file.exists()) {
						file.mkdirs();// mkdir自能单级目录，mkdir可以多级目录
					}
				File targetFile = new File(file, tagName + username);
				RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
				raf.seek(targetFile.length());
				raf.write(content.getBytes());
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param relativePath
	 */
	public static void createFile(String relativePath) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd卡目录
			File sdCardir = Environment.getExternalStorageDirectory();

			try {
				File file = new File(sdCardir.getCanonicalPath() + relativePath);
				// 判断文件是否存在，不存在则创建此文件
				if (!existsCheck)
					if (!file.exists())
						file.mkdirs();// mkdir自能单级目录，mkdir可以多级目录
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 读取文件的内容
	 * 
	 * @param relativePath
	 *            相对路径可以由 generatePath得到
	 * @param fileName
	 * @return
	 */
	public static String readFile(String relativePath, String tagName,
			String username) {
		String res = "";
		FileInputStream fis;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File sdCardir = Environment.getExternalStorageDirectory();
			try {
				fis = new FileInputStream(sdCardir.getCanonicalPath()
						+ relativePath + "/" + tagName + username);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				res = sb.toString().trim();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * 删除文件
	 * 
	 * @param relativePath
	 *            相对路径可以由 generatePath得到
	 * @param fileName
	 */
	public static void deleteFile(String relativePath, String fileName) {
		// sd卡目录
		File sdCardir = Environment.getExternalStorageDirectory();
		try {
			System.out.println(sdCardir.getAbsolutePath());
			File file = new File(sdCardir.getCanonicalPath() + relativePath);
			File targetFile = new File(file, fileName);
			targetFile.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param relativePath
	 * @return
	 */
	public static String[] getAllFileName(String relativePath) {
		String[] s;
		File sdCardir = Environment.getExternalStorageDirectory();
		File file = new File(sdCardir.getAbsoluteFile() + relativePath);
		File[] fs = file.listFiles();
		if(fs == null){
			System.out.println("null................null");
			return null;
		}
		s = new String[fs.length];
		int i = 0;
		for (File f : fs) {
			s[i] = f.getName().toString().trim();
			i++;
		}
		return s;
	}

	/**
	 * 检测文件的存在性，以防止文件重复
	 * @param relativePath
	 * @param tagName
	 * @param username
	 * @return
	 */
	public static boolean chackFileExist(String relativePath, String tagName,
			String username) {
		//String fileName = tagName + username;
		String fNames[] = getAllFileName(relativePath);
		if(fNames == null) return false;
		if (fNames.length > 0) {
			for (String name : fNames) {
				boolean result = StringUtils.compare(name, tagName + username);
				if (result) {// if compare return true,so this file is exist;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * 用于删除文本中与内容匹配的字符串
	 * 
	 * @param relativePath
	 *            相对路径可以由 generatePath得到
	 * @param tagName
	 *            文件的一个标识
	 * @param content
	 * @param username
	 * 
	 */
	public static void deleteContent(String relativePath, String tagName,
			String content, String username) {
		String fileContent = readFile(relativePath, tagName, username);
		fileContent.replaceAll(content, "");
		writeFile(relativePath, tagName, fileContent, username);
	}

	/**
	 * 
	 * 产生相对路径
	 * 
	 * @param pathID
	 * @return
	 */
	public static String generatePath(int pathID) {
		String relativePath = "";
		switch (pathID) {
		case PATHID_0:
			relativePath = BEHAVIOURPATH;
			break;
		case PATHID_1:
			relativePath = REPORTPATH;
			break;
		}
		return relativePath;
	}
}
