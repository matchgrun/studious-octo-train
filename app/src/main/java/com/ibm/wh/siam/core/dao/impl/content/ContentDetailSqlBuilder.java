/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.wh.siam.core.common.SiamOwnerTypes;
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
public class ContentDetailSqlBuilder
implements ContentDetailSqlBuilderIF
{
    protected static final String COL_OWNER_TYPE = "owner_type";
    protected static final String COL_OWNER_ID = "owner_id";
    protected static final String COL_CONTENT_SET_ID = "content_set_id";
    protected static final String COL_ACCESSOR_ID = "accessor_id";


    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger( ContentDetailSqlBuilder.class );

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
                sb, SiamTableNames.CONTENT_SET, COL_OWNER_TYPE, ALIAS_TYPE, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, SiamTableNames.CONTENT_SET, COL_OWNER_ID, ALIAS_ID, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, accessorRef.getTableName(), accessorRef.getColumnCode(), ALIAS_CODE, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, accessorRef.getTableName(), accessorRef.getColumnName(), ALIAS_NAME, false );
        sb.append( ",\n" );

        sb.append( "  " ).append( SiamTableNames.CONTENT_SET ).append( ".*\n");

        sb.append( "from\n" );
        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.CONTENT_SET ).append( "\n" );

        sb.append( "left join\n" );
        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( accessorRef.getTableName() ).append( "\n" );

        sb.append( "on\n" );
        sb.append( "  " );
        RdbmsUtil.buildJoinMatch(
                sb, SiamTableNames.CONTENT_SET, COL_OWNER_ID, accessorRef.getTableName(), accessorRef.getColumnId() );

        if ( csParamName != null ) {
            sb.append( "\nwhere\n");
            sb.append( "  " );
            RdbmsUtil.buildColumnSelection( sb, SiamTableNames.CONTENT_SET, COL_CONTENT_SET_ID );
            sb.append( " = :" ).append( csParamName );
        }

        return sb.toString();
    }

    @Override
    /**
     * Note this could be extended to provide ownership of a content set
     * by a group as well as by an organization. Reference the class
     * ContentAccessorSqlBuilder for how this could be implemented.
     */
    public String fetchSqlStatement( final String csParamName ) {
        return buildSql( new OwnerRefOrganization(), csParamName );
    }

    @Override
    public String fetchSqlAccessibleContent( final String ownerType ) {
        String sql = null;
        OwnerRefIF ownerRef = null;
        if( ownerType.equals( SiamOwnerTypes.ORGANIZATION ) ) {
            ownerRef = new OwnerRefOrganization();
        }
        else if( ownerType.equals( SiamOwnerTypes.GROUP ) ) {
            ownerRef = new OwnerRefGroup();
        }

        sql = buildAccessibleSql( ownerRef );
        return sql;
    }

    /**
     * Build SQL statement for specified owner reference.
     * @param accessorRef     Owner attribute definition.
     * @param paramOwnerCS    Parameter name for content set owner type.
     * @param paramAccessorId Parameter name for accessor ID.
     * @return SQL statement.
     */
    protected static String buildAccessibleSql( final OwnerRefIF ownerRef ) {
        StringBuilder sb = new StringBuilder( 200 );
        if( ownerRef == null ) {
            return null;
        }

        sb.append( "select \n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, ownerRef.getTableName(), ownerRef.getColumnCode(), ALIAS_CODE, false );
        sb.append( ",\n" );

        sb.append( "  " );
        RdbmsUtil.buildColumnSelectionAlias(
                sb, ownerRef.getTableName(), ownerRef.getColumnName(), ALIAS_NAME, false );
        sb.append( ",\n" );

        sb.append( "  " ).append( SiamTableNames.CONTENT_SET ).append( ".*\n");

        sb.append( "from\n" );
        sb.append( "  " ).append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.CONTENT_SET ).append( "\n" );


        sb.append( "  inner join " );
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.CONTENT_SET_ACCESS ).append( "\n" );
        sb.append( "          on " );
        RdbmsUtil.buildJoinMatch(
                sb, SiamTableNames.CONTENT_SET, COL_CONTENT_SET_ID, SiamTableNames.CONTENT_SET_ACCESS, COL_CONTENT_SET_ID );
        sb.append( "\n" );

        sb.append( "  left join  " );
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( ownerRef.getTableName()).append( "\n" );
        sb.append( "         on  " );
        RdbmsUtil.buildJoinMatch(
                sb, SiamTableNames.CONTENT_SET, COL_OWNER_ID, ownerRef.getTableName(), ownerRef.getColumnId() );
        sb.append( "\n" );

        sb.append( "where\n" );
        sb.append( "  " );
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.CONTENT_SET, COL_OWNER_TYPE );
        sb.append( " = :" ).append( PARM_NAME_OWNER_TYPE );
        sb.append( "\nand\n" );
        sb.append( "  " );
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.CONTENT_SET_ACCESS, COL_ACCESSOR_ID );
        sb.append( " = :" ).append( PARM_NAME_ACCESSOR_ID );

        return sb.toString();
    }

}
