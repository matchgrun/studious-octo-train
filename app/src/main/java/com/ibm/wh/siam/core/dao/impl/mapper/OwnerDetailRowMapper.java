/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.dto.OwnerDetail;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerSqlBuilderIF;

/**
 * @author Match Grun
 *
 */
public class OwnerDetailRowMapper
extends BaseSiamRowMapper
implements RowMapper<OwnerDetail>
{
    @Override
    public OwnerDetail mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        OwnerDetail item = new OwnerDetail();
        item.setOwnerId(rs.getString(OwnerSqlBuilderIF.ALIAS_ID));
        item.setOwnerType(rs.getString(OwnerSqlBuilderIF.ALIAS_TYPE));
        item.setOwnerCode(rs.getString(OwnerSqlBuilderIF.ALIAS_CODE));
        item.setOwnerName(rs.getString(OwnerSqlBuilderIF.ALIAS_NAME));
        return item;
    }

}
