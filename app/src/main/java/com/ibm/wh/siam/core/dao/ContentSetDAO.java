/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import java.util.List;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.ContentSet;
import com.ibm.wh.siam.core.dto.ContentSetDetail;

/**
 * @author Match Grun
 *
 */
public interface ContentSetDAO {
    Iterable<ContentSet> findAll();
    ContentSet findById( final String contentSetId );
    ContentSetDetail findContentSetDetailById( final String contentSetId );

    Iterable<ContentSet> findByDescriptor( final String descriptorId );
    Iterable<ContentSet> findByOwner( final String ownerId );

    public ContentSet insert( final RecordUpdaterIF recordUpdater, final ContentSet objToInsert );
    public ContentSet updateById( final RecordUpdaterIF recordUpdater, final ContentSet objToUpdate );
    public ContentSet deleteById( final RecordUpdaterIF recordUpdater, final ContentSet objToDelete );

    List<ContentSetDetail> findByAccessor( final String accessorId );
}
