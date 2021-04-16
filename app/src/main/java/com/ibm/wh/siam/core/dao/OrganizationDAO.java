package com.ibm.wh.siam.core.dao;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.Organization;

public interface OrganizationDAO {
    public Organization findById( final String orgId );
    public Organization findByCode( final String orgCode );
    public Organization findByAccount( final String acctNum );
    public Iterable<Organization> findByAccountGroup( final String acctGroup );

    public Organization insert( RecordUpdaterIF recordUpdater, Organization org );
    public Organization updateById( RecordUpdaterIF recordUpdater, Organization org );
    public Organization deleteById( RecordUpdaterIF recordUpdater, Organization org );
}
