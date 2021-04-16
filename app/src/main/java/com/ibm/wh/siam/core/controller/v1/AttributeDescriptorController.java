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
import com.ibm.wh.siam.core.request.attribute.AttributeDescriptorSaveRequest;
import com.ibm.wh.siam.core.response.attribute.AttributeDescriptorListResponse;
import com.ibm.wh.siam.core.response.attribute.AttributeDescriptorResponse;
import com.ibm.wh.siam.core.response.attribute.UniqueAttributeNameResponse;
import com.ibm.wh.siam.core.service.AttributeDescriptorService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class AttributeDescriptorController
extends BaseSiamController
{
    @Resource
    AttributeDescriptorService attributeDescriptorSvc;

    private static final Logger logger = LoggerFactory.getLogger( AttributeDescriptorController.class );

    @GetMapping("/attributeDescriptors")
    public AttributeDescriptorListResponse getAllDescriptors() {
        if( logger.isInfoEnabled() ) {
            logger.info("getAllDescriptors");
        }
        return attributeDescriptorSvc.findAll();
    }

    @GetMapping("/attributeDescriptor/id/{id}")
    public @ResponseBody AttributeDescriptorResponse getById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String descriptorId )
    {
        return attributeDescriptorSvc.findById( descriptorId );
    }

    @GetMapping("/attributeDescriptor/name/{name}")
    public @ResponseBody AttributeDescriptorResponse getByName(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="name", required=true ) String attributeName )
    {
        return attributeDescriptorSvc.findByName( attributeName );
    }

    @PostMapping(
            path = "/attributeDescriptor",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AttributeDescriptorResponse saveAttributeDescriptor(
        @RequestHeader HttpHeaders headers,
        @RequestBody AttributeDescriptorSaveRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("saveAttributeDescriptor...");
            logger.info("req=" + req );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            AttributeDescriptorResponse response =
                    attributeDescriptorSvc.save( recordUpdater, req);
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

    @DeleteMapping( "/attributeDescriptor/id/{id}")
    public @ResponseBody AttributeDescriptorResponse deleteById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String descriptorId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById...");
            logger.info("descriptorId=" + descriptorId );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            AttributeDescriptorResponse response = attributeDescriptorSvc.deleteById(recordUpdater, descriptorId);
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

    @GetMapping("/attributeMap")
    public UniqueAttributeNameResponse getAttributeMap() {
        if( logger.isInfoEnabled() ) {
            logger.info("getAttributeMap");
        }
        return attributeDescriptorSvc.fetchAttributeNameMap();
    }

}
