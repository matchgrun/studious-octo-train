/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.AttributeDescriptor;
import com.ibm.wh.siam.core.request.attribute.AttributeDescriptorSaveRequest;
import com.ibm.wh.siam.core.response.attribute.AttributeDescriptorListResponse;
import com.ibm.wh.siam.core.response.attribute.AttributeDescriptorResponse;
import com.ibm.wh.siam.core.response.attribute.UniqueAttributeNameResponse;

/**
 * @author Match Grun
 *
 */
public interface AttributeDescriptorService {

    public final String SELECTOR_ALL = "all";
    public final String SELECTOR_DESCRIPTOR_ID = "descriptorId";
    public final String SELECTOR_DESCRIPTOR_NAME = "attributeName";

    AttributeDescriptorListResponse findAll();
    AttributeDescriptorResponse findById(final String descriptorId );
    AttributeDescriptorResponse findByName(final String attribName );
    AttributeDescriptorResponse save( final RecordUpdaterIF recordUpdater, final AttributeDescriptor descriptor );
    AttributeDescriptorResponse save( final RecordUpdaterIF recordUpdater, final AttributeDescriptorSaveRequest req );

    AttributeDescriptorResponse deleteById( final RecordUpdaterIF recordUpdater, final String descriptorId );

    UniqueAttributeNameResponse fetchAttributeNameMap();
}
