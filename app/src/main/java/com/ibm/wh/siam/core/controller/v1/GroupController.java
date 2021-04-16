/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.controller.v1;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.exc.UsernameException;
import com.ibm.wh.siam.core.request.group.GroupAssignmentRequest;
import com.ibm.wh.siam.core.request.group.GroupListAssignmentRequest;
import com.ibm.wh.siam.core.request.group.GroupSaveRequest;
import com.ibm.wh.siam.core.response.group.GroupAssignmentResponse;
import com.ibm.wh.siam.core.response.group.GroupListAssignmentResponse;
import com.ibm.wh.siam.core.response.group.GroupListResponse;
import com.ibm.wh.siam.core.response.group.GroupResponse;
import com.ibm.wh.siam.core.response.group.OrganizationGroupListResponse;
import com.ibm.wh.siam.core.service.GroupService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class GroupController
extends BaseSiamController
{
    @Resource
    GroupService groupSvc;

    private static final Logger logger = LoggerFactory.getLogger( GroupController.class );

    @GetMapping("/group/code/{code}")
    public @ResponseBody GroupResponse getByCode(
        @PathVariable( name="code", required=true ) String groupCode )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getByCode...");
            logger.info("groupCode=" + groupCode );
        }
        return groupSvc.findByCode( groupCode );
    }

    @GetMapping("/group/id/{id}")
    public @ResponseBody GroupResponse getById(
        @PathVariable( name="id", required=true ) String groupId )
    {
        return groupSvc.findById( groupId );
    }

    @GetMapping("/groups/organization/{id}")
    public OrganizationGroupListResponse getOrganizationGroups(
            @PathVariable( name="id", required=true ) String orgId )
    {
        return groupSvc.findByOrganizationId( orgId );
    }

    @GetMapping("/groups/accountGroup/{acctGroup}")
    public GroupListResponse getByAccountGroups(
            @PathVariable( name="acctGroup", required=true ) String acctGroup )
    {
        return groupSvc.findByAccountGroup(acctGroup);
    }

    @PostMapping(
            path = "/group",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody GroupResponse saveGroup(
            @RequestHeader HttpHeaders headers,
            @RequestBody GroupSaveRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("saveGroup...");
            logger.info("req=" + req );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            GroupResponse response = groupSvc.save( recordUpdater, req);
            if( logger.isInfoEnabled() ) {
                logger.info("response=" + response);
            }
            return response;
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @DeleteMapping( "/group/id/{id}")
    public @ResponseBody GroupResponse deleteById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String grpId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById...");
            logger.info("grpId=" + grpId );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            GroupResponse response = groupSvc.deleteById(recordUpdater, grpId);
            if( logger.isInfoEnabled() ) {
                logger.info("response=" + response );
            }
            return response;
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PostMapping( path = "/group/assign",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public @ResponseBody GroupAssignmentResponse assignGroup(
            @RequestHeader HttpHeaders headers,
            @RequestBody GroupAssignmentRequest request )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("assignGroup...");
            logger.info( "request=" + request );
        }
        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            return groupSvc.assignGroup(recordUpdater, request );
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PutMapping( path = "/group/unassign",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public @ResponseBody GroupAssignmentResponse unassignGroup(
            @RequestHeader HttpHeaders headers,
            @RequestBody GroupAssignmentRequest request )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("unassignGroup...");
            logger.info( "request=" + request );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            return groupSvc.unassignGroup(recordUpdater, request);
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PostMapping( path = "/groups/assign",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public @ResponseBody GroupListAssignmentResponse assignGroups(
            @RequestHeader HttpHeaders headers,
            @RequestBody GroupListAssignmentRequest request )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("assignGroups...");
            logger.info( "request=" + request );
        }
        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            return groupSvc.assignGroup(recordUpdater, request );
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PutMapping( path = "/groups/unassign",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public @ResponseBody GroupListAssignmentResponse unassignGroups(
            @RequestHeader HttpHeaders headers,
            @RequestBody GroupListAssignmentRequest request )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("unassignGroups...");
            logger.info( "request=" + request );
        }
        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            return groupSvc.unassignGroup(recordUpdater, request );
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

}
