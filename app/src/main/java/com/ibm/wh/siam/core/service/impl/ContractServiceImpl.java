/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.wh.siam.core.dao.ContractDAO;
import com.ibm.wh.siam.core.dto.Contract;
import com.ibm.wh.siam.core.response.ContractListResponse;
import com.ibm.wh.siam.core.response.ContractResponse;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.service.ContractService;

/**
 * @author Match Grun
 *
 */
@Component
public class ContractServiceImpl
extends BaseSiamService
implements ContractService
{
    @Resource
    ContractDAO contractDao;

    private static final String ERRMSG_NOT_FOUND = "Contract not Found.";

    private static final Logger logger = LoggerFactory.getLogger( ContractServiceImpl.class );

    @Override
    public ContractResponse findById(final String contractId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "contractId=" + contractId );
        }
        ResponseStatus sts = null;
        ContractResponse response = new ContractResponse();
        Contract  contract = contractDao.findById(contractId);
        response.setContract(contract);
        if( contract == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public ContractListResponse findByOrganizationId( final String orgId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOrganizationId()");
            logger.info( "Org=" + orgId );
        }
        ResponseStatus sts = null;
        ContractListResponse response = new ContractListResponse();
        Iterable<Contract> listContracts = contractDao.findByOrganizationId(orgId);
        response.setListContracts(listContracts);
        if( listContracts == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public ContractListResponse findByCode( final String contractCode ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOrganizationId()");
            logger.info( "contractCode=" + contractCode );
        }
        ResponseStatus sts = null;
        ContractListResponse response = new ContractListResponse();
        Iterable<Contract> listContracts = contractDao.findByCode(contractCode);
        response.setListContracts(listContracts);
        if( listContracts == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

}
