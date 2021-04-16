/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Implements a cache manager based on concepts developed by THIDS infrastructure.
 * A "GeezerEliminationCache" is implemented that allows items to be cached by age. This
 * requires that a timer task is also implemented to process all caches. When the timer
 * thread runs, it processes all items in a cache. Items are removed if expired.
 * </p>
 * <p>
 * When object is cached, it is assigned a system time. When the timer task runs, this
 * access time is examined to determine whether the item is expired. After the data object
 * is cached, each access of the cached object will result in the access time being updated.
 * </p>
 * <p>
 * A cache may implement an elimination handler. This allows the application to be notified
 * when a cached item is being removed. Any specific handling may be processed by the
 * handler.
 * </p>
 *
 * @author Match Grun
 *
 */
@Component
@Scope("singleton")
public class CacheManager
extends TimerTask
{
    private static final Logger logger = LoggerFactory.getLogger( CacheManager.class );

    /**
     * Conversion factor to millisecs.
     */
    public static final long CONV_SECS = 1000L;

    public static final long DFL_DELAY = 20000L;
    public static final long DFL_PERIOD = 2000L;
    private long msecsPeriod = DFL_PERIOD;
    private long msecsDelay = DFL_DELAY;

    private static Map<String,GeezerEliminationCache> registeredCaches =
            new HashMap<String,GeezerEliminationCache>();

    private static Map<String,Set<String>> categorizedCaches =
            new HashMap<String,Set<String>>();

    Timer cacheTimer = null;

    /**
     * Register specified cache with manager.
     * @param cache     Cache.
     * @return  <i>true</i> if registered.
     */
    public boolean registerCache( final GeezerEliminationCache cache ) {
        boolean retVal = false;

        String name = cache.getCacheName();
        if( ! StringUtils.isEmpty(name)) {
            if( ! registeredCaches.containsKey(name) )
            {
                // Register cache.
                registeredCaches.put(name, cache);
                retVal = true;

                // Categorize cache.
                String category = cache.getCacheCategory();
                if( ! StringUtils.isEmpty(category)) {
                    addCategoryItem(category, name);
                }
            }
        }
        return retVal;
    }

    /**
     * Unregister specified cache from manager.
     * @param cacheName Cache name.
     */
    public void unregisterCache( final String cacheName ) {
        if( ! StringUtils.isEmpty( cacheName )) {
            GeezerEliminationCache cache = registeredCaches.get(cacheName);
            if( cache != null ) {
                String categoryName = cache.getCacheCategory();
                if( ! StringUtils.isEmpty(categoryName)) {
                    removeCategoryItem(categoryName, cacheName);
                }
            }
            registeredCaches.remove(cacheName);
        }
    }

    /**
     * Add cache to category name.
     * @param categoryName  Category name.
     * @param cacheName     Cache name.
     */
    private void addCategoryItem(
            final String categoryName,
            final String cacheName )
    {
        if( ! StringUtils.isEmpty(categoryName)) {
            Set<String> setNames = categorizedCaches.get(categoryName);
            if( setNames == null ) {
                setNames = new HashSet<String>();
            }
            if( ! setNames.contains(cacheName)) {
                setNames.add(cacheName);
            }
            categorizedCaches.put(categoryName, setNames);
        }
    }

    /**
     * Remove cache from category name.
     * @param categoryName  Category name.
     * @param cacheName     Cache name.
     */
    private void removeCategoryItem(
            final String categoryName,
            final String cacheName )
    {
        if( ! StringUtils.isEmpty(categoryName)) {
            Set<String> setNames = categorizedCaches.get(categoryName);
            if( setNames != null ) {
                if( setNames.contains(cacheName)) {
                    setNames.remove(cacheName);
                }
            }
        }
    }

    /**
     * Retrieve specified cache.
     * @param cacheName Cache name.
     * @return Cache instance, or <i>null</i> if not found.
     */
    public GeezerEliminationCache fetchCache( final String cacheName ) {
        GeezerEliminationCache cache = null;
        if( ! StringUtils.isEmpty( cacheName )) {
            cache = registeredCaches.get(cacheName);
        }
        return cache;
    }

    /**
     * Flush specified cache.
     * @param cacheName Cache name.
     */
    public void flushCache( final String cacheName ) {
        if( ! StringUtils.isEmpty( cacheName )) {
            GeezerEliminationCache cache = registeredCaches.get(cacheName);
            if( cache != null ) {
                cache.flush();
            }
        }
    }

    /**
     * Flush caches by specified category name.
     * @param categoryName Category name.
     */
    public void flushCachesByCategory( final String categoryName ) {
        if( ! StringUtils.isEmpty( categoryName )) {
            Set<String> setNames = categorizedCaches.get(categoryName);
            setNames.forEach( cacheName -> {
                flushCache(cacheName);
            });
        }
    }

    /**
     * Categorize cache with specified name. If cache has previously been categorized,
     * it will categorized with the specified (new) category name.
     * @param categoryName  Category name.
     * @param cacheName     Cache name.
     */
    public void categorizeCache(
            final String categoryName,
            final String cacheName )
    {
        if( StringUtils.isEmpty( categoryName )) return;
        if( StringUtils.isEmpty( cacheName )) return;

        GeezerEliminationCache cache = registeredCaches.get(cacheName);
        if( cache != null ) {
            String oldCategory = cache.getCacheCategory();
            if( ! StringUtils.isEmpty(oldCategory)) {
                removeCacheItem(oldCategory, cacheName);
            }

            addCategoryItem(categoryName, cacheName);
            cache.setCacheCategory(categoryName);
        }
    }

    /**
     * Remove specified cache item.
     * @param cacheName Cache name.
     * @param cacheKey  Cache key.
     */
    public void removeCacheItem(
            final String cacheName,
            final String cacheKey )
    {
        if( ! StringUtils.isEmpty( cacheName )) {
            GeezerEliminationCache cache = registeredCaches.get(cacheName);
            if( cache != null ) {
                cache.removeCachedItem(cacheKey);
            }
        }
    }

    /**
     * Perform processing for each registered cache.
     */
    private void performProcessing() {
        Set<Entry<String, GeezerEliminationCache>> setCaches = registeredCaches.entrySet();
        for( Iterator<Entry<String, GeezerEliminationCache>> iter = setCaches.iterator(); iter.hasNext(); ) {
            Entry<String, GeezerEliminationCache> entry = iter.next();
            GeezerEliminationCache cache = entry.getValue();
            cache.doBackgroundProcessing();
        }
    }

    @Override
    public void run() {
        performProcessing();
    }

    @Scheduled( fixedRate = DFL_PERIOD, initialDelay = DFL_DELAY )
    public void scheduled() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "scheduled()..." );
        }
        performProcessing();
    }

    public void describe() {
        if( logger.isInfoEnabled() ) {
            Set<Entry<String, GeezerEliminationCache>> setCaches = registeredCaches.entrySet();
            setCaches.forEach( entry -> {
                logger.info( "CacheManager:" + entry.getKey() );
            });
        }
    }

    public void startUp() {
        cacheTimer = new Timer("CacheManager");
        cacheTimer.schedule( this, msecsDelay, msecsPeriod );
    }


    public long getMsecsPeriod() {
        return msecsPeriod;
    }

    public void setMsecsPeriod( final long msecsPeriod) {
        this.msecsPeriod = msecsPeriod;
    }

    public long getMsecsDelay() {
        return msecsDelay;
    }

    public void setMsecsDelay( final long msecsDelay) {
        this.msecsDelay = msecsDelay;
    }

}
