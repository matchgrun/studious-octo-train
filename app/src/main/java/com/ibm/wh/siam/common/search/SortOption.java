/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.search;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Match Grun
 *
 */
@Data
public class SortOption
implements Serializable, SortOptionIF
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Sort order.
     */
    private int sortOrder = ASCENDING;

    /**
     * Sort field.
     */
    private String sortField;

    /**
     * Format for debug.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String clsName = this.getClass().getName();
        int pos = clsName.lastIndexOf( "." );
        sb.append( clsName.substring( 1 + pos ) );
        sb.append( ": order/field: { " );
        sb.append( sortOrder ).append( ";" );
        sb.append( sortField ).append( " }" );
        return sb.toString();
    }

}
