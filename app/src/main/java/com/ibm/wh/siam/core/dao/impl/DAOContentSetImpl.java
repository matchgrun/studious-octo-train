/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.ibm.wh.siam.core.common.SiamOwnerTypes;
import com.ibm.wh.siam.core.dao.ContentSetDAO;
import com.ibm.wh.siam.core.dao.impl.content.ContentDetailSqlBuilderIF;
import com.ibm.wh.siam.core.dao.impl.mapper.ContentSetDetailRowMapper;
import com.ibm.wh.siam.core.dao.impl.mapper.ContentSetRowMapper;
import com.ibm.wh.siam.core.dto.ContentSet;
import com.ibm.wh.siam.core.dto.ContentSetDetail;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOContentSetImpl
extends BaseSiamDAO
implements ContentSetDAO
{
    private static final String Q_ATTRIB_CONTENT_SET = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.CONTENT_SET;
    private static final String COLS_ALL = SiamTableNames.CONTENT_SET + ".*";

    private static final String PRIMARY_KEY = "content_set_id";

    private static final String SQL_ALL = "select " + COLS_ALL + " from " + Q_ATTRIB_CONTENT_SET;

    private static final String SQL_BY_CONTENT_SET_ID =
            "select " + COLS_ALL + " from " + Q_ATTRIB_CONTENT_SET +
            " where content_set_id = ?";

    private static final String SQL_BY_DESCRIPTOR_ID =
            "select " + COLS_ALL + " from " + Q_ATTRIB_CONTENT_SET +
            " where content_set_descriptor_id = :descriptorId";

    private static final String SQL_BY_OWNER_ALL =
            "select " + COLS_ALL + " from " + Q_ATTRIB_CONTENT_SET +
            " where owner_id = :ownerId";

    private static final String SQL_INSERT =
            "insert into " + Q_ATTRIB_CONTENT_SET +
            " ( owner_type, owner_id, content_set_descriptor_id," +
            " content_set_type, description," +
            " start_date, end_date," +
            " status, created_by, created_by_app," +
            " external_content_id, external_role_name" +
            ")" +
            " values (" +
            " :ownerType, :ownerId, :descriptorId," +
            " :contentSetType, :description," +
            " :startDate, :endDate," +
            " :status, :createdBy, :createdByApp," +
            " :externalContentId, :externalRoleName" +
            " )";

    private static final String SQL_UPDATE =
            "update " + Q_ATTRIB_CONTENT_SET + " set " +
            " content_set_type = :contentSetType," +
            " description = :description," +
            " start_date = :startDate," +
            " end_date = :endDate," +
            " external_content_id = :externalContentId," +
            " external_role_name = :externalRoleName," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where content_set_id = :contentSetId"
            ;

    private static final String SQL_DELETE =
            "delete from " + Q_ATTRIB_CONTENT_SET +
            " where content_set_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOContentSetImpl.class );

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;
    public DAOContentSetImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }

    @Resource
    ContentDetailSqlBuilderIF contentDetailSqlBuilder;

    @Override
    public Iterable<ContentSet> findAll() {
        return namedTemplate.getJdbcTemplate().query( SQL_ALL, new ContentSetRowMapper() );
    }

    @Override
    public ContentSet findById( final String contentSetId ) {
        return findSingleForValue(SQL_BY_CONTENT_SET_ID, contentSetId);
    }

    /**
     * Find single content set using specified SQL statement.
     * @param sqlFind       SQL statement to execute.
     * @param identifier    Identifer to find.
     * @return  Single content set, or <i>null</i> if not found.
     */
    private ContentSet findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue()");
            logger.info( "identifier=" + identifier );
            logger.info( "sqlFind=" + sqlFind );
        }
        ContentSet objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new ContentSetRowMapper());
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
    public Iterable<ContentSet> findByDescriptor( final String descriptorId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByDescriptor()");
            logger.info( "descriptorId=" + descriptorId );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("descriptorId", descriptorId );
        return namedTemplate.query(
                SQL_BY_DESCRIPTOR_ID,
                params,
                new ContentSetRowMapper() );
    }

    @Override
    public Iterable<ContentSet> findByOwner( final String ownerId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOwner()");
            logger.info( "ownerId=" + ownerId );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerId", ownerId );
        return namedTemplate.query(
                SQL_BY_OWNER_ALL,
                params,
                new ContentSetRowMapper() );
    }

    @Override
    public ContentSet insert(
            final RecordUpdaterIF recordUpdater,
            final ContentSet objToInsert)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insert()");
            logger.info( "objToInsert=" + objToInsert );
        }

        ContentSet objIns = null;

        normalizeObject(objToInsert);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("ownerType", objToInsert.getOwnerType() );
        param.addValue("ownerId", objToInsert.getOwnerId() );
        param.addValue("descriptorId", objToInsert.getContentSetDescriptorId());
        param.addValue("contentSetType", objToInsert.getContentSetType());
        param.addValue("description", objToInsert.getDescription());
        param.addValue("startDate", toSqlDate( objToInsert.getStartDate()));
        param.addValue("endDate", toSqlDate( objToInsert.getEndDate()));
        param.addValue("externalContentId", objToInsert.getExternalContentId());
        param.addValue("externalRoleName", objToInsert.getExternalRoleName());

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
    public ContentSet updateById(
            final RecordUpdaterIF recordUpdater,
            final ContentSet objToUpdate)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("updateById()");
            logger.info( "objToUpdate=" + objToUpdate );
        }

        ContentSet objUpdated = null;

        normalizeObject(objToUpdate);
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("contentSetType", objToUpdate.getContentSetType());
        param.addValue("description", objToUpdate.getDescription());
        param.addValue("startDate", toSqlDate( objToUpdate.getStartDate()));
        param.addValue("endDate", toSqlDate( objToUpdate.getEndDate()));
        param.addValue("externalContentId", objToUpdate.getExternalContentId());
        param.addValue("externalRoleName", objToUpdate.getExternalRoleName());

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
                objUpdated = findById(objToUpdate.getContentSetId());
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
    public ContentSet deleteById(
            final RecordUpdaterIF recordUpdater,
            final ContentSet objToDelete)
    {
        ContentSet objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "objToDelete=" + objToDelete );
        }

        String objId = objToDelete.getContentSetId();
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
    public ContentSetDetail findContentSetDetailById( final String contentSetId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findContentSetDetailById()");
            logger.info( "contentSetId=" + contentSetId );
        }
        ContentSetDetail contentSetDetail = null;
        String sql = contentDetailSqlBuilder.fetchSqlStatement( "contentSetId" );
        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "sql=\n" + sql );
        }
        */

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("contentSetId", contentSetId );
        List<ContentSetDetail> list = namedTemplate.query(
                sql,
                params,
                new ContentSetDetailRowMapper() );
        if( list != null ) {
            if( list.size() == 1 ) {
                contentSetDetail = list.get(0);
            }
        }

        return contentSetDetail;
    }

    @Override
    public List<ContentSetDetail> findByAccessor( final String accessorId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByAccessor()");
            logger.info( "accessorId=" + accessorId );
        }

        List<ContentSetDetail> list = executeAccessorQuery(accessorId, SiamOwnerTypes.ORGANIZATION);
        if( logger.isInfoEnabled() ) {
            logger.info( "list=\n" + list );
        }

        List<ContentSetDetail> listGroups = executeAccessorQuery(accessorId, SiamOwnerTypes.GROUP);
        if( logger.isInfoEnabled() ) {
            logger.info( "listGroups=\n" + listGroups );
        }

        list.addAll(listGroups);

        return list;
    }

    /**
     * Execute accessor query for specified accessor.
     * @param accessorId    Accessor ID.
     * @param ownerType     Content set owner types to retrieved.
     * @return  List of content set details.
     */
    private List<ContentSetDetail> executeAccessorQuery(
            final String accessorId,
            final String ownerType )
    {
        String sql = contentDetailSqlBuilder.fetchSqlAccessibleContent(ownerType);
        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "sql=\n" + sql );
        }
        */

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue( ContentDetailSqlBuilderIF.PARM_NAME_OWNER_TYPE, ownerType );
        params.addValue( ContentDetailSqlBuilderIF.PARM_NAME_ACCESSOR_ID, accessorId );
        List<ContentSetDetail> list = namedTemplate.query(
                sql,
                params,
                new ContentSetDetailRowMapper() );

        if( logger.isInfoEnabled() ) {
            logger.info( "list=\n" + list );
        }

        return list;
    }
}
