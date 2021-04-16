/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.dao;

import java.util.List;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.entitlement.dto.Entitlement;
import com.ibm.wh.siam.entitlement.dto.EntitlementProductOwner;

/**
 * @author Match Grun
 *
 */
public interface EntitlementDAO
{
    public Entitlement findById( final String entitlementId );
    public Iterable<Entitlement> findByOrganizationId( final String orgId );
    public List<EntitlementProductOwner> findByProductList( final List<String> listProductCode );
    public List<Entitlement> findByOwner( final String ownerType, final String ownerId );

    public Entitlement insert( final RecordUpdaterIF recordUpdater, final Entitlement objToUpdate );
    public Entitlement updateById( final RecordUpdaterIF recordUpdater, final Entitlement objToInsert );
    public Entitlement deleteById( final RecordUpdaterIF recordUpdater, final Entitlement objToDelete );

}
