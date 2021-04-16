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
import com.ibm.wh.siam.core.dao.OrganizationDAO;
import com.ibm.wh.siam.core.dao.impl.mapper.OrganizationRowMapper;
import com.ibm.wh.siam.core.dto.Organization;

@Repository
public class DAOOrganizationImpl
extends BaseSiamDAO
implements OrganizationDAO
{

    private static final String Q_ORGANIZATION = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.ORGANIZATION;
    private static final String COLS_ALL = SiamTableNames.ORGANIZATION + ".*";

    private static final String PRIMARY_KEY = "organization_id";

    // Never use this method
    @SuppressWarnings("unused")
    private static final String SQL_ALL = "select " + COLS_ALL + " from " + Q_ORGANIZATION;

    private static final String SQL_BY_ORG_CODE =
            "select " + COLS_ALL + " from " + Q_ORGANIZATION +
            " where organization_code = ?";

    private static final String SQL_BY_ORG_ID =
            "select " + COLS_ALL + " from " + Q_ORGANIZATION +
            " where organization_id = ?";

    private static final String SQL_BY_ACCOUNT_NUMBER =
            "select " + COLS_ALL + " from " + Q_ORGANIZATION +
            " where account_number = ?";

    private static final String SQL_BY_ACCOUNT_GROUP =
            "select " + COLS_ALL + " from " + Q_ORGANIZATION +
            " where account_group = :accountGroup" +
            " order by organization_name asc";

    private static final String SQL_INSERT =
            "insert into " + Q_ORGANIZATION +
            " (" +
            " organization_code, organization_name, organization_type, description, account_number," +
            " account_group, legacy_entity_id, legacy_entity_short_name, market_segment," +
            " status, created_by, created_by_app" +
            " ) values (" +
            " :orgCode, :orgName, :orgType, :description, :accountNumber," +
            " :accountGroup, :legacyId, :legacyName, :marketSegment," +
            " :status, :createdBy, :createdByApp" +
            " )";

    private static final String SQL_UPDATE =
            "update " + Q_ORGANIZATION + " set " +
            " organization_code = :orgCode," +
            " organization_name = :orgName," +
            " organization_type = :orgType," +
            " description = :description," +
            " account_number = :accountNumber," +
            " account_group = :accountGroup," +
            " legacy_entity_id = :legacyId," +
            " legacy_entity_short_name = :legacyName," +
            " market_segment = :marketSegment," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where organization_id = :organizationId"
            ;

    private static final String SQL_DELETE =
            "delete from " + Q_ORGANIZATION +
            " where organization_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOOrganizationImpl.class );

    NamedParameterJdbcTemplate namedTemplate;
    public DAOOrganizationImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }

    @Override
    public Organization findByCode(final String orgCode) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByCode()");
            logger.info( "orgCode=" + orgCode );
        }
        return findSingleForValue(SQL_BY_ORG_CODE, orgCode);
    }

    @Override
    public Organization findById(final String orgId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "orgId=" + orgId );
        }
        return findSingleForValue(SQL_BY_ORG_ID, orgId);
    }

    @Override
    public Organization findByAccount(String acctNum) {
        return findSingleForValue(SQL_BY_ACCOUNT_NUMBER, acctNum);
    }

    private Organization findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        Organization objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new OrganizationRowMapper());
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
    public Organization insert(
            final RecordUpdaterIF recordUpdater,
            final Organization objToInsert )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insert()");
            logger.info( "objToSave=" + objToInsert );
        }

        Organization objIns = null;

        normalizeObject(objToInsert);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("orgCode", objToInsert.getOrganizationCode() );
        param.addValue("orgName", objToInsert.getOrganizationName());
        param.addValue("orgType", objToInsert.getOrganizationType());
        param.addValue("description", objToInsert.getDescription());
        param.addValue("accountNumber", objToInsert.getAccountNumber());

        param.addValue("accountGroup", objToInsert.getAccountGroup());
        param.addValue("legacyId", objToInsert.getLegacyEntityId());
        param.addValue("legacyName", objToInsert.getLegacyEntityShortName());
        param.addValue("marketSegment", objToInsert.getMarketSegment());

        param.addValue("status", objToInsert.getStatus());
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
                if( logger.isInfoEnabled() ) {
                    logger.info( "objIns=" + objIns );
                }

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
    public Organization updateById(
            final RecordUpdaterIF recordUpdater,
            final Organization objToUpdate)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("updateById()");
            logger.info( "objToUpdate=" + objToUpdate );
        }

        Organization objUpdated = null;

        normalizeObject(objToUpdate);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("organizationId", objToUpdate.getOrganizationId() );
        param.addValue("orgCode", objToUpdate.getOrganizationCode() );
        param.addValue("orgName", objToUpdate.getOrganizationName());
        param.addValue("orgType", objToUpdate.getOrganizationType());
        param.addValue("description", objToUpdate.getDescription());
        param.addValue("accountNumber", objToUpdate.getAccountNumber());

        param.addValue("accountGroup", objToUpdate.getAccountGroup());
        param.addValue("legacyId", objToUpdate.getLegacyEntityId());
        param.addValue("legacyName", objToUpdate.getLegacyEntityShortName());
        param.addValue("marketSegment", objToUpdate.getMarketSegment());

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
                objUpdated = findById(objToUpdate.getOrganizationId() );
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
    public Organization deleteById(
            final RecordUpdaterIF recordUpdater,
            final Organization objToDelete)
    {
        Organization objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "Org=" + objToDelete );
        }

        String objId = objToDelete.getOrganizationId();
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
    public Iterable<Organization> findByAccountGroup( final String acctGroup ) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("accountGroup", acctGroup );
        return namedTemplate.query( SQL_BY_ACCOUNT_GROUP, params, new OrganizationRowMapper() );
    }

}
