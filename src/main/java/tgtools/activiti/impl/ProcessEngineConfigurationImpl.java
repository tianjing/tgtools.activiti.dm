package tgtools.activiti.impl;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 19:37
 */

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.impl.util.ReflectUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import tgtools.activiti.impl.db.DbSqlSessionFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ProcessEngineConfigurationImpl extends org.activiti.spring.SpringProcessEngineConfiguration {

    static {
        databaseTypeMappings = getDatabaseTypeMappings();
    }


    protected static Properties getDatabaseTypeMappings() {
        Properties databaseTypeMappings = new Properties();
        databaseTypeMappings.setProperty("H2", "h2");
        databaseTypeMappings.setProperty("MySQL", "mysql");
        databaseTypeMappings.setProperty("Oracle", "oracle");
        databaseTypeMappings.setProperty("PostgreSQL", "postgres");
        databaseTypeMappings.setProperty("Microsoft SQL Server", "mssql");
        databaseTypeMappings.setProperty("DB2", "db2");
        databaseTypeMappings.setProperty("DB2", "db2");
        databaseTypeMappings.setProperty("DB2/NT", "db2");
        databaseTypeMappings.setProperty("DB2/NT64", "db2");
        databaseTypeMappings.setProperty("DB2 UDP", "db2");
        databaseTypeMappings.setProperty("DB2/LINUX", "db2");
        databaseTypeMappings.setProperty("DB2/LINUX390", "db2");
        databaseTypeMappings.setProperty("DB2/LINUXZ64", "db2");
        databaseTypeMappings.setProperty("DB2/400 SQL", "db2");
        databaseTypeMappings.setProperty("DB2/6000", "db2");
        databaseTypeMappings.setProperty("DB2 UDB iSeries", "db2");
        databaseTypeMappings.setProperty("DB2/AIX64", "db2");
        databaseTypeMappings.setProperty("DB2/HPUX", "db2");
        databaseTypeMappings.setProperty("DB2/HP64", "db2");
        databaseTypeMappings.setProperty("DB2/SUN", "db2");
        databaseTypeMappings.setProperty("DB2/SUN64", "db2");
        databaseTypeMappings.setProperty("DB2/PTX", "db2");
        databaseTypeMappings.setProperty("DB2/2", "db2");
        databaseTypeMappings.setProperty("DM DBMS", "dm");
        databaseTypeMappings.setProperty("DM6 DBMS", "dm6");
        return databaseTypeMappings;
    }
    @Override
    protected InputStream getMyBatisXmlConfigurationSteam() {
        if (isDM6()) {
            return ReflectUtil.getResourceAsStream("tgtools/activiti/db/mappings/mappings.xml");
        }
        return super.getMyBatisXmlConfigurationSteam();
    }
    @Override
    protected void initSqlSessionFactory()
    {
        DbSqlSessionFactory.addDM7();
        DbSqlSessionFactory.addDM6();
        if(this.sqlSessionFactory == null) {
            InputStream inputStream = null;
            try {
                inputStream = this.getMyBatisXmlConfigurationSteam();

                Environment   e = new Environment("default", this.transactionFactory, this.dataSource);

                InputStreamReader reader = new InputStreamReader(inputStream);
                Properties properties = new Properties();
                properties.put("prefix", this.databaseTablePrefix);
                if(this.databaseType != null) {
                    properties.put("limitBefore", DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.get(this.databaseType));
                    properties.put("limitAfter", DbSqlSessionFactory.databaseSpecificLimitAfterStatements.get(this.databaseType));
                    properties.put("limitBetween", DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.get(this.databaseType));
                    properties.put("limitOuterJoinBetween", DbSqlSessionFactory.databaseOuterJoinLimitBetweenStatements.get(this.databaseType));
                    properties.put("orderBy", DbSqlSessionFactory.databaseSpecificOrderByStatements.get(this.databaseType));
                    properties.put("limitBeforeNativeQuery", ObjectUtils.toString(DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.get(this.databaseType)));
                }

                Configuration configuration = this.initMybatisConfiguration(e, reader, properties);
                this.sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
            } catch (Exception var9) {
                throw new ActivitiException("Error while building ibatis SqlSessionFactory: " + var9.getMessage(), var9);
            } finally {
                IoUtil.closeSilently(inputStream);
            }
        }
    }


    private boolean isDM6()
    {
        return "dm6".equals(getDatabaseType());
    }
}