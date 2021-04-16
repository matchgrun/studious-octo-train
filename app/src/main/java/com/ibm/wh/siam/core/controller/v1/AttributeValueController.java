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
import com.ibm.wh.siam.core.request.attribute.AttributeValueSaveRequest;
import com.ibm.wh.siam.core.response.attribute.AttributeValueSaveResponse;
import com.ibm.wh.siam.core.response.attribute.OwnerAttributeValueListResponse;
import com.ibm.wh.siam.core.response.attribute.OwnerAttributeValueResponse;
import com.ibm.wh.siam.core.response.attribute.OwnersWithAttributeNameResponse;
import com.ibm.wh.siam.core.service.AttributeValueService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class AttributeValueController
extends BaseSiamController
{
    @Resource
    AttributeValueService attributeValueSvc;

    private static final Logger logger = LoggerFactory.getLogger( AttributeValueController.class );

    @GetMapping("/attributes/owner/{ownerId}")
    public @ResponseBody OwnerAttributeValueResponse getAttributesByOwnerId(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="ownerId", required=true ) String ownerId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getAttributesByOwnerId...");
            logger.info("ownerId=" + ownerId );
        }
        return attributeValueSvc.findForOwnerId(ownerId);
    }

    @GetMapping("/attributes/owner/{ownerId}/descriptorId/{descriptorId}")
    public @ResponseBody OwnerAttributeValueResponse getAttributesForOwnerById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="ownerId", required=true ) String ownerId,
        @PathVariable( name="descriptorId", required=true ) String descriptorId
        )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getAttributesForOwnerById...");
            logger.info("ownerId=" + ownerId );
            logger.info("descriptorId=" + descriptorId );
        }
        return attributeValueSvc.findForOwnerByDescriptorId(ownerId, descriptorId);
    }

    @GetMapping("/attributes/owner/{ownerId}/descriptorName/{name}")
    public @ResponseBody OwnerAttributeValueResponse getAttributesForOwnerByName(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="ownerId", required=true ) String ownerId,
        @PathVariable( name="name", required=true ) String descriptorName
        )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getAttributesForOwnerByName...");
            logger.info("ownerId=" + ownerId );
            logger.info("descriptorName=" + descriptorName );
        }
        return attributeValueSvc.findForOwnerByDescriptorName(ownerId, descriptorName);
    }

    @GetMapping("/attributeValues/owner/{ownerId}")
    public @ResponseBody OwnerAttributeValueListResponse getAttributeListByOwner(
            @RequestHeader HttpHeaders headers,
            @PathVariable( name="ownerId", required=true ) String ownerId )
    {

        if( logger.isInfoEnabled() ) {
            logger.info("getAttributeListByOwner...");
            logger.info("ownerId=" + ownerId );
        }
        return attributeValueSvc.findAttributeListForOwner(ownerId);
    }

    @GetMapping("/attributeValues/owner/{ownerId}/descriptorId/{descriptorId}")
    public @ResponseBody OwnerAttributeValueListResponse getAttributeListForOwnerById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="ownerId", required=true ) String ownerId,
        @PathVariable( name="descriptorId", required=true ) String descriptorId
        )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getAttributeListForOwnerById...");
            logger.info("ownerId=" + ownerId );
            logger.info("descriptorId=" + descriptorId );
        }
        return attributeValueSvc.findAttributeListForOwnerById(ownerId, descriptorId);
    }

    @GetMapping("/attributeValues/owner/{ownerId}/descriptorName/{name}")
    public @ResponseBody OwnerAttributeValueListResponse getAttributeListForOwnerByName(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="ownerId", required=true ) String ownerId,
        @PathVariable( name="name", required=true ) String descriptorName
        )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getAttributeListForOwnerByName...");
            logger.info("ownerId=" + ownerId );
            logger.info("descriptorName=" + descriptorName );
        }
        return attributeValueSvc.findAttributeListForOwnerByName(ownerId, descriptorName);
    }

    @PostMapping(
            path = "/attributeValues",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AttributeValueSaveResponse saveAttributeValues(
        @RequestHeader HttpHeaders headers,
        @RequestBody AttributeValueSaveRequest request )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("saveAttributeValues...");
            logger.info("Request=" + request );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            AttributeValueSaveResponse response = attributeValueSvc.save( recordUpdater, request );
            if( logger.isInfoEnabled() ) {
                logger.info("response=" + response);
            }
            return response;
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                e.getMessage(),
                e );
        }
    }

    @GetMapping("/attributeOwners/ownerType/{ownerType}/descriptorName/{name}")
    public @ResponseBody OwnersWithAttributeNameResponse getOwnersWithAttributeName(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="ownerType", required=true ) String ownerType,
        @PathVariable( name="name", required=true ) String descriptorName
        )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getAttributeListForOwnerByName...");
            logger.info("ownerType=" + ownerType );
            logger.info("descriptorName=" + descriptorName );
        }
        return attributeValueSvc.findOwnersWithAttributeName(ownerType, descriptorName);
    }


}
