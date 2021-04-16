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
public class OwnerRefRole
implements OwnerRefIF
{
    private static final String columnName = SiamTableNames.ROLE + "." + "description";

    @Override
    public String getOwnerType() {
        return SiamOwnerTypes.ROLE;
    }

    @Override
    public String getTableName() {
        return SiamTableNames.ROLE;
    }

    @Override
    public String getColumnId() {
        return "role_id";
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getColumnCode() {
        return "role_name";
    }

    @Override
    public String getColumnOrder() {
        return "role_name";
    }
}
