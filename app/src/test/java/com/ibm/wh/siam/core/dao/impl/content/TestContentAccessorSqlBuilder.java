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
public class TestContentAccessorSqlBuilder {

    private static final Logger logger = LoggerFactory.getLogger( TestContentAccessorSqlBuilder.class );

    private void testOwnerBuilderSql( final OwnerRefIF ownerRef )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testOwnerBuilderSql()" );
        }

        String ownerType = ownerRef.getOwnerType();
        if( logger.isDebugEnabled() ) {
            logger.debug( "ownerType=" + ownerType );
        }

        String sql = ContentAccessorSqlBuilder.buildSql(ownerRef, "contentSetId" );
        if( logger.isDebugEnabled() ) {
            logger.debug( "sql=" + sql );
        }

        StringBuilder sb = new StringBuilder();

        sb.setLength(0);
        sb.append( SiamTableNames.CONTENT_SET_ACCESS ).append( "." ).append( "accessor_id" );
        String strAccId = sb.toString();

        sb.setLength(0);
        sb.append( SiamTableNames.CONTENT_SET_ACCESS ).append( "." ).append( "accessor_type" );
        String strAccType = sb.toString();

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
            logger.debug( "strAccId=" + strAccId );
            logger.debug( "strAccType=" + strAccType );
            logger.debug( "strId=" + strId );
            logger.debug( "strCode=" + strCode );
            logger.debug( "strName=" + strName );
        }

        assertTrue( sql.contains( strAccId ), "SQL does not contain column accessor id." );
        assertTrue( sql.contains( strAccType ), "SQL does not contain column accessor type." );

        assertTrue( sql.contains( strId ), "SQL does not contain column id." );
        assertTrue( sql.contains( strCode ), "SQL does not contain column code." );
        assertTrue( sql.contains( strName ), "SQL does not contain column name." );

        sb.setLength(0);
        sb.append( "as " ).append( ContentDetailSqlBuilder.ALIAS_ID );
        String aliasId = sb.toString();

        sb.setLength(0);
        sb.append( "as " ).append( ContentDetailSqlBuilder.ALIAS_CODE );
        String aliasCode = sb.toString();

        sb.setLength(0);
        sb.append( "as " ).append( ContentDetailSqlBuilder.ALIAS_TYPE );
        String aliasType = sb.toString();

        if( logger.isDebugEnabled() ) {
            logger.debug( "aliasId=" + aliasId );
            logger.debug( "aliasCode=" + aliasCode );
            logger.debug( "aliasType=" + aliasType );
        }

        sb.setLength(0);
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.CONTENT_SET );
        String strTabAcc = sb.toString();

        sb.setLength(0);
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( ownerRef.getTableName() );
        String strTable = sb.toString();

        sb.setLength(0);
        sb.append( SiamTableNames.CONTENT_SET_ACCESS ).append( ".accessor_id = " );
        sb.append( ownerRef.getTableName() ).append( ".").append( ownerRef.getColumnId() );
        String strWhere = sb.toString();

        if( logger.isDebugEnabled() ) {
            logger.debug( "strTabAcc=" + strTabAcc );
            logger.debug( "strTable=" + strTable );
            logger.debug( "strWhere=" + strWhere  );
        }

        assertTrue( sql.contains( strTabAcc ), "SQL does not contain table name(s) for accessor table." );
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

        testOwnerBuilderSql( new OwnerRefOrganization() );
    }

    @Test
    public void testBuilder02() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder02()");
        }

        testOwnerBuilderSql( new OwnerRefGroup() );
    }

}
