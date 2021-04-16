/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import com.ibm.wh.siam.core.dao.dto.OwnerDetail;

/**
 * @author Match Grun
 *
 */
public interface CoreCommonDAO {
    public String identifyOwnerType( final String ownerId );

    public OwnerDetail fetchOwnerDetail( final String ownerType, final String ownerId );
    public OwnerDetail fetchOwnerDetail( final String ownerId );

    public boolean validateOwner( final String ownerType, final String ownerId );
}
