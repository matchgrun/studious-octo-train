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
public class RequestFilter
implements Serializable
{
    /**
     * Serialized ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Search field name.
     */
    protected String fieldName = null;

    /**
     * Search term.
     */
    protected String searchTerm = null;

    /**
     * Search scope.
     */
    protected String scope = null;

    /**
     * Ignore case.
     */
    protected boolean ignoreCase = false;

    /**
     * String data type.
     */
    protected boolean typeString = true;

    /**
     * Alternate constructor.
     * @param name  Filter name.
     * @param searchTerm Value.
     */
    public RequestFilter(
            final String fieldName,
            final String searchTerm )
    {
        this.fieldName = fieldName;
        this.searchTerm = searchTerm;
    }

    /**
     * Format for debug.
     * @return Formatted string.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( 100 );
        String clsName = this.getClass().getName();
        int pos = clsName.lastIndexOf( "." );
        sb.append( clsName.substring( 1 + pos ) );
        sb.append( " { fieldName/scope/term/icase/istr: " );
        sb.append( fieldName ).append( ";" );
        sb.append( scope ).append( ";" );
        sb.append( searchTerm ).append( ";" );
        sb.append( ignoreCase ).append( ";" );
        sb.append( typeString ).append( " }" );
        return sb.toString();
    }

}
