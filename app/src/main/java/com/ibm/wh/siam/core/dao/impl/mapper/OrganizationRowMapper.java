package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.Organization;

public class OrganizationRowMapper
extends BaseSiamRowMapper
implements RowMapper<Organization>
{

    @Override
    public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {

        Organization item = new Organization();

        item.setOrganizationId(rs.getString("organization_id"));
        item.setOrganizationCode(rs.getString("organization_code"));
        item.setOrganizationName(rs.getString("organization_name"));
        item.setOrganizationType(rs.getString("organization_type"));
        item.setDescription(rs.getString("description"));
        item.setAccountNumber(rs.getString("account_number"));
        item.setAccountGroup(rs.getString("account_group"));
        item.setLegacyEntityId(rs.getString("legacy_entity_id"));
        item.setLegacyEntityShortName(rs.getString("legacy_entity_short_name"));
        item.setMarketSegment(rs.getString("market_segment"));
        // org.setHaveAttributes(rs.getString("have_attributes"));
        // org.setHaveContracts(rs.getString("have_contracts"));

        item.setStatus(rs.getString("status"));
        super.mapCreator(rs,  item);

        item.setLdapEntryDN(rs.getString("ldap_entry_dn"));
        item.setAdExternalDn(rs.getString("ad_external_dn"));

        return item;
    }

}
