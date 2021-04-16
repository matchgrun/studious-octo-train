/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.wh.siam.core.dto.BaseSiamObject;


/**
 * @author Match Grun
 *
 */
public abstract class BaseSiamRowMapper
{
    protected void mapCreator( ResultSet rs, BaseSiamObject obj )
    throws SQLException
    {
        obj.setCreateDate(rs.getTimestamp("create_date"));
        obj.setCreatedBy(rs.getString("created_by"));
        obj.setCreatedByApp(rs.getString("created_by_app"));
        obj.setModifyDate(rs.getTimestamp("modify_date"));
        obj.setModifiedBy(rs.getString("modified_by"));
        obj.setModifiedByApp(rs.getString("modified_by_app"));
    }

}
