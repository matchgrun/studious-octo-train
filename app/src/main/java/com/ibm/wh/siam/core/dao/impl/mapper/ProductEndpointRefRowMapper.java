/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.response.product.ProductEndpointRef;

/**
 * @author Match Grun
 *
 */
public class ProductEndpointRefRowMapper
extends BaseSiamRowMapper
implements RowMapper<ProductEndpointRef>
{
    @Override
    public ProductEndpointRef mapRow(ResultSet rs, int rowNum) throws SQLException {

        ProductEndpointRef item = new ProductEndpointRef();
        item.setProductId(rs.getString("product_id"));
        item.setEndpointId(rs.getString("endpoint_id"));
        item.setEndpointUrl(rs.getString("endpoint_url"));
        item.setProductCode(rs.getString("product_code"));
        return item;
    }

}
