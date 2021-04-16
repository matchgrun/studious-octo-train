/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.impl.content.ContentAccessorSqlBuilderIF;
import com.ibm.wh.siam.core.dto.ContentSetAccessDetail;

/**
 * @author Match Grun
 *
 */
public class ContentSetAccessDetailRowMapper
extends BaseSiamRowMapper
implements RowMapper<ContentSetAccessDetail>
{
    @Override
    public ContentSetAccessDetail mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        ContentSetAccessDetail item = new ContentSetAccessDetail();

        item.setContentSetAccessId(rs.getString("content_set_access_id"));
        item.setContentSetId(rs.getString("content_set_id"));
        item.setAccessorType(rs.getString("accessor_type"));
        item.setAccessorId(rs.getString("accessor_id"));
        item.setContentAccessId(rs.getString("content_access_id"));
        item.setStartDate(rs.getDate("start_date"));
        item.setEndDate(rs.getDate("end_date"));
        item.setStatus(rs.getString("status"));

        item.setAccessorCode(rs.getString(ContentAccessorSqlBuilderIF.ALIAS_CODE));
        item.setAccessorName(rs.getString(ContentAccessorSqlBuilderIF.ALIAS_NAME));

        super.mapCreator(rs,  item);
        return item;
    }

}
