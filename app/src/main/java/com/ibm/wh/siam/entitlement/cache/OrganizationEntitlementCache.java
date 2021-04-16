/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wh.siam.common.cache.GeezerEliminationCache;

/**
 * @author Match Grun
 *
 */
public class OrganizationEntitlementCache
extends GeezerEliminationCache
implements GeezerEliminationCache.EliminationHandlerIF
{

    private static final Logger logger = LoggerFactory.getLogger( EntitlementCacheManager.class );

    /**
     * @param name
     */
    public OrganizationEntitlementCache( final String name ) {
        super(name);
        setEliminationHandler(this);
    }

    @Override
    public void onEliminate(
            final String cacheKey,
            Object cachedData )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "onEliminate()/organization=" + cacheKey );
        }
    }

}
