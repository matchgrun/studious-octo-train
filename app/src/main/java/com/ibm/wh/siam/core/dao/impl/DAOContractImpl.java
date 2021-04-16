/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ibm.wh.siam.core.dao.ContractDAO;
import com.ibm.wh.siam.core.dao.impl.mapper.ContractRowMapper;
import com.ibm.wh.siam.core.dto.Contract;

/**
 * @author Match Grun
 *

 */

@Repository
public class DAOContractImpl
extends BaseSiamDAO
implements ContractDAO
{
    private static final String Q_CONTRACT = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.CONTRACT;
    private static final String COLS_ALL = SiamTableNames.CONTRACT + ".*";

    @SuppressWarnings("unused")
    private static final String PRIMARY_KEY = "contract_id";

    private static final String SQL_BY_CONTRACT_ID =
            "select " + COLS_ALL +  " from " + Q_CONTRACT +
            " where contract_id = ?";

    private static final String SQL_BY_CONTRACT_CODE =
            "select " + COLS_ALL +  " from " + Q_CONTRACT +
            " where contract_code = :contractCode";

    private static final String SQL_BY_ORG_ID =
            "select " + COLS_ALL + " from " + Q_CONTRACT +
            " where organization_id = :orgId";

    private static final Logger logger = LoggerFactory.getLogger( DAOContractImpl.class );

    // Setup template
    NamedParameterJdbcTemplate namedTemplate;

    public DAOContractImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }

    @Override
    public Contract findById(String contractID) {
        logger.info("findById()");
        if( logger.isInfoEnabled() ) {
            logger.info( "contractID=" + contractID );
        }
        return findSingleForValue(SQL_BY_CONTRACT_ID, contractID);
    }

    private Contract findSingleForValue(
            final String sqlFind,
            final String identifier )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSingleForValue()");
            logger.info( "identifier=" + identifier );
        }
        Contract objFound = null;
        try {
            objFound = namedTemplate.getJdbcTemplate().queryForObject( sqlFind, new Object[] {identifier}, new ContractRowMapper());
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
    public Iterable<Contract> findByOrganizationId( final String orgId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOrganizationId()");
            logger.info( "orgId=" + orgId );
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("orgId", orgId );
        return namedTemplate.query( SQL_BY_ORG_ID, params, new ContractRowMapper() );
    }

    @Override
    public Iterable<Contract> findByCode( final String contractCode ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOrganizationId()");
            logger.info( "contractCode=" + contractCode );
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("contractCode", contractCode );
        return namedTemplate.query( SQL_BY_CONTRACT_CODE, params, new ContractRowMapper() );
    }
}
