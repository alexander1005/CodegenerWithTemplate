/* 
 * CreateDate 2016-7-18
 *
 * Email ：darkidiot@icloud.com 
 * School：CUIT 
 * Copyright For darkidiot
 */
package org.darkidiot.frame;

import static org.darkidiot.frame.Configuration.getBeanTemplateLocation;
import static org.darkidiot.frame.Configuration.getDaoTemplateLocation;
import static org.darkidiot.frame.Configuration.getMybatisTemplateLocation;
import static org.darkidiot.frame.Configuration.getServiceImplTemplateLocation;
import static org.darkidiot.frame.Configuration.getServiceTemplateLocation;
import static org.darkidiot.frame.Configuration.getUseLombok;
import static org.darkidiot.frame.Configuration.setCodeTemplateType;
import static org.darkidiot.frame.Configuration.setUseLombok;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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

	/** 默认配置文件路径 */
	private static final String properties = "code-helper.properties";
	private static final String saveProperties = "./code-helper.properties";

	private static final String SQL_TEMPLATE = "sql_template.xml";

	private JFrame frame = new JFrame("darkidiot code helper");

	private JTextField driverField = new JTextField("com.mysql.jdbc.Driver");

	private JTextField urlField = new JTextField(
			"jdbc:mysql://127.0.0.1:3306/db_name?useUnicode=true&characterEncoding=UTF8");

	private JTextField usernameField = new JTextField("username");

	private JTextField passwordField = new JTextField("password");

	private JTextField tableField = new JTextField("table_name");

	private JTextField packageField = new JTextField("package_name");

	private JTextField authorField = new JTextField("author_name");

	/** 生成代码 */
	private JButton codeButton = new JButton(" Generate Code ");

	private JTextArea beanText = new JTextArea();

	private JTextArea mybatisText = new JTextArea();

	private JTextArea serviceText = new JTextArea();

	private JTextArea serviceImplText = new JTextArea();

	private JTextArea daoText = new JTextArea();

	static {
		// 设置默认字体样式
		FontUIResource fontRes = new FontUIResource(new Font("微软雅黑 Linght", 1, 14));
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource)
				UIManager.put(key, fontRes);
		}
	}

	public CodeHelper() throws Exception {
		init();
		createView();
	}

	/***
	 * 初始化
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		Properties p = new Properties();
		File file = new File(saveProperties);
		String text;
		if (file.exists()) {
			text = Util.read(file);
		} else {
			text = Util.read(properties);
		}
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
	 * 
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
		topPanel.add(getField("基础路径", packageField));
		topPanel.add(getField("作者", authorField));
		topPanel.add(getFieldSpecial("操作"));
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
		tab.addTab("Dao 实现", new JScrollPane(daoText));
		tab.addTab("MyBatis 配置", new JScrollPane(mybatisText));
		frame.setVisible(true);
		frame.setSize(700, 600);
		frame.setLayout(new BorderLayout());
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // 设置窗体全屏显示
		codeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				try {
					createCode();
				} catch (Exception e) {
					e.printStackTrace();
					beanText.setText(Util.getStack(e));
				} finally {
					tab.setSelectedIndex(0);
					beanScrollBar.setValue(beanScrollBar.getMinimum());
				}
			}
		});
		beanText.setLineWrap(true);// 激活自动换行功能
		beanText.setTabSize(4);
		serviceText.setLineWrap(true);// 激活自动换行功能
		serviceText.setTabSize(4);
		serviceImplText.setLineWrap(true);// 激活自动换行功能
		serviceImplText.setTabSize(4);
		daoText.setLineWrap(true);// 激活自动换行功能
		daoText.setTabSize(4);
		mybatisText.setLineWrap(true);// 激活自动换行功能
		mybatisText.setTabSize(4);
	}

	/**
	 * 取得字段
	 * 
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
		c.setPreferredSize(new Dimension(579, 30));
		return tr;
	}

	private JPanel getFieldSpecial(String title) {
		JPanel tr = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel(title);
		label.setPreferredSize(new Dimension(80, 30));
		tr.add(label);
		JCheckBox box = new JCheckBox();
		box.setText("lombok");
		box.setSelected(true);
		box.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setUseLombok(!getUseLombok());
			}
		});
		tr.add(box);
		JRadioButton daoRandioButton = new JRadioButton("Dao");
		daoRandioButton.setSelected(true);
		daoRandioButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setCodeTemplateType(MyBatisType.dao);
			}
		});
		JRadioButton mapperRandioButton = new JRadioButton("Mapper");
		mapperRandioButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setCodeTemplateType(MyBatisType.mapper);
			}
		});
		ButtonGroup group = new ButtonGroup();
		group.add(daoRandioButton);
		group.add(mapperRandioButton);
		tr.add(daoRandioButton);
		tr.add(mapperRandioButton);
		codeButton.setPreferredSize(new Dimension(356, 30));
		tr.add(codeButton);
		return tr;
	}

	/**
	 * 生成代码
	 * 
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
		String database = Util.matchs(p.getProperty("url"), ":\\d+/(\\S+)\\?", 1).get(0); // 匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		p.put("database", database);
		FileWriter writer = new FileWriter(saveProperties);
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
		if (!columns.isEmpty()) {
			String mybatisCode = getMyBatisCode(table, packages, author, columns);
			mybatisText.setText(mybatisCode);
			String domainCode = getBeanCode(table, packages, author, columns);
			beanText.setText(domainCode);
			String serviceCode = getServiceCode(table, packages, author);
			serviceText.setText(serviceCode);
			String serviceImplCode = getServiceImplCode(table, packages, author);
			serviceImplText.setText(serviceImplCode);
			String daoCode = getDaoCode(table, packages, author);
			daoText.setText(daoCode);
		}
	}

	/**
	 * 生成MyBatis代码
	 * 
	 * @author DarkIdiot
	 * @param table
	 * @param pack
	 * @param author
	 * @param columns
	 * @throws Exception
	 */
	private String getMyBatisCode(Table table, String pack, String author, List<Column> columns) throws Exception {
		String xml = Util.read(getMybatisTemplateLocation());
		StringBuilder sb = new StringBuilder();
		String headTemplate = Util.matchs(xml, "<head>([\\w\\W]+?)</head>", 1).get(0);// 匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		sb.append(headTemplate);

		String className = Util.firstCharUpperCase(Util.toFieldName(table.getName()));
		String mapperTemplate = Util.matchs(xml, "(<mapper[\\w\\W]+?</mapper>)", 1).get(0);
		String resultTemplate = Util.matchs(xml, "<resultEntry>([\\w\\W]+?)</resultEntry>", 1).get(0);
		String ifTemplate = Util.matchs(xml, "<ifEntry>([\\w\\W]+?)</ifEntry>", 1).get(0);
		String valueTemplate = Util.matchs(xml, "<valueEntry>([\\w\\W]+?)</valueEntry>", 1).get(0);
		String useuseGeneratedKeyTemplate = Util
				.matchs(xml, "<useuseGeneratedKeys>([\\w\\W]+?)</useuseGeneratedKeys>", 1).get(0);
		List<String> excludeIdCols = new ArrayList<String>();
		List<String> idCols = new ArrayList<String>();
		List<String> excludeIdVals = new ArrayList<String>();
		List<String> idVals = new ArrayList<String>();
		List<String> mappings = new ArrayList<String>();
		List<String> itemValsAll = new ArrayList<String>();
		StringBuilder useuseGeneratedKeys = new StringBuilder();
		for (int i = 0; i < columns.size(); i++) {
			Column c = columns.get(i);
			if (c.IsPrikey()) {
				idCols.add(c.getName());
				idVals.add(valueTemplate.replace("#value#", c.getField()));
				itemValsAll.add(valueTemplate.replace("#value#", "item." + c.getField()));
				if (c.IsAutoIncrement()) {
					useuseGeneratedKeys.append(useuseGeneratedKeyTemplate.replace("#" + "id" + "#", c.getField()));
				}
			} else {
				excludeIdCols.add(c.getName());
				excludeIdVals.add(valueTemplate.replace("#value#", c.getField()));
				itemValsAll.add(valueTemplate.replace("#value#", "item." + c.getField()));
			}
			String template = resultTemplate.replace("#class.package#", table.getName()).replaceAll("#class.name#",
					className);
			Map<String, String> fieldMap = new LinkedHashMap<String, String>();
			fieldMap.put("EntryValue", c.getName());
			fieldMap.put("EntryKey", c.getField());
			fieldMap.put("Entry", c.IsPrikey() ? "id" : "result");
			for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
				template = template.replace("#" + entry.getKey() + "#", entry.getValue());
			}
			mappings.add(template);
		}
		List<String> andIfEntrys = new ArrayList<String>();
		List<String> commaIfEntrys = new ArrayList<String>();
		for (Column c : columns) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("EntryValue", c.getField());
			map.put("EntryKey", c.getName());
			map.put("preJoiner", "and");
			map.put("sufJoiner", "");
			String template = ifTemplate;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				template = template.replace("#" + entry.getKey() + "#", entry.getValue());
			}
			andIfEntrys.add(template);
			if (!c.IsPrikey()) {
				map.put("preJoiner", "");
				map.put("sufJoiner", " ,");
				template = ifTemplate;
				for (Map.Entry<String, String> entry : map.entrySet()) {
					template = template.replace("#" + entry.getKey() + "#", entry.getValue());
				}
				commaIfEntrys.add(template);
			}
		}

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("table.name", table.getName());
		map.put("table.desc", table.getDesc());
		map.put("class.name", className);
		map.put("class.package", pack);
		map.put("author", author);
		map.put("columns.mapping", Util.join(mappings, "\n\t\t"));
		map.put("id", Util.join(idCols, ", "));
		map.put("col", Util.join(excludeIdCols, ", "));
		map.put("idVal", Util.join(idVals, ", "));
		map.put("val", Util.join(excludeIdVals, ", "));
		map.put("itemValsAll", Util.join(itemValsAll, ", "));
		map.put("andIfEntrys", Util.join(andIfEntrys, "\n\t\t"));
		map.put("commaIfEntrys", Util.join(commaIfEntrys, "\n\t\t"));
		map.put("useuseGeneratedKey", useuseGeneratedKeys.toString());

		for (Map.Entry<String, String> entry : map.entrySet()) {
			mapperTemplate = mapperTemplate.replace("#" + entry.getKey() + "#", entry.getValue());
		}
		sb.append(mapperTemplate);
		return sb.toString();
	}

	/**
	 * 生成实体代码
	 * 
	 * @author DarkIdiot
	 * @param table
	 * @param pack
	 * @param columns
	 * @throws Exception
	 */
	private String getBeanCode(Table table, String pack, String author, List<Column> columns) throws Exception {
		String xml = Util.read(getBeanTemplateLocation());
		long serialVersionUID = new Random().nextLong();
		serialVersionUID = serialVersionUID < 0 ? -serialVersionUID : serialVersionUID;
		String classTemplate = Util.matchs(xml, "<class>([\\w\\W]+?)</class>", 1).get(0);// 匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		String fieldTemplate = Util.matchs(xml, "<field>([\\w\\W]+?)</field>", 1).get(0);
		String methodTemplate = Util.matchs(xml, "<method>([\\w\\W]+?)</method>", 1).get(0);
		String importTemplate = Util.matchs(xml, "<import>([\\w\\W]+?)</import>", 1).get(0);
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
			for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
				template = template.replace("#" + entry.getKey() + "#", entry.getValue());
			}
			fields.append(template);
		}

		StringBuilder imports = new StringBuilder();
		Set<String> fieldSet = new HashSet<String>();
		for (Column c : columns) {
			if (c.getImport() != null) {
				fieldSet.add(c.getImport());
			}
		}
		for (String field : fieldSet) {
			String template = importTemplate;
			template = template.replace("#import#", field);
			imports.append(template + "\n");
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
			for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
				template = template.replace("#" + entry.getKey() + "#", entry.getValue());
			}
			methods.append(template);
		}

		Map<String, String> classMap = new LinkedHashMap<String, String>();
		classMap.put("table.name", table.getName());
		classMap.put("table.desc", table.getDesc());
		classMap.put("class.name", className);
		classMap.put("class.package", pack);
		classMap.put("fields", fields.toString());
		classMap.put("methods", methods.toString());
		classMap.put("now", Util.format(new Date()));
		classMap.put("author", author);
		classMap.put("imports", imports.toString());
		classMap.put("serialVersionUID", String.valueOf(serialVersionUID));
		for (Map.Entry<String, String> entry : classMap.entrySet()) {
			classTemplate = classTemplate.replace("#" + entry.getKey() + "#", entry.getValue());
		}
		return classTemplate.toString();
	}

	/**
	 * 生成Service代码
	 * 
	 * @author DarkIdiot
	 * @param table
	 * @param pack
	 * @return
	 * @throws Exception
	 */
	private String getServiceCode(Table table, String pack, String author) throws Exception {
		String xml = Util.read(getServiceTemplateLocation());
		String serviceTemplate = Util.matchs(xml, "<class>([\\w\\W]+?)</class>", 1).get(0);// 匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		String className = Util.firstCharUpperCase(Util.toFieldName(table.getName()));
		Map<String, String> classMap = new LinkedHashMap<String, String>();
		classMap.put("table.name", table.getName());
		classMap.put("table.desc", table.getDesc());
		classMap.put("class.name", className);
		classMap.put("class.package", pack);
		classMap.put("now", Util.format(new Date()));
		classMap.put("author", author);
		for (Map.Entry<String, String> entry : classMap.entrySet()) {
			serviceTemplate = serviceTemplate.replace("#" + entry.getKey() + "#", entry.getValue());
		}
		return serviceTemplate.toString();
	}

	/**
	 * 生成Dao代码
	 * 
	 * @author DarkIdiot
	 * @param table
	 * @param pack
	 * @param idColumn
	 * @return
	 * @throws Exception
	 */
	private String getDaoCode(Table table, String pack, String author) throws Exception {
		String xml = Util.read(getDaoTemplateLocation());
		String daoTemplate = Util.matchs(xml, "<class>([\\w\\W]+?)</class>", 1).get(0);// 匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		String className = Util.firstCharUpperCase(Util.toFieldName(table.getName()));
		Map<String, String> classMap = new LinkedHashMap<String, String>();
		classMap.put("table.name", table.getName());
		classMap.put("table.desc", table.getDesc());
		classMap.put("class.name", className);
		classMap.put("class.package", pack);
		classMap.put("now", Util.format(new Date()));
		classMap.put("author", author);
		for (Map.Entry<String, String> entry : classMap.entrySet()) {
			daoTemplate = daoTemplate.replace("#" + entry.getKey() + "#", entry.getValue());
		}
		return daoTemplate.toString();
	}

	/**
	 * 生成ServiceImpl代码
	 * 
	 * @author DarkIdiot
	 * @param table
	 * @param pack
	 * @param idColumn
	 * @return
	 * @throws Exception
	 */
	private String getServiceImplCode(Table table, String pack, String author) throws Exception {
		String xml = Util.read(getServiceImplTemplateLocation());
		String serviceImplTemplate = Util.matchs(xml, "<class>([\\w\\W]+?)</class>", 1).get(0);// 匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		String className = Util.firstCharUpperCase(Util.toFieldName(table.getName()));
		Map<String, String> classMap = new LinkedHashMap<String, String>();
		classMap.put("table.name", table.getName());
		classMap.put("table.desc", table.getDesc());
		classMap.put("class.name", className);
		classMap.put("class.package", pack);
		classMap.put("now", Util.format(new Date()));
		classMap.put("author", author);
		for (Map.Entry<String, String> entry : classMap.entrySet()) {
			serviceImplTemplate = serviceImplTemplate.replace("#" + entry.getKey() + "#", entry.getValue());
		}
		return serviceImplTemplate.toString();
	}

	/**
	 * 取得表的数据列
	 * 
	 * @author DarkIdiot
	 * @param p
	 *            参数
	 * @param tableName
	 *            表
	 * @return 数据列
	 * @throws Exception
	 */
	private List<Column> getColumns(Properties p, String tableName) throws Exception {
		String xml = Util.read(SQL_TEMPLATE);
		String sql = Util.matchs(xml, "<column>([\\w\\W]+?)</column>", 1).get(0);// 匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		sql = sql.replace("#table#", tableName);
		sql = sql.replace("#database#", p.getProperty("database"));
		Class.forName(p.getProperty("driver"));
		Connection conn = null;
		ResultSet rs = null;
		List<Column> rows = new ArrayList<Column>();
		try {
			conn = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"),
					p.getProperty("password"));
			rs = conn.prepareStatement(sql.toString()).executeQuery();
			while (rs.next()) {
				Column col = new Column(rs);
				rows.add(col);
			}
		} finally {
			if (conn != null)
				conn.close();
		}
		return rows;
	}

	/**
	 * 取得表信息
	 * 
	 * @author DarkIdiot
	 * @param p
	 *            参数
	 * @param tableName
	 *            表名称
	 * @return
	 * @throws Exception
	 */
	private Table getTable(Properties p, String tableName) throws Exception {
		String xml = Util.read(SQL_TEMPLATE);
		String sql = Util.matchs(xml, "<table>([\\w\\W]+?)</table>", 1).get(0);// 匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。
		sql = sql.replace("#table#", tableName);
		sql = sql.replace("#database#", p.getProperty("database"));
		Connection conn = null;
		ResultSet rs = null;
		Table table = new Table(tableName, tableName);
		try {
			Class.forName(p.getProperty("driver"));
			conn = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"),
					p.getProperty("password"));
			rs = conn.prepareStatement(sql.toString()).executeQuery();
			while (rs.next()) {
				table = new Table(tableName, rs.getString("table_desc"));
			}
		} finally {
			if (conn != null)
				conn.close();
		}
		return table;
	}

	/**
	 * 启动代码服务
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new CodeHelper();
	}
}
