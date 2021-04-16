package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.Organization;
import com.ibm.wh.siam.core.request.busentity.OrganizationSaveRequest;
import com.ibm.wh.siam.core.response.OrganizationListResponse;
import com.ibm.wh.siam.core.response.OrganizationResponse;

public interface OrganizationService {
    public OrganizationResponse findByCode( final String orgCode );
    public OrganizationResponse findById( final String orgId );
    public OrganizationResponse findByAccount( final String acctNum);
    public OrganizationListResponse findByAccountGroup( final String acctGroup );
    public OrganizationResponse save( final RecordUpdaterIF recordUpdater, final Organization org );
    public OrganizationResponse deleteById( final RecordUpdaterIF recordUpdater, final String orgId );

    public OrganizationResponse save( final RecordUpdaterIF recordUpdater, final OrganizationSaveRequest req );

}
