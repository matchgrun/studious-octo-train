/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.response.EntitlementListResponse;
import com.ibm.wh.siam.core.response.EntitlementResponse;
import com.ibm.wh.siam.entitlement.request.EntitlementAddRequest;
import com.ibm.wh.siam.entitlement.request.EntitlementCurrentRequest;
import com.ibm.wh.siam.entitlement.request.EntitlementModifyRequest;
import com.ibm.wh.siam.entitlement.request.EntitlementSaveRequest;
import com.ibm.wh.siam.entitlement.response.EntitlementAddResponse;
import com.ibm.wh.siam.entitlement.response.EntitlementCurrentResponse;
import com.ibm.wh.siam.entitlement.response.EntitlementModifyResponse;
import com.ibm.wh.siam.entitlement.response.EntitlementSaveResponse;

/**
 * @author Match Grun
 *
 */
public interface EntitlementService
{
    public EntitlementResponse findById( final String entitlementId );
    public EntitlementListResponse findByOrganizationId( final String orgId );
    public EntitlementListResponse findByOwner( final String ownerType, final String ownerId );

    public EntitlementSaveResponse save( final RecordUpdaterIF recordUpdater, final EntitlementSaveRequest req );
    public EntitlementAddResponse addEntitlements( final RecordUpdaterIF recordUpdater, final EntitlementAddRequest req );
    public EntitlementModifyResponse modifyEntitlements( final RecordUpdaterIF recordUpdater, final EntitlementModifyRequest req );
    public EntitlementResponse deleteById( final RecordUpdaterIF recordUpdater, final String entitlementId );

    public EntitlementCurrentResponse fetchCurrentEntitlements( final EntitlementCurrentRequest req );
}
