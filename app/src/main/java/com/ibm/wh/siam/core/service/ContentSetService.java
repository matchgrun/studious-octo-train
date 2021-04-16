/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.ContentSet;
import com.ibm.wh.siam.core.request.content.ContentSetSaveRequest;
import com.ibm.wh.siam.core.response.content.AccessibleContentSetDetallsResponse;
import com.ibm.wh.siam.core.response.content.ContentSetDetailResponse;
import com.ibm.wh.siam.core.response.content.ContentSetListResponse;
import com.ibm.wh.siam.core.response.content.ContentSetResponse;

/**
 * @author Match Grun
 *
 */
public interface ContentSetService {

    ContentSetListResponse findAll();
    ContentSetResponse findById(final String contentSetId );

    ContentSetListResponse findByDescriptor(final String descriptorId );
    ContentSetListResponse findContentSetListForOwner( final String ownerId );

    ContentSetResponse save( final RecordUpdaterIF recordUpdater, final ContentSetSaveRequest req );
    ContentSetResponse save( final RecordUpdaterIF recordUpdater, final ContentSet contentSet );
    ContentSetResponse deleteById( final RecordUpdaterIF recordUpdater, final String contentSetId );

    ContentSetDetailResponse findContentSetDetailsById(final String contentSetId );

    AccessibleContentSetDetallsResponse findContentSetByAccessor( final String accessorId );

}
