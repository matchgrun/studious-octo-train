/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.AttributeNameValue;

/**
 * @author Match Grun
 *
 */
public class AttributeNameValueRowMapper
extends BaseSiamRowMapper
implements RowMapper<AttributeNameValue>
{
    @Override
    public AttributeNameValue mapRow(ResultSet rs, int rowNum) throws SQLException {

        AttributeNameValue item = new AttributeNameValue();

        item.setAttributeId(rs.getString("attribute_id"));
        item.setAttributeDescriptorId(rs.getString("attribute_descriptor_id"));
        item.setAttributeName(rs.getString("attribute_name"));
        item.setOwnerType(rs.getString("owner_type"));
        item.setOwnerId(rs.getString("owner_id"));
        item.setAttributeValue(rs.getString("attribute_value"));
        item.setStatus(rs.getString("status"));
        super.mapCreator(rs,  item);
        return item;
    }

}
