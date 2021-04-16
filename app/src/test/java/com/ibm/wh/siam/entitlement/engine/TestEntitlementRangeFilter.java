/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.engine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wh.siam.entitlement.dto.Entitlement;

/**
 * @author Match Grun
 *
 */
public class TestEntitlementRangeFilter {

    private static final Logger logger = LoggerFactory.getLogger( TestEntitlementRangeFilter.class );

    private static final long TIME_OFFSET = 30000L;
    private static final long TIME_OUTSIDE = 100000L;

    private long timeCurrent = System.currentTimeMillis();
    private long timeWindowStart = 0L;
    private long timeWindowEnd = 0L;

    EntitlementRangeFilter filter = new EntitlementRangeFilter();

    @BeforeEach
    private void setup() {
        timeWindowStart = timeCurrent - TIME_OFFSET;
        timeWindowEnd = timeCurrent + TIME_OFFSET;
        filter.timeCurrent = timeCurrent;
    }

    @Test
    public void testFilter01() {
        if( logger.isInfoEnabled() ) {
            logger.info( "testFilter01()");
        }

        Entitlement ent = new Entitlement();
        ent.setStartDate(null);
        ent.setEndDate(null);

        boolean flagCheck = filter.checkEntitlementRange(ent);
        if( logger.isInfoEnabled() ) {
            logger.info( "flagCheck=" + flagCheck );
        }

        assertTrue( flagCheck, "Failed Open-Range." );
    }

    @Test
    public void testFilter02() {
        if( logger.isInfoEnabled() ) {
            logger.info( "testFilter02()");
        }

        Date dtStart = new Date(timeWindowStart);

        Entitlement ent = new Entitlement();
        ent.setStartDate( dtStart );
        ent.setEndDate(null);

        boolean flagCheck = filter.checkEntitlementRange(ent);
        if( logger.isInfoEnabled() ) {
            logger.info( "flagCheck=" + flagCheck );
        }
        assertTrue( flagCheck, "Failed Open-Range: Open End." );
    }

    @Test
    public void testFilter03() {
        if( logger.isInfoEnabled() ) {
            logger.info( "testFilter03()");
        }

        Date dtStart = new Date(timeWindowEnd);

        Entitlement ent = new Entitlement();
        ent.setStartDate( dtStart );
        ent.setEndDate(null);

        boolean flagCheck = filter.checkEntitlementRange(ent);
        if( logger.isInfoEnabled() ) {
            logger.info( "flagCheck=" + flagCheck );
        }
        assertFalse( flagCheck, "Failed Open-Range: Open End." );
    }

    @Test
    public void testFilter04() {
        if( logger.isInfoEnabled() ) {
            logger.info( "testFilter04()");
        }

        Date dtEnd = new Date(timeWindowEnd);

        Entitlement ent = new Entitlement();
        ent.setStartDate(null);
        ent.setEndDate(dtEnd);

        boolean flagCheck = filter.checkEntitlementRange(ent);
        if( logger.isInfoEnabled() ) {
            logger.info( "flagCheck=" + flagCheck );
        }
        assertTrue( flagCheck, "Failed Open-Range: Open Start." );
    }

    @Test
    public void testFilter05() {
        if( logger.isInfoEnabled() ) {
            logger.info( "testFilter05()");
        }

        Date dtEnd = new Date(timeWindowStart);

        Entitlement ent = new Entitlement();
        ent.setStartDate(null);
        ent.setEndDate(dtEnd);

        boolean flagCheck = filter.checkEntitlementRange(ent);
        if( logger.isInfoEnabled() ) {
            logger.info( "flagCheck=" + flagCheck );
        }
        assertFalse( flagCheck, "Failed Open-Range: Open Start." );
    }

    @Test
    public void testFilter06() {
        if( logger.isInfoEnabled() ) {
            logger.info( "testFilter06()");
        }

        Date dtStart = new Date(timeWindowStart);
        Date dtEnd = new Date(timeWindowEnd);

        Entitlement ent = new Entitlement();
        ent.setStartDate(dtStart);
        ent.setEndDate(dtEnd);

        boolean flagCheck = filter.checkEntitlementRange(ent);
        if( logger.isInfoEnabled() ) {
            logger.info( "flagCheck=" + flagCheck );
        }
        assertTrue( flagCheck, "Failed Closed-Range: Closed Start, Closed End." );
    }

    @Test
    public void testFilter07() {
        if( logger.isInfoEnabled() ) {
            logger.info( "testFilter07()");
        }

        // Range in the past
        Date dtStart = new Date(timeWindowStart - TIME_OUTSIDE);
        Date dtEnd = new Date(timeWindowStart - TIME_OFFSET);

        Entitlement ent = new Entitlement();
        ent.setStartDate(dtStart);
        ent.setEndDate(dtEnd);

        boolean flagCheck = filter.checkEntitlementRange(ent);
        if( logger.isInfoEnabled() ) {
            logger.info( "flagCheck=" + flagCheck );
        }
        assertFalse( flagCheck, "Failed Closed-Range, Past: Closed Start, Closed End." );
    }

    @Test
    public void testFilter08() {
        if( logger.isInfoEnabled() ) {
            logger.info( "testFilter08()");
        }

        // Range in the future
        Date dtStart = new Date(timeWindowEnd + TIME_OFFSET);
        Date dtEnd = new Date(timeWindowEnd + TIME_OUTSIDE);

        Entitlement ent = new Entitlement();
        ent.setStartDate(dtStart);
        ent.setEndDate(dtEnd);

        boolean flagCheck = filter.checkEntitlementRange(ent);
        if( logger.isInfoEnabled() ) {
            logger.info( "flagCheck=" + flagCheck );
        }
        assertFalse( flagCheck, "Failed Closed-Range, Future: Closed Start, Closed End." );
    }

}
