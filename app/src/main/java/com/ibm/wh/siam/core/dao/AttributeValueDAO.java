/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import java.util.List;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.dto.AttributeNameValueLW;
import com.ibm.wh.siam.core.dao.dto.OwnerDescriptorItem;
import com.ibm.wh.siam.core.dto.AttributeNameValue;
import com.ibm.wh.siam.core.dto.AttributeValue;

/**
 * @author Match Grun
 *
 */
public interface AttributeValueDAO {
    AttributeValue findById( final String attributeValueId );

    List<AttributeNameValue> findByOwner( final String ownerId );
    List<AttributeNameValue> findForOwnerByDescriptorId( final String ownerId, final String descriptorId );
    List<AttributeNameValue> findForOwnerByDescriptorName( final String ownerId, final String descriptorName );

    AttributeValue insert( final RecordUpdaterIF recordUpdater, final AttributeValue attrib );
    AttributeValue updateById( final RecordUpdaterIF recordUpdater, final AttributeValue attrib );
    AttributeValue deleteById( final RecordUpdaterIF recordUpdater, final AttributeValue attrib );

    List<AttributeNameValueLW> findALWForOwner( final String ownerId );
    List<AttributeNameValueLW> findALWForOwnerByDescriptorId( final String ownerId, final String descriptorId );
    List<AttributeNameValueLW> findALWForOwnerByDescriptorName( final String ownerId, final String descriptorName );

    AttributeValue insert( final RecordUpdaterIF recordUpdater, final AttributeNameValue anv );
    AttributeNameValue deleteById( final RecordUpdaterIF recordUpdater, final AttributeNameValue anv );

    Iterable<OwnerDescriptorItem> findOwnersWithAttributeName( final String ownerType, final String descriptorName );
}
