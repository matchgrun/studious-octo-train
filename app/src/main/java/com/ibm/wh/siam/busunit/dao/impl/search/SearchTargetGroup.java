/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao.impl.search;

import com.ibm.wh.siam.busunit.search.SearchConstants;
import com.ibm.wh.siam.core.dao.impl.SiamTableNames;

/**
 * @author Match Grun
 *
 */
public class SearchTargetGroup
implements SearchTargetIF
{
    @Override
    public String getTargetName() {
        return SearchConstants.TARGET_GROUP;
    }

    @Override
    public String getTableName() {
        return SiamTableNames.GROUP;
    }

    @Override
    public String getColumnItem() {
        return "group_id";
    }

    @Override
    public String getColumnDescription() {
        return "group_name";
    }

    @Override
    public String getColumnStatus() {
        return "status";
    }

    @Override
    public String getColumnOrder() {
        return "group_name";
    }
}

