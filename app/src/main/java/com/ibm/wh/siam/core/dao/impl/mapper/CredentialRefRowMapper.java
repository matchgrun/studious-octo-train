/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.CredentialRef;

/**
 * @author Match Grun
 *
 */
public class CredentialRefRowMapper
extends BaseSiamRowMapper
implements RowMapper<CredentialRef>
{

    @Override
    public CredentialRef mapRow(ResultSet rs, int rowNum) throws SQLException {

        CredentialRef cred = new CredentialRef();

        cred.setCredentialId(rs.getString("credential_id"));
        cred.setUserId(rs.getString("user_id"));
        cred.setFirstName(rs.getString("first_name"));
        cred.setLastName(rs.getString("last_name"));
        cred.setDisplayName(rs.getString("display_name"));
        cred.setEmailAddress(rs.getString("email_address"));
        cred.setTelephoneNumber(rs.getString("telephone_number"));
        cred.setInternalUser(rs.getBoolean("internal_user"));
        cred.setExternalLdapEntryDN(rs.getString("ldap_entry_dn"));
        cred.setExternalAdEntryDN(rs.getString("ad_external_dn"));

        cred.setStatus(rs.getString("status"));
        super.mapCreator(rs,  cred);

        return cred;
    }

}
