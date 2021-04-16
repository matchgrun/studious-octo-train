/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Match Grun
 *
 */
public class TestGeezerEliminationCache02
implements GeezerEliminationCache.EliminationHandlerIF
{
    private static final Logger logger = LoggerFactory.getLogger( TestGeezerEliminationCache02.class );

    private static final String TEST_CACHE_01 = "Test-Cache-01";
    private static final String TEST_CACHE_02 = "Test-Cache-02";
    private static final long TEST_AGE = 15000L;
    private static final long TEST_SLEEP = 20000L;

    private GeezerEliminationCache cache01 = null;
    private GeezerEliminationCache cache02 = null;
    CacheManager cacheManager = null;

    @BeforeEach
    private void setup() {
        cache01 = new GeezerEliminationCache( TEST_CACHE_01 );
        cache01.setMaxAge( TEST_AGE );

        cache02 = new GeezerEliminationCache( TEST_CACHE_02 );
        cache02.setMaxAge( TEST_AGE );

        if( logger.isInfoEnabled() ) {
            logger.info( "cache01.getMaxAge()=" + cache01.getMaxAge() );
        }

        cache01.addItem( "cache1Key1", "Match" );
        cache01.addItem( "cache1Key2", "Was" );
        cache01.addItem( "cache1Key3", "Here" );
        cache01.setEliminationHandler(this);

        cache02.addItem( "cache2Key1", "Kill Roy" );
        cache02.addItem( "cache2Key2", "Was" );
        cache02.addItem( "cache2Key3", "There" );

        cacheManager = new CacheManager();
        cacheManager.registerCache( cache01 );
        cacheManager.registerCache( cache02 );
    }

    @AfterEach
    private void teardown() {
        if( cacheManager != null ) {
            cacheManager.unregisterCache( cache01.getCacheName() );
            cacheManager.unregisterCache( cache02.getCacheName() );

            cache01 = null;
            cache02 = null;
        }
        cacheManager = null;
    }


    @Test
    public void testCache01() {
        if( logger.isInfoEnabled() ) {
            logger.info( "testCache01()");
        }

        cacheManager.describe();

        // Start cache processing
        cacheManager.startUp();

        if( logger.isInfoEnabled() ) {
            logger.info( "Starting..." );
        }

        try {
            Thread.sleep(TEST_SLEEP);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if( logger.isInfoEnabled() ) {
            logger.info( "Ended." );
        }
    }

    @Override
    public void onEliminate( final String cacheKey, final Object cachedData ) {
        if( logger.isInfoEnabled() ) {
            StringBuilder sb = new StringBuilder();
            sb.append( "onEliminate().../cacheKey=" ).append( cacheKey );
            sb.append( "cachedData=" ).append( cachedData );
            String msg = sb.toString();
            logger.info( msg );
        }
    }

}
