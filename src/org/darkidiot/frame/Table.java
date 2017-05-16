/* 
 * CreateDate 2016-7-18
 *
 * Email ：darkidiot@icloud.com 
 * School：CUIT 
 * Copyright For darkidiot
 */
package org.darkidiot.frame;

/**
 * 表定义
 * 
 * @author darkidiot
 * @version 1.0 
 */
public class Table {

	private String name;

	private String desc;

	public Table(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return "Table [name=" + name + ", desc=" + desc + "]";
	}
}