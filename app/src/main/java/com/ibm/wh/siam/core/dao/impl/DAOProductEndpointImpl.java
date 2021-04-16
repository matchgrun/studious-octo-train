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
import com.ibm.wh.siam.core.dao.ProductEndpointDAO;
import com.ibm.wh.siam.core.dao.impl.mapper.ProductEndpointRefRowMapper;
import com.ibm.wh.siam.core.dao.impl.mapper.ProductEndpointRowMapper;
import com.ibm.wh.siam.core.dto.ProductEndpoint;
import com.ibm.wh.siam.core.response.product.ProductEndpointRef;
import com.ibm.wh.siam.core.util.rdbms.RdbmsUtil;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOProductEndpointImpl
extends BaseSiamDAO
implements ProductEndpointDAO
{
    private static final String Q_ENDPOINT = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.PRODUCT_ENDPOINT;
    private static final String COLS_ALL = SiamTableNames.PRODUCT_ENDPOINT + ".*";

    private static final String PRIMARY_KEY = "endpoint_id";

    private static final String SQL_BY_ENDPOINT_ID =
            "select " + COLS_ALL + " from " + Q_ENDPOINT +
            " where endpoint_id = ?";

    private static final String SQL_BY_PRODUCT_ID =
            "select " + COLS_ALL + " from " + Q_ENDPOINT +
            " where product_id = :productId";


    private static final String SQL_INSERT =
            "insert into " + Q_ENDPOINT +
            " ( product_id, endpoint_url," +
            " status, created_by, created_by_app " +
            ")" +
            " values (" +
            " :productId, :endpointUrl," +
            " :status, :createdBy, :createdByApp" +
            " )";

    private static final String SQL_UPDATE =
            "update " + Q_ENDPOINT + " set " +
            " endpoint_url = :endpointUrl," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where endpoint_id = :endpointId"
            ;

    private static final String SQL_DELETE =
            "delete from " + Q_ENDPOINT +
            " where endpoint_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOProductEndpointImpl.class );

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;
    public DAOProductEndpointImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }


    @Override
    public ProductEndpoint findById(final String endpointId) {
        logger.info("findById()");
        if( logger.isInfoEnabled() ) {
            logger.info( "endpointId=" + endpointId );
        }
        return findSingleForValue(SQL_BY_ENDPOINT_ID, endpointId);
    }

    private ProductEndpoint findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue(S)");
            logger.info( "identifier=" + identifier );
            logger.info( "sqlFind=" + sqlFind );
        }
        ProductEndpoint objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new ProductEndpointRowMapper());
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
    public ProductEndpoint insert(
            final RecordUpdaterIF recordUpdater,
            final ProductEndpoint objToInsert)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insert()");
            logger.info( "objToInsert=" + objToInsert );
        }

        ProductEndpoint objIns = null;

        normalizeObject(objToInsert);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId", objToInsert.getProductId() );
        param.addValue("endpointUrl", objToInsert.getEndpointUrl());

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
    public ProductEndpoint updateById(
            final RecordUpdaterIF recordUpdater,
            final ProductEndpoint objToUpdate)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("updateById()");
            logger.info( "objToUpdate=" + objToUpdate );
        }

        ProductEndpoint objUpdated = null;

        normalizeObject(objToUpdate);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("endpointId", objToUpdate.getEndpointId() );
        param.addValue("productId", objToUpdate.getProductId() );
        param.addValue("endpointUrl", objToUpdate.getEndpointUrl());

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
    public ProductEndpoint deleteById(
            final RecordUpdaterIF recordUpdater,
            final ProductEndpoint objToDelete )
    {
        ProductEndpoint objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "prod=" + objToDelete );
        }

        String objId = objToDelete.getEndpointId();
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
    public List<ProductEndpoint> findByProduct(final String productId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByProduct()");
            logger.info( "productId=" + productId );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId );
        return namedTemplate.query( SQL_BY_PRODUCT_ID, params, new ProductEndpointRowMapper() );
    }

    /**
     * Build SQL for product join.
     * @return SQL statement.
     */
    private static String buildSqlProductEndpointQuery() {
        StringBuilder sb = new StringBuilder( 1000 );
        sb.append( "select " );
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.PRODUCT_ENDPOINT, "endpoint_id", true );
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.PRODUCT_ENDPOINT, "endpoint_url", true );
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.PRODUCT_ENDPOINT, "product_id", true );
        RdbmsUtil.buildColumnSelection( sb, SiamTableNames.PRODUCT, "product_code" );

//        sb.append( SiamTableNames.PRODUCT_ENDPOINT ).append( "." ).append( "endpoint_id" ).append( ", " );
//        sb.append( SiamTableNames.PRODUCT_ENDPOINT ).append( "." ).append( "endpoint_url" ).append( ", " );
//        sb.append( SiamTableNames.PRODUCT_ENDPOINT ).append( "." ).append( "product_id" ).append( ", " );
//        sb.append( SiamTableNames.PRODUCT ).append( "." ).append( "product_code" );

        sb.append( "\nfrom " );
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.PRODUCT_ENDPOINT ).append( ", " );
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( SiamTableNames.PRODUCT );

        sb.append( "\nwhere " );
        RdbmsUtil.buildJoinMatch(
                sb,
                SiamTableNames.PRODUCT_ENDPOINT,
                "product_id",
                SiamTableNames.PRODUCT,
                "product_id" );
//        sb.append( SiamTableNames.PRODUCT_ENDPOINT ).append( "." ).append( "product_id" );
//        sb.append( " = " );
//        sb.append( SiamTableNames.PRODUCT ).append( "." ).append( "product_id" );
        sb.append( "\nand lower( endpoint_url ) like :endpointUrl" );
        return sb.toString();
    }

    private static final String SQL_PRODUCT_BY_ENDPOINT_URL = buildSqlProductEndpointQuery();

    @Override
    public List<ProductEndpointRef> findEndpointByUrl( final String endpointUrl ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findEndpointByUrl()");
            logger.info( "endpointUrl=" + endpointUrl );
        }

        StringBuilder sb = new StringBuilder( endpointUrl );
        sb.append( "%" );
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("endpointUrl", sb.toString() );
        return namedTemplate.query( SQL_PRODUCT_BY_ENDPOINT_URL, params, new ProductEndpointRefRowMapper() );
    }

}
