package tgtools.activiti.impl.drivers.dm6;

import org.activiti.engine.impl.persistence.entity.ByteArrayRef;
import org.activiti.engine.impl.variable.VariableType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 20:33
 */
public class InsertParameterHandler implements ParameterHandler {
    private final TypeHandlerRegistry typeHandlerRegistry;

    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private BoundSql boundSql;
    private Configuration configuration;

    public InsertParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
        changeSql();
    }
    @Override
    public Object getParameterObject() {
        return null;
    }

    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {

    }
    private void changeSql()
    {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        String sql=boundSql.getSql();
        sql=StringUtils.replace(sql,"?","#{param}");

        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                //获取值
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    //替换sql中的占位符
                    sql = StringUtils.replace(sql,"#{param}",getValue(value,typeHandler),1);
                }
            }
            changeSql(sql);
        }

    }

    /**
     * 通过反射替换原有sql
     * @param pSql
     */
    private void changeSql(String pSql)
    {
        try {
            Field field=boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql,pSql);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 根据类型要求获取值
     * @param pValue
     * @param pTypeHandler
     * @return
     */
    private String getValue(Object pValue,TypeHandler pTypeHandler)
    {
        String value="null";
        if(null==pValue)
        {
            return value;
        }
        if(pTypeHandler instanceof org.activiti.engine.impl.db.IbatisVariableTypeHandler)
        {
            value="'"+((VariableType)pValue).getTypeName()+"'";
        }
        else if(pTypeHandler instanceof org.apache.ibatis.type.StringTypeHandler)
        {
            value="'"+pValue.toString()+"'";
        }
        else if(pTypeHandler instanceof org.apache.ibatis.type.IntegerTypeHandler)
        {
            value=pValue.toString();
        }
        else if(pTypeHandler instanceof org.activiti.engine.impl.persistence.ByteArrayRefTypeHandler)
        {
            if(null!=pValue && (pValue instanceof ByteArrayRef)&&null!=((ByteArrayRef)pValue).getId()) {
                value = "'"+((ByteArrayRef)pValue).getId()+"'";
            }
        }
        else if(pTypeHandler instanceof org.apache.ibatis.type.DoubleTypeHandler)
        {
            value=pValue.toString();
        }
        else if(pTypeHandler instanceof org.apache.ibatis.type.LongTypeHandler)
        {
            value=pValue.toString();
        }
        else if(pTypeHandler instanceof org.apache.ibatis.type.DateTypeHandler)
        {
            value= "'"+DateFormatUtils.format((Date)pValue,"yyyy-MM-dd hh:mm:ss")+"'";
        }
        return value;
    }
}
