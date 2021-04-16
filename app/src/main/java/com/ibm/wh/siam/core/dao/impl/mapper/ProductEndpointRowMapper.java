/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.ProductEndpoint;

/**
 * @author Match Grun
 *
 */
public class ProductEndpointRowMapper
extends BaseSiamRowMapper
implements RowMapper<ProductEndpoint>
{
    @Override
    public ProductEndpoint mapRow(ResultSet rs, int rowNum) throws SQLException {

        ProductEndpoint item = new ProductEndpoint();
        item.setProductId(rs.getString("product_id"));
        item.setEndpointId(rs.getString("endpoint_id"));
        item.setEndpointUrl(rs.getString("endpoint_url"));
        item.setStatus(rs.getString("status"));
        super.mapCreator(rs,  item);
        return item;
    }

}
