package com.ibm.wh.siam.busunit.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.impl.mapper.BaseSiamRowMapper;
import com.ibm.wh.siam.core.dto.OrganizationRelationship;

public class OrganizationRelationshipRowMapper
extends BaseSiamRowMapper
implements RowMapper<OrganizationRelationship>
{

    @Override
    public OrganizationRelationship mapRow(ResultSet rs, int rowNum) throws SQLException {

        OrganizationRelationship item = new OrganizationRelationship();

        item.setRelationshipId(rs.getString("relationship_id"));
        item.setOrganizationId(rs.getString("organization_id"));
        item.setRelatedOrgId(rs.getString("related_org_id"));
        item.setRelationshipType(rs.getString("relationship_type"));

        return item;
    }

}
