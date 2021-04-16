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
public class SearchTargetOrganization
implements SearchTargetIF
{
    public String getTargetName() {
        return SearchConstants.TARGET_ORGANIZATION;
    }

    public String getTableName() {
        return SiamTableNames.ORGANIZATION;
    }

    public String getColumnItem() {
        return "organization_id";
    }

    public String getColumnDescription() {
        return "organization_name";
    }

    public String getColumnStatus() {
        return "status";
    }

    public String getColumnOrder() {
        return "organization_name";
    }
}

