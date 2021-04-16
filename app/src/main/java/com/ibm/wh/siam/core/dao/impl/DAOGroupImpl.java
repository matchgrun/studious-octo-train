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
import com.ibm.wh.siam.core.dao.GroupDAO;
import com.ibm.wh.siam.core.dao.impl.extractor.SingleColumnItemExtractor;
import com.ibm.wh.siam.core.dao.impl.mapper.GroupRowMapper;
import com.ibm.wh.siam.core.dto.Group;
import com.ibm.wh.siam.core.util.rdbms.RdbmsUtil;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOGroupImpl
extends BaseSiamDAO
implements GroupDAO
{

    private static final String Q_GROUP = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.GROUP;
    private static final String Q_ORG_GROUP = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.ORGANIZATION_GROUP;
    private static final String COLS_ALL = SiamTableNames.GROUP + ".*";

    private static final String PRIMARY_KEY = "group_id";

    private static final String SQL_BY_GROUP_CODE =
            "select " + COLS_ALL + " from " + Q_GROUP +
            " where group_code = ?";

    private static final String SQL_BY_GROUP_ID =
            "select " + COLS_ALL +  " from " + Q_GROUP +
            " where group_id = ?";

    private static final String SQL_BY_ORGANIZATION_ID =
            "select " + COLS_ALL +
            " from " + Q_GROUP + ", " + Q_ORG_GROUP +
            " where organization_group.group_id = group_siam.group_id" +
            " and organization_group.organization_id = :orgId ";

    private static final String SQL_BY_ACCOUNT_GROUP =
            "select " + COLS_ALL + " from " + Q_GROUP +
            " where account_number = :accountGroup";

    private static final String SQL_INSERT =
            "insert into " + Q_GROUP +
            " ( group_code, group_name, group_type, description, account_group, legacy_group_id, legacy_group_name," +
            "status, created_by, created_by_app " +
            ")" +
            " values (" +
            " :groupCode, :groupName, :groupType, :description, :accountGroup, :legacyId, :legacyName," +
            " :status, :createdBy, :createdByApp" +
            " )";

    private static final String SQL_UPDATE =
            "update " + Q_GROUP + " set " +
            " group_code = :groupCode," +
            " group_name = :groupName," +
            " group_type = :groupType," +
            " description = :description," +
            " account_group = :accountGroup," +
            " legacy_group_id = :legacyId," +
            " legacy_group_name = :legacyName," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where group_id = :groupId"
            ;

    private static final String SQL_DELETE =
            "delete from " + Q_GROUP +
            " where group_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOGroupImpl.class );

    NamedParameterJdbcTemplate namedTemplate;
    public DAOGroupImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }


    @Override
    public Group findByCode(String grpCode) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByCode()");
            logger.info( "grpCode=" + grpCode );
        }
        return findSingleForValue(SQL_BY_GROUP_CODE, grpCode);
    }

    @Override
    public Group findById(String grpId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "grpId=" + grpId );
        }
        return findSingleForValue(SQL_BY_GROUP_ID, grpId);
    }

    private Group findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        Group objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject( sqlFind, new Object[] {identifier}, new GroupRowMapper());
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
    public Iterable<Group> findByOrganizationId( final String orgId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOrganizationId()");
            logger.info( "orgId=" + orgId );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("orgId", orgId );
        return namedTemplate.query( SQL_BY_ORGANIZATION_ID, params, new GroupRowMapper() );
    }


    @Override
    public Group insert(
            final RecordUpdaterIF recordUpdater,
            final Group objToInsert )
    {
        logger.info("insert()");

        if( logger.isInfoEnabled() ) {
            logger.info( "objToInsert=" + objToInsert );
        }

        Group objIns = null;

        normalizeObject(objToInsert);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("groupCode", objToInsert.getGroupCode() );
        param.addValue("groupName", objToInsert.getGroupName());
        param.addValue("groupType", objToInsert.getGroupType());
        param.addValue("description", objToInsert.getDescription());
        param.addValue("accountGroup", objToInsert.getAccountGroup());
        param.addValue("legacyId", objToInsert.getLegacyGroupId());
        param.addValue("legacyName", objToInsert.getLegacyGroupName());

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
    public Group updateById(
            final RecordUpdaterIF recordUpdater,
            final Group objToSave)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("updateById()");
            logger.info( "grp=" + objToSave );
        }

        Group objUpdated = null;

        normalizeObject(objToSave);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("groupId", objToSave.getGroupId() );
        param.addValue("groupCode", objToSave.getGroupCode() );
        param.addValue("groupName", objToSave.getGroupName());
        param.addValue("groupType", objToSave.getGroupType());
        param.addValue("description", objToSave.getDescription());
        param.addValue("accountGroup", objToSave.getAccountGroup());
        param.addValue("legacyId", objToSave.getLegacyGroupId());
        param.addValue("legacyName", objToSave.getLegacyGroupName());

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
                objUpdated = findById(objToSave.getGroupId() );
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
    public Group deleteById(
            final RecordUpdaterIF recordUpdater,
            final Group objToDelete)
    {
        Group objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "objToDelete=" + objToDelete );
        }

        String objId = objToDelete.getGroupId();
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
    public Iterable<Group> findByAccountGroup( final String acctGroup ) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("accountGroup", acctGroup );
        return namedTemplate.query( SQL_BY_ACCOUNT_GROUP, params, new GroupRowMapper() );
    }

    @Override
    public List<String> verifyGroupIds( final List<String> listGroupId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("verifyGroupIds()");
        }

        // Build SQL statement
        String nameItem = "group_id";
        String sql = buildSqlStatementVerifyIds(listGroupId, nameItem);
        SingleColumnItemExtractor extractor = new SingleColumnItemExtractor();
        extractor.setNameItem(nameItem);
        return namedTemplate.query( sql, extractor);
    }

    /**
     * Build SQL statement to verify list of group ID's.
     * @param listId    List of ID's to verify.
     * @param nameItem  Name of column of interest.
     * @return SQL statement.
     */
    private String buildSqlStatementVerifyIds(
            final List<String> listGroupId,
            final String nameItem )
    {
        int len = listGroupId.size() * 24;
        StringBuilder sb = new StringBuilder(len);
        sb.append( "select " ).append( nameItem ).append( " from " );
        sb.append( SiamTableNames.SIAM_DB_NAME ).append(".").append( SiamTableNames.GROUP );
        sb.append( " where " ).append( nameItem ).append( " in ( " );
        sb.append( RdbmsUtil.buildSeparatedIdList(listGroupId) );
        sb.append( " )" );
        return sb.toString();
    }


}
