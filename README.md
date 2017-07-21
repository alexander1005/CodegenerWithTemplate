# Mybatis 代码生成小工具

![](https://pandao.github.io/editor.md/images/logos/editormd-logo-180x180.png)

![](https://img.shields.io/github/tag/pandao/editor.md.svg)

+ ①`可视化`数据库连接配置<br>
+ ②自动生成service及dao层代码
> ps:支持Mapper和sqlSession两种配置风格<br>
+ ③代码结构简单、零依赖、可简单个性化定制<br>

### Usage:
```java
    git clone https://git.oschina.net/darkidiot/CodeHelper.git
    java -jar ./CodeHelper/dist/CodeHelper.jar
```
# How to Customize Your own CodeHelper?

## Code Review:
### Java:

```java
    private String getMyBatisCode(Table table, String pack, String author) throws Exception {
		String xml = Util.read(getDaoTemplateLocation());
		String daoTemplate = Util.matchs(xml, "<class>([\\w\\W]+?)</class>", 1).get(0);
        ... ... ...
        return daoTemplate.toString();
	}
```
### xml:
```xml
<mapper namespace="#class.package#.mapper.#class.name#Mapper">
    <resultMap id="BaseResultMap" type="#class.package#.model.#class.name#">
		#columns.mapping#
	</resultMap>
    ... ... ...
	<sql id="tb">
		#table.name#
	</sql>
    
	<sql id="cols_all">
		#id#, <include refid="cols_exclude_id" />
	</sql>
    ... ... ...
	<select id="load" parameterType="long" resultMap="BaseResultMap">
		SELECT
		<include refid="cols_all" />
		FROM
		<include refid="tb" />
		WHERE #id# = #{id}
	</select>
	... ... ...
	<insert id="create" parameterType="#class.package#.model.#class.name#">
	    INSERT INTO 
	    <include refid="tb" /> (<include refid="cols_all"/>)
	    VALUES(<include refid="vals_all"/>)
	</insert>
	... ... ...
	<update id="update" parameterType="#class.package#.model.#class.name#">
	    UPDATE <include refid="tb" />
	    <set>
	    	#commaIfEntrys#
	    </set>
	    WHERE #id# = #idVal#
	</update>
	... ... ...
	<delete id="delete" parameterType="long">
		DELETE FROM <include refid="tb" />
		WHERE #id# = #{id}
	</delete>	
</mapper>
```
#### You can modify above code for generate any code on the basis of your mind.
# End