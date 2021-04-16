/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.service;

import com.ibm.wh.siam.busunit.request.search.SearchRequest;
import com.ibm.wh.siam.busunit.response.search.SearchResponse;

/**
 * @author Match Grun
 *
 */
public interface BusinessUnitSearchService {
    public SearchResponse search( final SearchRequest request );
}
