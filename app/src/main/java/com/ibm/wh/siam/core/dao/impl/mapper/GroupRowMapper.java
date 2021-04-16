/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.Group;

/**
 * @author Match Grun
 *
 */
public class GroupRowMapper
extends BaseSiamRowMapper
implements RowMapper<Group>
{

    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {

        Group grp = new Group();

        grp.setGroupId(rs.getString("group_id"));
        grp.setGroupCode(rs.getString("group_code"));
        grp.setGroupName(rs.getString("group_name"));
        grp.setGroupType(rs.getString("group_type"));
        grp.setDescription(rs.getString("description"));
        grp.setAccountGroup(rs.getString("account_group"));

        grp.setStatus(rs.getString("status"));
        super.mapCreator(rs,  grp);
        /*

        grp.setCreateDate(rs.getTimestamp("create_date"));
        grp.setCreatedBy(rs.getString("created_by"));
        grp.setCreatedByApp(rs.getString("created_by_app"));
        grp.setModifyDate(rs.getTimestamp("modify_date"));
        grp.setModifiedBy(rs.getString("modified_by"));
        grp.setModifiedByApp(rs.getString("modified_by_app"));
        */

        grp.setLdapEntryDN(rs.getString("ldap_entry_dn"));
        grp.setAdExternalDN(rs.getString("ad_external_dn"));

        return grp;
    }

}
