/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.owner;

import com.ibm.wh.siam.core.common.SiamOwnerTypes;
import com.ibm.wh.siam.core.dao.impl.SiamTableNames;

/**
 * @author Match Grun
 *
 */
public class OwnerRefOrganization
implements OwnerRefIF
{
    @Override
    public String getOwnerType() {
        return SiamOwnerTypes.ORGANIZATION;
    }

    @Override
    public String getTableName() {
        return SiamTableNames.ORGANIZATION;
    }

    @Override
    public String getColumnId() {
        return "organization_id";
    }

    @Override
    public String getColumnName() {
        return "organization_name";
    }

    @Override
    public String getColumnCode() {
        return "organization_code";
    }

    @Override
    public String getColumnOrder() {
        return "organization_name";
    }
}
