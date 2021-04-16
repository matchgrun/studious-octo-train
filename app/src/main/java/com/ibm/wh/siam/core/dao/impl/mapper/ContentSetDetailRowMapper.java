/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.impl.content.ContentDetailSqlBuilderIF;
import com.ibm.wh.siam.core.dto.ContentSetDetail;

/**
 * @author Match Grun
 *
 */
public class ContentSetDetailRowMapper
extends BaseSiamRowMapper
implements RowMapper<ContentSetDetail>
{
    @Override
    public ContentSetDetail mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        ContentSetDetail item = new ContentSetDetail();

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

        item.setOwnerCode(rs.getString(ContentDetailSqlBuilderIF.ALIAS_CODE));
        item.setOwnerName(rs.getString(ContentDetailSqlBuilderIF.ALIAS_NAME));

        super.mapCreator(rs,  item);
        return item;
    }

}
