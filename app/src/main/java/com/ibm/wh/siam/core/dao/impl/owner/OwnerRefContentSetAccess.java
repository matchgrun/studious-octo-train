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
public class OwnerRefContentSetAccess
implements OwnerRefIF
{
    private static final String columnName = SiamTableNames.CONTENT_SET_ACCESS + "." + "description";

    @Override
    public String getOwnerType() {
        return SiamOwnerTypes.CONTENT_SET_ACCESS;
    }

    @Override
    public String getTableName() {
        return SiamTableNames.CONTENT_SET_ACCESS;
    }

    @Override
    public String getColumnId() {
        return "content_set_access_id";
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getColumnCode() {
        return "content_set_access_id";
    }

    @Override
    public String getColumnOrder() {
        return "content_set_access_id";
    }
}
