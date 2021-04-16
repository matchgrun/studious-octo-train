/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.cache;

import com.ibm.wh.siam.common.cache.GeezerEliminationCache;

/**
 * @author Match Grun
 *
 */
public interface EntitlementCacheManagerIF {

    /**
     * Setup the caches.
     */
    public void setupCaches();

    /**
     * Retrieve product cache.
     * @return Cache.
     */
    public GeezerEliminationCache fetchProductCache();

    /**
     * Retrieve entitlement cache for specified owner.
     * @param ownerType Owner type.
     * @return Cache, or <i>null</i> if none found.
     */
    public GeezerEliminationCache fetchEntitlementCache( final String ownerType );

}
