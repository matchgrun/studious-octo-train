/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.impl.mapper.BaseSiamRowMapper;

/**
 * @author Match Grun
 *
 */
public class CredentialRefLWRowMapper
extends BaseSiamRowMapper
implements RowMapper<CredentialRefLW>
{
    @Override
    public CredentialRefLW mapRow(ResultSet rs, int rowNum) throws SQLException {

        CredentialRefLW cred = new CredentialRefLW();
        cred.setCredentialId(rs.getString("credential_id"));
        cred.setUserId(rs.getString("user_id"));
        cred.setEmailAddress(rs.getString("email_address"));
        cred.setStatus(rs.getString("status"));
        return cred;
    }

}
