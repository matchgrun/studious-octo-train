/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.AttributeDescriptor;

/**
 * @author Match Grun
 *
 */
public class AttributeDescriptorRowMapper
extends BaseSiamRowMapper
implements RowMapper<AttributeDescriptor>
{
    @Override
    public AttributeDescriptor mapRow(ResultSet rs, int rowNum) throws SQLException {

        AttributeDescriptor item = new AttributeDescriptor();

        item.setAttributeDescriptorId(rs.getString("attribute_descriptor_id"));
        item.setAttributeName(rs.getString("attribute_name"));
        item.setDescription(rs.getString("description"));
        item.setStatus(rs.getString("status"));

        super.mapCreator(rs,  item);
        return item;
    }

}
