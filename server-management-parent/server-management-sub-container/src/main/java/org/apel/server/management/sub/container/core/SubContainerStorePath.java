package org.apel.server.management.sub.container.core;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.apel.server.management.sub.container.util.CenterProperties;

/**
 * 子容器存储路径
 * @author wubo
 */
public class SubContainerStorePath {
	
	/**
	 * 跟目录
	 */
	private static String rootDirectory;
	
	/**
	 * 子容器目录
	 */
	public static final String SUB_CONTAINER_DIRECTORY = "sub-directory";
	
	
	private static void loadRootDirectory(){
		rootDirectory = CenterProperties.getProperty("sub_container_root_directory");
		if(StringUtils.isEmpty(rootDirectory)){
			System.out.println("根目录不能为空!!!");
		}
	}
	
	/**
	 * 得到子容器目录
	 * @return
	 */
	public static String getSubContainerDirectory(){
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
	 * 子容器目录不存在就返回true,存在就返回false
	 * @return
	 */
	private static Boolean isNotExistOfSubContainerDirectory(){
		String subContainerDirectory = getSubContainerDirectory();
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
			String subContainerDirectory = getSubContainerDirectory();
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

}
