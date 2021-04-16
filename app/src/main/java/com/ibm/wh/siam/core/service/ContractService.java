/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.response.ContractResponse;
import com.ibm.wh.siam.core.response.ContractListResponse;

/**
 * @author Match Grun
 *
 */
public interface ContractService
{
    public ContractResponse findById( final String contractId );
    public ContractListResponse findByCode( final String contractCode );
    public ContractListResponse findByOrganizationId( final String orgId );
}
