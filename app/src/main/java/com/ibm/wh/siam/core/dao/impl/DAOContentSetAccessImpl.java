/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl;

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
import com.ibm.wh.siam.core.dao.ContentSetAccessDAO;
import com.ibm.wh.siam.core.dao.impl.content.ContentAccessorSqlBuilderIF;
import com.ibm.wh.siam.core.dao.impl.mapper.ContentSetAccessDetailRowMapper;
import com.ibm.wh.siam.core.dao.impl.mapper.ContentSetAccessRowMapper;
import com.ibm.wh.siam.core.dto.ContentSetAccess;
import com.ibm.wh.siam.core.dto.ContentSetAccessDetail;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOContentSetAccessImpl
extends BaseSiamDAO
implements ContentSetAccessDAO
{
    private static final String Q_ATTRIB_CONTENT_SET_ACCESS = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.CONTENT_SET_ACCESS;
    private static final String COLS_ALL = SiamTableNames.CONTENT_SET_ACCESS + ".*";

    private static final String PRIMARY_KEY = "content_set_access_id";

    private static final String SQL_BY_CONTENT_SET_ID=
            "select " + COLS_ALL + " from " + Q_ATTRIB_CONTENT_SET_ACCESS +
            " where content_set_id = :contentSetId" +
            " order by accessor_type, accessor_id";

    private static final String SQL_BY_ID =
            "select " + COLS_ALL + " from " + Q_ATTRIB_CONTENT_SET_ACCESS +
            " where content_set_access_id = ?";

    private static final String SQL_INSERT =
            "insert into " + Q_ATTRIB_CONTENT_SET_ACCESS +
            " ( content_set_id, accessor_type, accessor_id, content_access_id," +
            " start_date, end_date," +
            " status, created_by, created_by_app" +
            ")" +
            " values (" +
            " :contentSetId, :accessorType, :accessorId, :contentAccessId," +
            " :startDate, :endDate," +
            " :status, :createdBy, :createdByApp" +
            " )";

//    private static final String SQL_UPDATE =
//            "update " + Q_ATTRIB_CONTENT_SET_ACCESS + " set " +
//            " content_set_type = :contentSetType," +
//            " description = :description," +
//            " start_date = :startDate," +
//            " end_date = :endDate," +
//            " external_content_id = :externalContentId," +
//            " external_role_name = :externalRoleName," +
//            " status = :status," +
//            " modified_by = :modifiedBy," +
//            " modified_by_app = :modifiedByApp" +
//            " where content_set_id = :contentSetId"
//            ;

    private static final String SQL_DELETE =
            "delete from " + Q_ATTRIB_CONTENT_SET_ACCESS +
            " where content_set_access_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOContentSetAccessImpl.class );

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;
    public DAOContentSetAccessImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }

    @Resource
    ContentAccessorSqlBuilderIF contentAccessorSqlBuilder;

    @Override
    public ContentSetAccess findById( final String contentSetAccessId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "contentSetAccessId=" + contentSetAccessId );
        }
        return findSingleForValue(SQL_BY_ID, contentSetAccessId);
    }
//
//    @Override
//    public ContentSet findByDescriptor( final String descriptorId ) {
//        if( logger.isInfoEnabled() ) {
//            logger.info("findById()");
//            logger.info( "descriptorId=" + descriptorId );
//        }
//        return findSingleForValue(SQL_BY_DESCRIPTOR_ID, descriptorId);
//    }
//
    private ContentSetAccess findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue()");
            logger.info( "identifier=" + identifier );
            logger.info( "sqlFind=" + sqlFind );
        }
        ContentSetAccess objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new ContentSetAccessRowMapper());
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

//    @Override
//    public Iterable<ContentSet> findByOwner( final String ownerId )
//    {
//        if( logger.isInfoEnabled() ) {
//            logger.info("findByOwner()");
//            logger.info( "ownerId=" + ownerId );
//        }
//
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("ownerId", ownerId );
//        return namedTemplate.query(
//                SQL_BY_OWNER_ALL,
//                params,
//                new ContentSetRowMapper() );
//    }
//
//    @Override
    @Override
    public ContentSetAccess insert(
            final RecordUpdaterIF recordUpdater,
            final ContentSetAccess objToInsert)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insert()");
            logger.info( "objToInsert=" + objToInsert );
        }

        ContentSetAccess objIns = null;

        normalizeObject(objToInsert);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("contentSetId", objToInsert.getContentSetId());
        param.addValue("accessorType", objToInsert.getAccessorType() );
        param.addValue("accessorId", objToInsert.getAccessorId());
        param.addValue("contentAccessId", objToInsert.getContentAccessId());
        param.addValue("startDate", toSqlDate( objToInsert.getStartDate()));
        param.addValue("endDate", toSqlDate( objToInsert.getEndDate()));

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

//    @Override
//    public ContentSet updateById(
//            final RecordUpdaterIF recordUpdater,
//            final ContentSet objToUpdate)
//    {
//        if( logger.isInfoEnabled() ) {
//            logger.info("updateById()");
//            logger.info( "objToUpdate=" + objToUpdate );
//        }
//
//        ContentSet objUpdated = null;
//
//        normalizeObject(objToUpdate);
//        MapSqlParameterSource param = new MapSqlParameterSource();
//
//        param.addValue("contentSetType", objToUpdate.getContentSetType());
//        param.addValue("description", objToUpdate.getDescription());
//        param.addValue("startDate", toSqlDate( objToUpdate.getStartDate()));
//        param.addValue("endDate", toSqlDate( objToUpdate.getEndDate()));
//        param.addValue("externalContentId", objToUpdate.getExternalContentId());
//        param.addValue("externalRoleName", objToUpdate.getExternalRoleName());
//
//        param.addValue("status", objToUpdate.getStatus());
//        param.addValue("modifiedBy", recordUpdater.getName() );
//        param.addValue("modifiedByApp", recordUpdater.getApplication() );
//
//        try {
//            KeyHolder holder = new GeneratedKeyHolder();
//            int rc = namedTemplate.update(SQL_UPDATE, param, holder);
//            if( logger.isInfoEnabled() ) {
//                logger.info( "rc=" + rc );
//            }
//            if( rc > 0 ) {
//                objUpdated = findById(objToUpdate.getContentSetId());
//            }
//        }
//        catch (DataAccessException e) {
//            if( logger.isInfoEnabled() ) {
//                logger.info( "Update failed: " + e.getMostSpecificCause().getMessage() );
//            }
//        }
//        return objUpdated;
//    }
//
    @Override
    public ContentSetAccess deleteById(
            final RecordUpdaterIF recordUpdater,
            final ContentSetAccess objToDelete)
    {
        ContentSetAccess objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "objToDelete=" + objToDelete );
        }

        String objId = objToDelete.getContentSetAccessId();
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
    public Iterable<ContentSetAccess> findByContentSetId( final String contentSetId ) {
      if( logger.isInfoEnabled() ) {
          logger.info("findByContentSet()");
          logger.info( "contentSetId=" + contentSetId );
      }

      MapSqlParameterSource params = new MapSqlParameterSource();
      params.addValue("contentSetId", contentSetId );
      return namedTemplate.query(
              SQL_BY_CONTENT_SET_ID,
              params,
              new ContentSetAccessRowMapper() );
    }

    @Override
    public Iterable<ContentSetAccessDetail> findDetailByContentSetId( final String contentSetId ) {
      if( logger.isInfoEnabled() ) {
          logger.info("findByContentSet()");
          logger.info( "contentSetId=" + contentSetId );
      }

      String sql = contentAccessorSqlBuilder.fetchSqlStatement( "contentSetId" );
      /*
      if( logger.isInfoEnabled() ) {
          logger.info( "sql=" + sql );
      }
      */

      MapSqlParameterSource params = new MapSqlParameterSource();
      params.addValue("contentSetId", contentSetId );
      return namedTemplate.query(
              sql,
              params,
              new ContentSetAccessDetailRowMapper() );
    }



}
