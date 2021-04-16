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
public class OwnerRefContentSet
implements OwnerRefIF
{
    private static final String columnName = SiamTableNames.CONTENT_SET + "." + "description";

    @Override
    public String getOwnerType() {
        return SiamOwnerTypes.CONTENT_SET;
    }

    @Override
    public String getTableName() {
        return SiamTableNames.CONTENT_SET;
    }

    @Override
    public String getColumnId() {
        return "content_set_id";
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getColumnCode() {
        return "content_set_descriptor_id";
    }

    @Override
    public String getColumnOrder() {
        return "content_set_descriptor_id";
    }
}
