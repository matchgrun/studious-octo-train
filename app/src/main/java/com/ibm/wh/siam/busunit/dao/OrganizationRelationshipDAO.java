/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao;

import java.util.List;

import com.ibm.wh.siam.core.dao.dto.ParentChildNode;
import com.ibm.wh.siam.core.dto.OrganizationRelationship;

/**
 * @author Match Grun
 *
 */
public interface OrganizationRelationshipDAO {

    /**
     * Load relationships for specified organization id. Don't know how useful this will be.
     * @param orgID Organization ID.
     * @return  List of relationship types.
     */
    public List<OrganizationRelationship> loadRelationships( final String orgID );

    public List<ParentChildNode> loadNodes();

}
