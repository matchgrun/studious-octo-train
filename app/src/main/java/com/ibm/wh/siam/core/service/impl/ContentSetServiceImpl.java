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
import com.ibm.wh.siam.core.dao.ContentSetDAO;
import com.ibm.wh.siam.core.dao.CoreCommonDAO;
import com.ibm.wh.siam.core.dao.dto.AccessorDetail;
import com.ibm.wh.siam.core.dao.dto.OwnerDetail;
import com.ibm.wh.siam.core.dto.ContentSet;
import com.ibm.wh.siam.core.dto.ContentSetDetail;
import com.ibm.wh.siam.core.request.content.ContentSetSaveRequest;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.content.AccessibleContentSetDetallsResponse;
import com.ibm.wh.siam.core.response.content.ContentSetDetailResponse;
import com.ibm.wh.siam.core.response.content.ContentSetListResponse;
import com.ibm.wh.siam.core.response.content.ContentSetResponse;
import com.ibm.wh.siam.core.service.ContentSetService;
import com.ibm.wh.siam.core.service.OwnerService;
import com.ibm.wh.siam.core.util.AccessorUtil;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;

/**
 * @author Match Grun
 *
 */
@Component
public class ContentSetServiceImpl
extends BaseSiamService
implements ContentSetService
{
    @Resource
    ContentSetDAO contentSetDao;

    @Resource
    OwnerService ownerSvc;

    @Resource
    CoreCommonDAO coreCommonDao;

    private static final String ERRMSG_NOT_FOUND = "Content Set not Found.";
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Content Set.";
    private static final String ERRMSG_NOT_SPECIFIED = "Content Set not Specified.";
    private static final String ERRMSG_NOT_FOUND_ACCESSOR = "Content Accessor not Found.";

    private static final Logger logger = LoggerFactory.getLogger( ContentSetServiceImpl.class );

    @Override
    public ContentSetListResponse findAll() {
        ContentSetListResponse response = new ContentSetListResponse ();
        Iterable<ContentSet> list = contentSetDao.findAll();
        response.setListContentSets(list);
        response.setStatus(statusSuccess());
        return response;
    }

    @Override
    public ContentSetResponse findById( final String contentSetId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "contentSetId=" + contentSetId );
        }
        ResponseStatus sts = null;
        ContentSetResponse response = new ContentSetResponse();
        ContentSet item = contentSetDao.findById(contentSetId);
        response.setContentSet(item);
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
    public ContentSetListResponse findByDescriptor( final String descriptorId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByDescriptor()");
            logger.info( "descriptorId=" + descriptorId );
        }
        ContentSetListResponse response = new ContentSetListResponse();
        Iterable<ContentSet> list = contentSetDao.findByDescriptor(descriptorId);
        response.setListContentSets(list);
        response.setStatus(statusSuccess());
        return response;
    }

    @Override
    public ContentSetListResponse findContentSetListForOwner( final String ownerId ) {
        ContentSetListResponse response = new ContentSetListResponse ();
        Iterable<ContentSet> list = contentSetDao.findByOwner(ownerId);
        response.setListContentSets(list);
        response.setStatus(statusSuccess());
        return response;
    }

    @Override
    public ContentSetResponse save(
            final RecordUpdaterIF recordUpdater,
            final ContentSet objToSave )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("save()");
            logger.info( "objToSave=" + objToSave );
        }

        ContentSetResponse response = new ContentSetResponse();

        ContentSet objSaved = null;
        String objId = objToSave.getContentSetId();
        ContentSet objFound = contentSetDao.findById(objId);
        if( logger.isInfoEnabled() ) {
            logger.info( "objFound=" + objFound );
        }

        // Check whether there is another content set with the same id
        String contentSetCheck = objToSave.getContentSetDescriptorId();
        ContentSet objOther = contentSetDao.findById(contentSetCheck);
        if( logger.isInfoEnabled() ) {
            logger.info( "objOther=" + objOther );
        }

        ResponseStatus sts = null;
        int chk = checkKeys(objToSave, objFound, objOther);

        // Validate object
        if( chk != ACTION_DUPLICATE ) {
            List<ValidationError> listErrors = validateContentSet(recordUpdater, objToSave);
            if( listErrors.size() > 0 ) {
                sts = validationErrors();
                sts.setValidationErrors(listErrors);
                response.setStatus(sts);
                response.setContentSet(objToSave);
                return response;
            }
        }

        if( chk == ACTION_DUPLICATE ) {
            sts = statusDuplicateKey();
        }
        else if( chk == ACTION_INSERT ) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Inserting..." );
            }
            try {
                objSaved = contentSetDao.insert(recordUpdater, objToSave);
                if( objSaved == null ) {
                    sts = statusInsertFailed();
                }
                else {
                    sts = statusSuccess();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                sts = statusInsertFailed();
            }
        }
        else {
            // Update existing record
            if( logger.isInfoEnabled() ) {
                logger.info( "Updating..." );
            }
            try {
                objSaved = contentSetDao.updateById(recordUpdater, objToSave);
                if( objSaved == null ) {
                    sts = statusUpdateFailed();
                }
                else {
                    sts = statusSuccess();
                }
            }
            catch( Exception e) {
                sts = statusUpdateFailed();
            }
        }

        response.setStatus(sts);
        response.setContentSet(objSaved);

        return response;
    }

    @Override
    public ContentSetResponse deleteById(
            final RecordUpdaterIF recordUpdater,
            final String contentSetId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "contentSetId=" + contentSetId );
        }

        ContentSetResponse response = new ContentSetResponse();
        if( StringUtils.isEmpty( contentSetId ) ) {
            if( logger.isErrorEnabled() ) {
                logger.error( "No content set ID." );
            }
            response.setStatus( statusInvalid( ERRMSG_INVALID_OBJECT ) );
            return response;
        }

        ResponseStatus sts = null;
        ContentSet objDeleted = null;
        ContentSet objFound = contentSetDao.findById(contentSetId);
        if( objFound == null ) {
            response.setStatus(statusNotFound( ERRMSG_NOT_FOUND ));
        }
        else {
            objDeleted = contentSetDao.deleteById( recordUpdater, objFound );
            if( objDeleted == null ) {
                sts = statusDeleteFailed();
            }
            else {
                sts = statusSuccess();
            }
        }
        response.setStatus(sts);
        response.setContentSet(objDeleted);
        return response;
    }

    private int checkKeys(
            final ContentSet objSave,
            final ContentSet objFound,
            final ContentSet objOther )
    {
        if( objFound == null ) {
            // Inserting data
            if( objOther == null ) {
                // Allow insert to proceed
                return ACTION_INSERT;
            }
            // This would create duplicate on insert
            return ACTION_DUPLICATE;
        }

        // Updating data
        String objCheck = objSave.getContentSetId();
        if( objFound.getContentSetId().equals( objCheck ) ) {
            // Attempting to change current data, Allow update to proceed
            return ACTION_UPDATE;
        }

        // Check whether there is another record with the same code
        if( objOther == null ) {
            return ACTION_UPDATE;
        }

        // This would create duplicate on update
        return ACTION_DUPLICATE;
    }

    private List<ValidationError> validateContentSet(
            final RecordUpdaterIF recordUpdater,
            final ContentSet contentSet )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, contentSet );

        String ownerType = contentSet.getOwnerType();
        if( listErrors == null ) {
            listErrors = new ArrayList<ValidationError>();
            OwnerServiceImpl.validateOwnerType(listErrors, ownerType);
        }
        return listErrors;
    }

    @Override
    public ContentSetDetailResponse findContentSetDetailsById( final String contentSetId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findContentSetDetailsById()");
            logger.info( "contentSetId=" + contentSetId );
        }

        ResponseStatus sts = null;
        ContentSetDetailResponse response = new ContentSetDetailResponse();

        ContentSetDetail contentSet = contentSetDao.findContentSetDetailById(contentSetId);
        response.setContentSet(contentSet);
        if( contentSet == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public AccessibleContentSetDetallsResponse findContentSetByAccessor( final String accessorId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findContentSetByAccessor()");
            logger.info( "accessorId=" + accessorId );
        }

        ResponseStatus sts = null;
        AccessibleContentSetDetallsResponse response = new AccessibleContentSetDetallsResponse();

        OwnerDetail ownerDetail = coreCommonDao.fetchOwnerDetail(accessorId);
        if( ownerDetail == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_ACCESSOR );
        }
        else {
            AccessorDetail accessor = AccessorUtil.createFromOwner(ownerDetail);
            response.setAccessor(accessor);
            Iterable<ContentSetDetail> list = contentSetDao.findByAccessor(accessorId);
            response.setListContentSets(list);
            sts = statusSuccess();
        }
        response.setStatus(sts);

        return response;
    }

    @Override
    public ContentSetResponse save(
            final RecordUpdaterIF recordUpdater,
            final ContentSetSaveRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info( "save()");
            logger.info( "req=" + req );
        }

        ContentSetResponse response = null;
        List<ValidationError> listErrors = validateRequest( req );
        if( listErrors.size() > 0 ) {
            response = new ContentSetResponse();
            ResponseStatus sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        ContentSet objToSave = req.getContentSet();
        response = save( recordUpdater, objToSave );

        return response;
    }

    /**
     * Validate request object.
     * @param req   Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final ContentSetSaveRequest req )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "validateRequest()" );
        }
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );
        return listErrors;
    }

}
