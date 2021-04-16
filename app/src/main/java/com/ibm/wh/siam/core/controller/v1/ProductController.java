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
import com.ibm.wh.siam.core.request.product.ProductSaveRequest;
import com.ibm.wh.siam.core.response.product.ProductListResponse;
import com.ibm.wh.siam.core.response.product.ProductRangeListResponse;
import com.ibm.wh.siam.core.response.product.ProductResponse;
import com.ibm.wh.siam.core.service.ProductService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class ProductController
extends BaseSiamController
{

    @Resource
    ProductService<?> productSvc;

    private static final Logger logger = LoggerFactory.getLogger( ProductController.class );

    @GetMapping("/products")
    public ProductListResponse getAllProducts() {
        return productSvc.findAll();
    }

    @GetMapping("/product/id/{id}")
    public @ResponseBody ProductResponse getById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String prodId )
    {
        return productSvc.findById( prodId );
    }

    @GetMapping("/product/code/{code}")
    public @ResponseBody ProductResponse getByCode(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="code", required=true ) String prodCode )
    {
        return productSvc.findByCode( prodCode );
    }

    @PostMapping(
            path = "/product",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ProductResponse saveProduct(
        @RequestHeader HttpHeaders headers,
        @RequestBody ProductSaveRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("saveProduct...");
            logger.info("req=" + req );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            ProductResponse response = productSvc.save( recordUpdater, req);
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

    @DeleteMapping( "/product/id/{id}")
    public @ResponseBody ProductResponse deleteById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String prodId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById...");
            logger.info("prodId=" + prodId );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            ProductResponse response = productSvc.deleteById(recordUpdater, prodId);
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

    @GetMapping("/products/active")
    public ProductRangeListResponse getAllActiveProducts() {
        return productSvc.findAllActiveProducts();
    }

}
