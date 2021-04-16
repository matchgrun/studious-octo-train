/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.controller.v1;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.wh.siam.core.response.owner.OwnerDetailResponse;
import com.ibm.wh.siam.core.response.owner.OwnerTypeResponse;
import com.ibm.wh.siam.core.service.OwnerService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class SiamCoreController
extends BaseSiamController
{
    @Resource
    OwnerService ownerSvc;

    private static final Logger logger = LoggerFactory.getLogger( SiamCoreController.class );

    @GetMapping("/owner/id/{id}")
    public @ResponseBody OwnerTypeResponse getOwnerType(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String ownerId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getOwnerType()");
            logger.info("ownerId=" + ownerId );
        }

        return ownerSvc.findOwnerType(ownerId);
    }

    @GetMapping("/owner/details/{id}")
    public @ResponseBody OwnerDetailResponse getOwnerDetails(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String ownerId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getOwnerDetails()");
            logger.info("ownerId=" + ownerId );
        }

        return ownerSvc.findOwnerDetail(ownerId);
    }

}
