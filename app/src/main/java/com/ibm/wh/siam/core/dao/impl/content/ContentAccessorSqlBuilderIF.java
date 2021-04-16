/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.content;

/**
 * @author Match Grun
 *
 */
public interface ContentAccessorSqlBuilderIF {

    public static final String ALIAS_ID = "ca_accessor_id";
    public static final String ALIAS_TYPE = "ca_accessor_type";
    public static final String ALIAS_CODE = "ca_accessor_code";
    public static final String ALIAS_NAME  = "ca_accessor_name";

    /**
     * Return SQL statement that retrieves content access data.
     * @param csParamName   Parameter name for content set ID.
     * @return  SQL statement.
     */
    public String fetchSqlStatement( final String csParamName );
}
