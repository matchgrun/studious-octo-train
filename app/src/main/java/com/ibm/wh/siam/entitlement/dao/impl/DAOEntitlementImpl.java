/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.dao.impl;

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
import com.ibm.wh.siam.core.dao.impl.BaseSiamDAO;
import com.ibm.wh.siam.core.dao.impl.SiamTableNames;
import com.ibm.wh.siam.core.util.rdbms.RdbmsUtil;
import com.ibm.wh.siam.entitlement.dao.EntitlementDAO;
import com.ibm.wh.siam.entitlement.dao.impl.entitlement.EntitlementOwnerSqlBuilderIF;
import com.ibm.wh.siam.entitlement.dao.impl.mapper.EntitlementProductOwnerRowMapper;
import com.ibm.wh.siam.entitlement.dao.impl.mapper.EntitlementRowMapper;
import com.ibm.wh.siam.entitlement.dto.Entitlement;
import com.ibm.wh.siam.entitlement.dto.EntitlementProductOwner;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOEntitlementImpl
extends BaseSiamDAO
implements EntitlementDAO
{
    private static final String Q_ENTITLEMENT = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.ENTITLEMENT;
    private static final String COLS_ALL = SiamTableNames.ENTITLEMENT + ".*";

    private static final String PRIMARY_KEY = "entitlement_id";

    private static final String SQL_BY_ENTITLEMENT_ID =
            "select " + COLS_ALL + " from " + Q_ENTITLEMENT +
            " where entitlement_id = ?";

    private static final String SQL_BY_OWNER_ID =
            "select " + COLS_ALL + " from " + Q_ENTITLEMENT +
            " where owner_type = :ownerType" +
            " and owner_id = :ownerId";

    private static final String SQL_INSERT =
            "insert into " + Q_ENTITLEMENT +
            " ( product_id, owner_type, owner_id, start_date, end_date," +
            " status, created_by, created_by_app " +
            ")" +
            " values (" +
            " :productId, :ownerType, :ownerId, :startDate, :endDate," +
            " :status, :createdBy, :createdByApp" +
            " )";

    private static final String SQL_UPDATE =
            "update " + Q_ENTITLEMENT + " set " +
            " start_date = :startDate," +
            " end_date = :endDate," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where entitlement_id = :entitlementId"
            ;

    private static final String SQL_DELETE =
            "delete from " + Q_ENTITLEMENT +
            " where entitlement_id = ?"
            ;


    @Resource
    EntitlementOwnerSqlBuilderIF entitlementOwnerSqlBuilder;

    private static final Logger logger = LoggerFactory.getLogger( DAOEntitlementImpl.class );

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;

    public DAOEntitlementImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }

    @Override
    public Entitlement findById( final String entitlementId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "entitlementId=" + entitlementId );
        }

        return findSingleForValue(SQL_BY_ENTITLEMENT_ID, entitlementId);
    }

    private Entitlement findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue()");
            logger.info( "identifier=" + identifier );
        }
        Entitlement objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new EntitlementRowMapper());
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
    public Iterable<Entitlement> findByOrganizationId( final String orgId )
    {
        /*
        if( logger.isInfoEnabled() ) {
            logger.info("findByOrganizationId()");
            logger.info( "orgId=" + orgId );
        }
        */

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerId", orgId );
        params.addValue("ownerType", SiamOwnerTypes.ORGANIZATION );
        return namedTemplate.query( SQL_BY_OWNER_ID, params, new EntitlementRowMapper() );
    }

    @Override
    public List<EntitlementProductOwner> findByProductList( final List<String> listProductCode )
    {
        if( logger.isInfoEnabled() ) {
            logger.info( "findByProductList()" );
            logger.info( "listProductCode=" + listProductCode );
        }

        String sql = entitlementOwnerSqlBuilder.fetchSqlStatementProductList( SiamOwnerTypes.ORGANIZATION );
        if( logger.isInfoEnabled() ) {
            logger.info( "sql=\n" + sql );
        }

        StringBuilder sb = new StringBuilder( 300 );
        sb.append(sql);
        String strList = RdbmsUtil.buildSeparatedIdList(listProductCode);

        sb.append( "and " ).append( SiamTableNames.PRODUCT ).append( ".product_code in ( " ).append( strList ).append(" )" );
        sql = sb.toString();
        if( logger.isInfoEnabled() ) {
            logger.info( "sql=\n" + sql );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerType", SiamOwnerTypes.ORGANIZATION );
        return namedTemplate.query( sql, params, new EntitlementProductOwnerRowMapper() );
    }

    @Override
    public List<Entitlement> findByOwner(
            final String ownerType,
            final String ownerId )
    {
        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "findByOwner()" );
            logger.info( "ownerType=" + ownerType );
        }
        */

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerType", ownerType );
        params.addValue("ownerId", ownerId );
        return namedTemplate.query( SQL_BY_OWNER_ID, params, new EntitlementRowMapper() );
    }

    @Override
    public Entitlement insert(
            final RecordUpdaterIF recordUpdater,
            final Entitlement objToInsert)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insert()");
            logger.info( "objToInsert=" + objToInsert );
        }

        Entitlement objIns = null;

        normalizeObject(objToInsert);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId", objToInsert.getProductId() );
        param.addValue("ownerType", objToInsert.getOwnerType());
        param.addValue("ownerId", objToInsert.getOwnerId());
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

    @Override
    public Entitlement updateById(
            final RecordUpdaterIF recordUpdater,
            final Entitlement objToUpdate)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("updateById()");
            logger.info( "objToUpdate=" + objToUpdate );
        }

        Entitlement objUpdated = null;

        normalizeObject(objToUpdate);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("entitlementId", objToUpdate.getEntitlementId() );
        param.addValue("startDate", toSqlDate( objToUpdate.getStartDate()));
        param.addValue("endDate", toSqlDate( objToUpdate.getEndDate()));

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
                objUpdated = findById(objToUpdate.getProductId());
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
    public Entitlement deleteById(
            final RecordUpdaterIF recordUpdater,
            final Entitlement objToDelete )
    {
        Entitlement objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "objToDelete=" + objToDelete );
        }

        String objId = objToDelete.getEntitlementId();
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

}
