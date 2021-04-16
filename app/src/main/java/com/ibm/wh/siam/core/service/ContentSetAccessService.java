/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.request.content.ContentSetAccessSaveRequest;
import com.ibm.wh.siam.core.response.content.ContentSetAccessResponse;
import com.ibm.wh.siam.core.response.content.ContentSetAccessSaveResponse;
import com.ibm.wh.siam.core.response.content.ContentSetAccessorDetailsResponse;
import com.ibm.wh.siam.core.response.content.ContentSetAccessorResponse;

/**
 * @author Match Grun
 *
 */
public interface ContentSetAccessService {

    ContentSetAccessorResponse findAccessorsByContentSetId( final String contentSetId );
    ContentSetAccessorDetailsResponse findAccessorDetailsByContentSetId( final String contentSetId );

    ContentSetAccessResponse findById( final String contentSetAccessId );

    ContentSetAccessSaveResponse assignAccessor(
            final RecordUpdaterIF recordUpdater,
            final ContentSetAccessSaveRequest request );

    ContentSetAccessSaveResponse removeAccessor(
            final RecordUpdaterIF recordUpdater,
            final String contentSetId,
            final String contentSetAccessId );

}
