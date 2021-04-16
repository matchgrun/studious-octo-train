/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.attribute;

/**
 * @author Match Grun
 *
 */
public interface OwnerAttributeSqlBuilderIF {

    /**
     * Alias names.
     */
    public static final String ALIAS_OWNER_ID = "owner_id";
    public static final String ALIAS_OWNER_TYPE = "owner_type";
    public static final String ALIAS_OWNER_CODE = "owner_code";
    public static final String ALIAS_OWNER_NAME = "owner_name";

    /**
     * Parameter names.
     */
    public static final String PARAM_NAME_OWNER_TYPE = "ownerType";
    public static final String PARAM_NAME_DESC_NAME = "descriptorName";


    /**
     * Retrieve SQL statement for specified owner type.
     * @param ownerType Owner type.
     * @return  SQL statement.
     */
    public String fetchSqlStatement( final String ownerType );

}
