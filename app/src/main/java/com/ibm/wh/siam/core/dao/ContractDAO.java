/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import com.ibm.wh.siam.core.dto.Contract;

/**
 * @author Match Grun
 *
 */
public interface ContractDAO
{
    public Contract findById( final String contractID );
    public Iterable<Contract> findByCode( final String contractCode );
    public Iterable<Contract> findByOrganizationId( final String orgId );

}
