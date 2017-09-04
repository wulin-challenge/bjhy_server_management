package org.apel.server.management.main.container.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "base_application")
public class Application {

	@Id
	private String id;

	// 应用上下文
	private String  context;
	// 服务类型
	private String  serverType;
	// 服务类型名称
	private String  serverTypeName;
	// 是否有jar
	private Boolean  hasJar;
	// 应用名称
	private String  name;
	// 创建日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date  createDate;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public String getServerTypeName() {
		return serverTypeName;
	}
	public void setServerTypeName(String serverTypeName) {
		this.serverTypeName = serverTypeName;
	}
	public Boolean getHasJar() {
		return hasJar;
	}
	public void setHasJar(Boolean hasJar) {
		this.hasJar = hasJar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
