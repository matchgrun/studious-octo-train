/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.AttributeDescriptorDAO;
import com.ibm.wh.siam.core.dao.dto.AttributeMapItem;
import com.ibm.wh.siam.core.dto.AttributeDescriptor;
import com.ibm.wh.siam.core.request.attribute.AttributeDescriptorSaveRequest;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.attribute.AttributeDescriptorListResponse;
import com.ibm.wh.siam.core.response.attribute.AttributeDescriptorResponse;
import com.ibm.wh.siam.core.response.attribute.UniqueAttributeNameResponse;
import com.ibm.wh.siam.core.service.AttributeDescriptorService;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;

/**
 * @author Match Grun
 * @param <E>
 *
 */
@Component
public class AttributeDescriptorServiceImpl
extends BaseSiamService
implements AttributeDescriptorService
{
    @Resource
    AttributeDescriptorDAO attribDescriptorDao;

    private static final String ERRMSG_NOT_FOUND = "Attribute Descriptor not Found.";
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Attribute Descriptor.";
    private static final String ERRMSG_NOT_SPECIFIED = "Attribute Descriptor not Specified.";

    private static final Logger logger = LoggerFactory.getLogger( AttributeDescriptorServiceImpl.class );

    @Override
    public AttributeDescriptorListResponse findAll() {
        AttributeDescriptorListResponse response = new AttributeDescriptorListResponse ();
        Iterable<AttributeDescriptor> list = attribDescriptorDao.findAll();
        response.setListDescriptors(list);
        response.setStatus(statusSuccess());
        return response;
    }

    @Override
    public AttributeDescriptorResponse findById( final String descriptorId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "descriptorId=" + descriptorId );
        }
        ResponseStatus sts = null;
        AttributeDescriptorResponse response = new AttributeDescriptorResponse();
        AttributeDescriptor item = attribDescriptorDao.findById(descriptorId);
        response.setAttributeDescriptor(item);
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
    public AttributeDescriptorResponse findByName( final String attribName) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByName()");
            logger.info( "attribName=" + attribName );
        }
        ResponseStatus sts = null;
        AttributeDescriptorResponse response = new AttributeDescriptorResponse();
        AttributeDescriptor item = attribDescriptorDao.findByName(attribName);
        response.setAttributeDescriptor(item);
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
    public AttributeDescriptorResponse save(
            final RecordUpdaterIF recordUpdater,
            final AttributeDescriptor objToSave )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("save()");
            logger.info( "objToSave=" + objToSave );
        }

        AttributeDescriptorResponse response = new AttributeDescriptorResponse();

        AttributeDescriptor objSaved = null;
        String objId = objToSave.getAttributeDescriptorId();
        AttributeDescriptor objFound = attribDescriptorDao.findById(objId);
        if( logger.isInfoEnabled() ) {
            logger.info( "objFound=" + objFound );
        }

        // Check whether there is another product with the same code
        String codeCheck = objToSave.getAttributeName();
        AttributeDescriptor objOther = attribDescriptorDao.findByName(codeCheck);
        if( logger.isInfoEnabled() ) {
            logger.info( "objOther=" + objOther );
        }

        ResponseStatus sts = null;
        int chk = checkKeys(objToSave, objFound, objOther);

        // Validate object
        if( chk != ACTION_DUPLICATE ) {
            List<ValidationError> listErrors = validateAttributeDescriptor(recordUpdater, objToSave);
            if( listErrors.size() > 0 ) {
                sts = validationErrors();
                sts.setValidationErrors(listErrors);
                response.setStatus(sts);
                response.setAttributeDescriptor(objToSave);
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
                objSaved = attribDescriptorDao.insert(recordUpdater, objToSave);
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
                objSaved = attribDescriptorDao.updateById(recordUpdater, objToSave);
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
        response.setAttributeDescriptor(objSaved);

        return response;
    }

    @Override
    public AttributeDescriptorResponse deleteById(
            final RecordUpdaterIF recordUpdater,
            final String descriptorId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "descriptorId=" + descriptorId );
        }

        AttributeDescriptorResponse response = new AttributeDescriptorResponse();
        if( StringUtils.isEmpty( descriptorId ) ) {
            if( logger.isErrorEnabled() ) {
                logger.error( "No attribute descriptor ID." );
            }
            response.setStatus( statusInvalid( ERRMSG_INVALID_OBJECT ) );
            return response;
        }

        ResponseStatus sts = null;
        AttributeDescriptor objDeleted = null;
        AttributeDescriptor objFound = attribDescriptorDao.findById(descriptorId);
        if( objFound == null ) {
            response.setStatus(statusNotFound( ERRMSG_NOT_FOUND ));
        }
        else {
            objDeleted = attribDescriptorDao.deleteById( recordUpdater, objFound );
            if( objDeleted == null ) {
                sts = statusDeleteFailed();
            }
            else {
                sts = statusSuccess();
            }
        }
        response.setStatus(sts);
        response.setAttributeDescriptor(objDeleted);
        return response;
    }

    private int checkKeys(
            final AttributeDescriptor objSave,
            final AttributeDescriptor objFound,
            final AttributeDescriptor objOther )
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
        String objCheck = objSave.getAttributeName();
        if( objFound.getAttributeName().equals( objCheck ) ) {
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

    private List<ValidationError> validateAttributeDescriptor(
            final RecordUpdaterIF recordUpdater,
            final AttributeDescriptor descriptor )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, descriptor );
        return listErrors;
    }

    @Override
    public UniqueAttributeNameResponse fetchAttributeNameMap() {
        if( logger.isInfoEnabled() ) {
            logger.info("fetchAttributeNameMapping()");
        }

        ResponseStatus sts = null;
        Iterable<AttributeMapItem> list = attribDescriptorDao.findMapAttributeId();
        UniqueAttributeNameResponse response = new UniqueAttributeNameResponse();
        response.setAttributeNameMap(list);
        if( list == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);

        return response;
    }

    @Override
    public AttributeDescriptorResponse save(
            final RecordUpdaterIF recordUpdater,
            final AttributeDescriptorSaveRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info( "save()");
            logger.info( "req=" + req );
        }

        AttributeDescriptorResponse response = null;
        List<ValidationError> listErrors = validateRequest( req );
        if( listErrors.size() > 0 ) {
            response = new AttributeDescriptorResponse();
            ResponseStatus sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        AttributeDescriptor objToSave = req.getAttributeDescriptor();
        response = save( recordUpdater, objToSave );

        return response;
    }

    /**
     * Validate request object.
     * @param req   Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest( final AttributeDescriptorSaveRequest req )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "validateRequest()" );
        }
        FieldValidator fv = new FieldValidator();
        return fv.validateRequest( req );
    }


}
