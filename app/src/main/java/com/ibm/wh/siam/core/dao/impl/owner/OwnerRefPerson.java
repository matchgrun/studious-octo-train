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
public class OwnerRefPerson
implements OwnerRefIF
{
    @Override
    public String getOwnerType() {
        return SiamOwnerTypes.PERSON;
    }

    @Override
    public String getTableName() {
        return SiamTableNames.PERSON;
    }

    @Override
    public String getColumnId() {
        return "person_id";
    }

    @Override
    public String getColumnName() {
        return "display_name";
    }

    @Override
    public String getColumnCode() {
        return "person_id";
    }

    @Override
    public String getColumnOrder() {
        return "display_name";
    }
}
