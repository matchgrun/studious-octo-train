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

import com.ibm.wh.siam.core.dao.dto.ProductRange;

/**
 * @author Match Grun
 *
 */
public class TestProductRangeFilter {

    private static final Logger logger = LoggerFactory.getLogger( TestProductRangeFilter.class );

    private static final long TIME_OFFSET = 30000L;
    private static final long TIME_OUTSIDE = 100000L;

    private long timeCurrent = System.currentTimeMillis();
    private long timeWindowStart = 0L;
    private long timeWindowEnd = 0L;

    ProductRangeFilter filter = new ProductRangeFilter();

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

        ProductRange prod = new ProductRange();
        prod.setStartDate(null);

        boolean flagCheck = filter.checkProductRange(prod);
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

        ProductRange prod = new ProductRange();
        prod.setStartDate( dtStart );
        prod.setEndDate(null);

        boolean flagCheck = filter.checkProductRange(prod);
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

        ProductRange prod = new ProductRange();
        prod.setStartDate( dtStart );
        prod.setEndDate(null);

        boolean flagCheck = filter.checkProductRange(prod);
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

        ProductRange prod = new ProductRange();
        prod.setStartDate(null);
        prod.setEndDate(dtEnd);

        boolean flagCheck = filter.checkProductRange(prod);
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

        ProductRange prod = new ProductRange();
        prod.setStartDate(null);
        prod.setEndDate(dtEnd);

        boolean flagCheck = filter.checkProductRange(prod);
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

        ProductRange prod = new ProductRange();
        prod.setStartDate(dtStart);
        prod.setEndDate(dtEnd);

        boolean flagCheck = filter.checkProductRange(prod);
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

        ProductRange prod = new ProductRange();
        prod.setStartDate(dtStart);
        prod.setEndDate(dtEnd);

        boolean flagCheck = filter.checkProductRange(prod);
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

        ProductRange prod = new ProductRange();
        prod.setStartDate(dtStart);
        prod.setEndDate(dtEnd);

        boolean flagCheck = filter.checkProductRange(prod);
        if( logger.isInfoEnabled() ) {
            logger.info( "flagCheck=" + flagCheck );
        }
        assertFalse( flagCheck, "Failed Closed-Range, Future: Closed Start, Closed End." );
    }

}
