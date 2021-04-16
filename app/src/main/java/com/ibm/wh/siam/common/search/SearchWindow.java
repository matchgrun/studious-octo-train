/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.search;

import java.io.Serializable;

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
public class SearchWindow
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
    * Start position for search.
    */
    private int startPosition = 0;

    /**
     * Search window size required (number of objects).
     */
    private int windowSize = 0;

    /**
     * Total size of results.
     */
    private int totalSize = 0;

    /**
     * Copy window from specified window.
     * @param window Window to copy from.
     */
    public void copy( final SearchWindow window ) {
        if( window != null ) {
            this.startPosition = window.startPosition;
            this.windowSize = window.windowSize;
            this.totalSize = window.totalSize;
        }
    }

    /**
     * Format for debug.
     */
    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
       String clsName = this.getClass().getName();
       int pos = clsName.lastIndexOf( "." );
       sb.append( clsName.substring( 1 + pos ) );
       sb.append( ": start/window/total: { " );
       sb.append( startPosition ).append( "/" );
       sb.append( windowSize ).append( "/" );
       sb.append( totalSize ).append( " }" );
       return sb.toString();
    }

}
