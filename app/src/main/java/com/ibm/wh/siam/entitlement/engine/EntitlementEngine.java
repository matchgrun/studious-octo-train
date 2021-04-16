/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.common.cache.GeezerEliminationCache;
import com.ibm.wh.siam.core.common.CommonConstants;
import com.ibm.wh.siam.core.dao.ProductDAO;
import com.ibm.wh.siam.core.dao.dto.ProductRange;
import com.ibm.wh.siam.entitlement.cache.EntitlementCacheManagerIF;
import com.ibm.wh.siam.entitlement.dao.EntitlementDAO;
import com.ibm.wh.siam.entitlement.dto.Entitlement;
import com.ibm.wh.siam.entitlement.request.EntitlementCurrentOwnerQueryItem;
import com.ibm.wh.siam.entitlement.request.EntitlementCurrentRequest;

/**
 * @author Match Grun
 *
 */
@Component
public class EntitlementEngine
implements EntitlementEngineIF
{
    private static final Logger logger = LoggerFactory.getLogger( EntitlementEngine.class );

    @Resource
    EntitlementDAO entitlementDao;

    @Resource
    ProductDAO productDao;

    @Resource
    EntitlementCacheManagerIF entitlementCacheMgr;

    private static final String CACHE_KEY_ACTIVE_PRODUCTS = "Products:All-Products";

    @SuppressWarnings("unused")
    private void dumpList(
            final String msg,
            final List<Entitlement> list )
    {
        if( logger.isInfoEnabled() ) {
            logger.info(msg + " {");
            list.forEach( item -> {
                logger.info( "  - " + item );
            });
            logger.info("}");
        }
    }

    /**
     * Filter entitlements in list.
     * @param filter        Filter to be applied.
     * @param list          List to process.
     * @param setProducts   Set of unique product ID's to accumulate.
     * @return  List of entitlements that meet filter requirements.
     */
    private List<Entitlement> filterEntitlements(
            final EntitlementRangeFilter filter,
            final List<Entitlement> list,
            final Set<String> setProducts )
    {
        List<Entitlement> listReturn = new ArrayList<Entitlement>();
        for( Entitlement ent : list  ) {
            String stsCode = ent.getStatus();
            if( stsCode.equalsIgnoreCase(CommonConstants.STATUS_ACTIVE ) ) {
                if( filter.checkEntitlementRange(ent) ) {
                    listReturn.add(ent);
                    String productId = ent.getProductId();
                    if( ! setProducts.contains(productId) ) {
                        setProducts.add(productId);
                    }
                }
            }
        }
        return listReturn;
    }

    /**
     * Build a set of active products used by entitlements.
     * @param setProducts   Set of product ID's referenced by entitlements.
     * @return  List of active products.
     */
    @SuppressWarnings("unchecked")
    private Set<String> filterActiveProducts( final Set<String> setProducts ) {
        if( logger.isDebugEnabled() ) {
            logger.debug("filterActiveProducts()");
        }

        List<ProductRange> listProducts = null;

        GeezerEliminationCache cacheProduct = entitlementCacheMgr.fetchProductCache();
        Object objProducts = cacheProduct.fetchData(CACHE_KEY_ACTIVE_PRODUCTS );
        if( objProducts == null ) {
            if( logger.isDebugEnabled() ) {
                logger.debug("Retrieving from Store.");
            }

            listProducts = productDao.findActiveProductRange();
            cacheProduct.addItem( CACHE_KEY_ACTIVE_PRODUCTS, listProducts );
        }
        else {
            if( logger.isDebugEnabled() ) {
                logger.debug("Retrieving from Cache.");
            }

            listProducts = (List<ProductRange>) objProducts;
        }

        Set<String> setReturn = buildUniqueActiveProducts( listProducts, setProducts );
        return setReturn;
    }

    /**
     * Build set of unique products from list of active product ID's.
     * @param listProducts  List of all products.
     * @param setProducts   Set of product ID's.
     * @return
     */
    private Set<String> buildUniqueActiveProducts(
            final List<ProductRange> listProducts,
            final Set<String> setProducts )
    {
        Set<String> setReturn = new HashSet<String>();
        ProductRangeFilter filter = new ProductRangeFilter();
        listProducts.forEach( product -> {
            String productId = product.getProductId();
            if( setProducts.contains(productId)) {
                if( filter.checkProductRange( product )) {
                    setReturn.add(productId);
                }
            }
        });
        return setReturn;
    }

    /**
     * Filter entitlements by active products.
     * @param list              List of entitlements.
     * @param activeProducts    Set of active product ID's.
     * @return List of current entitlements.
     */
    private List<Entitlement> filterProducts(
            final List<Entitlement> list,
            final Set<String> activeProducts )
    {
        List<Entitlement> listReturn = new ArrayList<Entitlement>();
        for( Entitlement ent : list  ) {
            String productId = ent.getProductId();
            if( activeProducts.contains(productId) ) {
                listReturn.add(ent);
            }
        }
        return listReturn;
    }

    @Override
    public List<Entitlement> processRequest( final EntitlementCurrentRequest request ) {
        if( logger.isInfoEnabled() ) {
            logger.info("processRequest()");
        }
        EntitlementRangeFilter filter = new EntitlementRangeFilter();

        List<Entitlement> listEntitlements = new ArrayList<Entitlement>();
        List<EntitlementCurrentOwnerQueryItem> listQueryItems = request.getQueryItems();

        Set<String> setProducts = new HashSet<String>();

        listQueryItems.forEach( queryItem -> {
            /*
            if( logger.isInfoEnabled() ) {
                logger.info("queryItem");
                logger.info(queryItem.getOwnerType());
            }
            */

            String ownerType = queryItem.getOwnerType();
            List<String>  listIds = queryItem.getListOwnerIds();

            /*
            if( logger.isInfoEnabled() ) {
                logger.info( "ownerType=" + ownerType);
                logger.info( "listIds=" + listIds);
            }
            */

            listIds.forEach( ownerId -> {
                if( ! StringUtils.isEmpty( ownerId ) ) {
                    // List<Entitlement> listOwnerEnt = entitlementDao.findByOwner(ownerType, ownerId);
                    List<Entitlement> listOwnerEnt = fetchOwnerEntitlements(ownerType, ownerId);
                    /*
                    if( logger.isInfoEnabled() ) {
                        dumpList("listOwnerEnt", listOwnerEnt);
                    }
                    */
                    if( listOwnerEnt.size() > 0 ) {
                        listEntitlements.addAll( filterEntitlements( filter, listOwnerEnt, setProducts ) );
                    }
                }
            });
        });

        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "setProducts=" + setProducts);
            logger.info( "setProdSize=" + setProducts.size());
        }
        */

        if( setProducts.size() > 0 ) {
            // Filter products
            Set<String> activeProducts = filterActiveProducts(setProducts);
            return filterProducts(listEntitlements, activeProducts);
        }

        return listEntitlements;
    }

    @SuppressWarnings("unchecked")
    private List<Entitlement> fetchOwnerEntitlements(
            final String ownerType,
            final String ownerId )
    {
        List<Entitlement> listOwnerEnt = null;
        boolean flagCached = false;
        GeezerEliminationCache cacheEntitlement = entitlementCacheMgr.fetchEntitlementCache(ownerType);
        if( cacheEntitlement != null ) {
            // Retrieve from cache
            flagCached = true;
            Object objEntitlements = cacheEntitlement.fetchData( ownerId );
            if( objEntitlements != null ) {
                if( logger.isDebugEnabled() ) {
                    logger.debug("Retrieving from Cache.");
                }
                listOwnerEnt = (List<Entitlement>) objEntitlements;
            }
        }

        if( listOwnerEnt == null ) {
            if( logger.isDebugEnabled() ) {
                logger.debug("Retrieving from Store.");
            }
            listOwnerEnt = entitlementDao.findByOwner(ownerType, ownerId);
        }
        if( flagCached && ( listOwnerEnt != null ) ) {
            cacheEntitlement.addItem( ownerId, listOwnerEnt );
        }

        return listOwnerEnt;
    }

}
