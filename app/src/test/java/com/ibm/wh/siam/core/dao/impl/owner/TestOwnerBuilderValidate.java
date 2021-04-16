/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.owner;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wh.siam.core.dao.impl.SiamTableNames;

/**
 * @author Match Grun
 *
 */
public class TestOwnerBuilderValidate {
    private static final Logger logger = LoggerFactory.getLogger( TestOwnerBuilderValidate.class );

    private void testOwnerRefSqlValidate( final OwnerRefIF ownerRef )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testOwnerRefSqlValidate()" );
        }

        OwnerSqlBuilder builder = new OwnerSqlBuilder();
        String ownerType = ownerRef.getOwnerType();
        if( logger.isDebugEnabled() ) {
            logger.debug( "ownerType=" + ownerType );
        }

        String sql = builder.fetchSqlStatementValidate(ownerType);
        if( logger.isDebugEnabled() ) {
            logger.debug( "sql=" + sql );
        }

        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append(ownerRef.getTableName()).append( "." ).append( ownerRef.getColumnId());
        String strId = sb.toString();

        if( logger.isDebugEnabled() ) {
            logger.debug( "strId=" + strId );
        }

        assertTrue( sql.contains( strId ), "SQL does not contain column id." );

        sb.setLength(0);
        sb.append( "as " ).append( OwnerSqlBuilderIF.ALIAS_ID );
        String aliasId = sb.toString();

        assertTrue( sql.contains( aliasId ), "SQL does not contain column alias for id." );

        sb.setLength(0);
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( ownerRef.getTableName() );
        String strTable = sb.toString();

        sb.setLength(0);
        sb.append( ownerRef.getColumnId() ).append( " = :" ).append( OwnerSqlBuilderIF.PARM_NAME_OWNER_ID );
        String strWhere = sb.toString();

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
        testOwnerRefSqlValidate( new OwnerRefOrganization() );
    }

    @Test
    public void testBuilder02() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder02()");
        }
        testOwnerRefSqlValidate( new OwnerRefGroup() );
    }

    @Test
    public void testBuilder03() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder03()");
        }
        testOwnerRefSqlValidate( new OwnerRefAccount() );
    }

    @Test
    public void testBuilder04() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder04()");
        }
        testOwnerRefSqlValidate( new OwnerRefProduct() );
    }

    @Test
    public void testBuilder05() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder05()");
        }
        testOwnerRefSqlValidate( new OwnerRefContentSet() );
    }

    @Test
    public void testBuilder06() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder06()");
        }
        testOwnerRefSqlValidate( new OwnerRefContentSetAccess() );
    }

    @Test
    public void testBuilder07() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder07()");
        }
        testOwnerRefSqlValidate( new OwnerRefRole() );
    }

}
