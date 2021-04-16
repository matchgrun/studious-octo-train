/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.content;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wh.siam.core.dao.impl.SiamTableNames;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefGroup;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefIF;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerRefOrganization;

/**
 * @author Match Grun
 *
 */
public class TestContentDetailSqlBuilder {

    private static final Logger logger = LoggerFactory.getLogger( TestContentDetailSqlBuilder.class );

    private void testOwnerDetailBuilderSql( final OwnerRefIF ownerRef )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testOwnerBuilderSql()" );
        }

        String ownerType = ownerRef.getOwnerType();
        if( logger.isDebugEnabled() ) {
            logger.debug( "ownerType=" + ownerType );
        }

        String parmCSID = "contentSetId";
        String sql = ContentDetailSqlBuilder.buildSql(ownerRef, parmCSID );
        if( logger.isDebugEnabled() ) {
            logger.debug( "sql=\n" + sql );
        }

        StringBuilder sb = new StringBuilder();

        sb.setLength(0);
        sb.append( SiamTableNames.CONTENT_SET ).append( "." ).append( ContentDetailSqlBuilder.COL_OWNER_ID );
        String strOwnerId = sb.toString();

        sb.setLength(0);
        sb.append( SiamTableNames.CONTENT_SET ).append( "." ).append( ContentDetailSqlBuilder.COL_OWNER_TYPE );
        String strOwnerType = sb.toString();

        sb.setLength(0);
        sb.append(ownerRef.getTableName()).append( "." ).append( ownerRef.getColumnId());
        String strId = sb.toString();

        sb.setLength(0);
        sb.append(ownerRef.getTableName()).append( "." ).append( ownerRef.getColumnCode());
        String strCode = sb.toString();

        sb.setLength(0);
        sb.append(ownerRef.getTableName()).append( "." ).append( ownerRef.getColumnName());
        String strName = sb.toString();

        if( logger.isDebugEnabled() ) {
            logger.debug( "strOwnerId=" + strOwnerId );
            logger.debug( "strOwnerType=" + strOwnerType );
            logger.debug( "strId=" + strId );
            logger.debug( "strCode=" + strCode );
            logger.debug( "strName=" + strName );
        }

        assertTrue( sql.contains( strOwnerId ), "SQL does not contain column owner id." );
        assertTrue( sql.contains( strOwnerType ), "SQL does not contain column owner type." );

        assertTrue( sql.contains( strId ), "SQL does not contain column id." );
        assertTrue( sql.contains( strCode ), "SQL does not contain column code." );
        assertTrue( sql.contains( strName ), "SQL does not contain column name." );

        sb.setLength(0);
        sb.append( "as " ).append( ContentDetailSqlBuilderIF.ALIAS_ID );
        String aliasId = sb.toString();

        sb.setLength(0);
        sb.append( "as " ).append( ContentDetailSqlBuilderIF.ALIAS_CODE );
        String aliasCode = sb.toString();

        sb.setLength(0);
        sb.append( "as " ).append( ContentDetailSqlBuilderIF.ALIAS_TYPE );
        String aliasType = sb.toString();

        if( logger.isDebugEnabled() ) {
            logger.debug( "aliasId=" + aliasId );
            logger.debug( "aliasCode=" + aliasCode );
            logger.debug( "aliasType=" + aliasType );
        }

        sb.setLength(0);
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.CONTENT_SET );
        String strTabCS = sb.toString();

        sb.setLength(0);
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( ownerRef.getTableName() );
        String strTable = sb.toString();

        sb.setLength(0);
        sb.append( SiamTableNames.CONTENT_SET ).append( "." ).append( ContentDetailSqlBuilder.COL_CONTENT_SET_ID );
        sb.append( " = :" ).append( parmCSID );
        String strWhere = sb.toString();

        if( logger.isDebugEnabled() ) {
            logger.debug( "strTabCS=" + strTabCS );
            logger.debug( "strTable=" + strTable );
            logger.debug( "strWhere=" + strWhere  );
        }

        assertTrue( sql.contains( strTabCS ), "SQL does not contain table name(s) for content set table." );
        assertTrue( sql.contains( strTable ), "SQL does not contain table name(s) for table." );
        assertTrue( sql.contains( strWhere ), "SQL does not contain where clause." );

        if( logger.isDebugEnabled() ) {
            logger.debug( "\n-----------------------------------------------------------------------");
        }
    }

    @Test
    public void testBuilder01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder01()");
        }

        testOwnerDetailBuilderSql( new OwnerRefOrganization() );
    }

    @Test
    public void testBuilder02() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder02()");
        }

        testOwnerDetailBuilderSql( new OwnerRefGroup() );
    }

    private void testOwnerAccessibleSql( final OwnerRefIF ownerRef )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testOwnerBuilderSql()" );
        }

        String ownerType = ownerRef.getOwnerType();
        if( logger.isDebugEnabled() ) {
            logger.debug( "ownerType=" + ownerType );
        }

        String sql = ContentDetailSqlBuilder.buildAccessibleSql( ownerRef );
        if( logger.isDebugEnabled() ) {
            logger.debug( "sql=\n" + sql );
        }

        StringBuilder sb = new StringBuilder();

        sb.setLength(0);
        sb.append( SiamTableNames.CONTENT_SET ).append( "." ).append( ContentDetailSqlBuilder.COL_OWNER_ID );
        String strOwnerId = sb.toString();

        sb.setLength(0);
        sb.append( SiamTableNames.CONTENT_SET ).append( "." ).append( ContentDetailSqlBuilder.COL_OWNER_TYPE );
        String strOwnerType = sb.toString();

        sb.setLength(0);
        sb.append(ownerRef.getTableName()).append( "." ).append( ownerRef.getColumnId());
        String strId = sb.toString();

        sb.setLength(0);
        sb.append(ownerRef.getTableName()).append( "." ).append( ownerRef.getColumnCode());
        String strCode = sb.toString();

        sb.setLength(0);
        sb.append(ownerRef.getTableName()).append( "." ).append( ownerRef.getColumnName());
        String strName = sb.toString();

        if( logger.isDebugEnabled() ) {
            logger.debug( "strOwnerId=" + strOwnerId );
            logger.debug( "strOwnerType=" + strOwnerType );
            logger.debug( "strId=" + strId );
            logger.debug( "strCode=" + strCode );
            logger.debug( "strName=" + strName );
        }

        assertTrue( sql.contains( strOwnerId ), "SQL does not contain column owner id." );
        assertTrue( sql.contains( strOwnerType ), "SQL does not contain column owner type." );

        assertTrue( sql.contains( strId ), "SQL does not contain column id." );
        assertTrue( sql.contains( strCode ), "SQL does not contain column code." );
        assertTrue( sql.contains( strName ), "SQL does not contain column name." );

        sb.setLength(0);
        sb.append( "as " ).append( ContentDetailSqlBuilderIF.ALIAS_ID );
        String aliasId = sb.toString();

        sb.setLength(0);
        sb.append( "as " ).append( ContentDetailSqlBuilderIF.ALIAS_CODE );
        String aliasCode = sb.toString();

        sb.setLength(0);
        sb.append( "as " ).append( ContentDetailSqlBuilderIF.ALIAS_TYPE );
        String aliasType = sb.toString();

        if( logger.isDebugEnabled() ) {
            logger.debug( "aliasId=" + aliasId );
            logger.debug( "aliasCode=" + aliasCode );
            logger.debug( "aliasType=" + aliasType );
        }

        sb.setLength(0);
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.CONTENT_SET );
        String strTabCS = sb.toString();

        sb.setLength(0);
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( ownerRef.getTableName() );
        String strTable = sb.toString();

        sb.setLength(0);
        sb.append( SiamTableNames.CONTENT_SET ).append( "." ).append( ContentDetailSqlBuilder.COL_OWNER_TYPE );
        sb.append( " = :" ).append( ContentDetailSqlBuilderIF.PARM_NAME_OWNER_TYPE );
        String strWhere = sb.toString();

        sb.setLength(0);
        sb.append( SiamTableNames.CONTENT_SET_ACCESS ).append( "." ).append( ContentDetailSqlBuilder.COL_ACCESSOR_ID );
        sb.append( " = :" ).append( ContentDetailSqlBuilderIF.PARM_NAME_ACCESSOR_ID );
        String strWhere2 = sb.toString();

        if( logger.isDebugEnabled() ) {
            logger.debug( "strTabCS=" + strTabCS );
            logger.debug( "strTable=" + strTable );
            logger.debug( "strWhere=" + strWhere  );
            logger.debug( "strWhere2=" + strWhere2  );
        }

        assertTrue( sql.contains( strTabCS ), "SQL does not contain table name(s) for content set table." );
        assertTrue( sql.contains( strTable ), "SQL does not contain table name(s) for table." );
        assertTrue( sql.contains( strWhere ), "SQL does not contain where clause." );
        assertTrue( sql.contains( strWhere2 ), "SQL does not contain where2 clause." );

        if( logger.isDebugEnabled() ) {
            logger.debug( "\n-----------------------------------------------------------------------");
        }
    }

    @Test
    public void testBuilder03() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder03()");
        }

        testOwnerAccessibleSql( new OwnerRefOrganization() );
    }

    @Test
    public void testBuilder04() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder04()");
        }

        testOwnerAccessibleSql( new OwnerRefGroup() );
    }

}
