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

import com.ibm.wh.siam.core.common.CommonConstants;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.GroupCredentialDAO;
import com.ibm.wh.siam.core.dao.impl.mapper.GroupCredentialRowMapper;
import com.ibm.wh.siam.core.dto.GroupCredential;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOGroupCredentialImpl
extends BaseSiamDAO
implements GroupCredentialDAO
{
    private static final String Q_GROUP_CRED = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.GROUP_MEMBER;
    private static final String COLS_ALL = SiamTableNames.GROUP_MEMBER + ".*";

    private static final String PRIMARY_KEY = "member_id";

    private static final String SQL_BY_GROUP_CRED_ID =
            "select " + COLS_ALL + " from " + Q_GROUP_CRED +
            " where member_id = ?";

    @SuppressWarnings("unused")
    private static final String SQL_BY_GROUP_ID =
            "select " + COLS_ALL + " from " + Q_GROUP_CRED +
            " where group_id = ?";

    @SuppressWarnings("unused")
    private static final String SQL_BY_CRED_ID =
            "select " + COLS_ALL + " from " + Q_GROUP_CRED +
            " where credential_id = ?";

    private static final String SQL_BY_GROUP_AND_CRED_ID =
            "select " + COLS_ALL + " from " + Q_GROUP_CRED +
            " where group_id = :groupId" +
            " and credential_id = :credentialId";

    @SuppressWarnings("unused")
    // This could potentially cause thousands of values to be returned.
    private static final String SQL_BY_GROUP_CRED_IDS =
            "select member_id, group_id from " + Q_GROUP_CRED +
            " where group_id = :groupId ";

    private static final String SQL_INSERT =
            "insert into " + Q_GROUP_CRED +
            " (" +
            " group_id, credential_id" +
            " status, created_by, created_by_app" +
            " ) values (" +
            " :groupId, :credentialId, :status, :createdBy, :createdByApp" +
            " )";

    private static final String SQL_DELETE =
            "delete from " + Q_GROUP_CRED +
            " where member_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOGroupCredentialImpl.class );

    NamedParameterJdbcTemplate namedTemplate;
    public DAOGroupCredentialImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }



    @Override
    public GroupCredential findById( final String memberId) {
        return findSingleForValue(SQL_BY_GROUP_CRED_ID, memberId);
    }

    @Override
    public GroupCredential findByIds( final String groupId, final String credentialId) {
        GroupCredential orgGroup = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("groupId", groupId );
        params.addValue("credentialId", credentialId );
        List<GroupCredential> list =
                namedTemplate.query(
                        SQL_BY_GROUP_AND_CRED_ID,
                        params,
                        new GroupCredentialRowMapper() );

        if( list != null ) {
            if( list.size() == 1 ) {
                orgGroup = list.get(0);
            }
            else if( list.size() > 1 ) {
                if( logger.isInfoEnabled() ) {
                    logger.info( "Multiple OrganizationGroups Found." );
                }
            }
        }
        return orgGroup;
    }


    /**
     * Retrieve single object with specified SQL statement.
     * @param sqlFind       SQL statement.
     * @param identifier    Identifier.
     * @return Object.
     */
    private GroupCredential findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        logger.info("findSingleForValue()");
        if( logger.isInfoEnabled() ) {
            logger.info( "identifier=" + identifier );
        }
        GroupCredential objFound = null;
        try {
            objFound =
                namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new GroupCredentialRowMapper());

            if( logger.isInfoEnabled() ) {
                logger.info( "Found obj=" + objFound );
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
        }
        return objFound;
    }


    @Override
    public GroupCredential insert(
            final RecordUpdaterIF recordUpdater,
            final GroupCredential groupCred)
    {

        if( logger.isInfoEnabled() ) {
            logger.info("insert()");
            logger.info( "groupCred=" + groupCred );
        }

        GroupCredential objIns = null;

        normalizeObject(groupCred);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("groupId", groupCred.getGroupId() );
        param.addValue("credentialId", groupCred.getMemberId() );
        param.addValue("status", groupCred.getStatus());
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
    public GroupCredential deleteById(
            final RecordUpdaterIF recordUpdater,
            final GroupCredential groupCred )
    {
        GroupCredential objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "groupCred=" + groupCred );
        }

        String memberId = groupCred.getMemberId();
        JdbcTemplate template = namedTemplate.getJdbcTemplate();

        try {
            int rc = template.update( SQL_DELETE, memberId );
            if( rc > 0 ) {
                objDeleted = groupCred;
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
    public void insertBulk(
            final RecordUpdaterIF recordUpdater,
            final String groupId,
            final List<String> listCredentialId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insertBulk()");
        }

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("groupId", groupId );
        param.addValue("status", CommonConstants.STATUS_ACTIVE );
        param.addValue("createdBy", recordUpdater.getName() );
        param.addValue("createdByApp", recordUpdater.getApplication() );

        listCredentialId.forEach( credentialId -> {
            param.addValue("credentialId", credentialId );
            KeyHolder holder = new GeneratedKeyHolder();
            @SuppressWarnings("unused")
            int rc = namedTemplate.update(SQL_INSERT, param, holder);
            /*
            if( logger.isInfoEnabled() ) {
                logger.info( "rc=" + rc );
            }
            */
        });
    }

    @Override
    public void deleteBulk(
            final RecordUpdaterIF recordUpdater,
            final String groupId,
            final List<String> listOrgCredentialId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteBulk()");
        }

        JdbcTemplate template = namedTemplate.getJdbcTemplate();
        try {
            listOrgCredentialId.forEach( orgCredentialId -> {
                @SuppressWarnings("unused")
                int rc = template.update( SQL_DELETE, orgCredentialId );
                /*
                if( logger.isInfoEnabled() ) {
                    logger.info( "rc=" + rc );
                }
                */
            });
        }
        catch( EmptyResultDataAccessException e) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Object NOT-FOUND" );
            }
        }
        catch (DataAccessException e) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Delete failed: " + e.getMostSpecificCause().getMessage() );
            }
        }
    }



}
