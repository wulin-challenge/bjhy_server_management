package org.apel.server.management.application.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

/**
 * 应用程序操作文件的工具类
 * @author wubo
 *
 */
public class ApplicationStorePath {
	/**
	 * 应用程序的启动目录
	 */
	public static String START_DIRECTORY = "start-directory";
	
	/**
	 * 应用程序启动的产生传递到应用程序
	 */
	public static String APPLICATION_TO_SUB_CONTAINER = "application_to_sub_container.properties";
	
	/**
	 * 更目录
	 */
	private static String rootDirectory;
	
	/**
	 * 得到应用程序的更目录
	 * @return
	 */
	public static String getApplicationRootDirectory(){
		if(StringUtils.isEmpty(rootDirectory)){
			rootDirectory = System.getProperty("user.dir");
		}
		return replaceSpritAndSriptEnd(rootDirectory);
	}
	
	/**
	 * 得到应用程序子容器路径
	 * @return
	 */
	public static String getApplicationToSubContainerPath(){
		createApplicationStartDirectory();
		String applicationToSubContainerDirectory = getApplicationStartDirectory()+APPLICATION_TO_SUB_CONTAINER;
		return replaceSprit(applicationToSubContainerDirectory);
	}
	
	/**
	 * 创建应用程序子目录 文件
	 */
	public static void createApplicationToSubContainerFile(){
		String applicationToSubContainerPath = getApplicationToSubContainerPath();
		if(StringUtils.isEmpty(applicationToSubContainerPath)){
			System.out.println("因为 [应用程序子容器路径] 为空!! ,创建应用程序子目录 文件 失败!!");
			return;
		}
		
		File applicationToSubContainerFile = new File(applicationToSubContainerPath);
		
		if(!applicationToSubContainerFile.exists()){
			try {
				applicationToSubContainerFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建应用程序的启动目录
	 */
	public static void createApplicationStartDirectory(){
		
		//创建应用程序的启动目录
		String applicationStartDirectory = getApplicationStartDirectory();
		if(StringUtils.isEmpty(applicationStartDirectory)){
			System.out.println("应用程序的启动目录为空!!,创建应用程序的启动目录失败!!");
			return;
		}
		
		File applicationStartDirectoryFile = new File(applicationStartDirectory);
		if(!applicationStartDirectoryFile.exists()){
			applicationStartDirectoryFile.mkdirs();
		}
	}
	
	/**
	 * 得到应用程序的启动目录
	 * @return
	 */
	private static String getApplicationStartDirectory(){
		String applicationStartDirectory = getApplicationRootDirectory()+START_DIRECTORY;
		return replaceSpritAndSriptEnd(applicationStartDirectory);
	}
	
	/**
	 * 以斜杠结束
	 * @param directory
	 * @return
	 */
	private static String spritEnd(String directory){
		if(StringUtils.isEmpty(directory)){
			System.out.println("目录路径是空!!");
			return null;
		}
		if(!directory.endsWith("/")){
			directory +="/";
		}
		return directory;
	}
	
	/**
	 * 替换正斜杠并以正斜杠结束
	 * @param directory
	 * @return
	 */
	private static String replaceSpritAndSriptEnd(String directory){
		return replaceSprit(spritEnd(directory));
	}
	
	/**
	 * 替换反斜杠,解决在windows,linux下的路径问题
	 * @param path
	 * @return
	 */
	public static String replaceSprit(String path){
		if(StringUtils.isEmpty(path)){
			return "";
		}
		path = path.replace("\\\\", "/"); //// Java中4个反斜杠表示一个反斜杠
		path = path.replace("\\", "/"); 
		return path;
	}
	
	/**
	 * 删除某个文件夹及该文件夹下的所有文件及文件夹
	 * @param delpath
	 * @return
	 * @throws Exception
	 */
	public static boolean deleteFile(String delpath) throws Exception{
		try {
			delpath = delpath.replace("\\\\", "/"); //// Java中4个反斜杠表示一个反斜杠
			delpath = delpath.replace("\\", "/"); 
			File file = new File(delpath);
			// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(delpath + "/" + filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
						System.out.println(delfile.getAbsolutePath() + "删除文件成功");
					} else if (delfile.isDirectory()) {
						deleteFile(delpath + "/" + filelist[i]);
					}
				}
				System.out.println(file.getAbsolutePath() + "删除成功");
				file.delete();
			}

		} catch (FileNotFoundException e) {
			System.out.println("deletefile() Exception:" + e.getMessage());
		}
		return true;
	}

}
