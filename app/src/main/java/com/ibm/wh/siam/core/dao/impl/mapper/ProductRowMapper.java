/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.Product;

/**
 * @author Match Grun
 *
 */
public class ProductRowMapper
extends BaseSiamRowMapper
implements RowMapper<Product>
{
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {

        Product item = new Product();

        item.setProductId(rs.getString("product_id"));
        item.setProductCode(rs.getString("product_code"));
        item.setDescription(rs.getString("description"));
        item.setStartDate(rs.getDate("start_date"));
        item.setEndDate(rs.getDate("end_date"));
        item.setStatus(rs.getString("status"));

        super.mapCreator(rs,  item);
        item.setLdapEntryDN(rs.getString("ldap_entry_dn"));
        return item;
    }

}
