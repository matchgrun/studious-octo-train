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
public class OwnerRefGroup
implements OwnerRefIF
{
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
        return "group_name";
    }

    @Override
    public String getColumnCode() {
        return "group_code";
    }

    @Override
    public String getColumnOrder() {
        return "group_name";
    }
}
