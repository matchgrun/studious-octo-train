/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.request.search;

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
public class RequestWindow
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
     * Format for debug.
     */
    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append( "SearchWindow: start/window: { " );
       sb.append( startPosition ).append( "/" );
       sb.append( windowSize ).append( "/" );
       return sb.toString();
    }

}
