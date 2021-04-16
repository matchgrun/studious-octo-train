/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.dao.impl.entitlement;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.dao.impl.SiamTableNames;

/**
 * @author Match Grun
 *
 */
@Component
public class EntitlementOwnerSqlBuilder
implements EntitlementOwnerSqlBuilderIF
{

    private static final String JOIN_PRODUCT_ID =
            SiamTableNames.ENTITLEMENT + ".product_id" +
            " = " +
            SiamTableNames.PRODUCT + ".product_id";

    private static Map<String,String> mapSqlQueryProduct = buildSqlQueryMapProduct();
    private static Map<String,String> mapSqlQuery = buildSqlQueryMap();

    /**
     * Build column specification for column.
     * @param tableName   Table name.
     * @param columnName  Column name.
     * @param columnAlias Column alias.
     * @return  Column specification.
     */

    private static String buildColumnSpec(
            final String tableName,
            final String columnName,
            final String columnAlias )
    {
        StringBuilder sb = new StringBuilder( 50 );
        sb.append( tableName ).append(".").append(columnName );
        if( ! StringUtils.isEmpty( columnAlias ) ) {
            sb.append( " as " ).append( columnAlias );
        }
        return sb.toString();
    }

    /**
     * Build column specification for owner Id
     * @param ownerEnt  Entitlement owner.
     * @return  Column specification.
     */
    private static String buildColumnSpecOwnerId( final EntitlementOwnerIF ownerEnt ) {
        return buildColumnSpec( ownerEnt.getTableName(), ownerEnt.getColumnId(), "owner_id" );
    }

    /**
     * Build column specification for owner code
     * @param ownerEnt  Entitlement owner.
     * @return  Column specification.
     */
    private static String buildColumnSpecOwnerCode( final EntitlementOwnerIF ownerEnt ) {
        return buildColumnSpec( ownerEnt.getTableName(), ownerEnt.getColumnCode(), "owner_code" );
    }

    /**
     * Build column specification for owner name
     * @param ownerEnt  Entitlement owner.
     * @return  Column specification.
     */
    private static String buildColumnSpecOwnerName( final EntitlementOwnerIF ownerEnt ) {
        return buildColumnSpec( ownerEnt.getTableName(), ownerEnt.getColumnName(), "owner_name" );
    }

    /**
     * Build SQL statement for specified entitlement owner.
     * @param ownerEnt Entitlement owner definition.
     * @return SQL statement.
     */
    private static String buildSqlProduct( final EntitlementOwnerIF ownerEnt ) {
        StringBuilder sb = new StringBuilder( 300 );
        sb.append( "select \n" );

        sb.append( "  " ).append( buildColumnSpecOwnerId( ownerEnt ) ).append( ",\n" );
        sb.append( "  " ).append( buildColumnSpecOwnerCode( ownerEnt ) ).append( ",\n" );
        sb.append( "  " ).append( buildColumnSpecOwnerName( ownerEnt ) ).append( ",\n" );

        sb.append( "  " ).append( SiamTableNames.ENTITLEMENT ).append( ".entitlement_id,\n");
        sb.append( "  " ).append( SiamTableNames.ENTITLEMENT ).append( ".owner_type,\n");
        sb.append( "  " ).append( SiamTableNames.ENTITLEMENT ).append( ".start_date as entitlement_start,\n");
        sb.append( "  " ).append( SiamTableNames.ENTITLEMENT ).append( ".end_date as entitlement_end,\n");
        sb.append( "  " ).append( SiamTableNames.ENTITLEMENT ).append( ".status as entitlement_status,\n");

        sb.append( "  " ).append( SiamTableNames.PRODUCT ).append( ".product_id,\n");
        sb.append( "  " ).append( SiamTableNames.PRODUCT ).append( ".product_code,\n");
        sb.append( "  " ).append( SiamTableNames.PRODUCT ).append( ".description as product_name,\n");
        sb.append( "  " ).append( SiamTableNames.PRODUCT ).append( ".start_date as product_start,\n");
        sb.append( "  " ).append( SiamTableNames.PRODUCT ).append( ".end_date as product_end,\n");
        sb.append( "  " ).append( SiamTableNames.PRODUCT ).append( ".status as product_status\n");

        sb.append( "from\n" );
        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.PRODUCT ).append( ",\n" );
        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.ENTITLEMENT ).append( ",\n" );
        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( ownerEnt.getTableName() ).append( "\n" );

        sb.append( "where  " ).append( SiamTableNames.ENTITLEMENT ).append( ".owner_type = :ownerType\n" );
        sb.append( "and    " ).append( SiamTableNames.ENTITLEMENT ).append( ".owner_id = " );
        sb.append( ownerEnt.getTableName() ).append( "." ).append( ownerEnt.getColumnId() ).append( "\n" );
        sb.append( "and    " ).append( JOIN_PRODUCT_ID ).append( "\n" );

        // sb.append( "order by  " ).append( ownerAttrib.getTableName() ).append( "." ).append( ownerAttrib.getColumnOrder() );
        return sb.toString();
    }

    /**
     * Populate map entry for specified owner attribute.
     * @param map           Map to populate.
     * @param ownerAttrib   Owner attribute.
     */
    private static void buildMapEntryProduct(
            final Map<String, String> map,
            final EntitlementOwnerIF ownerEnt )
    {
        String sql = buildSqlProduct( ownerEnt );
        map.put( ownerEnt.getOwnerType(), sql );
    }

    /**
     * Build all map entries.
     * @return Map of SQL statement, keyed by owner type.
     */
    private static Map<String, String> buildSqlQueryMapProduct() {
        Map<String, String> map = new HashMap<String,String>();
        buildMapEntryProduct( map, new EntitlementOwnerOrganization() );
        buildMapEntryProduct( map, new EntitlementOwnerGroup() );
        return map;
    }

    @Override
    public String fetchSqlStatementProductList( final String ownerType ) {
        String sql = mapSqlQueryProduct.get( ownerType );
        return sql;
    }

    /**
     * Build SQL statement for specified entitlement owner.
     * @param ownerEnt Entitlement owner definition.
     * @return SQL statement.
     */
    private static String buildSqlOwner( final EntitlementOwnerIF ownerEnt ) {
        StringBuilder sb = new StringBuilder( 300 );
        sb.append( "select \n" );

//        sb.append( "  " ).append( buildColumnSpecOwnerId( ownerEnt ) ).append( ",\n" );
//        sb.append( "  " ).append( buildColumnSpecOwnerCode( ownerEnt ) ).append( ",\n" );
//        sb.append( "  " ).append( buildColumnSpecOwnerName( ownerEnt ) ).append( ",\n" );
//
//        sb.append( "  " ).append( SiamTableNames.ENTITLEMENT ).append( ".entitlement_id,\n");
//        sb.append( "  " ).append( SiamTableNames.ENTITLEMENT ).append( ".owner_type,\n");
//        sb.append( "  " ).append( SiamTableNames.ENTITLEMENT ).append( ".start_date as entitlement_start,\n");
//        sb.append( "  " ).append( SiamTableNames.ENTITLEMENT ).append( ".end_date as entitlement_end,\n");
//        sb.append( "  " ).append( SiamTableNames.ENTITLEMENT ).append( ".status as entitlement_status,\n");
//
//        sb.append( "from\n" );
//        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.ENTITLEMENT ).append( ",\n" );
//        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( ownerEnt.getTableName() ).append( "\n" );
//
//        sb.append( "where  " ).append( SiamTableNames.ENTITLEMENT ).append( ".owner_type = :ownerType\n" );
//        sb.append( "and    " ).append( SiamTableNames.ENTITLEMENT ).append( ".owner_id = :ownerId" );

        return sb.toString();
    }

    /**
     * Populate map entry for specified owner attribute.
     * @param map           Map to populate.
     * @param ownerAttrib   Owner attribute.
     */
    private static void buildMapEntry(
            final Map<String, String> map,
            final EntitlementOwnerIF ownerEnt )
    {
        String sql = buildSqlOwner( ownerEnt );
        map.put( ownerEnt.getOwnerType(), sql );
    }

    /**
     * Build all map entries.
     * @return Map of SQL statement, keyed by owner type.
     */
    private static Map<String, String> buildSqlQueryMap() {
        Map<String, String> map = new HashMap<String,String>();
        buildMapEntry( map, new EntitlementOwnerOrganization() );
        buildMapEntry( map, new EntitlementOwnerGroup() );
        return map;
    }

    @Override
    public String fetchSqlStatement( final String ownerType ) {
        String sql = mapSqlQuery.get( ownerType );
        return sql;
    }

}
