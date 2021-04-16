/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wh.siam.busunit.request.search.RequestFilter;
import com.ibm.wh.siam.busunit.request.search.RequestFilterIF;
import com.ibm.wh.siam.busunit.request.search.RequestWindow;
import com.ibm.wh.siam.busunit.request.search.SearchRequest;
import com.ibm.wh.siam.common.search.SearchDefinition;
import com.ibm.wh.siam.common.search.SearchFilterIF;
import com.ibm.wh.siam.common.search.SearchWindow;
import com.ibm.wh.siam.common.search.SortOption;

/**
 * @author Match Grun
 *
 */
public class TestSearchUtil {

    private static final int TEST_REQ_WINDOW_SIZE = 20;
    private static final int TEST_REQ_WINDOW_START = 0;

    private static final String TEST_SEARCH_FIELD = "name";
    private static final String TEST_SEARCH_VALUE = "john";


    private static final Logger logger = LoggerFactory.getLogger( TestSearchUtil.class );

    @Test
    public void testUtil01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil01()");
        }

        SearchUtil util = new SearchUtil();
        int cnt = util.fetchMaxCount();
        if( logger.isDebugEnabled() ) {
            logger.debug( "cnt=" + cnt );
        }

        assertTrue( cnt == SearchUtil.DFL_MAX_SEARCH_COUNT, "Failed to return default max count." );
    }

    @Test
    public void testUtil02() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil02()");
        }

        RequestWindow requestWindow = new RequestWindow();
        requestWindow.setWindowSize( TEST_REQ_WINDOW_SIZE );
        requestWindow.setStartPosition( TEST_REQ_WINDOW_START );

        SearchUtil util = new SearchUtil();
        SearchWindow searchWindow = util.createWindow(requestWindow);

        if( logger.isDebugEnabled() ) {
            logger.debug( "searchWindow=" + searchWindow );
        }

        assertTrue( searchWindow.getWindowSize() == TEST_REQ_WINDOW_SIZE, "Failed to return window size." );
        assertTrue( searchWindow.getStartPosition() == TEST_REQ_WINDOW_START, "Failed to return window start." );
        assertTrue( searchWindow.getTotalSize() == 0, "Failed to return empty total size." );
    }

    @Test
    public void testUtil03() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil03()");
        }

        RequestWindow requestWindow = new RequestWindow();
        requestWindow.setWindowSize( TEST_REQ_WINDOW_SIZE );
        requestWindow.setStartPosition( TEST_REQ_WINDOW_START );

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setRequestWindow(requestWindow);

        SearchUtil util = new SearchUtil();
        SearchWindow searchWindow = util.createWindow(searchRequest);

        if( logger.isDebugEnabled() ) {
            logger.debug( "searchWindow=" + searchWindow );
        }

        assertTrue( searchWindow.getWindowSize() == TEST_REQ_WINDOW_SIZE, "Failed to return window size." );
        assertTrue( searchWindow.getStartPosition() == TEST_REQ_WINDOW_START, "Failed to return window start." );
        assertTrue( searchWindow.getTotalSize() == 0, "Failed to return empty total size." );
    }

    @Test
    public void testUtil04() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil04()");
        }

        SearchRequest searchRequest = new SearchRequest();

        SearchUtil util = new SearchUtil();
        SearchDefinition searchDef = util.createSearchDefinition(searchRequest);

        if( logger.isDebugEnabled() ) {
            logger.debug( "searchDef=" + searchDef );
        }

        SearchWindow searchWindow = searchDef.getSearchWindow();

        assertTrue( searchWindow.getWindowSize() == SearchUtil.DFL_MAX_SEARCH_COUNT, "Failed to return window size." );
        assertTrue( searchWindow.getStartPosition() == TEST_REQ_WINDOW_START, "Failed to return window start." );
        assertTrue( searchWindow.getTotalSize() == 0, "Failed to return empty total size." );

        List<SearchFilterIF> listFilters = searchDef.getListSearchFilters();
        if( logger.isDebugEnabled() ) {
            logger.debug( "listFilters=" + listFilters );
        }
        assertNull( listFilters, "Failed to find NULL filters." );

        List<SortOption> listSortOptions = searchDef.getListSortOptions();
        if( logger.isDebugEnabled() ) {
            logger.debug( "listSortOptions=" + listSortOptions );
        }
        assertNull( listSortOptions, "Failed to find NULL sort options." );

        searchDef.clearSearchFilters();
        listFilters = searchDef.getListSearchFilters();
        assertNull( listFilters, "Failed to find NULL filters." );

        searchDef.clearSortOptions();;
        listSortOptions = searchDef.getListSortOptions();
        assertNull( listSortOptions, "Failed to find NULL sort options." );
    }

    @Test
    public void testUtil05() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil05()");
        }

        SearchRequest searchRequest = new SearchRequest();

        List<RequestFilter> listRequestFilters = new ArrayList<RequestFilter> ();
        {
            RequestFilter reqFilter = new RequestFilter();
            reqFilter.setFieldName( TEST_SEARCH_FIELD );
            reqFilter.setSearchTerm( TEST_SEARCH_VALUE );
            reqFilter.setScope( RequestFilterIF.EXACT_MATCH );
            reqFilter.setIgnoreCase( true );
            listRequestFilters.add(reqFilter);
        }

        searchRequest.setListSearchFilters( listRequestFilters );

        SearchUtil util = new SearchUtil();
        SearchDefinition searchDef = util.createSearchDefinition(searchRequest);

        if( logger.isDebugEnabled() ) {
            logger.debug( "searchDef=" + searchDef );
        }

        List<SearchFilterIF> listFilters = searchDef.getListSearchFilters();
        if( logger.isDebugEnabled() ) {
            logger.debug( "listFilters=" + listFilters );
        }
        assertNotNull( listFilters, "Failed to find filters." );
        assertTrue( listFilters.size() > 0, "Failed to find filters." );

        SearchFilterIF searchFilter = listFilters.get(0);
        if( logger.isDebugEnabled() ) {
            logger.debug( "searchFilter=" + searchFilter );
        }

        assertTrue( searchFilter.getFieldName().equals(TEST_SEARCH_FIELD), "Failed to match 'fieldName'." );
        assertTrue( searchFilter.getSearchTerm().equals(TEST_SEARCH_VALUE), "Failed to match 'searchTerm'." );
        assertTrue( searchFilter.getScope() == SearchFilterIF.EXACT_MATCH, "Failed to match 'scope'." );

    }

}
