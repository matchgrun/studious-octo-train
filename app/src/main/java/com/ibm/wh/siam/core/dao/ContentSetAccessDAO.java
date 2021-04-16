/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.ContentSetAccess;
import com.ibm.wh.siam.core.dto.ContentSetAccessDetail;

/**
 * @author Match Grun
 *
 */
public interface ContentSetAccessDAO {

    public ContentSetAccess findById( final String contentSetAccessId );

    public Iterable<ContentSetAccess> findByContentSetId( final String contentSetId );
    public Iterable<ContentSetAccessDetail> findDetailByContentSetId( final String contentSetId );

    public ContentSetAccess insert( final RecordUpdaterIF recordUpdater, final ContentSetAccess objToInsert );
//    public ContentSetAccess updateById( final RecordUpdaterIF recordUpdater, final ContentSetAccess objToUpdate );
    public ContentSetAccess deleteById( final RecordUpdaterIF recordUpdater, final ContentSetAccess objToDelete );
}
