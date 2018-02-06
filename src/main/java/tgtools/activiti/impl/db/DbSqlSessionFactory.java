package tgtools.activiti.impl.db;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 19:39
 */
public class DbSqlSessionFactory extends org.activiti.engine.impl.db.DbSqlSessionFactory {
    public static void addDM() {

        databaseSpecificLimitBeforeStatements.put("dm", "select * from ( select a.*, ROWNUM rnum from (");
        databaseSpecificLimitAfterStatements.put("dm", "  ) a where ROWNUM < #{lastRow}) where rnum  >= #{firstRow}");
        databaseSpecificLimitBetweenStatements.put("dm", "");
        databaseOuterJoinLimitBetweenStatements.put("dm", "");
        databaseSpecificOrderByStatements.put("dm", " order By  ${orderBy} ");
        addDatabaseSpecificStatement("dm", "selectExclusiveJobsToExecute", "selectExclusiveJobsToExecute_integerBoolean");
        addDatabaseSpecificStatement("dm", "selectUnlockedTimersByDuedate", "selectUnlockedTimersByDuedate");
        addDatabaseSpecificStatement("dm", "insertEventLogEntry", "insertEventLogEntry_oracle");
        //2.17
        databaseSpecificOrderByStatements.put("dm", " order by ${orderByColumns} ");
        addDatabaseSpecificStatement("dm", "bulkInsertVariableInstance", "bulkInsertVariableInstance");
        addDatabaseSpecificStatement("dm", "bulkInsertUser", "bulkInsertUser");
        addDatabaseSpecificStatement("dm", "bulkInsertTask", "bulkInsertTask");
        addDatabaseSpecificStatement("dm", "bulkInsertResource", "bulkInsertResource");
        addDatabaseSpecificStatement("dm", "bulkInsertProperty", "bulkInsertProperty");
        addDatabaseSpecificStatement("dm", "bulkInsertProcessDefinition", "bulkInsertProcessDefinition");
        addDatabaseSpecificStatement("dm", "bulkInsertModel", "bulkInsertModel");
        addDatabaseSpecificStatement("dm", "bulkInsertMembership", "bulkInsertMembership");
        addDatabaseSpecificStatement("dm", "bulkInsertTimer", "bulkInsertTimer");
        addDatabaseSpecificStatement("dm", "bulkInsertMessage", "bulkInsertMessage");
        addDatabaseSpecificStatement("dm", "bulkInsertIdentityInfo", "bulkInsertIdentityInfo");
        addDatabaseSpecificStatement("dm", "bulkInsertIdentityLink", "bulkInsertIdentityLink");
        addDatabaseSpecificStatement("dm", "bulkInsertMembership", "bulkInsertMembership");
        addDatabaseSpecificStatement("dm", "bulkInsertTimer", "bulkInsertTimer");
        addDatabaseSpecificStatement("dm", "bulkInsertMessage", "bulkInsertMessage");
        addDatabaseSpecificStatement("dm", "bulkInsertHistoricVariableInstance", "bulkInsertHistoricVariableInstance");
        addDatabaseSpecificStatement("dm", "bulkInsertHistoricTaskInstance", "bulkInsertHistoricTaskInstance");
        addDatabaseSpecificStatement("dm", "bulkInsertHistoricProcessInstance", "bulkInsertHistoricProcessInstance");
        addDatabaseSpecificStatement("dm", "bulkInsertHistoricIdentityLink", "bulkInsertHistoricIdentityLink");
        addDatabaseSpecificStatement("dm", "bulkInsertHistoricDetailVariableInstanceUpdate", "bulkInsertHistoricDetailVariableInstanceUpdate");
        addDatabaseSpecificStatement("dm", "bulkInsertHistoricFormProperty", "bulkInsertHistoricFormProperty");
        addDatabaseSpecificStatement("dm", "bulkInsertHistoricActivityInstance", "bulkInsertHistoricActivityInstance");
        addDatabaseSpecificStatement("dm", "bulkInsertGroup", "bulkInsertGroup");
        addDatabaseSpecificStatement("dm", "bulkInsertExecution", "bulkInsertExecution");
        addDatabaseSpecificStatement("dm", "bulkInsertMessageEventSubscription", "bulkInsertMessageEventSubscription");
        addDatabaseSpecificStatement("dm", "bulkInsertSignalEventSubscription", "bulkInsertSignalEventSubscription");
        addDatabaseSpecificStatement("dm", "bulkInsertCompensateEventSubscription", "bulkInsertCompensateEventSubscription");
        //addDatabaseSpecificStatement("dm", "bulkInsertEventLogEntry", "bulkInsertEventLogEntry_dm");
        addDatabaseSpecificStatement("dm", "bulkInsertDeployment", "bulkInsertDeployment");
        addDatabaseSpecificStatement("dm", "bulkInsertComment", "bulkInsertComment");
        addDatabaseSpecificStatement("dm", "bulkInsertByteArray", "bulkInsertByteArray");
        addDatabaseSpecificStatement("dm", "bulkInsertAttachment", "bulkInsertAttachment");
    }
}