/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.search;

/**
 * @author Match Grun
 *
 */
public interface SearchFilterIF {

    /**
     * Search scope - Begins with.
     */
    public static final int BEGINS_WITH = 0;

    /**
     * Search scope - Exact match.
     */
    public static final int EXACT_MATCH = 1;

    /**
     * Search scope - Contains.
     */
    public static final int CONTAINS = 2;

    /**
     * Return search name.
     * @return Name.
     */
    public String getFieldName();

    /**
     * Specify search name.
     * @param value Name.
     */
    public void setFieldName( final String value );

    /**
     * Return search term.
     * @return Value.
     */
    public String getSearchTerm();

    /**
     * Specify search term.
     * @param value Value.
     */
    public void setSearchTerm( final String value );

    /**
     * Return search scope.
     * @return Scope.
     */
    public int getScope();

    /**
     * Specify search scope.
     * @param value Scope.
     */
    public void setScope( final int value );

    /**
     * Check whether to ignore case.
     * @return <i>true</i> to ignore case.
     */
    public boolean getIgnoreCase();

    /**
     * Specify Check whether to ignore case.
     * @param value <i>true</i> to ignore case.
     */
    public void setIgnoreCase( final boolean value );

    /**
     * Check whether string data type.
     * @return <i>true</i> if string.
     */
    public boolean getTypeString();

    /**
     * Specify string data type.
     * @param value <i>true</i> if string.
     */
    public void setTypeString( final boolean value );

}
