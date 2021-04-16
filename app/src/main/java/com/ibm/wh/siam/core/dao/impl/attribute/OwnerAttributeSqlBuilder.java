/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.attribute;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ibm.wh.siam.core.dao.impl.SiamTableNames;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefAccount;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefContentSet;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefContentSetAccess;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefGroup;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefIF;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefOrganization;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefPerson;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefProduct;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefRole;
import com.ibm.wh.siam.core.util.rdbms.RdbmsUtil;

/**
 * @author Match Grun
 *
 */
@Component
public class OwnerAttributeSqlBuilder
implements OwnerAttributeSqlBuilderIF
{
    /**
     * Column names.
     */
    private static final String COL_OWNER_ID = "owner_id";
    private static final String COL_OWNER_TYPE = "owner_type";
    private static final String COL_DESC_ID = "attribute_descriptor_id";
    private static final String COL_ATTRIB_NAME = "attribute_name";

    private static final String JOIN_DESCRIPTOR_ID =
            RdbmsUtil.buildJoinMatch(
                    SiamTableNames.ATTRIBUTE_VALUE, COL_DESC_ID, SiamTableNames.ATTRIBUTE_DESCRIPTOR, COL_DESC_ID );

    private static Map<String,String> mapSqlQuery = buildSqlQueryMap();

    /**
     * Build SQL statement for specified owner attribute.
     * @param ownerAttrib   Owner attribute definition.
     * @return SQL statement.
     */
    private static String buildSql( final OwnerRefIF ownerAttrib ) {
        StringBuilder sb = new StringBuilder( 200 );
        sb.append( "select distinct\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, SiamTableNames.ATTRIBUTE_VALUE, COL_OWNER_ID, ALIAS_OWNER_ID, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, ownerAttrib.getTableName(), ownerAttrib.getColumnName(), ALIAS_OWNER_NAME, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, ownerAttrib.getTableName(), ownerAttrib.getColumnCode(), ALIAS_OWNER_CODE, false );
        sb.append( "\n" );

        sb.append( "from\n" );
        sb.append( "  " );
        RdbmsUtil.buildTableSelection( sb, SiamTableNames.SIAM_DB_NAME, SiamTableNames.ATTRIBUTE_VALUE, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildTableSelection( sb, SiamTableNames.SIAM_DB_NAME, SiamTableNames.ATTRIBUTE_DESCRIPTOR, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildTableSelection( sb, SiamTableNames.SIAM_DB_NAME, ownerAttrib.getTableName(), false );
        sb.append( "\n" );

        sb.append( "where  " );
        RdbmsUtil.buildJoinMatchParameter(sb, SiamTableNames.ATTRIBUTE_VALUE, COL_OWNER_TYPE, PARAM_NAME_OWNER_TYPE );
        sb.append( "\n" );

        sb.append( "and    " );
        RdbmsUtil.buildJoinMatchParameter(sb, SiamTableNames.ATTRIBUTE_DESCRIPTOR, COL_ATTRIB_NAME, PARAM_NAME_DESC_NAME );
        sb.append( "\n" );

        sb.append( "and    " ).append( JOIN_DESCRIPTOR_ID ).append( "\n" );

        sb.append( "and    " );
        RdbmsUtil.buildJoinMatch(
                sb, SiamTableNames.ATTRIBUTE_VALUE, COL_OWNER_ID, ownerAttrib.getTableName(), ownerAttrib.getColumnId() );
        sb.append( "\n" );


        sb.append( "order by  " );
        RdbmsUtil.buildColumnSelection( sb, ownerAttrib.getTableName(), ownerAttrib.getColumnOrder() );


        return sb.toString();
    }

    /**
     * Populate map entry for specified owner attribute.
     * @param map           Map to populate.
     * @param ownerAttrib   Owner attribute.
     */
    private static void buildMapEntry(
            final Map<String, String> map,
            final OwnerRefIF ownerAttrib )
    {
        String sql = buildSql( ownerAttrib );
        map.put( ownerAttrib.getOwnerType(), sql );
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
        buildMapEntry( map, new OwnerRefProduct() );
        buildMapEntry( map, new OwnerRefAccount() );
        buildMapEntry( map, new OwnerRefRole() );
        buildMapEntry( map, new OwnerRefContentSet() );
        buildMapEntry( map, new OwnerRefContentSetAccess() );
        return map;
    }

    @Override
    public String fetchSqlStatement( final String ownerType ) {
        String sql = mapSqlQuery.get( ownerType );
        return sql;
    }

}
