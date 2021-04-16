/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.controller.v1;

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
import com.ibm.wh.siam.core.controller.v1.BaseSiamController;
import com.ibm.wh.siam.core.exc.UsernameException;
import com.ibm.wh.siam.core.response.EntitlementListResponse;
import com.ibm.wh.siam.core.response.EntitlementResponse;
import com.ibm.wh.siam.entitlement.request.EntitlementAddRequest;
import com.ibm.wh.siam.entitlement.request.EntitlementCurrentRequest;
import com.ibm.wh.siam.entitlement.request.EntitlementModifyRequest;
import com.ibm.wh.siam.entitlement.request.EntitlementSaveRequest;
import com.ibm.wh.siam.entitlement.response.EntitlementAddResponse;
import com.ibm.wh.siam.entitlement.response.EntitlementCurrentResponse;
import com.ibm.wh.siam.entitlement.response.EntitlementModifyResponse;
import com.ibm.wh.siam.entitlement.response.EntitlementSaveResponse;
import com.ibm.wh.siam.entitlement.service.EntitlementService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class EntitlementController
extends BaseSiamController
{
    @Resource
    EntitlementService entitlementSvc;

    private static final Logger logger = LoggerFactory.getLogger( EntitlementController.class );

    @GetMapping("/entitlement/id/{id}")
    public @ResponseBody EntitlementResponse getById(
        @PathVariable( name="id", required=true ) String entitlementID )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getById()");
            logger.info( "entitlementID=" + entitlementID );
        }
        return entitlementSvc.findById( entitlementID );
    }


    @GetMapping("/entitlements/organization/{id}")
    public EntitlementListResponse getOrganizationEntitlements(
            @PathVariable( name="id", required=true ) String orgId )
    {
        return entitlementSvc.findByOrganizationId( orgId );
    }

    @GetMapping("/entitlements/owner/{ownerType}/{ownerId}")
    public EntitlementListResponse getOwnerEntitlements(
            @PathVariable( name="ownerType", required=true ) String ownerType,
            @PathVariable( name="ownerId", required=true ) String ownerId )
    {
        return entitlementSvc.findByOwner(ownerType, ownerId);
    }

    @PostMapping(
            path = "/entitlements/raw",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody EntitlementSaveResponse saveEntitlements(
            @RequestHeader HttpHeaders headers,
            @RequestBody EntitlementSaveRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("saveEntitlements...");
            logger.info("req=" + req );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            EntitlementSaveResponse response = entitlementSvc.save(recordUpdater, req);
            /*
            if( logger.isInfoEnabled() ) {
                logger.info("response=" + response);
            }
            */
            return response;
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PostMapping(
            path = "/entitlements",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody EntitlementAddResponse addEntitlements(
            @RequestHeader HttpHeaders headers,
            @RequestBody EntitlementAddRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("addEntitlements...");
            logger.info("req=" + req );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            EntitlementAddResponse response = entitlementSvc.addEntitlements(recordUpdater, req);
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

    @PutMapping(
            path = "/entitlements",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody EntitlementModifyResponse modifyEntitlements(
            @RequestHeader HttpHeaders headers,
            @RequestBody EntitlementModifyRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("modifyEntitlements...");
            logger.info("req=" + req );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            EntitlementModifyResponse response = entitlementSvc.modifyEntitlements(recordUpdater, req);
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

    @DeleteMapping( "/entitlement/id/{id}")
    public @ResponseBody EntitlementResponse deleteEntitlementById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String entitlementId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteEntitlementById...");
            logger.info("entitlementId=" + entitlementId );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            EntitlementResponse response = entitlementSvc.deleteById(recordUpdater, entitlementId);
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

    @PostMapping(
            path = "/entitlements/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody EntitlementCurrentResponse getCurrentEntitlements(
            @RequestBody EntitlementCurrentRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getCurrentEntitlements...");
            logger.info("req=" + req );
        }

        EntitlementCurrentResponse response = entitlementSvc.fetchCurrentEntitlements(req);
        /*
        if( logger.isInfoEnabled() ) {
            logger.info("response=" + response);
        }
        */
        return response;
    }

}
