/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.util;

import com.ibm.wh.siam.core.dao.dto.AccessorDetail;
import com.ibm.wh.siam.core.dao.dto.OwnerDetail;

/**
 * @author Match Grun
 *
 */
public class AccessorUtil {

    /**
     * Populate specified access detail from owner.
     * @param ownerDetail       Owner detail.
     * @param accessorDetail    Accessor detail.
     */
    public static void populateAccessor(
            final OwnerDetail ownerDetail,
            final AccessorDetail accessorDetail )
    {
        accessorDetail.setAccessorType(ownerDetail.getOwnerType());
        accessorDetail.setAccessorId(ownerDetail.getOwnerId());
        accessorDetail.setAccessorCode(ownerDetail.getOwnerCode());
        accessorDetail.setAccessorName(ownerDetail.getOwnerName());
    }

    /**
     * Create accessor detail from specified owner detail.
     * @param ownerDetail   Owner detail.
     * @return Accessor detail.
     */
    public static AccessorDetail createFromOwner( final OwnerDetail ownerDetail ) {
        AccessorDetail accessorDetail = null;
        if( ownerDetail != null ) {
            accessorDetail = new AccessorDetail();
            populateAccessor(ownerDetail, accessorDetail);
        }
        return accessorDetail;
    }
}
