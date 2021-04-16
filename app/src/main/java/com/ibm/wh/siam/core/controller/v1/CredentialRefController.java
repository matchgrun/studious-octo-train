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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.exc.UsernameException;
import com.ibm.wh.siam.core.request.credential.CredentialRefSaveRequest;
import com.ibm.wh.siam.core.response.credential.CredentialRefListResponse;
import com.ibm.wh.siam.core.response.credential.CredentialRefResponse;
import com.ibm.wh.siam.core.response.credential.GroupCredentialListResponse;
import com.ibm.wh.siam.core.service.CredentialService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class CredentialRefController
extends BaseSiamController
{
    @Resource
    CredentialService credentialSvc;

    private static final Logger logger = LoggerFactory.getLogger( CredentialRefController.class );

    @GetMapping("/credential/id/{id}")
    public @ResponseBody CredentialRefResponse getById(
        @PathVariable( name="id", required=true ) String groupId )
    {
        return credentialSvc.findById( groupId );
    }

    @GetMapping("/credentials/lastName/{name}")
    public CredentialRefListResponse getCredentialsByLastName(
            @PathVariable( name="name", required=true ) String lastName )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getCredentialsByLastName()...");
            logger.info("lastName=" + lastName );
        }

        return credentialSvc.findByLastName(lastName);
    }

    @GetMapping("/credentials/user/{id}")
    public CredentialRefListResponse getCredentialsByUserId(
            @PathVariable( name="id", required=true ) String userId )
    {
        return credentialSvc.findByUserId(userId);
    }

    @GetMapping("/credentials/email/{email}")
    public CredentialRefListResponse getCredentialsByEmail(
            @PathVariable( name="email", required=true ) String emailAddress )
    {
        return credentialSvc.findByEmail(emailAddress);
    }

    @GetMapping("/credentials/phone/{phone}")
    public CredentialRefListResponse getCredentialsByTelephoneNumber(
            @PathVariable( name="phone", required=true ) String phoneNumber )
    {
        return credentialSvc.findByTelephone(phoneNumber);
    }

    @GetMapping("/credentials/group/{id}")
    public GroupCredentialListResponse getCredentialsByGroup(
            @PathVariable( name="id", required=true ) String groupId )
    {
        return credentialSvc.findByGroupId(groupId);
    }

    @PostMapping(
            path = "/credential",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CredentialRefResponse saveOrganization(
            @RequestHeader HttpHeaders headers,
            @RequestBody CredentialRefSaveRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("saveOrganization...");
            logger.info("req=" + req );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            CredentialRefResponse response = credentialSvc.save(recordUpdater, req);
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

    @DeleteMapping( "/credential/id/{id}")
    public @ResponseBody CredentialRefResponse deleteById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String credentialId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById...");
            logger.info("credentialId=" + credentialId );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            CredentialRefResponse response = credentialSvc.deleteById(recordUpdater, credentialId);
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

}
