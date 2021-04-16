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
public class TestOwnerBuilder {
    private static final Logger logger = LoggerFactory.getLogger( TestOwnerBuilder.class );


    private void testOwnerRefSql( final OwnerRefIF ownerRef )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testOwnerRefSql()" );
        }
        OwnerSqlBuilder builder = new OwnerSqlBuilder();
        String ownerType = ownerRef.getOwnerType();
        if( logger.isDebugEnabled() ) {
            logger.debug( "ownerType=" + ownerType );
        }

        String sql = builder.fetchSqlStatement(ownerType);
        if( logger.isDebugEnabled() ) {
            logger.debug( "sql=" + sql );
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
        sb.append( "as " ).append( OwnerSqlBuilderIF.ALIAS_ID );
        String aliasId = sb.toString();

        sb.setLength(0);
        sb.append( "as " ).append( OwnerSqlBuilderIF.ALIAS_CODE );
        String aliasCode = sb.toString();

        sb.setLength(0);
        sb.append( "as " ).append( OwnerSqlBuilderIF.ALIAS_TYPE );
        String aliasType = sb.toString();

        assertTrue( sql.contains( aliasId ), "SQL does not contain column alias for id." );
        assertTrue( sql.contains( aliasCode ), "SQL does not contain column alias for code." );
        assertTrue( sql.contains( aliasType ), "SQL does not contain column alias for owner type." );

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
            logger.debug( "testBuilder05()");
        }
        testOwnerRefSql( new OwnerRefAccount() );
    }

}
