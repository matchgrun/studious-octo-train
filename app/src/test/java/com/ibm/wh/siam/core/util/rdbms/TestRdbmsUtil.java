/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.util.rdbms;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Match Grun
 *
 */
public class TestRdbmsUtil {

    private static final String TEST_SCHEMA = "schema";
    private static final String TEST_TABLE_1 = "table_1";
    private static final String TEST_TABLE_2 = "table_2";
    private static final String TEST_COLUMN_1 = "column_1";
    private static final String TEST_COLUMN_2 = "column_2";
    private static final String TEST_ALIAS_1 = "alias_1";
    private static final String TEST_PARAM = "testParam";


    // Table/column
    private static final String TEST_T1_C1 = TEST_TABLE_1 + "." + TEST_COLUMN_1;
    private static final String TEST_T2_C2 = TEST_TABLE_2 + "." + TEST_COLUMN_2;
    private static final String TEST_T1_C1_SEP = TEST_T1_C1 + ",";

    // Table/column with alias
    private static final String TEST_T1_C1_A1 = TEST_T1_C1 + " as " + TEST_ALIAS_1;
    private static final String TEST_T1_C1_A1_SEP = TEST_T1_C1_A1 + ",";

    // Join with no schema
    private static final String TEST_J1 = TEST_T1_C1 + " = " + TEST_T2_C2;

    // Join with schema
    private static final String TEST_S_T1_C1 = TEST_SCHEMA + "." + TEST_TABLE_1 + "." + TEST_COLUMN_1;
    private static final String TEST_S_T2_C2 = TEST_SCHEMA + "." + TEST_TABLE_2 + "." + TEST_COLUMN_2;

    private static final String TEST_J2 = TEST_S_T1_C1 + " = " + TEST_S_T2_C2;

    private static final Logger logger = LoggerFactory.getLogger( TestRdbmsUtil.class );

    @Test
    public void testUtil01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil01()");
        }

        StringBuilder sb = new StringBuilder();
        RdbmsUtil.buildColumnSelection(sb, TEST_TABLE_1, TEST_COLUMN_1 );

        String str = sb.toString();
        if( logger.isDebugEnabled() ) {
            logger.debug( "str=" + str );
        }
        assertTrue( str.equals(TEST_T1_C1), "Failed build column selection." );

        sb.setLength(0);
        RdbmsUtil.buildColumnSelection(sb, TEST_TABLE_1, TEST_COLUMN_1, true );

        str = sb.toString();
        if( logger.isDebugEnabled() ) {
            logger.debug( "str=" + str );
        }
        assertTrue( str.contains(TEST_T1_C1_SEP), "Failed build column selection, with separator." );

        sb.setLength(0);
        RdbmsUtil.buildColumnSelection(sb, TEST_TABLE_1, TEST_COLUMN_1, false );

        str = sb.toString();
        if( logger.isDebugEnabled() ) {
            logger.debug( "str=" + str );
        }
        assertTrue( str.equals(TEST_T1_C1), "Failed build column selection, with no separator." );
    }

    @Test
    public void testUtil02() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil02()");
        }

        StringBuilder sb = new StringBuilder();
        RdbmsUtil.buildColumnSelectionAlias(sb, TEST_TABLE_1, TEST_COLUMN_1, TEST_ALIAS_1, true );

        String str = sb.toString();
        if( logger.isDebugEnabled() ) {
            logger.debug( "str=" + str );
        }
        assertTrue( str.contains(TEST_T1_C1_A1_SEP), "Failed build column selection, with alias." );

        sb.setLength(0);;
        RdbmsUtil.buildColumnSelectionAlias(sb, TEST_TABLE_1, TEST_COLUMN_1, TEST_ALIAS_1, false );

        str = sb.toString();
        if( logger.isDebugEnabled() ) {
            logger.debug( "str=" + str );
        }
        assertTrue( str.equals(TEST_T1_C1_A1), "Failed build column selection, with alias, no separator." );
    }

    @Test
    public void testUtil03() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil03()");
        }

        StringBuilder sb = new StringBuilder();

        RdbmsUtil.buildJoinMatch(sb, TEST_TABLE_1, TEST_COLUMN_1, TEST_TABLE_2, TEST_COLUMN_2);

        String str = sb.toString();
        if( logger.isDebugEnabled() ) {
            logger.debug( "str=" + str );
        }
        assertTrue( str.contains(TEST_T1_C1), "Failed build join table 1." );
        assertTrue( str.contains(TEST_T2_C2), "Failed build join table 2." );
        assertTrue( str.contains(TEST_J1), "Failed build join criteria." );
    }

    @Test
    public void testUtil04() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil04()");
        }

        StringBuilder sb = new StringBuilder();

        RdbmsUtil.buildJoinMatch(sb, TEST_SCHEMA, TEST_TABLE_1, TEST_COLUMN_1, TEST_TABLE_2, TEST_COLUMN_2);

        String str = sb.toString();
        if( logger.isDebugEnabled() ) {
            logger.debug( "str=" + str );
        }
        assertTrue( str.contains(TEST_S_T1_C1), "Failed build join table 1." );
        assertTrue( str.contains(TEST_S_T2_C2), "Failed build join table 2." );
        assertTrue( str.contains(TEST_J2), "Failed build join criteria." );
    }

    @Test
    public void testUtil05() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil05()");
        }

        List<String> listIds = new ArrayList<String> ();
        listIds.add( "a" );
        listIds.add( "b" );
        listIds.add( "c" );
        if( logger.isDebugEnabled() ) {
            logger.debug( "listIds=" + listIds );
        }
        String expected = "'a', 'b', 'c'";

        String str = RdbmsUtil.buildSeparatedIdList( listIds );

        assertTrue( str.contains(expected), "Failed build build separated list." );

        StringBuilder sb = new StringBuilder();
        RdbmsUtil.buildSeparatedIdList( sb, listIds );

        str = sb.toString();
        if( logger.isDebugEnabled() ) {
            logger.debug( "str=" + str );
        }
        assertTrue( str.contains(expected), "Failed build build separated list." );
    }

    @Test
    public void testUtil06() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil06()");
        }

        StringBuilder sb = new StringBuilder();
        RdbmsUtil.buildJoinMatchParameter(sb, TEST_TABLE_1, TEST_COLUMN_1, TEST_PARAM );

        String str = sb.toString();
        if( logger.isDebugEnabled() ) {
            logger.debug( "str=" + str );
        }
        assertTrue( str.contains(TEST_T1_C1), "Failed build join table 1." );

        String strParm = ":" + TEST_PARAM;
        assertTrue( str.contains( strParm ), "Failed build join parameter." );
    }

}
