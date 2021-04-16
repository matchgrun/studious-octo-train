/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.owner;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.wh.siam.core.dao.impl.SiamTableNames;
import com.ibm.wh.siam.core.util.rdbms.RdbmsUtil;

/**
 * @author Match Grun
 *
 */
@Component
public class OwnerSqlBuilder
implements OwnerSqlBuilderIF
{

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger( OwnerSqlBuilder.class );

    private static Map<String,String> mapSqlQuery = buildSqlQueryMap();
    private static Map<String,String> mapSqlQueryValidate = buildSqlQueryMapValidate();

    /**
     * Populate map entry for specified owner attribute.
     * @param map       Map to populate.
     * @param ownerRef  Owner reference.
     */
    private static void buildMapEntry(
            final Map<String, String> map,
            final OwnerRefIF ownerRef )
    {
        String sql = buildSqlStatement( ownerRef );
        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "sql=" + sql);
        }
        */
        map.put( ownerRef.getOwnerType(), sql );
    }

    /**
     * Build all map entries.
     * @return Map of SQL statement, keyed by owner type.
     */
    private static Map<String, String> buildSqlQueryMap() {
        Map<String, String> map = new HashMap<String,String>();
        buildMapEntry( map, new OwnerRefOrganization() );
        buildMapEntry( map, new OwnerRefGroup() );
        buildMapEntry( map, new OwnerRefPerson() );
        buildMapEntry( map, new OwnerRefAccount() );
        return map;
    }

    /**
     * Populate map entry for specified owner attribute.
     * @param map       Map to populate.
     * @param ownerRef  Owner reference.
     */
    private static void buildMapEntryValidate(
            final Map<String, String> map,
            final OwnerRefIF ownerRef )
    {
        String sql = buildSqlStatementValidate( ownerRef );
        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "sql=" + sql);
        }
        */
        map.put( ownerRef.getOwnerType(), sql );
    }

    /**
     * Build all map entries.
     * @return Map of SQL statement, keyed by owner type.
     */
    private static Map<String, String> buildSqlQueryMapValidate() {
        Map<String, String> map = new HashMap<String,String>();
        buildMapEntryValidate( map, new OwnerRefOrganization() );
        buildMapEntryValidate( map, new OwnerRefGroup() );
        buildMapEntryValidate( map, new OwnerRefProduct() );
        buildMapEntryValidate( map, new OwnerRefAccount() );
        buildMapEntryValidate( map, new OwnerRefRole() );
        buildMapEntryValidate( map, new OwnerRefContentSet() );
        buildMapEntryValidate( map, new OwnerRefContentSetAccess() );
        return map;
    }

    @Override
    public String fetchSqlStatement( final String ownerType ) {
        return mapSqlQuery.get( ownerType );
    }

    /**
     * Build SQL statement for specified owner.
     * @param ownerRef  Owner reference.
     * @return SQL statement.
     */
    private static String buildSqlStatement( final OwnerRefIF ownerRef ) {
        StringBuilder sb = new StringBuilder( 200 );
        if( ownerRef == null ) {
            return null;
        }

        sb.append( "select \n" );
        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, ownerRef.getTableName(), ownerRef.getColumnId(), ALIAS_ID, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, ownerRef.getTableName(), ownerRef.getColumnCode(), ALIAS_CODE, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, ownerRef.getTableName(), ownerRef.getColumnName(), ALIAS_NAME, false );
        sb.append( ",\n" );

        sb.append( "  " );
        sb.append( ":" ).append( PARM_NAME_OWNER_TYPE ).append( " as " ).append( ALIAS_TYPE );
        sb.append( "\n" );

        sb.append( "from\n" );
        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( ownerRef.getTableName() ).append( "\n" );

        sb.append( "where\n");
        sb.append( "  " ).append( ownerRef.getColumnId() ).append( " = :" ).append( PARM_NAME_OWNER_ID );

        return sb.toString();
    }

    /**
     * Build SQL statement for specified owner.
     * @param ownerAttrib   Owner attribute.
     * @return SQL statement.
     */
    private static String buildSqlStatementValidate( final OwnerRefIF ownerAttrib ) {
        StringBuilder sb = new StringBuilder( 200 );
        if( ownerAttrib == null ) {
            return null;
        }

        sb.append( "select \n" );
        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, ownerAttrib.getTableName(), ownerAttrib.getColumnId(), ALIAS_ID, false );
        sb.append( "\n" );

        sb.append( "from\n" );
        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( ownerAttrib.getTableName() ).append( "\n" );

        sb.append( "where\n");
        sb.append( "  " ).append( ownerAttrib.getColumnId() ).append( " = :owner_id" );

        return sb.toString();
    }

    @Override
    public String fetchSqlStatementValidate( final String ownerType ) {
        String sql = mapSqlQueryValidate.get( ownerType );
        return sql;
    }

}
