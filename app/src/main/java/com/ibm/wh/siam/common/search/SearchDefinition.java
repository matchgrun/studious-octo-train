/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class SearchDefinition
implements Serializable
{
    /**
     * Serialized ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Search target.
     */
    private String searchTarget;

    /**
     * Search tag that identifies the type of search to be performed.
     */
    private String searchTag;

    /**
     * Search scope.
     */
    private int searchScope;

    /**
     * Search window.
     */
    private SearchWindow searchWindow;

    /**
     * List of sort options.
     */
    private List<SortOption> listSortOptions;

    /**
     * List of search filters.
     */
    private List<SearchFilterIF> listSearchFilters;

    /**
     * Clear sort options
     */
    public void clearSortOptions() {
        if( listSortOptions != null ) listSortOptions.clear();
    }

    /**
     * Add specified sort option.
     * @param sortOption Option.
     */
    public void add( final SortOption sortOption ) {
        if( sortOption != null ) {
            if( listSortOptions == null ) listSortOptions = new ArrayList<SortOption>();
            listSortOptions.add( sortOption );
        }
    }

    /**
     * Remove specified sort option.
     * @param sortOption Option.
     */
    public void remove( final SortOption sortOption ) {
        if( sortOption != null ) {
            if( listSortOptions != null ) {
                listSortOptions.remove( sortOption );
            }
        }
    }

    /**
     * Clear search filters.
     */
    public void clearSearchFilters() {
        if( listSearchFilters != null ) listSearchFilters.clear();
    }

    /**
     * Add specified search filter.
     * @param searchFilter Search Filter.
     */
    public void add( final SearchFilterIF searchFilter ) {
        if( searchFilter != null ) {
            if( listSearchFilters == null ) listSearchFilters = new ArrayList<SearchFilterIF>();
            listSearchFilters.add( searchFilter );
        }
    }

    /**
     * Remove specified search filter.
     * @param searchFilter Search Filter.
     */
    public void remove( final SearchFilterIF searchFilter ) {
        if( searchFilter != null ) {
            if( listSearchFilters != null ) {
                listSearchFilters.remove( searchFilter );
            }
        }
    }

    /**
     * Format for debug.
     */
    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder( 200 );
       String clsName = this.getClass().getName();
       int pos = clsName.lastIndexOf( "." );
       sb.append( clsName.substring( 1 + pos ) );
       sb.append( ": target/tag/scope/window/sort: { " );
       sb.append( searchTarget ).append( ";" );
       sb.append( searchTag ).append( ";" );
       sb.append( searchScope ).append( "; " );
       sb.append( searchWindow );
       if( listSearchFilters != null ) {
           if( listSearchFilters.size() > 0 ) {
               boolean flag = false;
               for( SearchFilterIF filter : listSearchFilters ) {
                   sb.append( "\n- " ).append( filter );
                   flag = true;
               }
               if( flag ) sb.append( "\n" );
           }
       }
       if( listSortOptions != null ) {
           if( listSortOptions .size() > 0 ) {
               boolean flag = false;
               for( SortOption option : listSortOptions ) {
                   sb.append( "\n- " ).append( option );
                   flag = true;
               }
               if( flag ) sb.append( "\n" );
           }
       }
       sb.append( "}" );

       return sb.toString();
    }

}
