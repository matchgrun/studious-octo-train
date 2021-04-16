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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.exc.UsernameException;
import com.ibm.wh.siam.core.request.content.ContentSetAccessSaveRequest;
import com.ibm.wh.siam.core.response.content.ContentSetAccessResponse;
import com.ibm.wh.siam.core.response.content.ContentSetAccessSaveResponse;
import com.ibm.wh.siam.core.response.content.ContentSetAccessorDetailsResponse;
import com.ibm.wh.siam.core.response.content.ContentSetAccessorResponse;
import com.ibm.wh.siam.core.service.ContentSetAccessService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class ContentSetAccessController
extends BaseSiamController
{
    @Resource
    ContentSetAccessService contentSetAccessSvc;

    private static final Logger logger = LoggerFactory.getLogger( ContentSetAccessController.class );

    @GetMapping("/contentSetAccess/id/{id}")
    public @ResponseBody ContentSetAccessResponse getByContentSetAccessId(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String contentSetAccessId )
    {
        return contentSetAccessSvc.findById( contentSetAccessId );
    }

    @GetMapping("/contentSetAccess/accessor/{id}")
    public @ResponseBody ContentSetAccessorResponse getAccessorsByContentSetId(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String contentSetId )
    {
        return contentSetAccessSvc.findAccessorsByContentSetId( contentSetId );
    }

    @GetMapping("/contentSetAccess/accessor/details/{id}")
    public @ResponseBody ContentSetAccessorDetailsResponse getAccessorDetailsByContentSetId(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String contentSetId )
    {
        return contentSetAccessSvc.findAccessorDetailsByContentSetId( contentSetId );
    }

    @PutMapping(
            path = "/contentSetAccess/assign",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ContentSetAccessSaveResponse assignAccessor(
        @RequestHeader HttpHeaders headers,
        @RequestBody ContentSetAccessSaveRequest contentSetAccessSaveRequest )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("assignAccessor...");
            logger.info("contentSetAccessSaveRequest=\n" + contentSetAccessSaveRequest );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            ContentSetAccessSaveResponse response =
                    contentSetAccessSvc.assignAccessor(recordUpdater, contentSetAccessSaveRequest );
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

    @DeleteMapping( "/contentSetAccess/id/{contentSetId}/remove/{accessId}")
    public @ResponseBody ContentSetAccessSaveResponse removeAccessor(
            @RequestHeader HttpHeaders headers,
            @PathVariable( name="contentSetId", required=true ) String contentSetId,
            @PathVariable( name="accessId", required=true ) String contentSetAccessId )
        {
            if( logger.isInfoEnabled() ) {
                logger.info("removeAccessor...");
            }

            try {
                RecordUpdaterIF recordUpdater = fetchUpdater(headers);
                ContentSetAccessSaveResponse response =
                        contentSetAccessSvc.removeAccessor(
                                recordUpdater,
                                contentSetId,
                                contentSetAccessId );
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

}
