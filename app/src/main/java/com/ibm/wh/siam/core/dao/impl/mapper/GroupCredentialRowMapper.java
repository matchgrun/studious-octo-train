/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.GroupCredential;

/**
 * @author Match Grun
 *
 */
public class GroupCredentialRowMapper
extends BaseSiamRowMapper
implements RowMapper<GroupCredential>
{

    @Override
    public GroupCredential mapRow(ResultSet rs, int rowNum) throws SQLException {

        GroupCredential orgGroup = new GroupCredential();
        orgGroup.setMemberId(rs.getString("member_id"));
        orgGroup.setCredentialId(rs.getString("group_id"));
        orgGroup.setGroupId(rs.getString("group_id"));
        orgGroup.setStatus(rs.getString("status"));
        super.mapCreator(rs,  orgGroup);

        return orgGroup;
    }

}
