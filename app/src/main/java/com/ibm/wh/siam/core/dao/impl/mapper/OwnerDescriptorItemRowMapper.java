/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.dto.OwnerDescriptorItem;

/**
 * @author Match Grun
 *
 */
public class OwnerDescriptorItemRowMapper
extends BaseSiamRowMapper
implements RowMapper<OwnerDescriptorItem>
{
    @Override
    public OwnerDescriptorItem mapRow(ResultSet rs, int rowNum) throws SQLException {

        OwnerDescriptorItem item = new OwnerDescriptorItem();
        item.setId(rs.getString("owner_id"));
        item.setName(rs.getString("owner_name"));
        item.setCode(rs.getString("owner_code"));
        return item;
    }

}
