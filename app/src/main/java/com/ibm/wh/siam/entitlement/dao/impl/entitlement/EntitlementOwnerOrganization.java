/**
 * 
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.dao.impl.entitlement;

import com.ibm.wh.siam.core.common.SiamOwnerTypes;
import com.ibm.wh.siam.core.dao.impl.SiamTableNames;

/**
 * @author Match Grun
 *
 */
public class EntitlementOwnerOrganization 
implements EntitlementOwnerIF
{
    public String getOwnerType() {
        return SiamOwnerTypes.ORGANIZATION;
    }

    public String getTableName() {
        return SiamTableNames.ORGANIZATION;
    }

    public String getColumnId() {
        return "organization_id";
    }

    public String getColumnName() {
        return "organization_name";
    }

    public String getColumnCode() {
        return "organization_code";
    }
    
}
