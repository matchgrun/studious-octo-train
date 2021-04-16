/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.response.owner.OwnerDetailResponse;
import com.ibm.wh.siam.core.response.owner.OwnerTypeResponse;

/**
 * @author Match Grun
 *
 */
public interface OwnerService {

    OwnerTypeResponse findOwnerType( final String ownerId );
    OwnerDetailResponse findOwnerDetail( final String ownerId );

}
