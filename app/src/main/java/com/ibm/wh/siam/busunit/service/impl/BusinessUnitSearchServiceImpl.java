/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.wh.siam.busunit.dao.BusinessUnitSearchDAO;
import com.ibm.wh.siam.busunit.request.search.SearchRequest;
import com.ibm.wh.siam.busunit.response.search.SearchResponse;
import com.ibm.wh.siam.busunit.search.SearchConstants;
import com.ibm.wh.siam.busunit.service.BusinessUnitSearchService;
import com.ibm.wh.siam.common.search.SearchResult;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.service.impl.BaseSiamService;
import com.ibm.wh.siam.core.util.SearchUtil;

/**
 * @author Match Grun
 *
 */
@Component
public class BusinessUnitSearchServiceImpl
extends BaseSiamService
implements BusinessUnitSearchService
{
    @Resource
    BusinessUnitSearchDAO searchDao;

    @Resource
    private SearchUtil searchUtil;

    private static final Logger logger = LoggerFactory.getLogger( BusinessUnitSearchServiceImpl.class );

    @Override
    public SearchResponse search( final SearchRequest request ) {
        if( logger.isInfoEnabled() ) {
            logger.info("search()");
            logger.info( request.toString() );
        }

        SearchResponse response = new SearchResponse();
        response.setSearchTarget( request.getSearchTarget() );
        response.setSearchId( request.getSearchId() );

        ResponseStatus sts = null;
        try {
            SearchResult searchResult = searchDao.search(request);
            response.setSearchResult(searchResult);
            response.setSearchStatus( SearchConstants.STATUS_SUCCESS );
            response.setSearchMessage( SearchConstants.MSG_SUCCESS );

            sts = statusSuccess();
        }
        catch (Exception e) {
            e.printStackTrace();
            sts = searchFailed( SearchConstants.MSG_SEARCH_FAILED );
            response.setSearchStatus( SearchConstants.STATUS_FAILED );
            response.setSearchMessage( SearchConstants.MSG_DB_FAILED );
        }
        response.setStatus(sts);

        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "response=\n" + response );
        }
        */

        return response;
    }

}
