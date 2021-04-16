/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.CommonConstants;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.GroupDAO;
import com.ibm.wh.siam.core.dao.OrganizationDAO;
import com.ibm.wh.siam.core.dao.OrganizationGroupDAO;
import com.ibm.wh.siam.core.dto.Group;
import com.ibm.wh.siam.core.dto.Organization;
import com.ibm.wh.siam.core.dto.OrganizationGroup;
import com.ibm.wh.siam.core.exc.ValidationException;
import com.ibm.wh.siam.core.request.group.GroupAssignmentItem;
import com.ibm.wh.siam.core.request.group.GroupAssignmentList;
import com.ibm.wh.siam.core.request.group.GroupAssignmentRequest;
import com.ibm.wh.siam.core.request.group.GroupListAssignmentRequest;
import com.ibm.wh.siam.core.request.group.GroupSaveRequest;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.group.GroupAssignmentResponse;
import com.ibm.wh.siam.core.response.group.GroupListAssignmentResponse;
import com.ibm.wh.siam.core.response.group.GroupListResponse;
import com.ibm.wh.siam.core.response.group.GroupResponse;
import com.ibm.wh.siam.core.response.group.OrganizationGroupListResponse;
import com.ibm.wh.siam.core.service.GroupService;
import com.ibm.wh.siam.core.util.AssignmentUtil;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;

/**
 * @author Match Grun
 *
 */
@Component
public class GroupServiceImpl
extends BaseSiamService
implements GroupService
{
    private static final String ERRMSG_NOT_FOUND = "Group not Found.";
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Group.";
    private static final String ERRMSG_NOT_FOUND_ORGANIZATION = "Organization not Found.";
    private static final String ERRMSG_ALEADY_ASSIGNED = "Group already assigned.";
    private static final String ERRMSG_NOT_ASSIGNED = "Group not currently assigned.";
    private static final String ERRMSG_NOT_SPECIFIED = "Group not Specified.";

    private static final String ERRMSG_INVALID_OPERATION = "Operation not Recognized.";

    @Resource
    GroupDAO groupDao;

    @Resource
    OrganizationDAO organizationDao;

    @Resource
    OrganizationGroupDAO organizationGroupDao;

    private static final Logger logger = LoggerFactory.getLogger( GroupServiceImpl.class );

    @Override
    public GroupResponse findByCode(final String groupCode) {
        ResponseStatus sts = null;
        GroupResponse response = new GroupResponse();
        Group grp = groupDao.findByCode(groupCode);
        response.setGroup(grp);
        if( grp == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public GroupResponse findById(final String groupId) {
        ResponseStatus sts = null;
        GroupResponse response = new GroupResponse();
        Group grp = groupDao.findById(groupId);
        response.setGroup(grp);
        if( grp == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public GroupResponse save(
            final RecordUpdaterIF recordUpdater,
            final Group objToSave )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("save()");
            logger.info( "objToSave=" + objToSave );
        }

        GroupResponse response = new GroupResponse();
        try {
            validateGroup(objToSave);
        }
        catch (ValidationException e) {
            if( logger.isInfoEnabled() ) {
                logger.error( "Invalid objToSave=" + objToSave );
            }
            response.setStatus( statusInvalid( ERRMSG_INVALID_OBJECT ) );
            response.setGroup(objToSave);
            return response;
        }

        String objId = objToSave.getGroupId();
        Group objSaved = null;
        Group objFound = groupDao.findByCode(objId);
        if( logger.isInfoEnabled() ) {
            logger.info( "objFound=" + objFound );
        }

        // Check whether there is another organization with the same code
        String objCheck = objToSave.getGroupCode();
        Group objOther = groupDao.findByCode(objCheck);
        if( logger.isInfoEnabled() ) {
            logger.info( "objOther=" + objOther );
        }

        ResponseStatus sts = null;
        int chk = checkKeys(objToSave, objFound, objOther);
        if( chk == ACTION_DUPLICATE ) {
            sts = statusDuplicateKey();
        }
        else if( chk == ACTION_INSERT ) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Inserting..." );
            }
            try {
                objSaved = groupDao.insert(recordUpdater, objToSave);
                if( objSaved == null ) {
                    sts = statusInsertFailed();
                }
                else {
                    sts = statusSuccess();
                }
            }
            catch (Exception e) {
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
                objSaved = groupDao.updateById(recordUpdater, objToSave);
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
        response.setGroup(objSaved);

        return response;
    }

    @Override
    public GroupResponse deleteById(
            final RecordUpdaterIF recordUpdater,
            final String groupId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "groupId=" + groupId );
        }

        GroupResponse response = new GroupResponse();
        if( StringUtils.isEmpty( groupId ) ) {
            if( logger.isErrorEnabled() ) {
                logger.error( "No groupID." );
            }
            response.setStatus( statusInvalid( ERRMSG_INVALID_OBJECT ) );
            return response;
        }

        ResponseStatus sts = null;
        Group orgDeleted = null;
        Group objFound = groupDao.findById(groupId);
        if( objFound == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            orgDeleted = groupDao.deleteById( recordUpdater, objFound );
            if( orgDeleted == null ) {
                sts = statusDeleteFailed();
            }
            else {
                sts = statusSuccess();
            }
        }

        response.setStatus(sts);
        response.setGroup(orgDeleted);
        return response;
    }

    @Override
    public OrganizationGroupListResponse findByOrganizationId( final String orgId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOrganizationId()");
            logger.info( "Org=" + orgId );
        }
        OrganizationGroupListResponse response = new OrganizationGroupListResponse();
        response.setOrganizationId(orgId);
        Iterable<Group> listGroups = groupDao.findByOrganizationId( orgId );
        response.setListGroups(listGroups);
        response.setStatus(statusSuccess());
        return response;
    }

    @Override
    public GroupListResponse findByAccountGroup( final String acctGroup) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByAccountGroup()");
            logger.info( "acctGroup=" + acctGroup );
        }
        GroupListResponse response = new GroupListResponse();
        Iterable<Group> listGroups = groupDao.findByAccountGroup( acctGroup );
        response.setListGroups(listGroups);
        response.setStatus(statusSuccess());
        return response;
    }

    private void validateGroup( final Group grp )
    throws ValidationException
    {
        if( grp == null ) {
            // Error
            throw new ValidationException( "Group IS-NULL" );
        }
        String grpCode = grp.getGroupCode();
        if( StringUtils.isEmpty(grpCode)) {
            throw new ValidationException( "GroupCode NULL-EMPTY." );
        }
    }

    private int checkKeys(
            final Group objToSave,
            final Group objFound,
            final Group objOther )
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
        String valueCheck = objToSave.getGroupCode();
        if( objFound.getGroupCode().equals( valueCheck ) ) {
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

    protected GroupAssignmentResponse assignGroup(
            final RecordUpdaterIF recordUpdater,
            final String orgId,
            final String groupId )
    {
        ResponseStatus sts = null;
        GroupAssignmentResponse response = new GroupAssignmentResponse();
        response.setOperation( CommonConstants.OPERATION_ASSIGN );

        Group group = null;
        Organization organization = organizationDao.findById(orgId);
        if( organization == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_ORGANIZATION );
        }
        else {
            group = groupDao.findById(groupId);
            if( group == null ) {
                sts = statusNotFound( ERRMSG_NOT_FOUND );
            }
            else {
                OrganizationGroup orgGroup = organizationGroupDao.findByIds(orgId, groupId);
                if( orgGroup != null ) {
                    // Already there
                    sts = statusSuccess();
                    sts.setMessage( ERRMSG_ALEADY_ASSIGNED );
                }
                else {
                    // Insert association.
                    OrganizationGroup objToSave = new OrganizationGroup();
                    objToSave.setGroupId(groupId);
                    objToSave.setOrganizationId(orgId);
                    objToSave.setStatus(CommonConstants.STATUS_ACTIVE);

                    OrganizationGroup grpSaved = organizationGroupDao.insert(recordUpdater, objToSave);
                    if( grpSaved == null ) {
                        sts = statusInsertFailed();
                    }
                    else {
                        response.setGroup(group);
                        response.setOrganization(organization);
                        response.setAssignmentId(grpSaved.getOrganizationGroupId());
                        sts = statusSuccess();
                    }
                }
            }
        }
        response.setStatus(sts);

        return response;
    }

    protected GroupAssignmentResponse unassignGroup(
            final RecordUpdaterIF recordUpdater,
            final String orgId,
            final String groupId)
    {
        ResponseStatus sts = null;
        GroupAssignmentResponse response = new GroupAssignmentResponse();
        response.setOperation(CommonConstants.OPERATION_UNASSIGN);

        Group group = null;
        Organization organization = organizationDao.findById(orgId);
        if( organization == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_ORGANIZATION );
        }
        else {
            group = groupDao.findById(groupId);
            if( group == null ) {
                sts = statusNotFound( ERRMSG_NOT_FOUND );
            }
            else {
                // Find group
                OrganizationGroup objToDelete = organizationGroupDao.findByIds(orgId, groupId);
                if( objToDelete == null ) {
                    // Already removed
                    sts = statusSuccess();
                    sts.setMessage( ERRMSG_NOT_ASSIGNED );
                }
                else {
                    OrganizationGroup objDeleted = organizationGroupDao.deleteById(recordUpdater, objToDelete );
                    if( objDeleted == null ) {
                        sts = statusDeleteFailed();
                    }
                    else {
                        response.setGroup(group);
                        response.setOrganization(organization);
                        response.setAssignmentId(objDeleted.getOrganizationGroupId());
                        sts = statusSuccess();
                    }
                }
            }
        }
        response.setStatus(sts);

        return response;
    }

    /**
     * Validate specified request.
     * @param recordUpdater     Record updater.
     * @param req               Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final RecordUpdaterIF recordUpdater,
            final GroupAssignmentRequest req )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );

        // Validate contained item
        if( req != null ) {
            GroupAssignmentItem grpAssign = req.getGroupItem();
            listErrors.addAll( fv.validate(recordUpdater, grpAssign ) );
        }
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
            final GroupListAssignmentRequest req )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );

        // Validate contained item
        if( req != null ) {
            GroupAssignmentList grpAssign = req.getGroupList();
            listErrors.addAll( fv.validate(recordUpdater, grpAssign ) );
        }
        return listErrors;
    }


    @Override
    public GroupAssignmentResponse assignGroup(
            final RecordUpdaterIF recordUpdater,
            final GroupAssignmentRequest request)
    {
        ResponseStatus sts = null;
        GroupAssignmentResponse response = null;
        List<ValidationError> listErrors = validateRequest(
                recordUpdater,
                request);
        if( listErrors.size() > 0 ) {
            response = new GroupAssignmentResponse();
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        GroupAssignmentItem grpAssign = request.getGroupItem();
        String operation = grpAssign.getOperation();
        if( ! AssignmentUtil.isValid(operation)) {
            sts = statusNotFound( ERRMSG_INVALID_OPERATION );
        }
        else {
            if( operation.equalsIgnoreCase(CommonConstants.OPERATION_ASSIGN)) {
                response = assignGroup(
                        recordUpdater,
                        grpAssign.getOrganizationId(),
                        grpAssign.getGroupId() );
            }
            else {
                sts = statusNotFound( ERRMSG_INVALID_OPERATION );
            }
        }
        if( response == null ) {
            response = new GroupAssignmentResponse();
            response.setStatus(sts);
        }
        return response;
    }

    @Override
    public GroupAssignmentResponse unassignGroup(
            final RecordUpdaterIF recordUpdater,
            final GroupAssignmentRequest request)
    {
        ResponseStatus sts = null;
        GroupAssignmentResponse response = null;

        GroupAssignmentItem grpAssign = request.getGroupItem();
        String operation = grpAssign.getOperation();
        if( ! AssignmentUtil.isValid(operation)) {
            sts = statusNotFound( ERRMSG_INVALID_OPERATION );
        }
        else {
            if( operation.equalsIgnoreCase(CommonConstants.OPERATION_UNASSIGN)) {
                response = unassignGroup(recordUpdater, grpAssign.getOrganizationId(), grpAssign.getGroupId() );
            }
            else {
                sts = statusNotFound( ERRMSG_INVALID_OPERATION );
            }
        }
        if( response == null ) {
            response = new GroupAssignmentResponse();
            response.setStatus(sts);
        }
        return response;
    }

    @Override
    public GroupListAssignmentResponse assignGroup(
            final RecordUpdaterIF recordUpdater,
            final GroupListAssignmentRequest request)
    {
        ResponseStatus sts = null;
        GroupListAssignmentResponse response = null;

        List<ValidationError> listErrors = validateRequest(
                recordUpdater,
                request);
        if( listErrors.size() > 0 ) {
            response = new GroupListAssignmentResponse();
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        GroupAssignmentList grpAssign = request.getGroupList();
        String operation = grpAssign.getOperation();
        if( ! AssignmentUtil.isValid(operation)) {
            sts = statusNotFound( ERRMSG_INVALID_OPERATION );
        }
        else {
            if( operation.equalsIgnoreCase(CommonConstants.OPERATION_ASSIGN)) {
                response =
                        assignGroup(
                                recordUpdater,
                                grpAssign.getOrganizationId(),
                                grpAssign.getListGroups());
            }
            else {
                sts = statusNotFound( ERRMSG_INVALID_OPERATION );
            }
        }
        if( response == null ) {
            response = new GroupListAssignmentResponse();
            response.setStatus(sts);
        }

        return response;
    }

    @Override
    public GroupListAssignmentResponse unassignGroup(
            final RecordUpdaterIF recordUpdater,
            final GroupListAssignmentRequest request)
    {
        ResponseStatus sts = null;
        GroupListAssignmentResponse response = null;

        List<ValidationError> listErrors = validateRequest(
                recordUpdater,
                request);
        if( listErrors.size() > 0 ) {
            response = new GroupListAssignmentResponse();
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        GroupAssignmentList grpAssign = request.getGroupList();
        String operation = grpAssign.getOperation();
        if( ! AssignmentUtil.isValid(operation)) {
            sts = statusNotFound( ERRMSG_INVALID_OPERATION );
        }
        else {
            if( operation.equalsIgnoreCase(CommonConstants.OPERATION_UNASSIGN)) {
                response =
                        unassignGroup(
                                recordUpdater,
                                grpAssign.getOrganizationId(),
                                grpAssign.getListGroups());
            }
            else {
                sts = statusNotFound( ERRMSG_INVALID_OPERATION );
            }
        }
        if( response == null ) {
            response = new GroupListAssignmentResponse();
            response.setStatus(sts);
        }

        return response;
    }

    protected GroupListAssignmentResponse  assignGroup(
            final RecordUpdaterIF recordUpdater,
            final String orgId,
            final List<String> listGroupId )
    {
        ResponseStatus sts = null;
        GroupListAssignmentResponse response = new GroupListAssignmentResponse();
        response.setOperation( CommonConstants.OPERATION_ASSIGN );

        Organization organization = organizationDao.findById(orgId);
        if( organization == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_ORGANIZATION );
        }
        else {
            List<String> listExisting = new ArrayList<String>();
            List<String> listCheck = new ArrayList<String>();

            Map<String, String> mapGroups = organizationGroupDao.findMapGroupId(orgId);
            listGroupId.forEach( groupId -> {
                if( mapGroups.containsKey(groupId) ) {
                    listExisting.add(groupId);
                }
                else {
                    listCheck.add(groupId);
                }
            });

            // Retrieve valid list of ID's
            List<String> listValid = groupDao.verifyGroupIds(listCheck);
            organizationGroupDao.insertBulk(recordUpdater, orgId, listValid);

            // Identify changes
            List<String> listAssigned = new ArrayList<String>();
            List<String> listFailed = new ArrayList<String>();
            Map<String, String> mapAll = organizationGroupDao.findMapGroupId(orgId);
            mapAll.forEach( ( groupId, v ) -> {
                listAssigned.add(groupId);
            });

            listCheck.forEach( groupId -> {
                if( ! mapAll.containsKey(groupId) ) {
                    listFailed.add(groupId);
                }
            });

            response.setOrganization(organization);
            response.setListGroupsExisting(listExisting);
            response.setListGroupsAssigned(listAssigned);
            response.setListGroupsFailed(listFailed);
            sts = statusSuccess();

        }
        response.setStatus(sts);
        return response;
    }

    protected GroupListAssignmentResponse  unassignGroup(
            final RecordUpdaterIF recordUpdater,
            final String orgId,
            final List<String> listGroupId )
    {
        ResponseStatus sts = null;
        GroupListAssignmentResponse response = new GroupListAssignmentResponse();
        response.setOperation( CommonConstants.OPERATION_UNASSIGN );

        Organization organization = organizationDao.findById(orgId);
        if( organization == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_ORGANIZATION );
        }
        else {
            List<String> listValidOrgId = new ArrayList<String>();
            List<String> listExisting= new ArrayList<String>();
            List<String> listFailed = new ArrayList<String>();

            // Identify organization group ID's to be deleted
            Map<String, String> mapGroups = organizationGroupDao.findMapGroupId(orgId);
            listGroupId.forEach( groupId -> {
                String orgGroupId = mapGroups.get(groupId);
                if( orgGroupId != null ) {
                    listValidOrgId.add(orgGroupId);
                    listExisting.add(groupId);
                }
                else {
                    listFailed.add(groupId);
                }
            });

            // Remove groups from organization.
            organizationGroupDao.deleteBulk(recordUpdater, orgId, listValidOrgId);

            // Identify changes
            List<String> listAssigned = new ArrayList<String>();
            Map<String, String> mapAll = organizationGroupDao.findMapGroupId(orgId);
            mapAll.forEach( ( groupId, v ) -> {
                listAssigned.add(groupId);
                if( mapGroups.containsKey(groupId) ) {
                    listFailed.add(groupId);
                }
            });

            response.setOrganization(organization);
            response.setListGroupsExisting(listExisting);
            response.setListGroupsAssigned(listAssigned);
            response.setListGroupsFailed(listFailed);
            sts = statusSuccess();

        }
        response.setStatus(sts);

        return response;
    }

    @Override
    public GroupResponse save(
            final RecordUpdaterIF recordUpdater,
            final GroupSaveRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info( "save()");
            logger.info( "req=" + req );
        }

        GroupResponse response = null;
        List<ValidationError> listErrors = validateRequest( req );
        if( listErrors.size() > 0 ) {
            response = new GroupResponse();
            ResponseStatus sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        Group objToSave = req.getGroup();
        response = save( recordUpdater, objToSave );

        return response;
    }

    /**
     * Validate request object.
     * @param req   Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final GroupSaveRequest req )
    {
        FieldValidator fv = new FieldValidator();
        return fv.validateRequest( req );
    }

}
