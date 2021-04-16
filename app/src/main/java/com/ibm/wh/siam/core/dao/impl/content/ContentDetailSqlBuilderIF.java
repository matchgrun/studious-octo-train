/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.content;

/**
 * @author Match Grun
 *
 */
public interface ContentDetailSqlBuilderIF {

    public static final String ALIAS_ID = "cs_owner_id";
    public static final String ALIAS_TYPE = "cs_owner_type";
    public static final String ALIAS_CODE = "cs_owner_code";
    public static final String ALIAS_NAME  = "cs_owner_name";

    public static final String PARM_NAME_OWNER_TYPE  = "owner_type";
    public static final String PARM_NAME_ACCESSOR_ID = "content_access_id";

    /**
     * Return SQL statement that retrieves content detail data.
     * @param csParamName   Parameter name for content set ID.
     * @return  SQL statement.
     */
    public String fetchSqlStatement( final String csParamName );

    /**
     * Return SQL statement that retrieves content set data that may be accessed
     * for content set accessors.
     * @param   ownerType Content set owner types to be retrieved.
     * @return  SQL statement.
     */
    public String fetchSqlAccessibleContent( final String ownerType );

}
