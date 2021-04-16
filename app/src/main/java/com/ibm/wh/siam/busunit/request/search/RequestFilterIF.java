/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.request.search;

/**
 * @author Match Grun
 *
 */
public interface RequestFilterIF {

    /**
     * Search scope - Begins with.
     */
    public static final String BEGINS_WITH = "Begins-With";

    /**
     * Search scope - Exact match.
     */
    public static final String EXACT_MATCH = "Exact-Match";

    /**
     * Search scope - Contains.
     */
    public static final String CONTAINS = "Contains";

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
    public String getScope();

    /**
     * Specify search scope.
     * @param value Scope.
     */
    public void setScope( final String value );

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
