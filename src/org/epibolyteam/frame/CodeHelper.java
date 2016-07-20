/* 
 * CreateDate 2016-7-18
 *
 * Email ：darkidiot@icloud.com 
 * School：CUIT 
 * Copyright For darkidiot
 */
package org.epibolyteam.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * 代码生成器
 * 
 * @author darkidiot
 * @version 1.0
 */
public class CodeHelper {
	
	/**默认配置文件路径*/
	private static final String properties = "code-helper.properties";
	
	private static final String SQL_TEMPLATE = "sql_template.xml";
	
	private static final String BEAN_TEMPLATE = "bean_template.xml";
	
	private static final String SERVICE_TEMPLATE = "service_template.xml";
	
	private static final String SERVICEIMPL_TEMPLATE = "serviceimpl_template.xml";
	
	private static final String MYBATIS_TEMPLATE = "mybatis_template.xml";

	private JFrame frame = new JFrame("epiboly team code helper");
	
	private JTextField driverField = new JTextField("com.mysql.jdbc.Driver");

	private JTextField urlField = new JTextField("jdbc:mysql://127.0.0.1:3306/db_name?useUnicode=true&characterEncoding=UTF8");

	private JTextField usernameField = new JTextField("username");

	private JTextField passwordField = new JTextField("password");

	private JTextField tableField = new JTextField("table_name");
	
	private JTextField packageField = new JTextField("package_name");
	
	private JTextField authorField = new JTextField("author_name");
	
	/** 生成代码 */
	private JButton codeButton = new JButton("========================= Generate code =========================");

	private JTextArea beanText = new JTextArea();

	private JTextArea mybatisText = new JTextArea();
	
	private JTextArea serviceText = new JTextArea();
	
	private JTextArea serviceImplText = new JTextArea();

	static {
		//设置默认字体样式
		FontUIResource fontRes = new FontUIResource(new Font("微软雅黑 Linght", 1, 14));
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource)
				UIManager.put(key, fontRes);
		}
	}

	public CodeHelper() throws Exception{
		init();
		createView();
	}
	
	/***
	 * 初始化
	 * @throws Exception
	 */
	private void init() throws Exception{
		Properties p = new Properties();
		String text = Util.read(properties);
		p.load(new StringReader(text));
		driverField.setText(p.getProperty("driver"));
		urlField.setText(p.getProperty("url"));
		usernameField.setText(p.getProperty("username"));
		passwordField.setText(p.getProperty("password"));
		tableField.setText(p.getProperty("table"));
		packageField.setText(p.getProperty("package"));
		authorField.setText(p.getProperty("author"));
	}

	/**
	 * 创建UI
	 * @author DarkIdiot
	 */
	private void createView() {
		final JTabbedPane tab = new JTabbedPane();
		JPanel topPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(topPanel, BoxLayout.Y_AXIS);
		topPanel.setLayout(boxLayout);
		topPanel.add(getField("驱动", driverField));
		topPanel.add(getField("URL", urlField));
		topPanel.add(getField("用户名", usernameField));
		topPanel.add(getField("密码", passwordField));
		topPanel.add(getField("数据库表", tableField));
		topPanel.add(getField("包路径", packageField));
		topPanel.add(getField("作者", authorField));
		topPanel.add(getField("按钮", codeButton));
		topPanel.setPreferredSize(new Dimension(0, 270));
		mybatisText.setBorder(BorderFactory.createEtchedBorder());
		beanText.setBorder(BorderFactory.createEtchedBorder());
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(tab, BorderLayout.CENTER);
		JScrollPane beanScroll = new JScrollPane(beanText);
		final JScrollBar beanScrollBar = beanScroll.getVerticalScrollBar();
		beanScrollBar.setUnitIncrement(100);
		tab.addTab("Bean 代码", beanScroll); 
		tab.addTab("Service 接口", new JScrollPane(serviceText));
		tab.addTab("Service 实现", new JScrollPane(serviceImplText));
		tab.addTab("MyBatis 配置", new JScrollPane(mybatisText));
		frame.setSize(700,600);
		frame.setLayout(new BorderLayout());
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);   // 设置窗体全屏显示
		frame.setVisible(true);
		codeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				try {
					createCode();
				} catch (Exception e) {
					e.printStackTrace();
					beanText.setText(Util.getStack(e));
				}finally{
					tab.setSelectedIndex(0);
					beanScrollBar.setValue(beanScrollBar.getMinimum());
				}
			}
		});
		beanText.setLineWrap(true);// 激活自动换行功能
		mybatisText.setLineWrap(true);// 激活自动换行功能
	}

	/**
	 * 取得字段
	 * @author DarkIdiot
	 * @param title
	 * @param c
	 * @return 标签 + 输入框
	 */
	private JPanel getField(String title, JComponent c) {
		JPanel tr = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel(title);
		label.setPreferredSize(new Dimension(80, 30));
		tr.add(label);
		tr.add(c);
		c.setPreferredSize(new Dimension(600, 30));
		return tr;
	}

	/**
	 * 生成代码
	 * @author DarkIdiot
	 */
	private void createCode() throws Exception {
		Properties p = new Properties();
		p.put("driver", driverField.getText());
		p.put("url", urlField.getText());
		p.put("username", usernameField.getText());
		p.put("password", passwordField.getText());
		p.put("table", tableField.getText());
		p.put("package", packageField.getText());
		p.put("author", authorField.getText());
		String database = Util.matchs(p.getProperty("url"), ":\\d+/(\\w+)", 1).get(0); //匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		p.put("database", database);
		FileWriter writer = new FileWriter(CodeHelper.class.getResource(properties).getFile()); 
		p.store(writer, "");
		writer.close();
		List<Column> columns = getColumns(p, tableField.getText());
		Table table = getTable(p, tableField.getText());
		mybatisText.setText("");
		beanText.setText("");
		serviceText.setText("");
		serviceImplText.setText("");
		String packages = p.getProperty("package");
		String author = p.getProperty("author");
		if( !columns.isEmpty() ){
			String mybatisCode = getMyBatisCode(table, packages, author, columns);
			mybatisText.setText(mybatisCode);
			String domainCode = getBeanCode(table, packages, author, columns);
			beanText.setText(domainCode);
			String serviceCode = getServiceCode(table, packages, author, columns.get(0));
			serviceText.setText(serviceCode);
			String serviceImplCode = getServiceImplCode(table, packages, author, columns.get(0));
			serviceImplText.setText(serviceImplCode);
		}
	}
	
	/**
	 * 生成MyBatis代码
	 * @author DarkIdiot
	 * @param table
	 * @param pack
	 * @param author
	 * @param columns
	 * @throws Exception
	 */
	private String getMyBatisCode(Table table, String pack, String author, List<Column> columns) throws Exception{
		String template = Util.read(MYBATIS_TEMPLATE);
		String className = Util.firstCharUpperCase(Util.toFieldName(table.getName()));
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("table.name", table.getName());
		map.put("table.desc", table.getDesc());
		map.put("class.name", className);
		map.put("class.full", pack + "." + className);
		map.put("author", author);
		Column idColumn = columns.get(0);
		map.put("id.column", idColumn.getName());
		map.put("id.field", idColumn.getField());
		List<String> insertFields = new ArrayList<String>();
		List<String> insertColumns = new ArrayList<String>();
		List<String> columnsSelect = new ArrayList<String>();
		List<String> columnsUpdate = new ArrayList<String>();
		List<String> columnsMapping = new ArrayList<String>();
		for (int i = 0; i < columns.size(); i++) {
			Column c = columns.get(i);
			if( i != 0 ){
				insertColumns.add(c.getName());
				insertFields.add("#{" + c.getField() + "}");
				columnsUpdate.add(c.getName() + " = #{" + c.getField() + "}");
			}
			columnsSelect.add(c.getName());
			columnsMapping.add("		<result property=\"" + c.getField() + "\" column=\"" + c.getName() + "\"/>\n");
		}
		map.put("columns.insert", Util.join(insertColumns, ", "));
		map.put("fields.insert", Util.join(insertFields, ", "));
		map.put("columns.select", Util.join(columnsSelect, ", "));
		map.put("columns.update", Util.join(columnsUpdate, ", "));
		map.put("columns.mapping", Util.join(columnsMapping, ""));
		for (Map.Entry<String, String> entry: map.entrySet()) {
			template = template.replace("#" + entry.getKey() + "#", entry.getValue());
		}
		return template;
	}
	
	/**
	 * 生成实体代码
	 * @author DarkIdiot
	 * @param table
	 * @param pack
	 * @param columns
	 * @throws Exception
	 */
	private String getBeanCode(Table table, String pack, String author, List<Column> columns) throws Exception{
		String xml = Util.read(BEAN_TEMPLATE);
		String classTemplate = Util.matchs(xml, "<class>([\\w\\W]+?)</class>", 1).get(0);//匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		String fieldTemplate = Util.matchs(xml, "<field>([\\w\\W]+?)</field>", 1).get(0);
		String methodTemplate = Util.matchs(xml, "<method>([\\w\\W]+?)</method>", 1).get(0);
		String className = Util.firstCharUpperCase(Util.toFieldName(table.getName()));
		StringBuilder fields = new StringBuilder();
		for (Column c : columns) {
			String template = fieldTemplate;
			Map<String, String> fieldMap = new LinkedHashMap<String, String>();
			fieldMap.put("field.col", c.getName());
			fieldMap.put("field.name", c.getField());
			fieldMap.put("field.length", String.valueOf(c.getLength()));
			fieldMap.put("field.nullable", String.valueOf(c.isNullable()));
			fieldMap.put("field.desc", String.valueOf(c.getDesc()));
			fieldMap.put("field.type", c.getFieldType());
			for (Map.Entry<String, String> entry: fieldMap.entrySet()) {
				template = template.replace("#" + entry.getKey() + "#", entry.getValue());
			}
			fields.append(template);
		}
		StringBuilder methods = new StringBuilder();
		for (Column c : columns) {
			String template = methodTemplate;
			Map<String, String> fieldMap = new LinkedHashMap<String, String>();
			fieldMap.put("method.get", "get" + Util.firstCharUpperCase(c.getField()));
			fieldMap.put("method.set", "set" + Util.firstCharUpperCase(c.getField()));
			fieldMap.put("field.name", c.getField());
			fieldMap.put("field.desc", Util.isEmpty(c.getDesc()) ? c.getField() : String.valueOf(c.getDesc()));
			fieldMap.put("field.type", c.getFieldType());
			for (Map.Entry<String, String> entry: fieldMap.entrySet()) {
				template = template.replace("#" + entry.getKey() + "#", entry.getValue());
			}
			methods.append(template);
		}
		Map<String, String> classMap = new LinkedHashMap<String, String>();
		classMap.put("table.name", table.getName());
		classMap.put("table.desc", table.getDesc());
		classMap.put("class.name", className);
		classMap.put("class.package", pack);
		classMap.put("class.full", pack + "." + className);
		classMap.put("fields", fields.toString());
		classMap.put("methods", methods.toString());
		classMap.put("now", Util.format(new Date()));
		classMap.put("author", author);
		for (Map.Entry<String, String> entry: classMap.entrySet()) {
			classTemplate = classTemplate.replace("#" + entry.getKey() + "#", entry.getValue());
		}
		return classTemplate.toString();
	}
	
	/**
	 * 生成Service代码
	 * @author DarkIdiot
	 * @param table
	 * @param pack
	 * @param idColumn
	 * @return
	 * @throws Exception
	 */
	private String getServiceCode(Table table, String pack, String author, Column idColumn) throws Exception{
		String xml = Util.read(SERVICE_TEMPLATE);
		String serviceTemplate = Util.matchs(xml, "<class>([\\w\\W]+?)</class>", 1).get(0);//匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		String className = Util.firstCharUpperCase(Util.toFieldName(table.getName()));
		Map<String, String> classMap = new LinkedHashMap<String, String>();
		classMap.put("table.name", table.getName());
		classMap.put("table.desc", table.getDesc());
		classMap.put("class.name", className);
		classMap.put("class.package", pack);
		classMap.put("class.full", pack + "." + className);
		classMap.put("now", Util.format(new Date()));
		classMap.put("author", author);
		for (Map.Entry<String, String> entry: classMap.entrySet()) {
			serviceTemplate = serviceTemplate.replace("#" + entry.getKey() + "#", entry.getValue());
		}
		return serviceTemplate.toString();
	}
	
	/**
	 * 生成ServiceImpl代码
	 * @author DarkIdiot
	 * @param table
	 * @param pack
	 * @param idColumn
	 * @return
	 * @throws Exception
	 */
	private String getServiceImplCode(Table table, String pack, String author, Column idColumn) throws Exception{
		String xml = Util.read(SERVICEIMPL_TEMPLATE);
		String serviceTemplate = Util.matchs(xml, "<class>([\\w\\W]+?)</class>", 1).get(0);//匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		String className = Util.firstCharUpperCase(Util.toFieldName(table.getName()));
		Map<String, String> classMap = new LinkedHashMap<String, String>();
		classMap.put("table.name", table.getName());
		classMap.put("table.desc", table.getDesc());
		classMap.put("class.name", className);
		classMap.put("class.package", pack);
		classMap.put("class.full", pack + "." + className);
		classMap.put("now", Util.format(new Date()));
		classMap.put("author", author);
		for (Map.Entry<String, String> entry: classMap.entrySet()) {
			serviceTemplate = serviceTemplate.replace("#" + entry.getKey() + "#", entry.getValue());
		}
		return serviceTemplate.toString();
	}

	/**
	 * 取得表的数据列
	 * @author DarkIdiot
	 * @param p 参数
	 * @param tableName 表
	 * @return 数据列
	 * @throws Exception
	 */
	private List<Column> getColumns(Properties p, String tableName) throws Exception {
		String xml = Util.read(SQL_TEMPLATE);
		String sql = Util.matchs(xml, "<column>([\\w\\W]+?)</column>", 1).get(0);//匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		sql = sql.replace("#table#", tableName);
		sql = sql.replace("#database#", p.getProperty("database"));
		Class.forName(p.getProperty("driver"));
		Connection conn= null;
		ResultSet rs = null;
		List<Column> rows = new ArrayList<Column>();
		try {
			conn = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));
			rs = conn.prepareStatement(sql.toString()).executeQuery();
			while (rs.next()) {
				Column col = new Column(rs);
				rows.add(col);
			}
		}finally{
			if( conn != null )
				conn.close();
		}
		return rows;
	}
	
	/**
	 * 取得表信息
	 * @author DarkIdiot
	 * @param p 参数
	 * @param tableName 表名称
	 * @return
	 * @throws Exception
	 */
	private Table getTable(Properties p, String tableName) throws Exception {
		String xml = Util.read(SQL_TEMPLATE);
		String sql = Util.matchs(xml, "<table>([\\w\\W]+?)</table>", 1).get(0);//匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		sql = sql.replace("#table#", tableName);
		sql = sql.replace("#database#", p.getProperty("database"));
		Connection conn= null;
		ResultSet rs = null;
		Table table = new Table(tableName, tableName);
		try {
			Class.forName(p.getProperty("driver"));
			conn = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));
			rs = conn.prepareStatement(sql.toString()).executeQuery();
			while (rs.next()) {
				table = new Table(tableName, rs.getString("table_desc"));
			}
		}finally{
			if( conn != null )
				conn.close();
		}
		return table;
	}
	
	/**
	 * 启动代码服务
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new CodeHelper();
	}
}