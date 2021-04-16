/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import java.util.List;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.ProductEndpoint;
import com.ibm.wh.siam.core.response.product.ProductEndpointRef;

/**
 * @author Match Grun
 *
 */
public interface ProductEndpointDAO {
    public ProductEndpoint findById( final String endpointId );
    public List<ProductEndpoint> findByProduct( final String productId );
    public List<ProductEndpointRef> findEndpointByUrl( final String endpointUrl );

    public ProductEndpoint insert( RecordUpdaterIF recordUpdater, ProductEndpoint endpoint );
    public ProductEndpoint updateById( RecordUpdaterIF recordUpdater, ProductEndpoint endpoint );
    public ProductEndpoint deleteById( RecordUpdaterIF recordUpdater, ProductEndpoint endpoint );

}
