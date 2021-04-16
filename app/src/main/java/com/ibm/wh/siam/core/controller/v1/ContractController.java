/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.controller.v1;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.wh.siam.core.response.ContractListResponse;
import com.ibm.wh.siam.core.response.ContractResponse;
import com.ibm.wh.siam.core.service.ContractService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class ContractController
{
    @Resource
    ContractService contractSvc;

    private static final Logger logger = LoggerFactory.getLogger( ContractController.class );

    @GetMapping("/contract/id/{id}")
    public @ResponseBody ContractResponse getById(
        @PathVariable( name="id", required=true ) String contractId )
    {
        logger.info("getById()");
        if( logger.isInfoEnabled() ) {
            logger.info( "contractId=" + contractId );
        }
        ContractResponse response= contractSvc.findById( contractId );
        if( logger.isInfoEnabled() ) {
            logger.info( "contract=" + response );
        }
        return response;
    }

    @GetMapping("/contract/code/{code}")
    public ContractListResponse getByCode(
            @PathVariable( name="code", required=true ) String contractCode )
    {
        return contractSvc.findByCode(contractCode);
    }

    @GetMapping("/contracts/organization/{id}")
    public ContractListResponse getOrganizationContracts(
            @PathVariable( name="id", required=true ) String orgId )
    {
        return contractSvc.findByOrganizationId( orgId );
    }

}
