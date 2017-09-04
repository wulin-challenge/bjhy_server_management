package org.apel.server.management.main.container.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 应用程序的版本
 * @author wubo
 */
@Table(name="base_app_version")
@Entity
public class ApplicationVersion {

	/** 
	 * 主键ID
	 */
	@Id
	private String id;
	
	/**
	 *  当前应用程序的版本
	 */
	private int appVersion;
	
	/** 
	 * 备份的zip文件的存储路径
	 */
	private java.lang.String storePath;
	
	/** 
	 * 存储日期
	 */
	private java.util.Date storeDate;
	
	/**
	 * 应用程序实体
	 */
	@ManyToOne(targetEntity=Application.class)
	@JoinColumn(name="application_id")
	private Application application;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(int appVersion) {
		this.appVersion = appVersion;
	}

	public java.lang.String getStorePath() {
		return storePath;
	}

	public void setStorePath(java.lang.String storePath) {
		this.storePath = storePath;
	}

	public java.util.Date getStoreDate() {
		return storeDate;
	}

	public void setStoreDate(java.util.Date storeDate) {
		this.storeDate = storeDate;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
	   
}
