/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.cache;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ibm.wh.siam.common.cache.CacheManager;
import com.ibm.wh.siam.common.cache.GeezerEliminationCache;
import com.ibm.wh.siam.core.common.SiamOwnerTypes;
import com.ibm.wh.siam.core.service.ProductService;

/**
 * @author Match Grun
 *
 */
@Component
@Scope("singleton")
/**
 * <p>
 * Implement caching for the entitlement engine. Caches are set up for the following:
 * </p>
 *
 * <ul>
 * <li>Products.</li>
 * <li>Organization entitlements.</li>
 * <li>Group entitlements.</li>
 * </ul>
 *
 * <p>
 * Note that person entitlements are not cached. It is not current expected that there
 * are many people with entitlements in the request being processed.
 * </p>

 *
 * @author Match Grun
 *
 */
public class EntitlementCacheManager
implements EntitlementCacheManagerIF
{

    private static final String CACHE_PRODUCT = "Entitlement.ProductCache";
    private static final String CACHE_ENTITLEMENT_ORG = "Entitlement.OrganizationEntitlementCache";
    private static final String CACHE_ENTITLEMENT_GROUP = "Entitlement.GroupEntitlementCache";

    private static final long CONV_MINS = 60L * CacheManager.CONV_SECS;

    private static final long AGE_PRODUCT = 2L * CONV_MINS;
//    private static final long AGE_PRODUCT = 30L * CONV_MINS;

    private static final long AGE_ENTITLEMENTS_ORG = 1L * CONV_MINS;
    // private static final long AGE_ENTITLEMENTS_ORG = 10L * CONV_MINS;

    private static final long AGE_ENTITLEMENTS_GROUP = 1L * CONV_MINS;
    // private static final long AGE_ENTITLEMENTS_GROUP = 10L * CONV_MINS;


    private static final Logger logger = LoggerFactory.getLogger( EntitlementCacheManager.class );

    private ProductCache cacheProduct = null;
    private OrganizationEntitlementCache cacheEntitlementOrgs = null;
    private GroupEntitlementCache cacheEntitlementGroups = null;

    CacheManager cacheManager = new CacheManager();

    @Override
    @PostConstruct
    public void setupCaches() {
        if( logger.isInfoEnabled() ) {
            logger.info( "setupCaches()" );
        }


        // Product cache. Categorize to make sure other services can flush.
        cacheProduct = new ProductCache( CACHE_PRODUCT );
        cacheProduct.setMaxAge( AGE_PRODUCT );
        cacheProduct.setCacheCategory(ProductService.PRODUCT_CACHE_CATEGORY);
        cacheManager.registerCache( cacheProduct );

        // Organization entitlement cache.
        cacheEntitlementOrgs = new OrganizationEntitlementCache( CACHE_ENTITLEMENT_ORG );
        cacheEntitlementOrgs.setMaxAge( AGE_ENTITLEMENTS_ORG );
        cacheManager.registerCache( cacheEntitlementOrgs );

        // Group entitlement cache.
        cacheEntitlementGroups = new GroupEntitlementCache( CACHE_ENTITLEMENT_GROUP );
        cacheEntitlementGroups.setMaxAge( AGE_ENTITLEMENTS_GROUP );
        cacheManager.registerCache( cacheEntitlementGroups );

    }

    @Override
    public GeezerEliminationCache fetchProductCache() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "fetchProductCache()" );
        }
        return cacheManager.fetchCache(CACHE_PRODUCT);
    }

    @Override
    public GeezerEliminationCache fetchEntitlementCache( final String ownerType ) {
        if( logger.isDebugEnabled() ) {
            logger.debug( "fetchEntitlementCache()/type=" + ownerType );
        }
        GeezerEliminationCache cache = null;
        String typeUC = SiamOwnerTypes.defaultValue(ownerType);
        if( typeUC.equals( SiamOwnerTypes.ORGANIZATION ) ) {
            cache = cacheManager.fetchCache(CACHE_ENTITLEMENT_ORG);
        }
        if( typeUC.equals( SiamOwnerTypes.GROUP ) ) {
            cache = cacheManager.fetchCache(CACHE_ENTITLEMENT_GROUP);
        }
        return cache;
    }
}
