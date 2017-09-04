package org.apel.server.management.sub.container.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.WatchEvent;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apel.server.management.common.domain.enums.ApplicationTransferEnum;
import org.apel.server.management.common.util.ProcessUtil;
import org.apel.server.management.common.util.ZipUtil;
import org.apel.server.management.sub.container.util.CenterProperties;
import org.apel.server.management.sub.container.util.TransferProperties;
import org.apel.server.management.sub.container.watcher.FileWatcher;
import org.apel.server.management.sub.container.watcher.TransferDealWith;

/**
 * 代理应用程序操作本地文件类
 * @author wubo
 *
 */
public class AgentStorePath {
	
	/**
	 * 跟目录
	 */
	private static String rootDirectory;
	
	/**
	 * 子容器目录
	 */
	public static final String SUB_CONTAINER_DIRECTORY = "sub-directory";

	   /** 根据应用程序部署路径参数得到应用程序部署目录
	    * 
	    * @param deployPath
	   */
	   public java.lang.String getDeployDirectory(java.lang.String deployPath) {
	      return null;
	   }
	   

		private static void loadRootDirectory(){
			rootDirectory = CenterProperties.getProperty("sub_container_root_directory");
			if(StringUtils.isEmpty(rootDirectory)){
				System.out.println("根目录不能为空!!!");
			}
		}
		
		/**
		 * 得到代理容器目录
		 * @return
		 */
		public static String getAgentContainerDirectory(){
			String subDirectory = getRootDirectory()+SUB_CONTAINER_DIRECTORY+"/";
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
				System.out.println("子容器的根目录不能为空!!");
				return null;
			}
			if(!rootDirectory.endsWith("/")){
				rootDirectory +="/";
			}
			return replaceSprit(rootDirectory);
		}
		
		/**
		 * 创建应用程序目录
		 * @return
		 */
		public static String createApplicationDirectory(){
			String uuid = getUUID();
			String applicationDirectory = replaceSprit(getAgentContainerDirectory()+uuid);
			File file = new File(applicationDirectory);
			if(!file.exists()){
				file.mkdirs();
			}
			return replaceSprit(uuid);
		}
		
		/**
		 * 应用程序路径
		 * @param relativePath
		 * @return
		 */
		public static String getApplicationPath(String relativePath){
			return replaceSpritAndSriptEnd(getAgentContainerDirectory()+relativePath);
		}
		
		/**
		 * 应用程序目录
		 * @param relativePath
		 * @return
		 */
		public static String getApplicationDirectory(String relativeDirectory){
			return replaceSpritAndSriptEnd(getAgentContainerDirectory()+relativeDirectory);
		}
		
		/**
		 * 部署应用
		 * @param relativePath
		 * @param is
		 * @return
		 */
		public static Boolean deployApplication(String relativePath,InputStream is){
			String applicationPath = getApplicationPath(relativePath);
			File destination = new File(applicationPath);
			try {
				FileUtils.copyInputStreamToFile(is, destination);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			};
			return true;
		}
		
		public static void unzip(String relativeDirectory,String relativePath){
			String applicationDirectory = getApplicationDirectory(relativeDirectory);
			String applicationPath = getApplicationPath(relativePath);
			ZipUtil.unzip(applicationPath, applicationDirectory);
		}
		
		/**
		 * 启动应用程序
		 * @param relativePath
		 * @return
		 */
		public static String startup(String relativePath){
			String startupPath = getStartupPath(relativePath);
			try {
				Process process = ProcessUtil.startup(startupPath);
				ProcessUtil.console(process);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		private String getPid(String relativePath){
			String zipDirectory = getApplicationZipDirectory(relativePath);
			
			String startDirectory = replaceSprit(zipDirectory+"/start-directory");
			
//			application_to_sub_container.properties
			
			FileWatcher FileWatcher = new FileWatcher(startDirectory,new TransferDealWith() {
				
				public void fileModifyEvent(WatchEvent<?> event) {
					
					String transferStartDirectoryPath = replaceSprit(startDirectory+"/application_to_sub_container.properties");
					TransferProperties transferProperties = new TransferProperties(transferStartDirectoryPath);
					
					String startStatus = transferProperties.getProperty(ApplicationTransferEnum.startStatus.getCode());
					String operatingSystem = transferProperties.getProperty(ApplicationTransferEnum.operatingSystem.getCode());
					String pid = transferProperties.getProperty(ApplicationTransferEnum.pid.getCode());
					
					System.out.println(startStatus);
					System.out.println(operatingSystem);
					System.out.println(pid);
				}
				
				public void fileDeleteEvent(WatchEvent<?> event) {
					
				}
				
				public void fileCreateEvent(WatchEvent<?> event) {
					
				}
			});
			
			return null;
			
		}
		
		/**
		 * 得到应用程序的zip目录
		 * @param relativePath
		 * @return
		 */
		public static String getApplicationZipDirectory(String relativePath){
			String applicationPath = getApplicationPath(relativePath);
			String applicationZipDirectory = applicationPath.substring(0,applicationPath.lastIndexOf("."));
			return applicationZipDirectory;
		}
		
		/**
		 * 得到应用程序的启动路径
		 * @param relativePath
		 * @return
		 */
		public static String getStartupPath(String relativePath){
			String startupPath = replaceSprit(getApplicationZipDirectory(relativePath)+"/startup.bat");
			return startupPath;
		}
		
		/**
		 * 生成启动文件
		 * @return
		 */
		public static Boolean createStartupFile(String relativeDirectory,String relativePath){
			String startupPath = getStartupPath(relativePath);
			File startupFile = new File(startupPath);
			if(startupFile.exists()){
				try {
					deleteFile(startupPath);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			
			InputStream is = getInputStreamFromString(getStartupContext(getApplicationZipDirectory(relativePath), startupPath, null));
			try {
				FileUtils.copyInputStreamToFile(is, startupFile);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		/**
		 * 得到启动内容
		 * @param applicationZipDirectory 应用程序解压后目录
		 * @param startupPath 启动路径
		 * @param memoryParams 内存参数
		 * @return
		 */
		private static String getStartupContext(String applicationZipDirectory,String startupPath,String memoryParams){
			String defaultMomeryParams = " -Xms256M -Xmx256M -XX:PermSize=128M -XX:MaxPermSize=256M ";
			
			if(StringUtils.isEmpty(memoryParams)){
				memoryParams = defaultMomeryParams;
			}
			
			//设置jdk
			String jdkSet = "";
			String defaultJdk = CenterProperties.getProperty("default_jdk");
			
			defaultJdk = replaceSprit(defaultJdk);
			
			if(StringUtils.isNotEmpty(defaultJdk)){
				jdkSet += "SET JAVA_HOME="+defaultJdk+"\n";
				jdkSet += "SET CLASSPATH=.;%JAVA_HOME%/lib/dt.jar;%JAVA_HOME%/lib/tools.jar\n";
				jdkSet += "SET PATH=%JAVA_HOME%/bin\n";
			}
			
			String context = "@echo off\n"
					+ "title 应用程序\n"
					+ jdkSet
					+ "cd "+applicationZipDirectory+"\n"
					+ "java "+memoryParams+" -jar "+applicationZipDirectory+"/server-application.jar\n"
					+ "pause";
			
			return context;
		}
		
		/**
	     * 将字符串变成输入流
	     * @param str
	     * @return
	     */
	    public static InputStream getInputStreamFromString(String str){
	    	ByteArrayInputStream byteArray = new ByteArrayInputStream(str.getBytes());
	    	return byteArray;
	    }
		
		/**
		 * 子容器目录不存在就返回true,存在就返回false
		 * @return
		 */
		private static Boolean isNotExistOfSubContainerDirectory(){
			String subContainerDirectory = getAgentContainerDirectory();
			File subContainerDirectoryFile = new File(subContainerDirectory);
			if(subContainerDirectoryFile.exists()){
				return false;
			}
			return true;
		}
		
		/**
		 * 创建子容器目录
		 */
		public static void createSubContainerDirectory(){
			Boolean notExistOfSubContainerDirectory = isNotExistOfSubContainerDirectory();
			if(notExistOfSubContainerDirectory){
				String subContainerDirectory = getAgentContainerDirectory();
				File subContainerDirectoryFile = new File(subContainerDirectory);
				subContainerDirectoryFile.mkdirs();
			}
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
		
		/**
		 * 得到uuid
		 * @return
		 */
		public static String getUUID(){
			return UUID.randomUUID().toString().replace("-", "").toUpperCase();
		}
}
