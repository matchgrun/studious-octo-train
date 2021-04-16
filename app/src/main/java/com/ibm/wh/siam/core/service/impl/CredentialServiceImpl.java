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

import com.ibm.wh.siam.core.common.CommonConstants;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.CredentialRefDAO;
import com.ibm.wh.siam.core.dao.GroupCredentialDAO;
import com.ibm.wh.siam.core.dao.GroupDAO;
import com.ibm.wh.siam.core.dao.dto.CredentialRefLW;
import com.ibm.wh.siam.core.dto.CredentialRef;
import com.ibm.wh.siam.core.dto.Group;
import com.ibm.wh.siam.core.dto.GroupCredential;
import com.ibm.wh.siam.core.request.credential.CredentialAssignmentItem;
import com.ibm.wh.siam.core.request.credential.CredentialAssignmentRequest;
import com.ibm.wh.siam.core.request.credential.CredentialRefSaveRequest;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.credential.CredentialAssignmentResponse;
import com.ibm.wh.siam.core.response.credential.CredentialRefListResponse;
import com.ibm.wh.siam.core.response.credential.CredentialRefResponse;
import com.ibm.wh.siam.core.response.credential.GroupCredentialListResponse;
import com.ibm.wh.siam.core.service.CredentialService;
import com.ibm.wh.siam.core.util.AssignmentUtil;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;

/**
 * @author Match Grun
 *
 */
@Component
public class CredentialServiceImpl
extends BaseSiamService
implements CredentialService
{
    private static final String ERRMSG_NOT_FOUND = "Credential not Found.";
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Credential.";
    private static final String ERRMSG_NOT_FOUND_GROUP = "Group not Found.";
    private static final String ERRMSG_ALEADY_ASSIGNED = "Credential already assigned.";
    private static final String ERRMSG_NOT_ASSIGNED = "Credential not currently assigned.";
    private static final String ERRMSG_NOT_SPECIFIED = "Credential not Specified.";

    private static final String ERRMSG_INVALID_OPERATION = "Operation not Recognized.";

    @Resource
    CredentialRefDAO credentialDao;

    @Resource
    GroupDAO groupDao;

    @Resource
    GroupCredentialDAO groupCredentialDao;

    private static final Logger logger = LoggerFactory.getLogger( CredentialServiceImpl.class );

    @Override
    public CredentialRefResponse findById( final String credentialId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "credentialId=" + credentialId );
        }
        ResponseStatus sts = null;
        CredentialRefResponse response = new CredentialRefResponse();
        CredentialRef credential = credentialDao.findById(credentialId);
        response.setCredential(credential);
        if( credential == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public CredentialRefListResponse findByUserId( final String userId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByUserId()");
            logger.info( "userId=" + userId );
        }
        ResponseStatus sts = null;
        CredentialRefListResponse response = new CredentialRefListResponse();
        Iterable<CredentialRef> listCredentials = credentialDao.findByUserId(userId);
        response.setListCredentials(listCredentials);
        if( listCredentials == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public CredentialRefListResponse findByLastName( final String lastName) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByLastName()");
            logger.info( "lastName=" + lastName );
        }
        ResponseStatus sts = null;
        CredentialRefListResponse response = new CredentialRefListResponse();
        Iterable<CredentialRef> listCredentials = credentialDao.findByLastName(lastName);
        response.setListCredentials(listCredentials);
        if( listCredentials == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public CredentialRefListResponse findByEmail( final String emailAddress) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByEmail()");
            logger.info( "emailAddress=" + emailAddress );
        }
        ResponseStatus sts = null;
        CredentialRefListResponse response = new CredentialRefListResponse();
        Iterable<CredentialRef> listCredentials = credentialDao.findByEmailAddress(emailAddress);
        response.setListCredentials(listCredentials);
        if( listCredentials == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public CredentialRefListResponse findByTelephone( final String phoneNumber) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByTelephone()");
            logger.info( "phoneNumber=" + phoneNumber );
        }
        ResponseStatus sts = null;
        CredentialRefListResponse response = new CredentialRefListResponse();
        Iterable<CredentialRef> listCredentials = credentialDao.findByPhoneNumber(phoneNumber);
        response.setListCredentials(listCredentials);
        if( listCredentials == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public GroupCredentialListResponse findByGroupId( final String groupId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByGroupId()");
            logger.info( "groupId=" + groupId );
        }
        ResponseStatus sts = null;
        GroupCredentialListResponse response = new GroupCredentialListResponse();
        Iterable<CredentialRefLW> listCredentials = credentialDao.findByGroupId(groupId);
        response.setGroupId(groupId);
        response.setListCredentials(listCredentials);
        if( listCredentials == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public CredentialRefResponse save(
            final RecordUpdaterIF recordUpdater,
            final CredentialRef objToSave)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("save()");
            logger.info( "objToSave=" + objToSave );
        }

        CredentialRefResponse response = new CredentialRefResponse();
        CredentialRef objSaved = null;
        String objId = objToSave.getCredentialId();
        CredentialRef  objFound = credentialDao.findById(objId);
        if( logger.isInfoEnabled() ) {
            logger.info( "objFound=" + objFound );
        }

        ResponseStatus sts = null;

        // Validate object
        List<ValidationError> listErrors = validateCredentialRef(recordUpdater, objToSave);
        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            response.setCredential(objToSave);;
            return response;
        }

        if( objFound == null ) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Inserting..." );
            }
            try {
                objSaved = credentialDao.insert(recordUpdater, objToSave);
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
                objSaved = credentialDao.updateById(recordUpdater, objToSave);
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
        response.setCredential(objSaved);

        return response;
    }

    /**
     * Validate organization and record updater.
     * @param recordUpdater Record updater.
     * @param org           Organization.
     * @return List of validation errors.
     */
    private List<ValidationError> validateCredentialRef(
            final RecordUpdaterIF recordUpdater,
            final CredentialRef cred )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, cred);
        return listErrors;
    }

    @Override
    public CredentialRefResponse deleteById(
            final RecordUpdaterIF recordUpdater,
            final String credentialId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "credentialId=" + credentialId );
        }

        CredentialRefResponse response = new CredentialRefResponse();
        if( StringUtils.isEmpty( credentialId ) ) {
            if( logger.isErrorEnabled() ) {
                logger.error( "No credential ID." );
            }
            response.setStatus( statusInvalid( ERRMSG_INVALID_OBJECT ) );
            return response;
        }

        ResponseStatus sts = null;
        CredentialRef objDeleted = null;
        CredentialRef objFound = credentialDao.findById(credentialId);
        if( objFound == null ) {
            response.setStatus(statusNotFound( ERRMSG_NOT_FOUND ));
        }
        else {
            objDeleted = credentialDao.deleteById( recordUpdater, objFound );
            if( objDeleted == null ) {
                sts = statusDeleteFailed();
            }
            else {
                sts = statusSuccess();
            }
        }

        response.setStatus(sts);
        response.setCredential(objDeleted);
        return response;
    }

    @Override
    public CredentialRefResponse save(
            final RecordUpdaterIF recordUpdater,
            final CredentialRefSaveRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info( "save()");
            logger.info( "req=" + req );
        }

        CredentialRefResponse response = new CredentialRefResponse();
        List<ValidationError> listErrors = validateRequest( req );
        if( listErrors.size() > 0 ) {
            ResponseStatus sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        CredentialRef objToSave = req.getCredential();
        response = save( recordUpdater, objToSave );

        return response;
    }

    /**
     * Validate request object.
     * @param req   Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final CredentialRefSaveRequest req )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "validateRequest()" );
        }
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );
        return listErrors;
    }

    /**
     * Validate specified request.
     * @param recordUpdater     Record updater.
     * @param req               Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final RecordUpdaterIF recordUpdater,
            final CredentialAssignmentRequest req )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );

        // Validate contained item
        if( req != null ) {
            CredentialAssignmentItem grpAssign = req.getCredentialItem();
            listErrors.addAll( fv.validate(recordUpdater, grpAssign ) );
        }
        return listErrors;
    }

    /**
     * Assign credential.
     * @param recordUpdater Record updater.
     * @param groupId       Group ID.
     * @param credentialId  Credential ID.
     * @return Response.
     */
    protected CredentialAssignmentResponse assignCredential(
            final RecordUpdaterIF recordUpdater,
            final String groupId,
            final String credentialId )
    {
        ResponseStatus sts = null;
        CredentialAssignmentResponse response = new CredentialAssignmentResponse();
        response.setOperation( CommonConstants.OPERATION_ASSIGN );

        CredentialRef credential = null;
        Group group = groupDao.findById(groupId);
        if( group == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_GROUP );
        }
        else {
            credential = credentialDao.findById(credentialId);
            if( credential == null ) {
                sts = statusNotFound( ERRMSG_NOT_FOUND );
            }
            else {
                GroupCredential credGroup = groupCredentialDao.findByIds(groupId, credentialId);
                if( credGroup != null ) {
                    // Already there
                    sts = statusSuccess();
                    sts.setMessage( ERRMSG_ALEADY_ASSIGNED );
                }
                else {
                    // Insert association.
                    GroupCredential objToSave = new GroupCredential();
                    objToSave.setGroupId(groupId);
                    objToSave.setCredentialId(credentialId);
                    objToSave.setStatus(CommonConstants.STATUS_ACTIVE);

                    GroupCredential credSaved = groupCredentialDao.insert(recordUpdater, objToSave);
                    if( credSaved == null ) {
                        sts = statusInsertFailed();
                    }
                    else {
                        response.setCredential(credential);
                        response.setGroup(group);
                        response.setAssignmentId(credSaved.getMemberId());
                        sts = statusSuccess();
                    }
                }
            }
        }
        response.setStatus(sts);

        return response;
    }


    @Override
    public CredentialAssignmentResponse assignCredential(
            final RecordUpdaterIF recordUpdater,
            final CredentialAssignmentRequest request )
    {
        ResponseStatus sts = null;
        CredentialAssignmentResponse response = null;
        List<ValidationError> listErrors = validateRequest(
                recordUpdater,
                request);
        if( listErrors.size() > 0 ) {
            response = new CredentialAssignmentResponse();
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        CredentialAssignmentItem credAssign = request.getCredentialItem();
        String operation = credAssign.getOperation();
        if( ! AssignmentUtil.isValid(operation)) {
            sts = statusNotFound( ERRMSG_INVALID_OPERATION );
        }
        else {
            if( operation.equalsIgnoreCase(CommonConstants.OPERATION_ASSIGN)) {
                response = assignCredential(
                        recordUpdater,
                        credAssign.getGroupId(),
                        credAssign.getCredentialId() );
            }
            else {
                sts = statusNotFound( ERRMSG_INVALID_OPERATION );
            }
        }
        if( response == null ) {
            response = new CredentialAssignmentResponse();
            response.setStatus(sts);
        }
        return response;
    }

    /**
     * Unassign credential.
     * @param recordUpdater Record updater.
     * @param groupId       Group ID.
     * @param credentialId  Credential ID.
     * @return Response.
     */
    protected CredentialAssignmentResponse unassignCredential(
            final RecordUpdaterIF recordUpdater,
            final String groupId,
            final String credentialId)
    {
        ResponseStatus sts = null;
        CredentialAssignmentResponse response = new CredentialAssignmentResponse();
        response.setOperation(CommonConstants.OPERATION_UNASSIGN);

        CredentialRef credential = null;
        Group group = groupDao.findById(groupId);
        if( group == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_GROUP );
        }
        else {
            credential = credentialDao.findById(credentialId);
            if( credential == null ) {
                sts = statusNotFound( ERRMSG_NOT_FOUND );
            }
            else {
                // Find group credential
                GroupCredential objToDelete = groupCredentialDao.findByIds(groupId, credentialId);
                if( objToDelete == null ) {
                    // Already removed
                    sts = statusSuccess();
                    sts.setMessage( ERRMSG_NOT_ASSIGNED );
                }
                else {
                    GroupCredential objDeleted = groupCredentialDao.deleteById(recordUpdater, objToDelete );
                    if( objDeleted == null ) {
                        sts = statusDeleteFailed();
                    }
                    else {
                        response.setGroup(group);
                        response.setCredential(credential);
                        response.setAssignmentId(objDeleted.getMemberId());
                        sts = statusSuccess();
                    }
                }
            }
        }
        response.setStatus(sts);

        return response;
    }

    @Override
    public CredentialAssignmentResponse unassignCredential(
            final RecordUpdaterIF recordUpdater,
            final CredentialAssignmentRequest request)
    {
        ResponseStatus sts = null;
        CredentialAssignmentResponse response = null;

        CredentialAssignmentItem credAssign = request.getCredentialItem();
        String operation = credAssign.getOperation();
        if( ! AssignmentUtil.isValid(operation)) {
            sts = statusNotFound( ERRMSG_INVALID_OPERATION );
        }
        else {
            if( operation.equalsIgnoreCase(CommonConstants.OPERATION_UNASSIGN)) {
                response = unassignCredential(recordUpdater, credAssign.getGroupId(), credAssign.getCredentialId() );
            }
            else {
                sts = statusNotFound( ERRMSG_INVALID_OPERATION );
            }
        }
        if( response == null ) {
            response = new CredentialAssignmentResponse();
            response.setStatus(sts);
        }
        return response;
    }

}
