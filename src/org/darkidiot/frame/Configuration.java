/* 
 * CreateDate 2016-7-18
 *
 * Email ：darkidiot@icloud.com 
 * School：CUIT 
 * Copyright For darkidiot
 */
package org.darkidiot.frame;

/**
 * 配置类
 * 
 * @author darkidiot
 * @version 1.0
 */
public class Configuration {

	private static MyBatisType codeTemplateType = MyBatisType.dao;

	private static Boolean useLombok = true;

	private static final String lombok_suffix = "_lombok";
	private static final String prefix = "%s/";

	private static final String BEAN_TEMPLATE = "%sbean%s_template.xml";

	private static final String SERVICE_TEMPLATE = "%sservice%s_template.xml";

	private static final String SERVICEIMPL_TEMPLATE = "%sserviceimpl%s_template.xml";

	private static final String DAO_TEMPLATE = "%sdao%s_template.xml";

	private static final String MYBATIS_TEMPLATE = "%smybatis%s_template.xml";

	public static String getBeanTemplateLocation() {
		return String.format(BEAN_TEMPLATE, String.format(prefix, codeTemplateType), useLombok ? lombok_suffix : "");
	}

	public static String getServiceTemplateLocation() {
		return String.format(SERVICE_TEMPLATE, String.format(prefix, codeTemplateType), useLombok ? "" : "");
	}

	public static String getServiceImplTemplateLocation() {
		return String.format(SERVICEIMPL_TEMPLATE, String.format(prefix, codeTemplateType), useLombok ? "" : "");
	}

	public static String getDaoTemplateLocation() {
		return String.format(DAO_TEMPLATE, String.format(prefix, codeTemplateType), useLombok ? "" : "");
	}

	public static String getMybatisTemplateLocation() {
		return String.format(MYBATIS_TEMPLATE, String.format(prefix, codeTemplateType), useLombok ? "" : "");
	}

	public static void setCodeTemplateType(MyBatisType codeTemplateType) {
		Configuration.codeTemplateType = codeTemplateType;
	}

	public static void setUseLombok(Boolean useLombok) {
		Configuration.useLombok = useLombok;
	}

	public static Boolean getUseLombok() {
		return useLombok;
	}

	public static void main(String[] args) {
		System.out.println(getBeanTemplateLocation());
		System.out.println(getServiceTemplateLocation());
		System.out.println(getServiceImplTemplateLocation());
		System.out.println(getDaoTemplateLocation());
		System.out.println(getMybatisTemplateLocation());
	}

}
