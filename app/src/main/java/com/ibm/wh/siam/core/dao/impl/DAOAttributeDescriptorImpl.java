/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl;

import java.util.HashMap;
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
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.AttributeDescriptorDAO;
import com.ibm.wh.siam.core.dao.dto.AttributeMapItem;
import com.ibm.wh.siam.core.dao.impl.extractor.AttributeDescriptorExtractor;
import com.ibm.wh.siam.core.dao.impl.mapper.AttributeDescriptorRowMapper;
import com.ibm.wh.siam.core.dao.impl.mapper.AttributeMapItemRowMapper;
import com.ibm.wh.siam.core.dto.AttributeDescriptor;
import com.ibm.wh.siam.core.util.rdbms.RdbmsUtil;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOAttributeDescriptorImpl
extends BaseSiamDAO
implements AttributeDescriptorDAO
{
    private static final String Q_ATTRIB_DESCRIPT = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.ATTRIBUTE_DESCRIPTOR;
    private static final String COLS_ALL = SiamTableNames.ATTRIBUTE_DESCRIPTOR + ".*";

    private static final String PRIMARY_KEY = "attribute_descriptor_id";

    private static final String SQL_ALL = "select " + COLS_ALL + " from " + Q_ATTRIB_DESCRIPT;

    private static final String SQL_BY_DESCRIPTOR_ID =
            "select " + COLS_ALL + " from " + Q_ATTRIB_DESCRIPT +
            " where attribute_descriptor_id = ?";

    private static final String SQL_BY_ATTRIB_NAME =
            "select " + COLS_ALL + " from " + Q_ATTRIB_DESCRIPT +
            " where attribute_name = ?";

    private static final String SQL_INSERT =
            "insert into " + Q_ATTRIB_DESCRIPT +
            " ( attribute_name, description," +
            " status, created_by, created_by_app " +
            ")" +
            " values (" +
            " :attribName, :description," +
            " :status, :createdBy, :createdByApp" +
            " )";

    private static final String SQL_UPDATE =
            "update " + Q_ATTRIB_DESCRIPT + " set " +
            " description = :description," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where attribute_descriptor_id = :descriptorId"
            ;

    private static final String SQL_DELETE =
            "delete from " + Q_ATTRIB_DESCRIPT +
            " where attribute_descriptor_id = ?"
            ;

    private static final String SQL_ATTRIB_MAP_ALL =
            "select attribute_descriptor_id, attribute_name from " + Q_ATTRIB_DESCRIPT +
            " order by attribute_name";


    private static final Logger logger = LoggerFactory.getLogger( DAOAttributeDescriptorImpl.class );

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;
    public DAOAttributeDescriptorImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }


    @Override
    public Iterable<AttributeDescriptor> findAll() {
        return namedTemplate.getJdbcTemplate().query( SQL_ALL, new AttributeDescriptorRowMapper() );
    }

    @Override
    public AttributeDescriptor findById( final String descriptorId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "descriptorId=" + descriptorId );
        }
        return findSingleForValue(SQL_BY_DESCRIPTOR_ID, descriptorId);
    }

    @Override
    public AttributeDescriptor findByName( final String attribName) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "attribName=" + attribName );
        }
        return findSingleForValue(SQL_BY_ATTRIB_NAME, attribName);
    }

    private AttributeDescriptor findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue()");
            logger.info( "identifier=" + identifier );
            logger.info( "sqlFind=" + sqlFind );
        }
        AttributeDescriptor objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new AttributeDescriptorRowMapper());
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
    public AttributeDescriptor insert(
            final RecordUpdaterIF recordUpdater,
            final AttributeDescriptor objToInsert)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insert()");
            logger.info( "objToInsert=" + objToInsert );
        }

        AttributeDescriptor objIns = null;

        normalizeObject(objToInsert);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("attribName", objToInsert.getAttributeName() );
        param.addValue("description", objToInsert.getDescription());

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
    public AttributeDescriptor updateById(
            final RecordUpdaterIF recordUpdater,
            final AttributeDescriptor objToUpdate)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("updateById()");
            logger.info( "objToUpdate=" + objToUpdate );
        }

        AttributeDescriptor objUpdated = null;

        normalizeObject(objToUpdate);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("descriptorId", objToUpdate.getAttributeDescriptorId() );
        param.addValue("description", objToUpdate.getDescription());

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
                objUpdated = findById(objToUpdate.getAttributeDescriptorId());
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
    public AttributeDescriptor deleteById(
            final RecordUpdaterIF recordUpdater,
            final AttributeDescriptor objToDelete)
    {
        AttributeDescriptor objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "objToDelete=" + objToDelete );
        }

        String objId = objToDelete.getAttributeDescriptorId();
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

    private String buildSqlListCommand( final List<String> listNames ) {
        String sql =
                "select attribute_descriptor_id, attribute_name from " + Q_ATTRIB_DESCRIPT +
                " where attribute_name in ( ";

        StringBuilder sb = new StringBuilder( 500 );
        RdbmsUtil.buildSeparatedIdList(sb, listNames);

        // Drop trailing comma
        int iLen = sb.length();
        if( iLen > 0 ) {
            sb.insert(0, sql);
            sb.append(" ) order by attribute_name" );
            return sb.toString();
        }
        return null;
    }

    @Override
    public Map<String, String> findMapAttributeId( final List<String> listNames ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findMapAttributeId()");
        }
        Map<String, String> map = new HashMap<String, String>();
        String sql = buildSqlListCommand(listNames);
        if( ! StringUtils.isEmpty(sql)) {
            map = namedTemplate.query(sql, new AttributeDescriptorExtractor());
        }
        return map;
    }


    @Override
    public Iterable<AttributeMapItem> findMapAttributeId() {
        if( logger.isInfoEnabled() ) {
            logger.info("findMapAttributeId()");
        }
        return namedTemplate.query( SQL_ATTRIB_MAP_ALL, new AttributeMapItemRowMapper() );
    }


}
