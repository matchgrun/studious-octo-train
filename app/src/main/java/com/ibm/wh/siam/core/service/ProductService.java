/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.Product;
import com.ibm.wh.siam.core.request.product.ProductSaveRequest;
import com.ibm.wh.siam.core.response.product.ProductListResponse;
import com.ibm.wh.siam.core.response.product.ProductRangeListResponse;

/**
 * @author Match Grun
 *
 */
public interface ProductService<ProductResponse> {

    /**
     * Category name for Product caching.
     */
    public static final String PRODUCT_CACHE_CATEGORY = "Cache:ProductCategory";

    ProductListResponse findAll();

    com.ibm.wh.siam.core.response.product.ProductResponse findByCode(final String prodCode);
    com.ibm.wh.siam.core.response.product.ProductResponse findById(final String prodId);
    com.ibm.wh.siam.core.response.product.ProductResponse save( final RecordUpdaterIF recordUpdater, final Product  prod );
    com.ibm.wh.siam.core.response.product.ProductResponse save( final RecordUpdaterIF recordUpdater, final ProductSaveRequest request );
    com.ibm.wh.siam.core.response.product.ProductResponse deleteById( final RecordUpdaterIF recordUpdater, final String prodId );

    ProductRangeListResponse findAllActiveProducts();

}
