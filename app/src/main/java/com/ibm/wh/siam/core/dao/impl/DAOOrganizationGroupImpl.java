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
import com.ibm.wh.siam.core.dao.OrganizationGroupDAO;
import com.ibm.wh.siam.core.dao.impl.extractor.OrganizationGroupExtractor;
import com.ibm.wh.siam.core.dao.impl.mapper.OrganizationGroupRowMapper;
import com.ibm.wh.siam.core.dto.OrganizationGroup;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOOrganizationGroupImpl
extends BaseSiamDAO
implements OrganizationGroupDAO
{
    private static final String Q_ORG_GROUP = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.ORGANIZATION_GROUP;
    private static final String COLS_ALL = SiamTableNames.ORGANIZATION_GROUP + ".*";

    private static final String PRIMARY_KEY = "organization_group_id";

    private static final String SQL_BY_ORG_GROUP_ID =
            "select " + COLS_ALL + " from " + Q_ORG_GROUP +
            " where organization_group_id = ?";

    private static final String SQL_BY_ORG_ID =
            "select " + COLS_ALL + " from " + Q_ORG_GROUP +
            " where organization_id = ?";

    private static final String SQL_BY_GROUP_ID =
            "select " + COLS_ALL + " from " + Q_ORG_GROUP +
            " where group_id = ?";

    private static final String SQL_BY_ORG_AND_GROUP_ID =
            "select " + COLS_ALL + " from " + Q_ORG_GROUP +
            " where organization_id = :orgId" +
            " and group_id = :groupId";

    private static final String SQL_BY_ORGANIZATION_GROUP_IDS =
            "select organization_group_id, group_id from " + Q_ORG_GROUP +
            " where organization_id = :orgId ";

    private static final String SQL_INSERT =
            "insert into " + Q_ORG_GROUP +
            " (" +
            " organization_id, group_id, " +
            " status, created_by, created_by_app" +
            " ) values (" +
            " :orgId, :groupId, :status, :createdBy, :createdByApp" +
            " )";

    private static final String SQL_DELETE =
            "delete from " + Q_ORG_GROUP +
            " where organization_group_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOOrganizationGroupImpl.class );

    NamedParameterJdbcTemplate namedTemplate;
    public DAOOrganizationGroupImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }


    @Override
    public OrganizationGroup findById(final String orgGroupId) {
        return findSingleForValue(SQL_BY_ORG_GROUP_ID, orgGroupId);
    }

    private OrganizationGroup findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        logger.info("findSingleForValue()");
        if( logger.isInfoEnabled() ) {
            logger.info( "identifier=" + identifier );
        }
        OrganizationGroup objFound = null;
        try {
            objFound =
                namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new OrganizationGroupRowMapper());

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
    public OrganizationGroup findByIds(
            final String orgId,
            final String groupId)
    {
        OrganizationGroup orgGroup = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("orgId", orgId );
        params.addValue("groupId", groupId );
        List<OrganizationGroup> list =
                namedTemplate.query(
                        SQL_BY_ORG_AND_GROUP_ID,
                        params,
                        new OrganizationGroupRowMapper() );

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

    @Override
    public Iterable<OrganizationGroup> findByOrganizationId(final String orgId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("orgId", orgId );
        return namedTemplate.query( SQL_BY_ORG_ID, params, new OrganizationGroupRowMapper() );
    }

    @Override
    public Iterable<OrganizationGroup> findByGroupId(final String groupId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("groupId", groupId );
        return namedTemplate.query( SQL_BY_GROUP_ID, params, new OrganizationGroupRowMapper() );
    }


    @Override
    public OrganizationGroup insert(
            final RecordUpdaterIF recordUpdater,
            final OrganizationGroup orgGroup)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insert()");
            logger.info( "orgGroup=" + orgGroup );
        }

        OrganizationGroup objIns = null;

        normalizeObject(orgGroup);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("orgId", orgGroup.getOrganizationId() );
        param.addValue("groupId", orgGroup.getGroupId() );
        param.addValue("status", orgGroup.getStatus());
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
    public OrganizationGroup deleteById(
            final RecordUpdaterIF recordUpdater,
            final OrganizationGroup orgGroup)
    {
        OrganizationGroup objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "orgGroup=" + orgGroup );
        }

        String orgGroupId = orgGroup.getOrganizationGroupId();
        JdbcTemplate template = namedTemplate.getJdbcTemplate();

        try {
            int rc = template.update( SQL_DELETE, orgGroupId );
            if( rc > 0 ) {
                objDeleted = orgGroup;
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
    public Map<String, String> findMapGroupId( final String orgId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findMapGroupId()");
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("orgId", orgId );
        return namedTemplate.query(
                SQL_BY_ORGANIZATION_GROUP_IDS,
                params,
                new OrganizationGroupExtractor());
    }

    @Override
    public void insertBulk(
            final RecordUpdaterIF recordUpdater,
            final String orgId,
            final List<String> listGroupId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insertBulk()");
        }

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("orgId", orgId );
        param.addValue("status", CommonConstants.STATUS_ACTIVE );
        param.addValue("createdBy", recordUpdater.getName() );
        param.addValue("createdByApp", recordUpdater.getApplication() );

        listGroupId.forEach( groupId -> {
            param.addValue("groupId", groupId );
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
            final String orgId,
            final List<String> listOrgGroupId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteBulk()");
        }

        JdbcTemplate template = namedTemplate.getJdbcTemplate();
        try {
            listOrgGroupId.forEach( orgGroupId -> {
                @SuppressWarnings("unused")
                int rc = template.update( SQL_DELETE, orgGroupId );
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
