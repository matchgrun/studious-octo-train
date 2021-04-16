/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.impl.mapper.BaseSiamRowMapper;
import com.ibm.wh.siam.entitlement.dto.EntitlementProductOwner;

/**
 * @author Match Grun
 *
 */
public class EntitlementProductOwnerRowMapper
extends BaseSiamRowMapper
implements RowMapper<EntitlementProductOwner>
{

    @Override
    public EntitlementProductOwner mapRow(ResultSet rs, int rowNum)
            throws SQLException
    {
        EntitlementProductOwner item = new EntitlementProductOwner();

        item.setEntitlementId(rs.getString("entitlement_id"));
        item.setOwnerType(rs.getString("owner_type"));
        item.setOwnerId(rs.getString("owner_id"));
        item.setProductId(rs.getString("product_id"));
        item.setEntitlementStartDate(rs.getDate("entitlement_start"));
        item.setEntitlementEndDate(rs.getDate("entitlement_end"));
        item.setStatus(rs.getString("entitlement_status"));

        item.setOwnerCode(rs.getString("owner_code"));
        item.setOwnerName(rs.getString("owner_name"));

        item.setProductCode(rs.getString("product_code"));
        item.setProductName(rs.getString("product_name"));
        item.setProductStatus(rs.getString("product_status"));
        item.setProductStartDate(rs.getDate("product_start"));
        item.setProductEndDate(rs.getDate("product_end"));

        return item;
    }

}
