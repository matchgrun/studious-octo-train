/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.SecurityQuestionAnswer;

/**
 * @author Match Grun
 *
 */
public class SecurityQuestionAnswerRowMapper
extends BaseSiamRowMapper
implements RowMapper<SecurityQuestionAnswer>
{
    @Override
    public SecurityQuestionAnswer mapRow(ResultSet rs, int rowNum)
            throws SQLException
    {

        SecurityQuestionAnswer item = new SecurityQuestionAnswer();
        item.setQuestionId(rs.getString("question_id"));
        item.setCredentialId(rs.getString("credential_id"));
        item.setDisplaySequence(rs.getInt("display_sequence"));
        item.setQuestion(rs.getString("question"));
        item.setAnswer(rs.getString("answer"));
        item.setEncryptionCode(rs.getString("encryption_code"));
        item.setStatus(rs.getString("status"));

        super.mapCreator(rs,  item);
        item.setLdapEntryDN(rs.getString("ldap_entry_dn"));

        return item;
    }

}
