/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.SecurityQuestion;

/**
 * @author Match Grun
 *
 */
public class SecurityQuestionRowMapper
extends BaseSiamRowMapper
implements RowMapper<SecurityQuestion>
{
    @Override
    public SecurityQuestion mapRow(ResultSet rs, int rowNum)
            throws SQLException
    {
        SecurityQuestion item = new SecurityQuestion();
        item.setCredentialId(rs.getString("credential_id"));
        item.setQuestionId(rs.getString("question_id"));
        item.setDisplaySequence(rs.getInt("display_sequence"));
        item.setQuestion(rs.getString("question"));
        item.setStatus(rs.getString("status"));

        super.mapCreator(rs,  item);
        item.setLdapEntryDN(rs.getString("ldap_entry_dn"));

        return item;
    }

}
