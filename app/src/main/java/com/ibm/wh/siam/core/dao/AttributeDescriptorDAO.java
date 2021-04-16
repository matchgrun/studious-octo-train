/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import java.util.List;
import java.util.Map;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.dto.AttributeMapItem;
import com.ibm.wh.siam.core.dto.AttributeDescriptor;

/**
 * @author Match Grun
 *
 */
public interface AttributeDescriptorDAO {
    Iterable<AttributeDescriptor> findAll();
    AttributeDescriptor findById( final String descriptorId );
    AttributeDescriptor findByName( final String attribName );

    AttributeDescriptor insert( final RecordUpdaterIF recordUpdater, final AttributeDescriptor descriptor );
    AttributeDescriptor updateById( final RecordUpdaterIF recordUpdater, final AttributeDescriptor descriptor );
    AttributeDescriptor deleteById( final RecordUpdaterIF recordUpdater, final AttributeDescriptor descriptor );

    Map<String, String> findMapAttributeId( final List<String> listNames );
    Iterable<AttributeMapItem> findMapAttributeId();
}
