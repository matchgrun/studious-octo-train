/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.attribute;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * @author Match Grun
 *
 */
public class TestOwnerAttributeSqlBuilder {

    private static final Logger logger = LoggerFactory.getLogger( TestOwnerAttributeSqlBuilder.class );


    private void testOwnerRefSql( final OwnerRefIF ownerRef )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testOwnerRefSql()" );
        }
        OwnerAttributeSqlBuilder builder = new OwnerAttributeSqlBuilder();
        String ownerType = ownerRef.getOwnerType();
        if( logger.isDebugEnabled() ) {
            logger.debug( "ownerType=" + ownerType );
        }

        String sql = builder.fetchSqlStatement(ownerType);
        if( logger.isDebugEnabled() ) {
            logger.debug( "sql=\n" + sql );
        }

        StringBuilder sb = new StringBuilder();
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
            logger.debug( "strId=" + strId );
            logger.debug( "strCode=" + strCode );
            logger.debug( "strName=" + strName );
        }

        assertTrue( sql.contains( strId ), "SQL does not contain column id." );
        assertTrue( sql.contains( strCode ), "SQL does not contain column code." );
        assertTrue( sql.contains( strName ), "SQL does not contain column name." );

        sb.setLength(0);
        sb.append( "as " ).append( OwnerAttributeSqlBuilderIF.ALIAS_OWNER_NAME );
        String aliasName = sb.toString();

        sb.setLength(0);
        sb.append( "as " ).append( OwnerAttributeSqlBuilderIF.ALIAS_OWNER_CODE );
        String aliasCode = sb.toString();

        if( logger.isDebugEnabled() ) {
            logger.debug( "aliasName=" + aliasName );
            logger.debug( "aliasCode=" + aliasCode );
        }

        assertTrue( sql.contains( aliasName ), "SQL does not contain column alias for name." );
        assertTrue( sql.contains( aliasCode ), "SQL does not contain column alias for code." );

        sb.setLength(0);
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( ownerRef.getTableName() );
        String strTable = sb.toString();
        logger.debug( "strTable=" + strTable );

        assertTrue( sql.contains( strTable ), "SQL does not contain table name(s) for table." );

        if( logger.isDebugEnabled() ) {
            logger.debug( "\n-----------------------------------------------------------------------");
        }
    }

    @Test
    public void testBuilder01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder01()");
        }
        testOwnerRefSql( new OwnerRefOrganization() );
    }

    @Test
    public void testBuilder02() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder02()");
        }
        testOwnerRefSql( new OwnerRefGroup() );
    }

    @Test
    public void testBuilder03() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder03()");
        }
        testOwnerRefSql( new OwnerRefPerson() );
    }

    @Test
    public void testBuilder04() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder04()");
        }
        testOwnerRefSql( new OwnerRefProduct() );
    }

    @Test
    public void testBuilder05() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder05()");
        }
        testOwnerRefSql( new OwnerRefAccount() );
    }

    @Test
    public void testBuilder06() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder06()");
        }
        testOwnerRefSql( new OwnerRefRole() );
    }

    @Test
    public void testBuilder07() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder07()");
        }
        testOwnerRefSql( new OwnerRefContentSet() );
    }

    @Test
    public void testBuilder08() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testBuilder08()");
        }
        testOwnerRefSql( new OwnerRefContentSetAccess() );
    }

}
