/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.SecurityQuestionSet;

/**
 * @author Match Grun
 *
 */
public class SecurityQuestionSetRowMapper
extends BaseSiamRowMapper
implements RowMapper<SecurityQuestionSet>
{
    @Override
    public SecurityQuestionSet mapRow(ResultSet rs, int rowNum)
            throws SQLException
    {
        SecurityQuestionSet item = new SecurityQuestionSet();
        item.setQuestionSetId(rs.getString("question_set_id"));
        item.setQuestionSetName(rs.getString("question_set_name"));
        item.setDescription(rs.getString("description"));
        item.setRequiredQuestions(rs.getInt("required_questions"));
        item.setSecurityLevel(rs.getInt("security_level"));
        item.setStatus(rs.getString("status"));
        super.mapCreator(rs,  item);
        item.setLdapEntryDN(rs.getString("ldap_entry_dn"));

        return item;
    }

}
