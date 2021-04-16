/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.AttributeValue;

/**
 * @author Match Grun
 *
 */
public class AttributeValueRowMapper
extends BaseSiamRowMapper
implements RowMapper<AttributeValue>
{
    @Override
    public AttributeValue mapRow(ResultSet rs, int rowNum) throws SQLException {

        AttributeValue item = new AttributeValue();

        item.setAttributeId(rs.getString("attribute_id"));
        item.setAttributeDescriptorId(rs.getString("attribute_descriptor_id"));
        item.setOwnerType(rs.getString("owner_type"));
        item.setOwnerId(rs.getString("owner_id"));
        item.setAttributeValue(rs.getString("attribute_value"));
        item.setStatus(rs.getString("status"));

        super.mapCreator(rs,  item);
        item.setLdapEntryDN(rs.getString("ldap_entry_dn"));
        return item;
    }

}
