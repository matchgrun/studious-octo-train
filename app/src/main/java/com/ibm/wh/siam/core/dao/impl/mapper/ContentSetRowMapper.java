/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.ContentSet;

/**
 * @author Match Grun
 *
 */
public class ContentSetRowMapper
extends BaseSiamRowMapper
implements RowMapper<ContentSet>
{
    @Override
    public ContentSet mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        ContentSet item = new ContentSet();

        item.setContentSetId(rs.getString("content_set_id"));
        item.setOwnerType(rs.getString("owner_type"));
        item.setOwnerId(rs.getString("owner_id"));
        item.setContentSetDescriptorId(rs.getString("content_set_descriptor_id"));
        item.setContentSetType(rs.getString("content_set_type"));
        item.setDescription(rs.getString("description"));
        item.setStartDate(rs.getDate("start_date"));
        item.setEndDate(rs.getDate("end_date"));
        item.setStatus(rs.getString("status"));
        item.setExternalContentId(rs.getString("external_content_id"));
        item.setExternalRoleName(rs.getString("external_role_name"));

        super.mapCreator(rs,  item);
        return item;
    }

}
