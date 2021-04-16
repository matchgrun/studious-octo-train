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
import com.ibm.wh.siam.core.request.product.ProductEndpointSaveRequest;
import com.ibm.wh.siam.core.response.product.ProductEndpointListResponse;
import com.ibm.wh.siam.core.response.product.ProductEndpointSaveResponse;
import com.ibm.wh.siam.core.response.product.ProductListEndpointResponse;
import com.ibm.wh.siam.core.service.ProductEndpointService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class ProductEndpointController
extends BaseSiamController
{

    @Resource
    ProductEndpointService productEndpointSvc;

    private static final Logger logger = LoggerFactory.getLogger( ProductEndpointController.class );

    @GetMapping("/product/endpoints/{id}")
    public ProductEndpointListResponse getProductEndpoints(
            @PathVariable( name="id", required=true ) String productId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getProductEndpoints()...");
            logger.info("productId=" + productId );
        }

        return productEndpointSvc.findByProduct(productId);
    }


    @PostMapping(
            path = "/product/endpoints",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ProductEndpointSaveResponse assignEndpoints(
        @RequestHeader HttpHeaders headers,
        @RequestBody ProductEndpointSaveRequest request )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("assignEndpoints...");
            logger.info("Request=" + request );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            ProductEndpointSaveResponse response = productEndpointSvc.save( recordUpdater, request );
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

    @GetMapping("/products/endpoint/{url}")
    public ProductListEndpointResponse getProductsForEndpoint(
            @PathVariable( name="url", required=true ) String endpointUrl )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getProductsForEndpoint()...");
            logger.info("endpointUrl=" + endpointUrl );
        }

        return productEndpointSvc.findProductByEndpointUrl(endpointUrl);
    }

}
