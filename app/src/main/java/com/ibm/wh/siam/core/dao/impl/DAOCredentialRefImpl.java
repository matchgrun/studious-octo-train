/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl;

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
import com.ibm.wh.siam.core.dao.CredentialRefDAO;
import com.ibm.wh.siam.core.dao.dto.CredentialRefLW;
import com.ibm.wh.siam.core.dao.dto.CredentialRefLWRowMapper;
import com.ibm.wh.siam.core.dao.impl.mapper.CredentialRefRowMapper;
import com.ibm.wh.siam.core.dto.CredentialRef;
import com.ibm.wh.siam.core.util.rdbms.RdbmsUtil;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOCredentialRefImpl
extends BaseSiamDAO
implements CredentialRefDAO
{

    private static final String Q_CREDENTIAL = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.CREDENTIAL;
    private static final String Q_GROUP_CRED = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.GROUP_MEMBER;
    private static final String COLS_ALL = SiamTableNames.CREDENTIAL + ".*";

    private static final String PRIMARY_KEY = "credential_id";

    private static final String SQL_BY_CREDENTIAL_ID =
            "select " + COLS_ALL +  " from " + Q_CREDENTIAL +
            " where credential_id = ?";

    private static final String SQL_BY_USER_ID =
            "select " + COLS_ALL + " from " + Q_CREDENTIAL +
            " where lower(user_id) = :userId";

    private static final String SQL_BY_LAST_NAME =
            "select " + COLS_ALL + " from " + Q_CREDENTIAL +
            " where lower(last_name) = :lastName";

    private static final String SQL_BY_EMAIL =
            "select " + COLS_ALL + " from " + Q_CREDENTIAL +
            " where lower(email_address) = :emailAddress";

    private static final String SQL_BY_PHONE =
            "select " + COLS_ALL + " from " + Q_CREDENTIAL +
            " where lower(telephone_number) = :telephoneNumber";

    private static final String SQL_BY_GROUP_ID =buildSqlGroupCredentials();

    private static final String SQL_INSERT =
            "insert into " + Q_CREDENTIAL +
            " ( user_id, last_name, first_name, display_name," +
            " email_address, telephone_number, internal_user, ldap_entry_dn, " +
            "status, created_by, created_by_app " +
            ")" +
            " values (" +
            " :userId, :lastName, :firstName, :displayName," +
            " :emailAddress, :telephoneNumber, :internalUser, :ldapEntryDn," +
            " :status, :createdBy, :createdByApp" +
            " )";

    private static final String SQL_UPDATE =
            "update " + Q_CREDENTIAL + " set " +
            " user_id = :userId," +
            " last_name = :lastName," +
            " first_name = :firstName," +
            " display_name = :displayName," +
            " email_address = :emailAddress," +
            " telephone_number = :telephoneNumber," +
            " internal_user = :internalUser," +
            " ldap_entry_dn = :ldapEntryDn" +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where credential_id = :credentialId"
            ;

    private static final String SQL_DELETE =
            "delete from " + Q_CREDENTIAL +
            " where credential_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOCredentialRefImpl.class );

    NamedParameterJdbcTemplate namedTemplate;
    public DAOCredentialRefImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }

    @Override
    public CredentialRef findById( final String credentialId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "credentialId=" + credentialId );
        }
        return findSingleForValue(SQL_BY_CREDENTIAL_ID, credentialId);
    }

    private CredentialRef findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        CredentialRef objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new CredentialRefRowMapper());
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
    public Iterable<CredentialRef> findByUserId( final String userId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByUserId()");
            logger.info( "userId=" + userId );
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId.toLowerCase() );
        return namedTemplate.query( SQL_BY_USER_ID, params, new CredentialRefRowMapper() );
    }

    @Override
    public Iterable<CredentialRef> findByLastName( final String lastName) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByUserId()");
            logger.info( "lastName=" + lastName );
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("lastName", lastName.toLowerCase() );
        return namedTemplate.query( SQL_BY_LAST_NAME, params, new CredentialRefRowMapper() );
    }

    @Override
    public Iterable<CredentialRef> findByEmailAddress( final String emailAddress) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByEmailAddress()");
            logger.info( "emailAddress=" + emailAddress );
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("emailAddress", emailAddress.toLowerCase() );
        return namedTemplate.query( SQL_BY_EMAIL, params, new CredentialRefRowMapper() );
    }

    @Override
    public Iterable<CredentialRef> findByPhoneNumber( final String phoneNumber) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByPhoneNumber()");
            logger.info( "phoneNumber=" + phoneNumber );
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("telephoneNumber", phoneNumber.toLowerCase() );
        return namedTemplate.query( SQL_BY_PHONE, params, new CredentialRefRowMapper() );
    }

    @Override
    public CredentialRef insert(
            final RecordUpdaterIF recordUpdater,
            final CredentialRef objToInsert)
    {
        logger.info("insert()");

        if( logger.isInfoEnabled() ) {
            logger.info( "objToInsert=" + objToInsert );
        }

        CredentialRef objIns = null;

        normalizeObject(objToInsert);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", objToInsert.getUserId() );
        param.addValue("lastName", objToInsert.getLastName());
        param.addValue("firstName", objToInsert.getFirstName());
        param.addValue("displayName", objToInsert.getDisplayName());
        param.addValue("emailAddress", objToInsert.getEmailAddress());
        param.addValue("telephoneNumber", objToInsert.getTelephoneNumber());
        param.addValue("internalUser", objToInsert.isInternalUser());
        param.addValue("ldapEntryDn", objToInsert.getExternalLdapEntryDN());

        param.addValue("status", objToInsert.getStatus());
        param.addValue("createdBy", recordUpdater.getName() );
        param.addValue("createdByApp", recordUpdater.getApplication() );

        try {
            KeyHolder holder = new GeneratedKeyHolder();
            int rc = namedTemplate.update(SQL_INSERT, param, holder);
            if( logger.isInfoEnabled() ) {
                logger.info( "rc=" + rc );
            }
            if( logger.isInfoEnabled() ) {
                logger.info( "holder.keyList [" + holder.getKeyList() + "]");
                Map<String, Object> map = holder.getKeys();
                logger.info( "holder.keys [" + map + "]");
            }

            if ( rc > 0 ) {
                Map<String, Object> map = holder.getKeys();
                Object objUid = map.get( PRIMARY_KEY );
                if( objUid != null ) {
                    if( objUid instanceof String ) {
                        String objId = ( String ) objUid;
                        objIns = findById(objId);
                    }
                }
            }
        }
        catch (DataAccessException e) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Insert failed: " + e.getMostSpecificCause().getMessage() );
            }
        }
        if( logger.isInfoEnabled() ) {
            logger.info( "grpIns=" + objIns );
        }
        return objIns;
    }

    @Override
    public CredentialRef updateById(
            final RecordUpdaterIF recordUpdater,
            final CredentialRef objToSave )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("updateById()");
            logger.info( "grp=" + objToSave );
        }

        CredentialRef objUpdated = null;

        normalizeObject(objToSave);
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("credentialId", objToSave.getCredentialId() );
        param.addValue("userId", objToSave.getUserId() );
        param.addValue("lastName", objToSave.getLastName());
        param.addValue("firstName", objToSave.getFirstName());
        param.addValue("displayName", objToSave.getDisplayName());
        param.addValue("emailAddress", objToSave.getEmailAddress());
        param.addValue("telephoneNumber", objToSave.getTelephoneNumber());
        param.addValue("internalUser", objToSave.isInternalUser());
        param.addValue("ldapEntryDn", objToSave.getExternalLdapEntryDN());

        param.addValue("status", objToSave.getStatus());
        param.addValue("modifiedBy", recordUpdater.getName() );
        param.addValue("modifiedByApp", recordUpdater.getApplication() );

        try {
            KeyHolder holder = new GeneratedKeyHolder();
            int rc = namedTemplate.update(SQL_UPDATE, param, holder);
            if( logger.isInfoEnabled() ) {
                logger.info( "rc=" + rc );
            }
            if( rc > 0 ) {
                objUpdated = findById(objToSave.getCredentialId() );
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
    public CredentialRef deleteById(
            final RecordUpdaterIF recordUpdater,
            final CredentialRef objToDelete)
    {
        CredentialRef objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "objToDelete=" + objToDelete );
        }

        String objId = objToDelete.getCredentialId();
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
    public CredentialRef findByEntryDn(
            final String externalLdapId,
            final String ldapEntryDN)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Build SQL statement for groups.
     * @return SQL.
     */
    private static String buildSqlGroupCredentials() {
        StringBuilder sb = new StringBuilder( 200 );

        sb.append( "select ");
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.CREDENTIAL, "credential_id", true);
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.CREDENTIAL, "user_id", true);
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.CREDENTIAL, "email_address", true);
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.CREDENTIAL, "status", false);

        sb.append( "\nfrom " );
        sb.append( Q_CREDENTIAL ).append( ", " );
        sb.append( Q_GROUP_CRED ).append( "\n" );

        sb.append( "where " );
        RdbmsUtil.buildJoinMatch( sb, SiamTableNames.GROUP_MEMBER, "credential_id", SiamTableNames.CREDENTIAL, "credential_id" );
        sb.append( "\nand " );
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.GROUP_MEMBER, "group_id" );
        sb.append( " = :groupId\n" );

        sb.append( "order by " );
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.CREDENTIAL, "user_id" );

        return sb.toString();
    }


    @Override
    public Iterable<CredentialRefLW> findByGroupId( final String groupId ) {
        /*
        if( logger.isInfoEnabled() ) {
            logger.info("findByGroupId()");
            logger.info( "groupId=" + groupId );
        }
        */
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("groupId", groupId );
        return namedTemplate.query( SQL_BY_GROUP_ID, params, new CredentialRefLWRowMapper() );
    }

}
