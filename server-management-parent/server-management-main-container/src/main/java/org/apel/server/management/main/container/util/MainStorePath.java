package org.apel.server.management.main.container.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
/**
 * 主应用程序操作本地文件类
 * @author wubo
 *
 */
public class MainStorePath {
	
	/**
	 * 跟目录
	 */
	private static String rootDirectory;
	
	/**
	 * 主容器目录
	 */
	public static final String MAIN_CONTAINER_DIRECTORY = "main-directory";

	   /** 根据应用程序部署路径参数得到应用程序部署目录
	    * 
	    * @param deployPath
	   */
	   public java.lang.String getDeployDirectory(java.lang.String deployPath) {
	      return null;
	   }
	   

		private static void loadRootDirectory(){
			rootDirectory = CenterProperties.getProperty("main_container_root_directory");
			if(StringUtils.isEmpty(rootDirectory)){
				System.out.println("根目录不能为空!!!");
			}
		}
		
		/**
		 * 得到主容器目录
		 * @return
		 */
		public static String getMainContainerDirectory(){
			String subDirectory = getRootDirectory()+MAIN_CONTAINER_DIRECTORY+"/";
			return replaceSprit(subDirectory);
		}
		
		/**
		 * 得到容器的更目录
		 * @param rootDirectory
		 * @return
		 */
		public static String getRootDirectory(){
			if(StringUtils.isEmpty(rootDirectory)){
				loadRootDirectory();
			}
			
			if(StringUtils.isEmpty(rootDirectory)){
				System.out.println("主容器的根目录不能为空!!");
				return null;
			}
			if(!rootDirectory.endsWith("/")){
				rootDirectory +="/";
			}
			return replaceSprit(rootDirectory);
		}
		
		/**
		 * main容器目录不存在就返回true,存在就返回false
		 * @return
		 */
		private static Boolean isNotExistOfMainContainerDirectory(){
			String subContainerDirectory = getMainContainerDirectory();
			File subContainerDirectoryFile = new File(subContainerDirectory);
			if(subContainerDirectoryFile.exists()){
				return false;
			}
			return true;
		}
		
		/**
		 * 创建主容器目录
		 */
		public static void createMainContainerDirectory(){
			Boolean notExistOfMainContainerDirectory = isNotExistOfMainContainerDirectory();
			if(notExistOfMainContainerDirectory){
				String subContainerDirectory = getMainContainerDirectory();
				File subContainerDirectoryFile = new File(subContainerDirectory);
				subContainerDirectoryFile.mkdirs();
			}
		}
		
		/**
		 * 得到部署应用的文件
		 * @param relativePath
		 * @return
		 */
		public static File getDeployApplicationFile(String relativePath){
			
			String applicationPath = replaceSprit(getMainContainerDirectory()+relativePath);
			File applicationFile = new File(applicationPath);
			
			if(applicationFile.exists()){
				return applicationFile;
			}else{
				System.out.println("该应用文件不存在"+relativePath);
				return null;
			}
		}
		
		/**
		 * 得到部署应用的文件流
		 * @param relativePath
		 * @return
		 */
		public static InputStream getDeployApplicationInputStream(String relativePath){
			File deployApplicationFile = getDeployApplicationFile(relativePath);
			InputStream is = null;
			try {
				is = new FileInputStream(deployApplicationFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return is;
		}
		
		/**
		 * 存储文件
		 * @param is
		 * @return
		 */
		public static String storeFile(InputStream is){
			createMainContainerDirectory();
			String mainContainerDirectory = getMainContainerDirectory();
			String storePath = getYyyyMMddPath();
			storePath = replaceSpritAndSriptEnd(storePath);
			
			File storeDirectory = new File(storePath);
			if(storeDirectory.exists()){
				storeDirectory.mkdirs();
			}
			
			storePath += getUUID();
			File storeFile = new File(replaceSprit(mainContainerDirectory+storePath));
			try {
				FileUtils.copyInputStreamToFile(is, storeFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return replaceSprit(storePath);
		}
		
		/**
		 * 得到年月日路径
		 * @return
		 */
		private static String getYyyyMMddPath(){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String format = sdf.format(new Date());
			return format;
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
		public static String replaceSpritAndSriptEnd(String directory){
			return replaceSprit(spritEnd(directory));
		}
		
		/**
		 * 删除某个文件夹及该文件夹下的所有文件及文件夹
		 * @param delpath
		 * @return
		 * @throws Exception
		 */
		public boolean deleteFile(String delpath) throws Exception{
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
		
		/**
		 * 得到UUID
		 * @return
		 */
		public static String getUUID(){
			return UUID.randomUUID().toString().replace("-", "").toUpperCase();
		}
}
