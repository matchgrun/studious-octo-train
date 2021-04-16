/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author Match Grun
 *
 */
public class GeezerEliminationCache
{
    /**
     * Default cache age (300 seconds).
     */
    public static final long DFL_CACHE_AGE = 300L * CacheManager.CONV_SECS;

    private String cacheName = null;
    private String cacheCategory = null;
    private long maxAge = DFL_CACHE_AGE;

    /**
     * Elimination handler.
     */
    private EliminationHandlerIF eliminationHandler = null;

    /**
     * The cache.
     */
    private Map<String,CachedItem> mapCache = new ConcurrentHashMap<String,CachedItem>();

    /**
     * Lock to manage access.
     */
    protected ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static final Logger logger = LoggerFactory.getLogger( CacheManager.class );

    /**
     * Create cache with specified name.
     * @param name  Cache name.
     */
    public GeezerEliminationCache( final String name ) {
        if( logger.isDebugEnabled() ) {
            logger.debug("GeezerEliminationCache()/" + name);
        }
        this.cacheName = name;
    }

    /**
     * Create cache with specified name and maximum item age.
     * @param name      Cache name.
     * @param maxAge    Maximum item age (in millisecs).
     */
    public GeezerEliminationCache(
            final String name,
            final long maxAge )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug("GeezerEliminationCache(,)/" + name);
        }

        this.cacheName = name;
        this.setMaxAge(maxAge);
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName( final String cacheName ) {
        this.cacheName = cacheName;
    }

    public String getCacheCategory() {
        return cacheCategory;
    }

    public void setCacheCategory( final String cacheCategory ) {
        this.cacheCategory = cacheCategory;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge( final long maxItemAge ) {
        if( maxItemAge > 0L ) {
            this.maxAge = maxItemAge;
        } else {
            this.maxAge = DFL_CACHE_AGE;
        }
    }

    public EliminationHandlerIF getEliminationListener() {
        return eliminationHandler;
    }

    public void setEliminationHandler( final EliminationHandlerIF handler ) {
        this.eliminationHandler = handler;
    }

    /**
     * Test whether cached item has expired.
     * @param tCurrent      Current time.
     * @param cachedItem    Cached item.
     */
    public boolean hasExpired (
            final long tCurrent,
            final CachedItem cachedItem )
    {
        long tExpire = cachedItem.getTimeAccessed() + maxAge;
        return ( tExpire < tCurrent );
    }

    /**
     * Test whether cached item has expired.
     * @param cachedItem    Cached item.
     */
    private boolean hasExpired ( final CachedItem cachedItem ) {
        return hasExpired( System.currentTimeMillis(), cachedItem );
    }

    /**
     * Add item to cache.
     * @param cacheKey  Cache key.
     * @param obj       Object to cache.
     */
    public synchronized void addItem(
            final String cacheKey,
            final Object obj )
    {
        if( obj != null ) {
            if( ! StringUtils.isEmpty(cacheKey) ) {
                try {
                   readWriteLock.writeLock().lock();
                   CachedItem cachedItem = new CachedItem( obj );
                   mapCache.put(cacheKey, cachedItem);
                }
                finally {
                   readWriteLock.writeLock().unlock();
                }
            }
        }
    }

    /**
     * Remove cached item.
     * @param cacheKey  Cache key.
     * @return  Cached item.
     */
    public CachedItem removeCachedItem( final String cacheKey ) {
        CachedItem cachedItem = null;
        if( ! StringUtils.isEmpty(cacheKey) ) {
            cachedItem = removeItem(cacheKey);
        }
        return cachedItem;
    }

    /**
     * Remove cached item.
     * @param cacheKey  Cache key.
     */
    private CachedItem removeItem( final String cacheKey ) {
        return mapCache.remove(cacheKey);
    }

    /**
     * Retrieve item with specified key.
     * @param cacheKey  Cache key.
     * @return Cached item.
     */
    private CachedItem getItem( final String cacheKey ) {
        CachedItem cachedItem = null;
        try {
            readWriteLock.readLock().lock();
            cachedItem = mapCache.get(cacheKey);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
        return cachedItem;
    }
    /**
     * Retrieve cached item.
     * @param cacheKey  Cache key.
     * @return  Cached item.
     */
    private CachedItem fetchCachedItem( final String cacheKey ) {
        CachedItem cachedItem = null;
        if( ! StringUtils.isEmpty(cacheKey) ) {
            cachedItem = getItem( cacheKey );
            if( cachedItem != null ) {
                if( hasExpired( cachedItem ) ) {
                    removeItem( cacheKey );
                    cachedItem = null;
                }
                else {
                    touchItem( cachedItem );
                }
            }
        }
        return cachedItem;
    }

    /**
     * Touch cache item to prevent premature elimination.
     * @param cachedItem Item to touch.
     */
    private void touchItem( final CachedItem cachedItem ) {
        if( cachedItem != null ) {
            cachedItem.setTimeAccessed( System.currentTimeMillis() );
        }
    }
    /**
     * Retrieve cached object.
     * @param cacheKey  Cache key.
     * @return  Cached object.
     */
    public synchronized Object fetchData( final String cacheKey ) {
        Object cachedObject = null;
        CachedItem cachedItem = fetchCachedItem(cacheKey);
        if( cachedItem != null ) {
            cachedObject = cachedItem.getCachedObject();
        }
        return cachedObject;
    }

    /**
     * Flush all entries from cache.
     */
    public void flush() {
        mapCache.clear();
    }

    public interface EliminationHandlerIF {
        /**
         * Perform event when item removed from cache.
         * @param cacheKey      Cache key.
         * @param cachedData    Cached object.
         */
        public void onEliminate( final String cacheKey, final Object cachedData );
    }

    public void doBackgroundProcessing() {
        synchronized ( mapCache ) {
            doBackgroundProcessing( mapCache );
        }
    }

    /**
     * Perform background processing of specified cache.
     * @param cache Cache to process.
     */
    private void doBackgroundProcessing( final Map<String,CachedItem> cache ) {
        long tNow = System.currentTimeMillis();
        Set<Entry<String, CachedItem>> setEntry = cache.entrySet();
        for( Iterator<Entry<String, CachedItem>> iter = setEntry.iterator(); iter.hasNext(); ) {
            Entry<String, CachedItem> entry = iter.next();
            String cacheKey = entry.getKey();
            CachedItem cachedItem = entry.getValue();
            if( hasExpired( tNow, cachedItem ) ) {
                if( eliminationHandler != null ) {
                    eliminationHandler.onEliminate( cacheKey, cachedItem.getCachedObject() );
                }
                iter.remove();
            }
        }
    }

}
