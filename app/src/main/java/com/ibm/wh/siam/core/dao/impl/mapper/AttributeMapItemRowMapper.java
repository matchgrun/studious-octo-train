/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.dto.AttributeMapItem;

/**
 * @author Match Grun
 *
 */
public class AttributeMapItemRowMapper
extends BaseSiamRowMapper
implements RowMapper<AttributeMapItem>
{
    @Override
    public AttributeMapItem mapRow(ResultSet rs, int rowNum) throws SQLException {

        AttributeMapItem item = new AttributeMapItem();
        item.setId(rs.getString("attribute_descriptor_id"));
        item.setName(rs.getString("attribute_name"));
        return item;
    }

}
