package org.apel.server.management.sub.container.util;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 传输properties文件
 * @author wubo
 *
 */
public class TransferProperties {
	
	private PropertiesConfiguration properties;
	
	/**
	 * 应用程序传输路径
	 */
	private String applicationTransferPath;

	public TransferProperties(String applicationTransferPath) {
		super();
		this.applicationTransferPath = applicationTransferPath;
		initPropertiesConfiguration();
	}
	
	/**
	 * 初始化 PropertiesConfiguration
	 */
	private void initPropertiesConfiguration(){
		try {
			properties = new PropertiesConfiguration(new File(applicationTransferPath));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 随机拿到第一个，该方法一般很少使用，除非确定当前的key值是全系统唯一的
	 * @param key
	 * @return
	 */
	public String getProperty(String key){
		if(properties.getProperty(key) != null){
			return (String) properties.getProperty(key);
		}
		return null;
	}
	
	/**
	 * 随机设置到第一个，该方法一般很少使用，除非确定当前的key值是全系统唯一的
	 * @param key
	 * @param value
	 * @return 返回true表示设置成功,false则反之
	 */
	public Boolean setProperty(String key,String value){
		Boolean flag = false;
		try {
			if(properties.getProperty(key) != null){
				properties.setProperty(key, value);
				properties.save();
			}else{
				properties.setProperty(key, value);
				properties.save();
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return flag;
	}
}
