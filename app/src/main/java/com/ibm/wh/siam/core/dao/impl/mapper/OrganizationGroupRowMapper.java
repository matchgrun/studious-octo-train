/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.OrganizationGroup;

/**
 * @author Match Grun
 *
 */
public class OrganizationGroupRowMapper
extends BaseSiamRowMapper
implements RowMapper<OrganizationGroup>
{

    @Override
    public OrganizationGroup mapRow(ResultSet rs, int rowNum) throws SQLException {

        OrganizationGroup orgGroup = new OrganizationGroup();

        orgGroup.setOrganizationGroupId(rs.getString("organization_group_id"));
        orgGroup.setOrganizationId(rs.getString("organization_id"));
        orgGroup.setGroupId(rs.getString("group_id"));

        orgGroup.setStatus(rs.getString("status"));
        super.mapCreator(rs,  orgGroup);

        return orgGroup;
    }

}
