/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ibm.wh.siam.busunit.dao.BusinessUnitSearchDAO;
import com.ibm.wh.siam.busunit.dao.impl.mapper.IntegerCountRowMapper;
import com.ibm.wh.siam.busunit.dao.impl.mapper.SearchItemRowMapper;
import com.ibm.wh.siam.busunit.dao.impl.search.SearchBuilderIF;
import com.ibm.wh.siam.busunit.request.search.SearchRequest;
import com.ibm.wh.siam.common.search.SearchDefinition;
import com.ibm.wh.siam.common.search.SearchItem;
import com.ibm.wh.siam.common.search.SearchResult;
import com.ibm.wh.siam.common.search.SearchWindow;
import com.ibm.wh.siam.core.util.SearchUtil;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOBusinessUnitSearchImpl
implements BusinessUnitSearchDAO
{
    private static final Logger logger = LoggerFactory.getLogger( DAOBusinessUnitSearchImpl.class );

//    private static final String Q_ORGANIZATION = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.ORGANIZATION;
//    private static final String SQL_COUNT_ALL = "select count(*) from " + Q_ORGANIZATION;

    @Resource
    private SearchUtil searchUtil;

    @Resource
    SearchBuilderIF searchBuilder;

    NamedParameterJdbcTemplate namedTemplate;
    public DAOBusinessUnitSearchImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }

    @Override
    public SearchResult search( final SearchRequest searchRequest ) {
        if( logger.isInfoEnabled() ) {
            logger.info("DAO:search()");
        }

        if( logger.isInfoEnabled() ) {
            logger.info( searchRequest.toString() );
        }

        SearchDefinition searchDefinition = searchUtil.createSearchDefinition(searchRequest);
        if( logger.isInfoEnabled() ) {
            logger.info( searchDefinition.toString() );
        }

        SearchWindow searchWindow = searchDefinition.getSearchWindow();
        String sql = searchBuilder.buildSqlSearch( searchDefinition );

        /*
        if( logger.isInfoEnabled() ) {
            logger.info( sql );
        }
        */

        int iCount = -1;
        SearchItemRowMapper rowMapper = searchBuilder.buildRowMapper(searchDefinition);
        MapSqlParameterSource params = searchBuilder.buildSqlParameterSource(searchDefinition);
        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "params=\n"+ params);
        }
        */

        // Perform the searh
        List<SearchItem> listItems = namedTemplate.query( sql, params, rowMapper );
        if( listItems != null ) {
            iCount = listItems.size();
        }

        // Count number of records that met search criteria.
        int totalCount = -1;
        String sqlCount = searchBuilder.buildSqlCount( searchDefinition );
        List<Integer> listCounts = namedTemplate.query( sqlCount, params, new IntegerCountRowMapper() );
        totalCount = IntegerCountRowMapper.fetchCounts(listCounts);

        SearchWindow resultWindow = new SearchWindow();
        resultWindow.setStartPosition( searchWindow.getStartPosition() );
        resultWindow.setWindowSize( iCount );
        resultWindow.setTotalSize(totalCount);

        SearchResult searchResult = new SearchResult();
        searchResult.setSearchWindow( resultWindow );
        searchResult.setSearchResults( listItems );

        return searchResult;
    }

}
