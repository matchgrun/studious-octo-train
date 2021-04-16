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
import com.ibm.wh.siam.core.dao.AttributeValueDAO;
import com.ibm.wh.siam.core.dao.dto.AttributeNameValueLW;
import com.ibm.wh.siam.core.dao.dto.AttributeNameValueLWRowMapper;
import com.ibm.wh.siam.core.dao.dto.OwnerDescriptorItem;
import com.ibm.wh.siam.core.dao.impl.attribute.OwnerAttributeSqlBuilderIF;
import com.ibm.wh.siam.core.dao.impl.mapper.AttributeNameValueRowMapper;
import com.ibm.wh.siam.core.dao.impl.mapper.AttributeValueRowMapper;
import com.ibm.wh.siam.core.dao.impl.mapper.OwnerDescriptorItemRowMapper;
import com.ibm.wh.siam.core.dto.AttributeNameValue;
import com.ibm.wh.siam.core.dto.AttributeValue;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOAttributeValueImpl
extends BaseSiamDAO
implements AttributeValueDAO
{
    private static final String Q_ATTRIB_VALUE = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.ATTRIBUTE_VALUE;
    private static final String Q_ATTRIB_DESC = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.ATTRIBUTE_DESCRIPTOR;
    private static final String COLS_ALL = SiamTableNames.ATTRIBUTE_VALUE + ".*";

    private static final String PRIMARY_KEY = "attribute_id";

    private static final String SQL_BY_ATTRIBUTE_ID =
            "select " + COLS_ALL + " from " + Q_ATTRIB_VALUE +
            " where attribute_id = ?";

    private static final String JOIN_DESCRIPTOR_ID =
            SiamTableNames.ATTRIBUTE_VALUE + ".attribute_descriptor_id" +
            " = " +
            SiamTableNames.ATTRIBUTE_DESCRIPTOR + ".attribute_descriptor_id";

    private static final String ORDER_ATTRIBS =
            Q_ATTRIB_VALUE + ".attribute_descriptor_id" + ", " + Q_ATTRIB_VALUE + ".attribute_id";

    private static final String JOIN_TABLES =
            Q_ATTRIB_VALUE + "," + Q_ATTRIB_DESC;

    private static final String SQLHW_COLUMNS =
            Q_ATTRIB_DESC + ".attribute_name, " + COLS_ALL;

    private static final String SQL_ALL_FOR_OWNER_ID =
            "select " + SQLHW_COLUMNS +
            " from  " + JOIN_TABLES +
            " where " + JOIN_DESCRIPTOR_ID +
            " and   " + Q_ATTRIB_VALUE + ".owner_id = :ownerId" +
            " order by " + ORDER_ATTRIBS;

    private static final String SQL_FOR_OWNER_BY_NAME =
            "select " + SQLHW_COLUMNS +
            " from  " + JOIN_TABLES +
            " where " + JOIN_DESCRIPTOR_ID +
            " and   " + Q_ATTRIB_VALUE + ".owner_id = :ownerId" +
            " and   " + Q_ATTRIB_DESC + ".attribute_name = :attributeName" +
            " order by " + ORDER_ATTRIBS;

    private static final String SQL_FOR_OWNER_DESCRIPTOR_ID =
            "select " + SQLHW_COLUMNS +
            " from  " + JOIN_TABLES +
            " where " + JOIN_DESCRIPTOR_ID +
            " and   " + Q_ATTRIB_VALUE + ".owner_id = :ownerId" +
            " and   " + Q_ATTRIB_VALUE + ".attribute_descriptor_id = :descriptorId" +
            " order by " + ORDER_ATTRIBS;

    private static final String SQL_INSERT =
            "insert into " + Q_ATTRIB_VALUE +
            " ( attribute_descriptor_id, owner_type, owner_id, attribute_value," +
            " status, created_by, created_by_app " +
            ")" +
            " values (" +
            " :descriptorId, :ownerType, :ownerId, :attribValue," +
            " :status, :createdBy, :createdByApp" +
            " )";

    @SuppressWarnings("unused")
    private static final String SQL_UPDATE =
            "update " + Q_ATTRIB_VALUE + " set " +
            " attribute_value = :attribValue," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where attribute_id = :attributeId"
            ;

    private static final String SQL_DELETE =
            "delete from " + Q_ATTRIB_VALUE +
            " where attribute_id = ?"
            ;

    private static final String SQLLW_COLUMNS =
            Q_ATTRIB_DESC + ".attribute_name, " +
            Q_ATTRIB_VALUE + ".attribute_descriptor_id," +
            Q_ATTRIB_VALUE + ".attribute_id," +
            Q_ATTRIB_VALUE + ".attribute_value";

    private static final String SQLLW_ALL_FOR_OWNER_ID =
            "select " + SQLLW_COLUMNS +
            " from  " + JOIN_TABLES +
            " where " + JOIN_DESCRIPTOR_ID +
            " and   " + Q_ATTRIB_VALUE + ".owner_id = :ownerId" +
            " order by " + ORDER_ATTRIBS;

    private static final String SQLLW_FOR_OWNER_BY_NAME =
            "select " + SQLLW_COLUMNS +
            " from  " + JOIN_TABLES +
            " where " + JOIN_DESCRIPTOR_ID +
            " and   " + Q_ATTRIB_VALUE + ".owner_id = :ownerId" +
            " and   " + Q_ATTRIB_DESC + ".attribute_name = :attributeName" +
            " order by " + ORDER_ATTRIBS;

    private static final String SQLLW_FOR_OWNER_DESCRIPTOR_ID =
            "select " + SQLLW_COLUMNS +
            " from  " + JOIN_TABLES +
            " where " + JOIN_DESCRIPTOR_ID +
            " and   " + Q_ATTRIB_VALUE + ".owner_id = :ownerId" +
            " and   " + Q_ATTRIB_VALUE + ".attribute_descriptor_id = :descriptorId" +
            " order by " + ORDER_ATTRIBS;

    private static final Logger logger = LoggerFactory.getLogger( DAOAttributeValueImpl.class );

    @Resource
    OwnerAttributeSqlBuilderIF ownerAttributeSqlBuilder;

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;
    public DAOAttributeValueImpl( NamedParameterJdbcTemplate template ) {
        this.namedTemplate = template;
    }


    @Override
    public AttributeValue findById( final String attributeValueId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "attributeValueId=" + attributeValueId );
        }
        return findSingleForValue(SQL_BY_ATTRIBUTE_ID, attributeValueId);
    }

    private AttributeValue findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue()");
            logger.info( "identifier=" + identifier );
            logger.info( "sqlFind=" + sqlFind );
        }
        AttributeValue objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new AttributeValueRowMapper());
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
    public List<AttributeNameValue> findByOwner( final String ownerId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOwner()");
            logger.info( "ownerId=" + ownerId );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerId", ownerId );
        return namedTemplate.query(
                SQL_ALL_FOR_OWNER_ID,
                params,
                new AttributeNameValueRowMapper() );
    }

    @Override
    public List<AttributeNameValue> findForOwnerByDescriptorId(
            final String ownerId,
            final String descriptorId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOwner()");
            logger.info( "ownerId=" + ownerId );
            logger.info( "descriptorId=" + descriptorId );
            logger.info( SQL_FOR_OWNER_DESCRIPTOR_ID );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerId", ownerId );
        params.addValue("descriptorId", descriptorId);
        return namedTemplate.query(
                SQL_FOR_OWNER_DESCRIPTOR_ID,
                params,
                new AttributeNameValueRowMapper() );
    }

    @Override
    public List<AttributeNameValue> findForOwnerByDescriptorName(
            final String ownerId,
            final String attributeName)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOwner()");
            logger.info( "ownerId=" + ownerId );
            logger.info( "attributeName=" + attributeName );
            logger.info( SQL_FOR_OWNER_BY_NAME );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerId", ownerId );
        params.addValue("attributeName", attributeName);
        return namedTemplate.query(
                SQL_FOR_OWNER_BY_NAME,
                params,
                new AttributeNameValueRowMapper() );
    }

    @Override
    public AttributeValue insert(
            final RecordUpdaterIF recordUpdater,
            final AttributeValue descriptor)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AttributeValue updateById(
            final RecordUpdaterIF recordUpdater,
            final AttributeValue descriptor)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AttributeValue deleteById(
            final RecordUpdaterIF recordUpdater,
            final AttributeValue descriptor)
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<AttributeNameValueLW> findALWForOwner(
            final String ownerId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findALWForOwner()");
            logger.info( "ownerId=" + ownerId );
            logger.info( SQLLW_ALL_FOR_OWNER_ID );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerId", ownerId );
        return namedTemplate.query(
                SQLLW_ALL_FOR_OWNER_ID,
                params,
                new AttributeNameValueLWRowMapper() );
    }


    @Override
    public List<AttributeNameValueLW> findALWForOwnerByDescriptorId(
            final String ownerId,
            final String descriptorId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findALWForOwnerByDescriptorId()");
            logger.info( "ownerId=" + ownerId );
            logger.info( "descriptorId=" + descriptorId );
            logger.info( SQLLW_FOR_OWNER_DESCRIPTOR_ID );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerId", ownerId );
        params.addValue("descriptorId", descriptorId);
        return namedTemplate.query(
                SQLLW_FOR_OWNER_DESCRIPTOR_ID,
                params,
                new AttributeNameValueLWRowMapper() );
    }


    @Override
    public List<AttributeNameValueLW> findALWForOwnerByDescriptorName(
            final String ownerId,
            final String attributeName )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findALWForOwnerByDescriptorName()");
            logger.info( "ownerId=" + ownerId );
            logger.info( "attributeName=" + attributeName );
            logger.info( SQLLW_FOR_OWNER_BY_NAME );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerId", ownerId );
        params.addValue("attributeName", attributeName);
        return namedTemplate.query(
                SQLLW_FOR_OWNER_BY_NAME,
                params,
                new AttributeNameValueLWRowMapper() );
    }


    @Override
    public AttributeValue insert(
            final RecordUpdaterIF recordUpdater,
            final AttributeNameValue objToInsert )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insert(anv)");
            logger.info( "objToInsert=" + objToInsert );
        }

        AttributeValue objIns = null;

        normalizeObject(objToInsert);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("descriptorId", objToInsert.getAttributeDescriptorId() );
        param.addValue("ownerType", objToInsert.getOwnerType() );
        param.addValue("ownerId", objToInsert.getOwnerId() );
        param.addValue("attribValue", objToInsert.getAttributeValue());

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
    public AttributeNameValue deleteById(
            final RecordUpdaterIF recordUpdater,
            final AttributeNameValue objToDelete)
    {
        AttributeNameValue  objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "prod=" + objToDelete );
        }

        String objId = objToDelete.getAttributeId();
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
    public Iterable<OwnerDescriptorItem> findOwnersWithAttributeName(
            final String ownerType,
            final String descriptorName)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findOwnersWithAttributeName()");
        }

        Iterable<OwnerDescriptorItem> listOwners = null;
        String sql = ownerAttributeSqlBuilder.fetchSqlStatement(ownerType);
        /*
        if( logger.isInfoEnabled() ) {
            logger.info("sql=\n" + sql);
        }
        */

        if( sql != null ) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue( OwnerAttributeSqlBuilderIF.PARAM_NAME_OWNER_TYPE, ownerType );
            params.addValue( OwnerAttributeSqlBuilderIF.PARAM_NAME_DESC_NAME, descriptorName );
            listOwners = namedTemplate.query(
                    sql,
                    params,
                    new OwnerDescriptorItemRowMapper() );
        }
        return listOwners;
    }


}
