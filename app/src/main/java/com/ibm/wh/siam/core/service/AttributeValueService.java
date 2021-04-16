/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.request.attribute.AttributeValueSaveRequest;
import com.ibm.wh.siam.core.response.attribute.AttributeValueSaveResponse;
import com.ibm.wh.siam.core.response.attribute.OwnerAttributeValueListResponse;
import com.ibm.wh.siam.core.response.attribute.OwnerAttributeValueResponse;
import com.ibm.wh.siam.core.response.attribute.OwnersWithAttributeNameResponse;

/**
 * @author Match Grun
 *
 */
public interface AttributeValueService {

    public final String SELECTOR_ALL = "all";
    public final String SELECTOR_DESCRIPTOR_ID = "descriptorId";
    public final String SELECTOR_DESCRIPTOR_NAME = "attributeName";

    OwnerAttributeValueResponse findForOwnerId( final String ownerId );
    OwnerAttributeValueResponse findForOwnerByDescriptorName( final String owner, final String descriptorName );
    OwnerAttributeValueResponse findForOwnerByDescriptorId( final String ownerId, final String descriptorId );

    OwnerAttributeValueListResponse findAttributeListForOwner( final String ownerId );
    OwnerAttributeValueListResponse findAttributeListForOwnerByName( final String ownerId, final String descriptorName );
    OwnerAttributeValueListResponse findAttributeListForOwnerById( final String ownerId, final String descriptorId );

    AttributeValueSaveResponse save( final RecordUpdaterIF recordUpdater, final AttributeValueSaveRequest req );
    OwnersWithAttributeNameResponse findOwnersWithAttributeName( final String ownerType, final String descriptorName );
}
