/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ibm.wh.siam.core.dao.SecurityQuestionSetDAO;
import com.ibm.wh.siam.core.dao.impl.mapper.SecurityQuestionSetRowMapper;
import com.ibm.wh.siam.core.dto.SecurityQuestionSet;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOSecurityQuestionSetImpl
extends BaseSiamDAO
implements SecurityQuestionSetDAO
{
    private static final String Q_SECURITY_SET =
            SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.SECURITY_QUESTION_SET;
    private static final String COLS_ALL = SiamTableNames.SECURITY_QUESTION_SET + ".*";

    @SuppressWarnings("unused")
    private static final String PRIMARY_KEY = "question_id";

    private static final String SQL_BY_QUESTION_SET_ID =
            "select " + COLS_ALL + " from " + Q_SECURITY_SET +
            " where question_set_id = ?";

    private static final String SQL_BY_QUESTION_SET_NAME =
            "select " + COLS_ALL + " from " + Q_SECURITY_SET +
            " where question_set_name = ?";

    @SuppressWarnings("unused")
    private static final String SQL_INSERT =
            "insert into " + Q_SECURITY_SET +
            " ( question_set_id, display_sequence, question," +
            " status, created_by, created_by_app " +
            ")" +
            " values (" +
            " :questionSetId, :sequence, :question," +
            " :status, :createdBy, :createdByApp" +
            " )";

    @SuppressWarnings("unused")
    private static final String SQL_UPDATE =
            "update " + Q_SECURITY_SET + " set " +
            " question = :question," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where question_id = :questionId"
            ;

    @SuppressWarnings("unused")
    private static final String SQL_DELETE =
            "delete from " + Q_SECURITY_SET +
            " where question_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOSecurityQuestionSetImpl.class );

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;

    // Setup template.
    public DAOSecurityQuestionSetImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }


    @Override
    public SecurityQuestionSet findById(String questionSetId) {
        return findSingleForValue(SQL_BY_QUESTION_SET_ID, questionSetId);
    }

    @Override
    public SecurityQuestionSet findByName(String questionSetName) {
        return findSingleForValue(SQL_BY_QUESTION_SET_NAME, questionSetName);
    }

    private SecurityQuestionSet findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue(S)");
            logger.info( "identifier=" + identifier );
            logger.info( "sqlFind=" + sqlFind );
        }
        SecurityQuestionSet objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new SecurityQuestionSetRowMapper());
            if( logger.isInfoEnabled() ) {
                logger.info( "Found obj=" + objFound );
            }
        }
        catch( EmptyResultDataAccessException e) {
            if( logger.isInfoEnabled() ) {
                logger.info( "SecurityQuestionSet NOT-FOUND" );
            }
        }
        catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objFound;
    }

}
