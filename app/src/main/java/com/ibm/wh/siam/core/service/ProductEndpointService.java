/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.ProductEndpoint;
import com.ibm.wh.siam.core.request.product.ProductEndpointSaveRequest;
import com.ibm.wh.siam.core.response.product.ProductEndpointListResponse;
import com.ibm.wh.siam.core.response.product.ProductEndpointResponse;
import com.ibm.wh.siam.core.response.product.ProductEndpointSaveResponse;
import com.ibm.wh.siam.core.response.product.ProductListEndpointResponse;

/**
 * @author Match Grun
 *
 */
public interface ProductEndpointService {
    ProductEndpointResponse findById( final String endpointId );
    ProductEndpointResponse deleteById( final RecordUpdaterIF recordUpdater, final String endpointId );
    ProductEndpointResponse save( final RecordUpdaterIF recordUpdater, final ProductEndpoint endpoint );
    ProductEndpointSaveResponse save( final RecordUpdaterIF recordUpdater, final ProductEndpointSaveRequest req );

    ProductEndpointListResponse findByProduct(final String productId);
    ProductListEndpointResponse findProductByEndpointUrl(final String endpointUrl);

}
