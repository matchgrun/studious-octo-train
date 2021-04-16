/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.search;

import java.io.Serializable;
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
public class SearchResult
implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Search window; this contains a search window that describes the window
     * pertaining to the search results.
     */
    private SearchWindow searchWindow;

    /**
     * Search results.
     */
    private List<?> searchResults;

    /**
     * Format for debug.
     */
    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder( 200 );
       String clsName = this.getClass().getName();
       int pos = clsName.lastIndexOf( "." );
       sb.append( clsName.substring( 1 + pos ) );
       sb.append( ": window/results: {" );
       sb.append( "\n- " ).append( searchWindow );
       sb.append( "\nResults: " );

       if( searchResults == null ) {
           sb.append( "{{null}}" );
       }
       else {
           sb.append( "{" );
           searchResults.forEach( item -> {
               sb.append( "\n - " ).append( item );
           });
           sb.append( "\n}" );
       }

       sb.append( "\n}" );
       return sb.toString();
    }
}
