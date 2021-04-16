/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.request.search;

import java.io.Serializable;
import java.util.List;

import com.ibm.wh.siam.common.search.SortOption;

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
public class SearchRequest
implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Search target.
     */
    private String searchTarget;

    /**
     * Search identifier.
     */
    private String searchId;

    /**
     * Search window.
     */
    private RequestWindow requestWindow;

    /**
     * List of search filters.
     */
    private List<RequestFilter> listSearchFilters;

    /**
     * List of sort options.
     */
    private List<SortOption> listSortOptions;

}
