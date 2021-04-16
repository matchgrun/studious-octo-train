/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.search;

import java.io.Serializable;

/**
 * @author Match Grun
 *
 */
public class SearchFilter
implements Serializable, SearchFilterIF
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
    protected int scope = EXACT_MATCH;

    /**
     * Ignore case.
     */
    protected boolean ignoreCase = false;

    /**
     * String data type.
     */
    protected boolean typeString = true;

    /**
     * Default constructor.
     */
    public SearchFilter() {
    }

    /**
     * Alternate constructor.
     * @param name  Filter name.
     * @param searchTerm Value.
     */
    public SearchFilter(
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
        if( scope == EXACT_MATCH ) {
            sb.append( "EXACT" );
        }
        else if( scope == BEGINS_WITH ) {
            sb.append( "BEGIN" );
        }
        else if( scope == CONTAINS ) {
            sb.append( "CONTAIN" );
        }
        sb.append( ";" );
        sb.append( searchTerm ).append( ";" );
        sb.append( ignoreCase ).append( ";" );
        sb.append( typeString ).append( " }" );
        return sb.toString();
    }

    /**
     * Return search name.
     * @return Name.
     */
    @Override
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * Specify search name.
     * @param value Name.
     */
    @Override
    public void setFieldName( final String value ) {
        this.fieldName = value;
    }

    /**
     * Return search value.
     * @return Value.
     */
    @Override
    public String getSearchTerm() {
        return this.searchTerm;
    }

    /**
     * Specify search Value.
     * @param value Value.
     */
    @Override
    public void setSearchTerm( final String value ) {
        this.searchTerm = value;
    }

    /**
     * Return search scope.
     * @return Scope.
     */
    @Override
    public int getScope() {
        return this.scope;
    }

    /**
     * Specify search scope.
     * @param value Scope.
     */
    @Override
    public void setScope( final int value ) {
        this.scope = value;
    }

    /**
     * Check whether to ignore case.
     * @return <i>true</i> to ignore case.
     */
    @Override
    public boolean getIgnoreCase() {
        return this.ignoreCase;
    }

    /**
     * Specify whether to ignore case.
     * @param value <i>true</i> to ignore case.
     */
    @Override
    public void setIgnoreCase( final boolean value ) {
        this.ignoreCase = value;
    }

    /**
     * Check whether string data type.
     * @return <i>true</i> if string.
     */
    @Override
    public boolean getTypeString() {
        return this.typeString;
    }

    /**
     * Specify string data type.
     * @param value <i>true</i> if string.
     */
    @Override
    public void setTypeString( final boolean value ) {
        this.typeString = value;
    }

}
