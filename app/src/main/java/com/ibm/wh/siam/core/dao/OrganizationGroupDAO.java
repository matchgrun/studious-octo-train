/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import java.util.List;
import java.util.Map;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.OrganizationGroup;

/**
 * @author Match Grun
 *
 */
public interface OrganizationGroupDAO {

    public OrganizationGroup findById( final String orgGroupId );
    public OrganizationGroup findByIds( final String orgId, final String groupId );
    public Iterable<OrganizationGroup> findByOrganizationId( final String orgId );
    public Iterable<OrganizationGroup> findByGroupId( final String groupId );

    public OrganizationGroup insert( final RecordUpdaterIF recordUpdater, final OrganizationGroup orgGroup );
    public OrganizationGroup deleteById( final RecordUpdaterIF recordUpdater, final OrganizationGroup orgGroup );

    public Map<String, String> findMapGroupId( final String orgId );

    public void insertBulk( final RecordUpdaterIF recordUpdater, final String orgId, final List<String> listGroupId );
    public void deleteBulk( final RecordUpdaterIF recordUpdater, final String orgId, final List<String> listOrgGroupId );

}
