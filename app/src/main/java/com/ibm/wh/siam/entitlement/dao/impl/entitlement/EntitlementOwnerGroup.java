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
public class EntitlementOwnerGroup
implements EntitlementOwnerIF
{
    private static final String columnName = SiamTableNames.GROUP + "." + "description";

    @Override
    public String getOwnerType() {
        return SiamOwnerTypes.GROUP;
    }

    @Override
    public String getTableName() {
        return SiamTableNames.GROUP;
    }

    @Override
    public String getColumnId() {
        return "group_id";
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getColumnCode() {
        return "group_code";
    }

}
