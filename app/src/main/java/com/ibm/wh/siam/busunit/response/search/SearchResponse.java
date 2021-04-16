/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.response.search;

import com.ibm.wh.siam.common.search.SearchResult;
import com.ibm.wh.siam.core.response.BaseSiamResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SearchResponse
extends BaseSiamResponse
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    String searchTarget;
    String searchStatus;
    String searchMessage;
    private String searchId;
    SearchResult searchResult;
}
