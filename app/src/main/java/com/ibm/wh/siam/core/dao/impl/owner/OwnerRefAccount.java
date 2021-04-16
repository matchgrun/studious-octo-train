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
public class OwnerRefAccount
implements OwnerRefIF
{
    @Override
    public String getOwnerType() {
        return SiamOwnerTypes.ACCOUNT;
    }

    @Override
    public String getTableName() {
        return SiamTableNames.SERVICE_ACCOUNT;
    }

    @Override
    public String getColumnId() {
        return "account_id";
    }

    @Override
    public String getColumnName() {
        return "display_name";
    }

    @Override
    public String getColumnCode() {
        return "user_id";
    }

    @Override
    public String getColumnOrder() {
        return "user_id";
    }
}
