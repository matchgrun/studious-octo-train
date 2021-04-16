/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.busunit.request.search.RequestFilter;
import com.ibm.wh.siam.busunit.request.search.RequestFilterIF;
import com.ibm.wh.siam.busunit.request.search.RequestWindow;
import com.ibm.wh.siam.busunit.request.search.SearchRequest;
import com.ibm.wh.siam.common.search.SearchDefinition;
import com.ibm.wh.siam.common.search.SearchFilter;
import com.ibm.wh.siam.common.search.SearchFilterIF;
import com.ibm.wh.siam.common.search.SearchWindow;

/**
 * @author Match Grun
 *
 */
@Component
public class SearchUtil
{

    public static final int DFL_MAX_SEARCH_COUNT = 50;

    private static final Logger logger = LoggerFactory.getLogger( SearchUtil.class );

    @Autowired
    private Environment env;

    private static Map<String,Integer> mapScopes = buildMapScopes();

    /**
     * Build mapping of request filter to search filter.
     * @return  Map.
     */
    private static Map<String,Integer> buildMapScopes() {
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put( RequestFilterIF.BEGINS_WITH.toLowerCase(), SearchFilterIF.BEGINS_WITH );
        map.put( RequestFilterIF.EXACT_MATCH.toLowerCase(), SearchFilterIF.EXACT_MATCH );
        map.put( RequestFilterIF.CONTAINS.toLowerCase(), SearchFilterIF.CONTAINS );
        return map;
    }

    /**
     * Return maximum search count if paged results search.
     * @return  Max count.
     */
    public int fetchMaxCount() {
        int maxCount = 0;

        try {
            String str = env.getProperty("siam.search.max-size");
            maxCount = Integer.parseInt( str );
        }
        catch( Exception e ) {
            maxCount = DFL_MAX_SEARCH_COUNT;
        }
        if( maxCount < 1 ) maxCount = 0;
        return maxCount;
    }

    /**
     * Create new search window from specified request window.
     * @param requestWindow Request window.
     * @return Search window.
     */
    public SearchWindow createWindow( final RequestWindow requestWindow ) {
        SearchWindow searchWindow = new SearchWindow();
        int cnt = requestWindow.getWindowSize();
        int maxCount = fetchMaxCount();
        if( cnt > maxCount ) cnt = maxCount;
        searchWindow.setWindowSize(cnt);

        int iPos = requestWindow.getStartPosition();
        if( iPos < 0 ) iPos = 0;
        searchWindow.setStartPosition(iPos);
        return searchWindow;
    }

    /**
     * Create new search window from specified request window.
     * @param searchRequest Search request.
     * @return Search window.
     */
    public SearchWindow createWindow( final SearchRequest searchRequest ) {
        RequestWindow requestWindow = searchRequest.getRequestWindow();
        if( requestWindow == null ) {
            requestWindow = new RequestWindow();
            int maxCount = fetchMaxCount();
            requestWindow.setStartPosition(0);
            requestWindow.setWindowSize(maxCount);
        }
        SearchWindow searchWindow = createWindow( requestWindow );
        return searchWindow;
    }

    /**
     * Create search criteria from specified search request.
     * @param searchRequest Request.
     * @return  Search criteria.
     */
    public SearchDefinition createSearchDefinition( final SearchRequest searchRequest ) {
        SearchDefinition searchDefinition = new SearchDefinition();
        searchDefinition.setSearchTarget( searchRequest.getSearchTarget() );

        SearchWindow searchWindow = createWindow(searchRequest);
        searchDefinition.setSearchWindow( searchWindow );

        List<RequestFilter> list = searchRequest.getListSearchFilters();
        if( logger.isInfoEnabled() ) {
            logger.info( "list=\n" + list );
        }
        if( list != null ) {
            List<SearchFilterIF> listFilters = new ArrayList<SearchFilterIF>();
            list.forEach( item -> {
                SearchFilter filter = validateFilter( item );
                if( logger.isInfoEnabled() ) {
                    logger.info( "filter=\n" + filter );
                }
                if( filter != null ) {
                    listFilters.add(filter);
                }
            });
            searchDefinition.setListSearchFilters(listFilters);
        }

        return searchDefinition;
    }

    /**
     * Validate request filter.
     * @param filterIn  Filter to be validated.
     * @return  Search filter.
     */
    private SearchFilter validateFilter( final RequestFilter filterIn ) {
        SearchFilter filter = null;
        boolean haveError = false;

        String name = null;
        try {
            name = filterIn.getFieldName().trim();
            if( StringUtils.isEmpty(name)) {
                haveError = true;
            }
        }
        catch (Exception e) {
            return filter;
        }

        int indScope = -999;
        try {
            String scopeIn = filterIn.getScope().trim().toLowerCase();
            if( StringUtils.isEmpty(scopeIn)) {
                haveError = true;
            }
            Integer scopeVal = mapScopes.get(scopeIn);
            indScope = scopeVal.intValue();
        }
        catch (Exception e) {
            return filter;
        }

        String term = null;
        try {
            term = filterIn.getSearchTerm().trim();
            if( StringUtils.isEmpty(term)) {
                haveError = true;
            }
        }
        catch (Exception e) {
            return filter;
        }

        if( ! haveError ) {
            filter = new SearchFilter();
            filter.setFieldName(name);
            filter.setScope(indScope);
            filter.setSearchTerm(term);
        }

        return filter;
    }

}
