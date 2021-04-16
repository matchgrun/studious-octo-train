/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.dto.ProductRange;

/**
 * @author Match Grun
 *
 */
public class ProductRangeRowMapper
implements RowMapper<ProductRange>
{
    @Override
    public ProductRange mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductRange item = new ProductRange();
        item.setProductId(rs.getString("product_id"));
        item.setStartDate(rs.getDate("start_date"));
        item.setEndDate(rs.getDate("end_date"));
        return item;
    }

}
