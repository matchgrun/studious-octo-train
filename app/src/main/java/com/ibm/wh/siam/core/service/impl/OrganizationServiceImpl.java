package com.ibm.wh.siam.core.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.OrganizationDAO;
import com.ibm.wh.siam.core.dto.Organization;
import com.ibm.wh.siam.core.request.busentity.OrganizationSaveRequest;
import com.ibm.wh.siam.core.response.OrganizationListResponse;
import com.ibm.wh.siam.core.response.OrganizationResponse;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.service.OrganizationService;
import com.ibm.wh.siam.core.util.SearchUtil;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;

@Component
public class OrganizationServiceImpl
extends BaseSiamService
implements OrganizationService
{

    @Resource
    OrganizationDAO organizationDao;

    @Resource
    private SearchUtil searchUtil;

    private static final String ERRMSG_NOT_FOUND = "Organization not Found.";
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Organization.";
    private static final String ERRMSG_NOT_SPECIFIED = "Organization not Specified.";

    private static final Logger logger = LoggerFactory.getLogger( OrganizationServiceImpl.class );

    @Override
    public OrganizationResponse findByCode(final String orgCode) {
        ResponseStatus sts = null;
        OrganizationResponse response = new OrganizationResponse();
        Organization org = organizationDao.findByCode(orgCode);
        response.setOrganization(org);
        if( org == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public OrganizationResponse findById(final String orgId) {
        ResponseStatus sts = null;
        OrganizationResponse response = new OrganizationResponse();
        Organization objFound = organizationDao.findById(orgId);
        response.setOrganization(objFound);
        if( objFound == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);

        return response;
    }

    @Override
    public OrganizationResponse findByAccount(final String acctNum) {
        ResponseStatus sts = null;
        OrganizationResponse response = new OrganizationResponse();
        Organization objFound = organizationDao.findByAccount(acctNum);
        response.setOrganization(objFound);
        if( objFound == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public OrganizationResponse save(
            final RecordUpdaterIF recordUpdater,
            final Organization objToSave)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("save()");
            logger.info( "objToSave=" + objToSave );
        }

        OrganizationResponse response = new OrganizationResponse();
        Organization objSaved = null;
        String objId = objToSave.getOrganizationId();
        Organization  objFound = organizationDao.findById(objId);
        if( logger.isInfoEnabled() ) {
            logger.info( "objFound=" + objFound );
        }

        // Check whether there is another product with the same code
        String codeCheck = objToSave.getOrganizationCode();
        Organization objOther = organizationDao.findByCode(codeCheck);
        if( logger.isInfoEnabled() ) {
            logger.info( "objOther=" + objOther );
        }

        ResponseStatus sts = null;
        int chk = checkKeys(objToSave, objFound, objOther);

        // Validate object
        if( chk != ACTION_DUPLICATE ) {
            List<ValidationError> listErrors = validateOrganization(recordUpdater, objToSave);
            if( listErrors.size() > 0 ) {
                sts = validationErrors();
                sts.setValidationErrors(listErrors);
                response.setStatus(sts);
                response.setOrganization(objToSave);
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
                objSaved = organizationDao.insert(recordUpdater, objToSave);
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
                objSaved = organizationDao.updateById(recordUpdater, objToSave);
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
        response.setOrganization(objSaved);

        return response;
    }

    @Override
    public OrganizationResponse deleteById(
            final RecordUpdaterIF recordUpdater,
            final String orgId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "Org=" + orgId );
        }

        OrganizationResponse response = new OrganizationResponse();
        if( StringUtils.isEmpty( orgId ) ) {
            if( logger.isErrorEnabled() ) {
                logger.error( "No organization ID." );
            }
            response.setStatus( statusInvalid( ERRMSG_INVALID_OBJECT ) );
            return response;
        }

        ResponseStatus sts = null;
        Organization objDeleted = null;
        Organization objFound = organizationDao.findById(orgId);
        if( objFound == null ) {
            response.setStatus(statusNotFound( ERRMSG_NOT_FOUND ));
        }
        else {
            objDeleted = organizationDao.deleteById( recordUpdater, objFound );
            if( objDeleted == null ) {
                sts = statusDeleteFailed();
            }
            else {
                sts = statusSuccess();
            }
        }

        response.setStatus(sts);
        response.setOrganization(objDeleted);
        return response;
    }

    /**
     * Validate organization and record updater.
     * @param recordUpdater Record updater.
     * @param org           Organization.
     * @return List of validation errors.
     */
    private List<ValidationError> validateOrganization(
            final RecordUpdaterIF recordUpdater,
            final Organization org )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, org );
        return listErrors;
    }

    private int checkKeys(
            final Organization objToSave,
            final Organization objFound,
            final Organization objOther )
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
        String valueCheck = objToSave.getOrganizationCode();
        if( objFound.getOrganizationCode().equals( valueCheck ) ) {
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

    @Override
    public OrganizationListResponse findByAccountGroup( final String acctGroup ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByAccountGroup()");
            logger.info( "acctGroup=" + acctGroup );
        }
        OrganizationListResponse response = new OrganizationListResponse();
        Iterable<Organization> listOrganizations = organizationDao.findByAccountGroup( acctGroup );
        response.setListOrganizations(listOrganizations);;
        response.setStatus(statusSuccess());
        return response;
    }

    @Override
    public OrganizationResponse save(
            final RecordUpdaterIF recordUpdater,
            final OrganizationSaveRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info( "save()");
            logger.info( "req=" + req );
        }

        OrganizationResponse response = new OrganizationResponse();
        List<ValidationError> listErrors = validateRequest( req );
        if( listErrors.size() > 0 ) {
            ResponseStatus sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        Organization objToSave = req.getOrganization();
        response = save( recordUpdater, objToSave );

        return response;
    }

    /**
     * Validate request object.
     * @param req   Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final OrganizationSaveRequest req )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "validateRequest()" );
        }
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );
        return listErrors;
    }

}
