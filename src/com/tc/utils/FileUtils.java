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
 * �����װ��sdCardĿ���ļ��Ĳ���
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
	 * ������д���ļ���
	 * 
	 * @param relativePath
	 *            ���·�������� generatePath�õ�
	 * @param content
	 * @param fileName
	 */
	public static void writeFile(String relativePath, String tagName,
			String content, String username) {
		// �ж��Ƿ����SD��
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd��Ŀ¼
			File sdCardir = Environment.getExternalStorageDirectory();
			try {
				System.out.println(sdCardir.getAbsolutePath());
				File file = new File(sdCardir.getCanonicalPath() + relativePath);
				// �ж��ļ��Ƿ���ڣ��������򴴽����ļ�
				if (!existsCheck)
					if (!file.exists()) {
						file.mkdirs();// mkdir���ܵ���Ŀ¼��mkdir���Զ༶Ŀ¼
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
	 * �����ļ�
	 * 
	 * @param relativePath
	 */
	public static void createFile(String relativePath) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd��Ŀ¼
			File sdCardir = Environment.getExternalStorageDirectory();

			try {
				File file = new File(sdCardir.getCanonicalPath() + relativePath);
				// �ж��ļ��Ƿ���ڣ��������򴴽����ļ�
				if (!existsCheck)
					if (!file.exists())
						file.mkdirs();// mkdir���ܵ���Ŀ¼��mkdir���Զ༶Ŀ¼
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * ��ȡ�ļ�������
	 * 
	 * @param relativePath
	 *            ���·�������� generatePath�õ�
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
	 * ɾ���ļ�
	 * 
	 * @param relativePath
	 *            ���·�������� generatePath�õ�
	 * @param fileName
	 */
	public static void deleteFile(String relativePath, String fileName) {
		// sd��Ŀ¼
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
	 * ����ļ��Ĵ����ԣ��Է�ֹ�ļ��ظ�
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
	 * ����ɾ���ı���������ƥ����ַ���
	 * 
	 * @param relativePath
	 *            ���·�������� generatePath�õ�
	 * @param tagName
	 *            �ļ���һ����ʶ
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
	 * �������·��
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
