/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao;

import com.ibm.wh.siam.busunit.request.search.SearchRequest;
import com.ibm.wh.siam.common.search.SearchResult;

/**
 * @author Match Grun
 *
 */
public interface BusinessUnitSearchDAO {
    public SearchResult search( final SearchRequest request );
}
