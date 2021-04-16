/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.dao.impl.entitlement;

/**
 * @author Match Grun
 *
 */
public interface EntitlementOwnerSqlBuilderIF {


    /**
     * Retrieve SQL statement to retrieve entitlements for specified owner type.
     * @param ownerType Owner type.
     * @return  SQL statement.
     */
    public String fetchSqlStatement( final String ownerType );

    /**
     * Retrieve SQL statement to retrieve entitlements by product list for specified owner type.
     * @param ownerType Owner type.
     * @return  SQL statement.
     */
    public String fetchSqlStatementProductList( final String ownerType );

}
