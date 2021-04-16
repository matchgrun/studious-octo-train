/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.impl.mapper.BaseSiamRowMapper;
import com.ibm.wh.siam.entitlement.dto.Entitlement;

/**
 * @author Match Grun
 *
 */
public class EntitlementRowMapper
extends BaseSiamRowMapper
implements RowMapper<Entitlement>
{

    @Override
    public Entitlement mapRow(ResultSet rs, int rowNum)
            throws SQLException
    {
        Entitlement entitlement = new Entitlement();

        entitlement.setEntitlementId(rs.getString("entitlement_id"));
        entitlement.setOwnerType(rs.getString("owner_type"));
        entitlement.setOwnerId(rs.getString("owner_id"));
        entitlement.setProductId(rs.getString("product_id"));
        entitlement.setStartDate(rs.getDate("start_date"));
        entitlement.setEndDate(rs.getDate("end_date"));
        entitlement.setStatus(rs.getString("status"));

        super.mapCreator(rs,  entitlement);
        entitlement.setLdapEntryDN(rs.getString("ldap_entry_dn"));

        return entitlement;
    }

}
