/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.ContentSetAccessDAO;
import com.ibm.wh.siam.core.dao.ContentSetDAO;
import com.ibm.wh.siam.core.dto.ContentSet;
import com.ibm.wh.siam.core.dto.ContentSetAccess;
import com.ibm.wh.siam.core.dto.ContentSetAccessDetail;
import com.ibm.wh.siam.core.dto.ContentSetDetail;
import com.ibm.wh.siam.core.request.content.ContentSetAccessItem;
import com.ibm.wh.siam.core.request.content.ContentSetAccessSaveRequest;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.content.ContentSetAccessResponse;
import com.ibm.wh.siam.core.response.content.ContentSetAccessSaveResponse;
import com.ibm.wh.siam.core.response.content.ContentSetAccessorDetailsResponse;
import com.ibm.wh.siam.core.response.content.ContentSetAccessorResponse;
import com.ibm.wh.siam.core.response.owner.OwnerTypeResponse;
import com.ibm.wh.siam.core.service.ContentSetAccessService;
import com.ibm.wh.siam.core.service.OwnerService;
import com.ibm.wh.siam.core.util.StatusUtil;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;

/**
 * @author Match Grun
 *
 */
@Component
public class ContentSetAccessServiceImpl
extends BaseSiamService
implements ContentSetAccessService
{
    @Resource
    ContentSetAccessDAO contentSetAccessDao;

    @Resource
    ContentSetDAO contentSetDao;

    @Resource
    OwnerService ownerSvc;


    private static final String ERRMSG_NOT_FOUND = "Content Set Access not Found.";
    private static final String ERRMSG_NOT_FOUND_CONTENT_SET = "Content Set not Found.";
    private static final String ERRMSG_NOT_FOUND_ACCESSOR = "Content Accessor not Found.";
    private static final String ERRMSG_NOT_FOUND_CSA_ID = "Content Set Access not Found.";

    @SuppressWarnings("unused")
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Content Access Set.";

    private static final Logger logger = LoggerFactory.getLogger( ContentSetAccessServiceImpl.class );

    @Override
    public ContentSetAccessResponse findById( final String contentSetAccessId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "contentSetAccessId=" + contentSetAccessId );
        }
        ResponseStatus sts = null;
        ContentSetAccessResponse response = new ContentSetAccessResponse();
        ContentSetAccess item = contentSetAccessDao.findById(contentSetAccessId);
        response.setContentSetAccess(item);
        if( item == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public ContentSetAccessorResponse findAccessorsByContentSetId( final String contentSetId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByContentSetId()");
            logger.info( "contentSetId=" + contentSetId );
        }
        ResponseStatus sts = null;
        ContentSetAccessorResponse response = new ContentSetAccessorResponse();

        ContentSet contentSet = contentSetDao.findById(contentSetId);
        response.setContentSet(contentSet);
        if( contentSet == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_CONTENT_SET );
        }
        else {
            Iterable<ContentSetAccess> list = contentSetAccessDao.findByContentSetId(contentSetId);
            response.setListAccessors( list );
            sts = statusSuccess();
        }
        response.setStatus(sts);


        if( contentSet != null ) {
            response.setContentSet(contentSet);
            response.getStatus();
        }
        return response;
    }

    @Override
    public ContentSetAccessSaveResponse assignAccessor(
            final RecordUpdaterIF recordUpdater,
            final ContentSetAccessSaveRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("assignAccessor()");
            logger.info( "req=" + req );
        }

        ResponseStatus sts = null;
        ContentSetAccessSaveResponse response = new ContentSetAccessSaveResponse();

        // Validate request
        List<ValidationError> listErrors = validateRequest( recordUpdater, req );
        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        ContentSetAccessItem csaItem = req.getContentAccess();
        String contentSetId = csaItem.getContentSetId();
        ContentSet contentSet = contentSetDao.findById(contentSetId);
        if( contentSet == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_CONTENT_SET );
        }
        else {
            // Validate accessor
            String accessorId = csaItem.getAccessorId();
            OwnerTypeResponse reqOwner = ownerSvc.findOwnerType(accessorId);
            ResponseStatus stsOwner = reqOwner.getStatus();

            // Check that accessor exists
            if( ! StringUtils.isEmpty( stsOwner.getErrorCode() ) ) {
                listErrors = new ArrayList<ValidationError>();
                ValidationError vError = new ValidationError("accessorId", ERRMSG_NOT_FOUND_ACCESSOR);
                listErrors.add(vError);

                sts = validationErrors();
                sts.setValidationErrors(listErrors);
            }
            else {
                // Insert content access record
                ContentSetAccess objInsert = populateContentSetAccess(csaItem, reqOwner);
                ContentSetAccess csaSaved = contentSetAccessDao.insert(recordUpdater, objInsert);
                response.setContentSetAccess(csaSaved);
                sts = statusSuccess();
            }

        }
        response.setStatus(sts);

        return response;
    }

    private List<ValidationError> validateRequest(
            final RecordUpdaterIF recordUpdater,
            final ContentSetAccessSaveRequest req )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );

        // Validate contained item
        if( req != null ) {
            ContentSetAccessItem csaItem = req.getContentAccess();
            listErrors.addAll( fv.validate(recordUpdater, csaItem ) );
        }
        return listErrors;
    }

    /**
     * Populate content set access from specified item.
     * @param csaItem   Content save item.
     * @param reqOwner  Request owner.
     * @return  Populated object.
     */
    private ContentSetAccess populateContentSetAccess(
            final ContentSetAccessItem csaItem,
            final OwnerTypeResponse reqOwner )
    {
        ContentSetAccess csa = new ContentSetAccess();
        csa.setContentSetId( csaItem.getContentSetId() );
        csa.setAccessorId( reqOwner.getOwnerId() );
        csa.setAccessorType( reqOwner.getOwnerType() );
        csa.setContentAccessId( csaItem.getContentAccessId());
        csa.setStartDate( csaItem.getStartDate() );
        csa.setEndDate(csaItem.getEndDate() );
        csa.setStatus( StatusUtil.defaultValue( csaItem.getStatus() ));
        return csa;
    }

    @Override
    public ContentSetAccessSaveResponse removeAccessor(
            final RecordUpdaterIF recordUpdater,
            final String contentSetId,
            final String contentSetAccessId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("removeAccessor()");
            logger.info( "contentSetId=" + contentSetId  );
            logger.info( "contentSetAccessId=" + contentSetAccessId  );
        }

        ResponseStatus sts = null;
        ContentSetAccessSaveResponse response = new ContentSetAccessSaveResponse();

        // Validate request
        List<ValidationError> listErrors = validateRequest(recordUpdater, contentSetId, contentSetAccessId);
        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        ContentSet contentSet = contentSetDao.findById(contentSetId);
        if( contentSet == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_CONTENT_SET );
        }
        else {
            // Retrieve content set access from datastore
            ContentSetAccess csaFound = contentSetAccessDao.findById(contentSetAccessId);
            if( csaFound == null ) {
                listErrors = new ArrayList<ValidationError>();
                ValidationError vError = new ValidationError("contentSetAccessId", ERRMSG_NOT_FOUND_CSA_ID);
                listErrors.add(vError);

                sts = validationErrors();
                sts.setValidationErrors(listErrors);
            }
            else {
                ContentSetAccess csaDeleted = contentSetAccessDao.deleteById(recordUpdater, csaFound);
                response.setContentSetAccess(csaDeleted);
                sts = statusSuccess();
            }
        }
        response.setStatus(sts);

        return response;
    }

    private List<ValidationError> validateRequest(
            final RecordUpdaterIF recordUpdater,
            final String contentSetId,
            final String contentSetAccessId)
    {
        List<ValidationError> listErrors = new ArrayList<ValidationError>();
        if( StringUtils.isEmpty(contentSetId) ) {
            ValidationError vError = new ValidationError("contentSetId",FieldValidator.MSG_EMPTY);
            listErrors.add(vError);
        }
        if( StringUtils.isEmpty(contentSetAccessId) ) {
            ValidationError vError = new ValidationError("contentSetAccessId",FieldValidator.MSG_EMPTY);
            listErrors.add(vError);
        }
        return listErrors;
    }


    @Override
    public ContentSetAccessorDetailsResponse findAccessorDetailsByContentSetId( final String contentSetId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findAccessorDetailsByContentSetId()");
            logger.info( "contentSetId=" + contentSetId );
        }
        ResponseStatus sts = null;
        ContentSetAccessorDetailsResponse response = new ContentSetAccessorDetailsResponse();

        ContentSetDetail contentSet = contentSetDao.findContentSetDetailById(contentSetId);
        response.setContentSet(contentSet);
        if( contentSet == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_CONTENT_SET );
        }
        else {
            Iterable<ContentSetAccessDetail> list = contentSetAccessDao.findDetailByContentSetId(contentSetId);
            response.setListAccessors( list );
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

}
