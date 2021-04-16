/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.SecurityQuestionAnswerDAO;
import com.ibm.wh.siam.core.dao.impl.mapper.SecurityQuestionAnswerRowMapper;
import com.ibm.wh.siam.core.dao.impl.mapper.SecurityQuestionRowMapper;
import com.ibm.wh.siam.core.dto.SecurityQuestion;
import com.ibm.wh.siam.core.dto.SecurityQuestionAnswer;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOSecurityQuestionAnswerImpl
extends BaseSiamDAO
implements SecurityQuestionAnswerDAO
{
    private static final String Q_SECURITY_ANSWER =
            SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.SECURITY_QUESTION_ANSWER;
    private static final String COLS_ALL = SiamTableNames.SECURITY_QUESTION_ANSWER + ".*";

    private static final String PRIMARY_KEY = "question_id";

    private static final String SQL_BY_QUESTION_ID =
            "select " + COLS_ALL + " from " + Q_SECURITY_ANSWER +
            " where question_id = ?";

    private static final String SQL_BY_CREDENTIAL_ID =
            "select " + COLS_ALL + " from " + Q_SECURITY_ANSWER +
            " where credential_id = :credentialId" +
            " order by display_sequence";

    private static final String SQL_INSERT =
            "insert into " + Q_SECURITY_ANSWER +
            " ( credential_id, display_sequence, question, answer, encryption_code," +
            " status, created_by, created_by_app " +
            ")" +
            " values (" +
            " :credentialId, :sequence, :question, :answer, :encryptionCode," +
            " :status, :createdBy, :createdByApp" +
            " )";

    private static final String SQL_UPDATE =
            "update " + Q_SECURITY_ANSWER + " set " +
            " question = :question," +
            " answer = :answer," +
            " encryption_code = :encryptionCode," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where question_id = :questionId"
            ;

    private static final String SQL_DELETE =
            "delete from " + Q_SECURITY_ANSWER +
            " where question_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOSecurityQuestionAnswerImpl.class );

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;

    // Setup template.
    public DAOSecurityQuestionAnswerImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }


    @Override
    public SecurityQuestionAnswer findById(final String questionId) {
        return findSingleForValue(SQL_BY_QUESTION_ID, questionId);
    }

    private SecurityQuestionAnswer findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue(S)");
            logger.info( "identifier=" + identifier );
            logger.info( "sqlFind=" + sqlFind );
        }
        SecurityQuestionAnswer objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new SecurityQuestionAnswerRowMapper());
            if( logger.isInfoEnabled() ) {
                logger.info( "Found obj=" + objFound );
            }
        }
        catch( EmptyResultDataAccessException e) {
            if( logger.isInfoEnabled() ) {
                logger.info( "SecurityQuestionAnswer NOT-FOUND" );
            }
        }
        catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objFound;
    }

    @Override
    public List<SecurityQuestionAnswer> findByCredential( final String credentialId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByCredential()");
            logger.info( "credentialId=" + credentialId );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("credentialId", credentialId );
        return namedTemplate.query( SQL_BY_CREDENTIAL_ID, params, new SecurityQuestionAnswerRowMapper() );
    }

    @Override
    public SecurityQuestionAnswer insert(
            final RecordUpdaterIF recordUpdater,
            final SecurityQuestionAnswer sqa)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insert()");
            logger.info( "sqa=" + sqa );
        }

        SecurityQuestionAnswer objIns = null;

        normalizeObject(objIns);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("credentialId", sqa.getCredentialId() );
        param.addValue("sequence", sqa.getDisplaySequence());
        param.addValue("question", sqa.getQuestion());
        param.addValue("answer", sqa.getAnswer());
        param.addValue("encryptionCode", sqa.getEncryptionCode());

        param.addValue("status", sqa.getStatus());
        param.addValue("createdBy", recordUpdater.getName() );
        param.addValue("createdByApp", recordUpdater.getApplication() );

        try {
            KeyHolder holder = new GeneratedKeyHolder();
            int rc = namedTemplate.update(SQL_INSERT, param, holder);
            if( logger.isInfoEnabled() ) {
                logger.info( "rc=" + rc );
            }
            if ( rc > 0 ) {
                Map<String, Object> map = holder.getKeys();
                Object objUid = map.get( PRIMARY_KEY );
                if( logger.isInfoEnabled() ) {
                    logger.info( "holder.keyList [" + holder.getKeyList() + "]");
                    logger.info( "holder.keys    [" + map + "]");
                    logger.info( "objUid         [" + objUid + "]");
                    logger.info( "objUid IS-A    [" + objUid.getClass().getName() + "]");
                }
                if( objUid != null ) {
                    if( objUid instanceof String ) {
                        String objId = ( String ) objUid;
                        objIns = findById(objId);
                    }
                }
            }
            if( logger.isInfoEnabled() ) {
                logger.info( "objIns=" + objIns );
            }
        }
        catch (DataAccessException e) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Insert failed: " + e.getMostSpecificCause().getMessage() );
            }
        }
        return objIns;
    }

    @Override
    public SecurityQuestionAnswer updateById(
            final RecordUpdaterIF recordUpdater,
            final SecurityQuestionAnswer objToUpdate)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("updateById()");
            logger.info( "objToUpdate=" + objToUpdate );
        }

        SecurityQuestionAnswer objUpdated = null;

        normalizeObject(objToUpdate);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("questionId", objToUpdate.getQuestionId() );
        param.addValue("question", objToUpdate.getQuestion());
        param.addValue("answer", objToUpdate.getAnswer());
        param.addValue("encryptionCode", objToUpdate.getEncryptionCode());

        param.addValue("status", objToUpdate.getStatus());
        param.addValue("modifiedBy", recordUpdater.getName() );
        param.addValue("modifiedByApp", recordUpdater.getApplication() );

        try {
            KeyHolder holder = new GeneratedKeyHolder();
            int rc = namedTemplate.update(SQL_UPDATE, param, holder);
            if( logger.isInfoEnabled() ) {
                logger.info( "rc=" + rc );
            }
            if( rc > 0 ) {
                objUpdated = findById(objToUpdate.getQuestionId());
            }
        }
        catch (DataAccessException e) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Update failed: " + e.getMostSpecificCause().getMessage() );
            }
        }
        return objUpdated;
    }

    @Override
    public SecurityQuestionAnswer deleteById(
            final RecordUpdaterIF recordUpdater,
            final SecurityQuestionAnswer objToDelete)
    {
        SecurityQuestionAnswer objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "Org=" + objToDelete );
        }

        String objId = objToDelete.getQuestionId();
        JdbcTemplate template = namedTemplate.getJdbcTemplate();

        try {
            int rc = template.update( SQL_DELETE, objId );
            if( rc > 0 ) {
                objDeleted = objToDelete;
            }
        }
        catch( EmptyResultDataAccessException e) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Object NOT-FOUND" );
            }
        }
        catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if( logger.isInfoEnabled() ) {
                logger.info( "Delete failed: " + e.getMostSpecificCause().getMessage() );
            }
        }

        return objDeleted;
    }


    @Override
    public List<SecurityQuestion> findQuestionsByCredential(String credentialId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findQuestionsByCredential()");
            logger.info( "credentialId=" + credentialId );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("credentialId", credentialId );
        return namedTemplate.query( SQL_BY_CREDENTIAL_ID, params, new SecurityQuestionRowMapper() );
    }

}
