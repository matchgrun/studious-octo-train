/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.util.rdbms;

import java.util.List;

import org.springframework.util.StringUtils;

/**
 * @author Match Grun
 *
 */
public class RdbmsUtil {

    protected static final String STR_COMMA = ",";
    protected static final String STR_PERIOD = ".";
    protected static final String STR_EQUAL = " = ";
    protected static final String STR_SEPAR = ", ";
    protected static final String STR_APOS = "'";
    protected static final String STR_COLON = ":";

    // Column alias
    protected static final String STR_AS = " as ";

    /**
     * Build join SQL clause into buffer for two tables in specified schema.
     * @param sb            Buffer.
     * @param schemaName    Schema name.
     * @param table1        Table name 1.
     * @param column1       Column name for table 1.
     * @param table2        Table name 2.
     * @param column2       Column name for table 2.
     */
    public static void buildJoinMatch(
            final StringBuilder sb,
            final String schemaName,
            final String table1,
            final String column1,
            final String table2,
            final String column2
            )
    {
        if( ! StringUtils.isEmpty( schemaName ) ) {
            sb.append( schemaName ).append( STR_PERIOD );
        }
        sb.append( table1 ).append( STR_PERIOD ).append( column1 ).append( STR_EQUAL );

        if( ! StringUtils.isEmpty( schemaName ) ) {
            sb.append( schemaName ).append( STR_PERIOD );
        }
        sb.append( table2 ).append( STR_PERIOD ).append( column2 );
    }

    /**
     * Build join SQL clause into buffer for two tables.
     * @param sb        Buffer.
     * @param table1    Table name 1.
     * @param column1   Column name for table 1.
     * @param table2    Table name 2.
     * @param column2   Column name for table 2.
     */
    public static void buildJoinMatch(
            final StringBuilder sb,
            final String table1,
            final String column1,
            final String table2,
            final String column2
            )
    {
        sb.append( table1 ).append( STR_PERIOD ).append( column1 ).append( STR_EQUAL );
        sb.append( table2 ).append( STR_PERIOD ).append( column2 );
    }

    /**
     * Build join SQL clause for two tables.
     * @param table1    Table name 1.
     * @param column1   Column name for table 1.
     * @param table2    Table name 2.
     * @param column2   Column name for table 2.
     * @return  Formatted string.
     */
    public static String buildJoinMatch(
            final String table1,
            final String column1,
            final String table2,
            final String column2
            )
    {
        StringBuilder sb = new StringBuilder(100);
        buildJoinMatch( sb, table1, column1, table2, column2 );
        return sb.toString();
    }

    /**
     * Build join SQL clause into buffer for table/column with parameter name.
     * @param sb        Buffer.
     * @param table     Table name 1.
     * @param column    Column name for table 1.
     * @param paramName Parameter name.
     */
    public static void buildJoinMatchParameter(
            final StringBuilder sb,
            final String table,
            final String column,
            final String paramName
            )
    {
        sb.append( table ).append( STR_PERIOD ).append( column ).append( STR_EQUAL );
        sb.append( STR_COLON ).append( paramName );
    }

    /**
     * Build column selection into buffer.
     * @param sb        Buffer.
     * @param table     Table name.
     * @param column    Column name for table.
     */
    public static void buildColumnSelection(
            final StringBuilder sb,
            final String table,
            final String column )
    {
        sb.append( table ).append( STR_PERIOD ).append( column );
    }

    /**
     * Build column selection into buffer.
     * @param sb        Buffer.
     * @param table     Table name.
     * @param column    Column name for table.
     * @param useSep    <i>true</i> to append separator.
     */
    public static void buildColumnSelection(
            final StringBuilder sb,
            final String table,
            final String column,
            final boolean useSep )
    {
        sb.append( table ).append( STR_PERIOD ).append( column );
        if( useSep ) sb.append( STR_SEPAR );
    }

    /**
     * Build table selection into buffer.
     * @param sb        Buffer.
     * @param dbname    Database name for table.
     * @param table     Table name.
     * @param useSep    <i>true</i> to append separator.
     */
    public static void buildTableSelection(
            final StringBuilder sb,
            final String dbname,
            final String table,
            final boolean useSep )
    {
        sb.append( dbname).append( STR_PERIOD ).append( table );
        if( useSep ) sb.append( STR_SEPAR );
    }

    /**
     * Build column selection into buffer.
     * @param sb        Buffer.
     * @param table     Table name.
     * @param column    Column name for table.
     * @param aliasName Alias name.
     * @param useSep    <i>true</i> to append separator.
     */
    public static void buildColumnSelectionAlias(
            final StringBuilder sb,
            final String table,
            final String column,
            final String aliasName,
            final boolean useSep )
    {
        sb.append( table ).append( STR_PERIOD ).append( column );
        sb.append( STR_AS ).append( aliasName );
        if( useSep ) sb.append( STR_SEPAR );
    }

    /**
     * Build separated list of strings used as ID's.
     * @param listIds List of ID's.
     * @return Separated list.
     */
    public static String buildSeparatedIdList( final List<String> listIds ) {
        int len = listIds.size() * 24;
        StringBuilder sb = new StringBuilder(len);
        buildSeparatedIdList(sb, listIds);
        return sb.toString();
    }

    /**
     * Build separated list of strings used as ID's into specified buffer.
     * @param sb        Buffer.
     * @param listIds   List of ID's.
     * @return Separated list.
     */
    public static void buildSeparatedIdList(
            final StringBuilder sb,
            final List<String> listIds )
    {
        if( listIds != null ) {
            listIds.forEach( str -> {
                if( str != null ) {
                    str = str.trim();
                    if( str.length() > 0 ) {
                        if( sb.length() > 0 ) sb.append(STR_SEPAR);
                        sb.append( STR_APOS ).append(str).append(STR_APOS);
                    }
                }
            });
        }
    }

}
