/**
 * 
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.dao.impl.entitlement;

/**
 * @author Match Grun
 *
 */
public interface EntitlementOwnerIF {
    public String getOwnerType();
    public String getTableName();
    public String getColumnId();
    public String getColumnName();
    public String getColumnCode();
}
