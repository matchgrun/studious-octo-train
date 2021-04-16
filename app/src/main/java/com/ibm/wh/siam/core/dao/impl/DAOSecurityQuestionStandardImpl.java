/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ibm.wh.siam.core.dao.SecurityQuestionStandardDAO;
import com.ibm.wh.siam.core.dao.impl.mapper.SecurityQuestionRowMapper;
import com.ibm.wh.siam.core.dao.impl.mapper.SecurityQuestionStandardRowMapper;
import com.ibm.wh.siam.core.dto.SecurityQuestion;
import com.ibm.wh.siam.core.dto.SecurityQuestionStandard;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOSecurityQuestionStandardImpl
extends BaseSiamDAO
implements SecurityQuestionStandardDAO
{
    private static final String Q_SECURITY_STD =
            SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.SECURITY_QUESTION_STD;
    private static final String COLS_ALL = SiamTableNames.SECURITY_QUESTION_STD + ".*";

    @SuppressWarnings("unused")
    private static final String PRIMARY_KEY = "question_id";

    private static final String SQL_BY_QUESTION_ID =
            "select " + COLS_ALL + " from " + Q_SECURITY_STD +
            " where question_id = ?";

    private static final String SQL_BY_QUESTION_SET_ID =
            "select " + COLS_ALL + " from " + Q_SECURITY_STD +
            " where question_set_id = :questionSetId" +
            " order by display_sequence";

    @SuppressWarnings("unused")
    private static final String SQL_INSERT =
            "insert into " + Q_SECURITY_STD +
            " ( question_set_id, display_sequence, question," +
            " status, created_by, created_by_app " +
            ")" +
            " values (" +
            " :questionSetId, :sequence, :question," +
            " :status, :createdBy, :createdByApp" +
            " )";

    @SuppressWarnings("unused")
    private static final String SQL_UPDATE =
            "update " + Q_SECURITY_STD + " set " +
            " question = :question," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where question_id = :questionId"
            ;

    @SuppressWarnings("unused")
    private static final String SQL_DELETE =
            "delete from " + Q_SECURITY_STD +
            " where question_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOSecurityQuestionStandardImpl.class );

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;

    // Setup template.
    public DAOSecurityQuestionStandardImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }


    @Override
    public SecurityQuestion findById(final String questionId) {
        return findSingleForValue(SQL_BY_QUESTION_ID, questionId);
    }

    private SecurityQuestion findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue(S)");
            logger.info( "identifier=" + identifier );
            logger.info( "sqlFind=" + sqlFind );
        }
        SecurityQuestion objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new SecurityQuestionRowMapper());
            if( logger.isInfoEnabled() ) {
                logger.info( "Found obj=" + objFound );
            }
        }
        catch( EmptyResultDataAccessException e) {
            if( logger.isInfoEnabled() ) {
                logger.info( "SecurityQuestion NOT-FOUND" );
            }
        }
        catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objFound;
    }

    @Override
    public List<SecurityQuestionStandard> fetchStandardQuestions(final String questionSetId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByCredential()");
            logger.info( "questionSetId=" + questionSetId );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("questionSetId", questionSetId );
        return namedTemplate.query( SQL_BY_QUESTION_SET_ID, params, new SecurityQuestionStandardRowMapper() );
    }

}
