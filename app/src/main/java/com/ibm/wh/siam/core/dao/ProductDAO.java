/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import java.util.List;
import java.util.Map;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.dto.ProductRange;
import com.ibm.wh.siam.core.dto.Product;

/**
 * @author Match Grun
 *
 */
public interface ProductDAO {
    public Iterable<Product> findAll();

    public Product findByCode( final String prodCode );
    public Product findById( final String prodId );

    public Product insert( RecordUpdaterIF recordUpdater, Product prod );
    public Product updateById( RecordUpdaterIF recordUpdater, Product prod );
    public Product deleteById( RecordUpdaterIF recordUpdater, Product prod );

    public Map<String,String> findMapProductIds( final List<String> listIds );

    public List<ProductRange> findActiveProductRange();

}
