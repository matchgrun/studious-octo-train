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
import com.ibm.wh.siam.core.request.content.ContentSetSaveRequest;
import com.ibm.wh.siam.core.response.content.AccessibleContentSetDetallsResponse;
import com.ibm.wh.siam.core.response.content.ContentSetDetailResponse;
import com.ibm.wh.siam.core.response.content.ContentSetListResponse;
import com.ibm.wh.siam.core.response.content.ContentSetResponse;
import com.ibm.wh.siam.core.service.ContentSetService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class ContentSetController
extends BaseSiamController
{
    @Resource
    ContentSetService contentSetSvc;

    private static final Logger logger = LoggerFactory.getLogger( ContentSetController.class );

    @GetMapping("/contentSets")
    public @ResponseBody ContentSetListResponse getContentSets(
        @RequestHeader HttpHeaders headers )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getContentSets...");
        }
        return contentSetSvc.findAll();
    }

    @GetMapping("/contentSet/id/{id}")
    public @ResponseBody ContentSetResponse getByContentSetId(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String contentSetId )
    {
        return contentSetSvc.findById( contentSetId );
    }

    @GetMapping("/contentSets/descriptor/{descriptor}")
    public @ResponseBody ContentSetListResponse getByDescriptorId(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="descriptor", required=true ) String descriptorId )
    {
        return contentSetSvc.findByDescriptor( descriptorId );
    }

    @GetMapping("/contentSets/owner/{ownerId}")
    public @ResponseBody ContentSetListResponse getContentSetsByOwnerId(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="ownerId", required=true ) String ownerId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getAttributesByOwnerId...");
            logger.info("ownerId=" + ownerId );
        }
        return contentSetSvc.findContentSetListForOwner(ownerId);
    }

    @PostMapping(
            path = "/contentSet",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ContentSetResponse saveContentSet(
        @RequestHeader HttpHeaders headers,
        @RequestBody ContentSetSaveRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("saveContentSet...");
            logger.info("req=" + req );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            ContentSetResponse response = contentSetSvc.save( recordUpdater, req );
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

    @DeleteMapping( "/contentSet/id/{id}")
    public @ResponseBody ContentSetResponse deleteById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String contentSetId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById...");
            logger.info("contentSetId=" + contentSetId );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            ContentSetResponse response = contentSetSvc.deleteById(recordUpdater, contentSetId);
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

    @GetMapping("/contentSet/id/detail/{id}")
    public @ResponseBody ContentSetDetailResponse getByContentSetDetailId(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String contentSetId )
    {
        return contentSetSvc.findContentSetDetailsById( contentSetId );
    }

    @GetMapping("/contentSets/accessor/{accessorId}")
    public @ResponseBody AccessibleContentSetDetallsResponse getContentSetsByAccessorId(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="accessorId", required=true ) String accessorId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getContentSetsByAccessorId...");
            logger.info("accessorId=" + accessorId );
        }
        return contentSetSvc.findContentSetByAccessor(accessorId);
    }



}
