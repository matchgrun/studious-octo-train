/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.wh.siam.core.dao.impl.SiamTableNames;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefGroup;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefIF;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefOrganization;
import com.ibm.wh.siam.core.util.rdbms.RdbmsUtil;

/**
 * @author Match Grun
 *
 */
@Component
public class ContentAccessorSqlBuilder
implements ContentAccessorSqlBuilderIF
{
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger( ContentAccessorSqlBuilder.class );

    /**
     * Build SQL statement for specified owner reference.
     * @param accessorRef   Owner attribute definition.
     * @param csParamName   Parameter name for content set ID.
     * @return SQL statement.
     */
    protected static String buildSql(
            final OwnerRefIF accessorRef,
            final String csParamName )
    {
        StringBuilder sb = new StringBuilder( 200 );

        sb.append( "select \n" );
        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, SiamTableNames.CONTENT_SET_ACCESS, "accessor_type", ALIAS_TYPE, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, SiamTableNames.CONTENT_SET_ACCESS, "accessor_id", ALIAS_ID, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, accessorRef.getTableName(), accessorRef.getColumnCode(), ALIAS_CODE, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, accessorRef.getTableName(), accessorRef.getColumnName(), ALIAS_NAME, false );
        sb.append( ",\n" );

        sb.append( "  " ).append( SiamTableNames.CONTENT_SET_ACCESS ).append( ".*\n");

        sb.append( "from\n" );
        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.CONTENT_SET_ACCESS ).append( ",\n" );
        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( accessorRef.getTableName() ).append( "\n" );

        sb.append( "where\n");
        sb.append( "  " );
        RdbmsUtil.buildJoinMatch(
                sb, SiamTableNames.CONTENT_SET_ACCESS, "accessor_id", accessorRef.getTableName(), accessorRef.getColumnId() );

        sb.append( "\nand\n");
        sb.append( "  " );
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.CONTENT_SET_ACCESS, "content_set_id" );
        sb.append( " = :" ).append( csParamName );

        return sb.toString();
    }

    @Override
    public String fetchSqlStatement( final String csParamName ) {
        StringBuilder sb = new StringBuilder( 400 );

        sb.append( buildSql( new OwnerRefOrganization(), csParamName ) );
        sb.append( "\nunion all\n" );
        sb.append( buildSql( new OwnerRefGroup(), csParamName ) );
        sb.append( "\n" );
        sb.append( "order by\n" );
        sb.append( "  " ).append( ALIAS_TYPE ).append( ", " ).append( ALIAS_CODE ).append( ", ").append( ALIAS_NAME );

        return sb.toString();
    }

}
