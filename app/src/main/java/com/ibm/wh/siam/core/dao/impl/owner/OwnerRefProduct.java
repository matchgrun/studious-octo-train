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
public class OwnerRefProduct
implements OwnerRefIF
{
    private static final String columnName = SiamTableNames.PRODUCT + "." + "description";
    
    @Override
    public String getOwnerType() {
        return SiamOwnerTypes.PRODUCT;
    }

    @Override
    public String getTableName() {
        return SiamTableNames.PRODUCT;
    }

    @Override
    public String getColumnId() {
        return "product_id";
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getColumnCode() {
        return "product_code";
    }

    @Override
    public String getColumnOrder() {
        return "product_code";
    }
}
