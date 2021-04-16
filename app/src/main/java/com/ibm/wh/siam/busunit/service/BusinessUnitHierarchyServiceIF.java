/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.service;

import com.ibm.wh.siam.busunit.response.OrganizationRelationshipListResponse;
import com.ibm.wh.siam.busunit.response.tree.OrganizationAncestryResponse;
import com.ibm.wh.siam.busunit.response.tree.OrganizationSubTreeResponse;

/**
 * @author Match Grun
 *
 */
public interface BusinessUnitHierarchyServiceIF {

    public void buildOrganizationTree();

    public OrganizationRelationshipListResponse findByOrganizationId( final String organizationId );
    public OrganizationAncestryResponse findAncestorsForOrganization( final String organizationId );
    public OrganizationSubTreeResponse findSubTreeNodesForOrganization( final String organizationId );

}
