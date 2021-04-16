/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.owner;

/**
 * @author Match Grun
 *
 */
public interface OwnerSqlBuilderIF {
    public static final String ALIAS_ID = "siamowner_id";
    public static final String ALIAS_TYPE = "siamowner_type";
    public static final String ALIAS_CODE = "siamowner_code";
    public static final String ALIAS_NAME  = "siamowner_name";

    public static final String PARM_NAME_OWNER_TYPE = "owner_type";
    public static final String PARM_NAME_OWNER_ID = "owner_id";

    /**
     * Retrieve SQL statement to determine owner details.
     * @param ownerType Owner type.
     * @return  SQL statement.
     */
    public String fetchSqlStatement( final String ownerType );

    /**
     * Retrieve SQL statement to validate owner id for specified owner type details.
     * @param ownerType Owner type.
     * @return  SQL statement.
     */
    public String fetchSqlStatementValidate( final String ownerType );

}
