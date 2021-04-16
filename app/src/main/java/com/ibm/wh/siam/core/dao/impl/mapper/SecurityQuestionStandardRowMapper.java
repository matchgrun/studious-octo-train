/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.SecurityQuestionStandard;

/**
 * @author Match Grun
 *
 */
public class SecurityQuestionStandardRowMapper
extends BaseSiamRowMapper
implements RowMapper<SecurityQuestionStandard>
{
    @Override
    public SecurityQuestionStandard mapRow(ResultSet rs, int rowNum)
            throws SQLException
    {
        SecurityQuestionStandard item = new SecurityQuestionStandard();
        item.setQuestionId(rs.getString("question_id"));
        item.setQuestionSetId(rs.getString("question_set_id"));
        item.setDisplaySequence(rs.getInt("display_sequence"));
        item.setQuestion(rs.getString("question"));
        item.setStatus(rs.getString("status"));

        super.mapCreator(rs,  item);
        item.setLdapEntryDN(rs.getString("ldap_entry_dn"));

        return item;
    }

}
