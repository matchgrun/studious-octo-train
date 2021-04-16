/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl;

import com.ibm.wh.siam.core.dto.BaseSiamObject;
import com.ibm.wh.siam.core.util.StatusUtil;

/**
 * @author Match Grun
 *
 */
public class BaseSiamDAO {

    protected java.sql.Date toSqlDate( final java.util.Date date ) {
        java.sql.Date jsd = null;
        if( date != null ) {
            jsd = new java.sql.Date( date.getTime() );
        }
        return jsd;
    }

//    protected static final String STR_PERIOD = ".";
//    protected static final String STR_EQUAL = " = ";
//    protected static final String STR_SEPAR = ", ";
//
//    /**
//     * Build join SQL clause into buffer for two tables in specified schema.
//     * @param sb            Buffer.
//     * @param schemaName    Schema name.
//     * @param table1        Table name 1.
//     * @param column1       Column name for table 1.
//     * @param table2        Table name 2.
//     * @param column2       Column name for table 2.
//     */
//    protected void buildJoinMatch(
//            final StringBuilder sb,
//            final String schemaName,
//            final String table1,
//            final String column1,
//            final String table2,
//            final String column2
//            )
//    {
//        if( ! StringUtils.isEmpty( schemaName ) ) {
//            sb.append( schemaName ).append( STR_PERIOD );
//        }
//        sb.append( table1 ).append( STR_PERIOD ).append( column1 ).append( STR_EQUAL );
//
//        if( ! StringUtils.isEmpty( schemaName ) ) {
//            sb.append( schemaName ).append( STR_PERIOD );
//        }
//        sb.append( table2 ).append( STR_PERIOD ).append( column2 );
//    }
//
//    /**
//     * Build join SQL clause into buffer for two tables.
//     * @param sb        Buffer.
//     * @param table1    Table name 1.
//     * @param column1   Column name for table 1.
//     * @param table2    Table name 2.
//     * @param column2   Column name for table 2.
//     */
//    protected static void buildJoinMatch(
//            final StringBuilder sb,
//            final String table1,
//            final String column1,
//            final String table2,
//            final String column2
//            )
//    {
//        sb.append( table1 ).append( STR_PERIOD ).append( column1 ).append( STR_EQUAL );
//        sb.append( table2 ).append( STR_PERIOD ).append( column2 );
//    }
//
//    /**
//     * Build column selection into buffer.
//     * @param sb        Buffer.
//     * @param table     Table name.
//     * @param column    Column name for table.
//     */
//    protected static void buildColumnSelection(
//            final StringBuilder sb,
//            final String table,
//            final String column )
//    {
//        sb.append( table ).append( STR_PERIOD ).append( column );
//    }
//
//    /**
//     * Build column selection into buffer.
//     * @param sb        Buffer.
//     * @param table     Table name.
//     * @param column    Column name for table.
//     * @param useSep    <i>true</i> to append separator.
//     */
//    protected static void buildColumnSelection(
//            final StringBuilder sb,
//            final String table,
//            final String column,
//            final boolean useSep )
//    {
//        sb.append( table ).append( STR_PERIOD ).append( column );
//        if( useSep ) sb.append( STR_SEPAR );
//    }

    //    protected void updateCreator(
//            final RecordUpdaterIF recordUpdater,
//            final BaseSiamObject obj )
//    {
//        obj.setCreatedBy(recordUpdater.getName());
//        obj.setCreatedByApp(recordUpdater.getApplication());
//    }
//
//    protected void updateModifier(
//            final RecordUpdaterIF recordUpdater,
//            final BaseSiamObject obj )
//    {
//        obj.setModifiedBy(recordUpdater.getName());
//        obj.setModifiedByApp(recordUpdater.getApplication());
//    }

    /**
     * Normalize object for database update.
     * @param obj   Object to process.
     */
    protected void normalizeObject( final BaseSiamObject obj ) {
        if( obj != null ) {
            String status = obj.getStatus();
            obj.setStatus( StatusUtil.defaultValue(status) );
        }
    }

}
