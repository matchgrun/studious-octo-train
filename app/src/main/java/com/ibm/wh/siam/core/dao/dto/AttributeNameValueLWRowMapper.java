/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author Match Grun
 *
 */
public class AttributeNameValueLWRowMapper
implements RowMapper<AttributeNameValueLW> {
    @Override
    public AttributeNameValueLW mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        AttributeNameValueLW item = new AttributeNameValueLW();
        item.setAttributeDescriptorId(rs.getString("attribute_descriptor_id"));
        item.setAttributeId(rs.getString("attribute_id"));
        item.setAttributeName(rs.getString("attribute_name"));
        item.setAttributeValue(rs.getString("attribute_value"));
        return item;
    }
}
