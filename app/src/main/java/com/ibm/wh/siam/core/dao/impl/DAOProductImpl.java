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

import com.ibm.wh.siam.core.common.CommonConstants;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.ProductDAO;
import com.ibm.wh.siam.core.dao.dto.ProductRange;
import com.ibm.wh.siam.core.dao.impl.extractor.ProductIdExtractor;
import com.ibm.wh.siam.core.dao.impl.mapper.ProductRangeRowMapper;
import com.ibm.wh.siam.core.dao.impl.mapper.ProductRowMapper;
import com.ibm.wh.siam.core.dto.Product;
import com.ibm.wh.siam.core.util.rdbms.RdbmsUtil;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOProductImpl
extends BaseSiamDAO
implements ProductDAO
{
    private static final String Q_PRODUCT = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.PRODUCT;
    private static final String COLS_ALL = SiamTableNames.PRODUCT + ".*";

    private static final String PRIMARY_KEY = "product_id";

    private static final String SQL_ALL = "select " + COLS_ALL + " from " + Q_PRODUCT;

    private static final String SQL_BY_PROD_CODE =
            "select " + COLS_ALL + " from " + Q_PRODUCT +
            " where product_code = ?";

    private static final String SQL_BY_PROD_ID =
            "select " + COLS_ALL + " from " + Q_PRODUCT +
            " where product_id = ?";

    private static final String SQL_PRODUCT_RANGE =
            "select product_id, start_date, end_date from " + Q_PRODUCT +
            " where status = :status";


    private static final String SQL_INSERT =
            "insert into " + Q_PRODUCT +
            " ( product_code, description, start_date, end_date," +
            " is_parent," +
            " status, created_by, created_by_app " +
            ")" +
            " values (" +
            " :productCode, :description, :startDate, :endDate," +
            " 'N'," +
            " :status, :createdBy, :createdByApp" +
            " )";

    private static final String SQL_UPDATE =
            "update " + Q_PRODUCT + " set " +
            " description = :description," +
            " start_date = :startDate," +
            " end_date = :endDate," +
            " status = :status," +
            " modified_by = :modifiedBy," +
            " modified_by_app = :modifiedByApp" +
            " where product_id = :productId"
            ;

    private static final String SQL_DELETE =
            "delete from " + Q_PRODUCT +
            " where product_id = ?"
            ;

    private static final Logger logger = LoggerFactory.getLogger( DAOProductImpl.class );

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;
    public DAOProductImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }


    @Override
    public Iterable<Product> findAll() {
        return namedTemplate.getJdbcTemplate().query( SQL_ALL, new ProductRowMapper() );
    }

    @Override
    public Product findByCode(final String prodCode) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByCode()");
            logger.info( "prodCode=" + prodCode );
        }
        return findSingleForValue(SQL_BY_PROD_CODE, prodCode);
    }

    @Override
    public Product findById(String prodId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "prodId=" + prodId );
        }
        return findSingleForValue(SQL_BY_PROD_ID, prodId);
    }

    private Product findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue()");
            logger.info( "identifier=" + identifier );
            logger.info( "sqlFind=" + sqlFind );
        }
        Product objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject(
                    sqlFind,
                    new Object[] {identifier},
                    new ProductRowMapper());
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
    public Product insert(
            final RecordUpdaterIF recordUpdater,
            final Product objToInsert )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("insert()");
            logger.info( "objToInsert=" + objToInsert );
        }

        Product objIns = null;

        normalizeObject(objToInsert);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productCode", objToInsert.getProductCode() );
        param.addValue("description", objToInsert.getDescription());
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
    public Product updateById(
            final RecordUpdaterIF recordUpdater,
            final Product objToUpdate )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("updateById()");
            logger.info( "objToUpdate=" + objToUpdate );
        }

        Product objUpdated = null;

        normalizeObject(objToUpdate);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId", objToUpdate.getProductId() );
        param.addValue("description", objToUpdate.getDescription());
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
    public Product deleteById(
            final RecordUpdaterIF recordUpdater,
            final Product objToDelete )
    {
        Product objDeleted = null;

        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "objToDelete=" + objToDelete );
        }

        String objId = objToDelete.getProductId();
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
    public Map<String, String> findMapProductIds( final List<String> listIds ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findMapProductIds()");
        }
        String sql = buildSqlListCommand(listIds);
        if( logger.isInfoEnabled() ) {
            logger.info("listIds=\n" + listIds);
            logger.info("sql=\n" + sql);
        }
        Map<String, String> map = new HashMap<String, String>();
        if( ! StringUtils.isEmpty(sql)) {
            map = namedTemplate.query(sql, new ProductIdExtractor());
        }
        return map;
    }

    private String buildSqlListCommand( final List<String> listIds ) {
        String sql =
                "select product_id, product_code from " + Q_PRODUCT +
                " where product_id in ( ";

        StringBuilder sb = new StringBuilder( 500 );
        RdbmsUtil.buildSeparatedIdList(sb, listIds);

        // Drop trailing comma
        int iLen = sb.length();
        if( iLen > 0 ) {
            sb.insert(0, sql);
            sb.append(" ) order by product_id" );
            return sb.toString();
        }
        return null;
    }

    @Override
    public List<ProductRange> findActiveProductRange() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", CommonConstants.STATUS_ACTIVE );
        return namedTemplate.query( SQL_PRODUCT_RANGE, params, new ProductRangeRowMapper() );
    }

}
