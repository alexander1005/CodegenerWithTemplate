/* 
 * CreateDate 2016-7-18
 *
 * Email ：darkidiot@icloud.com 
 * School：CUIT 
 * Copyright For darkidiot
 */
package org.epibolyteam.frame;

import java.sql.ResultSet;

/**
 * 数据列定义
 * 
 * @author darkidiot
 * @version 1.0 
 */
public class Column {

	private String name;

	private String desc;

	private String type;

	private boolean nullable;

	private int length;
	
	public Column(ResultSet rs) throws Exception {
		this.name = rs.getString("name");
		this.desc = rs.getString("desc");
		this.type = rs.getString("type");
		this.nullable = rs.getBoolean("nullable");
		String length = rs.getString("length");
		this.length = length != null ? Integer.parseInt(length) : 0;
	}

	public Column(String name, String desc, String type, boolean nullable, int length) {
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.nullable = nullable;
		this.length = length;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public String getType() {
		return type;
	}

	public boolean isNullable() {
		return nullable;
	}

	public int getLength() {
		return length;
	}
	
	public String getField(){
		return Util.toFieldName(name);
	}
	
	public String getFieldType(){
		type = type.toLowerCase();
		if( type.contains("varchar") || type.contains("text") ){
			return "java.lang.String";
		}else if(type.equals("int") || type.equals("tinyint") ){
			return "java.lang.Integer";
		}else if(type.contains("bigint") || type.contains("long") || type.contains("number")){
			return "java.lang.Long";
		}else if(type.contains("double") ){
			return "java.lang.Double";
		}else if(type.contains("date") || type.contains("time") ){
			return "java.util.Date";
		}else if(type.contains("decimal") ){
			return "java.math.BigDecimal";
		}
		return "unknown";
	}

	@Override
	public String toString() {
		return "{name=" + name + ", title=" + desc + ", type=" + type + ", nullable=" + nullable + ", length=" + length + "}";
	}
}