/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.tree;

import java.util.List;

import com.ibm.wh.siam.common.tree.SingleParentNode;
import com.ibm.wh.siam.common.tree.SingleParentTree;
import com.ibm.wh.siam.core.dao.dto.ParentChildNode;

/**
 * @author Match Grun
 *
 */
public interface OrganizationTreeManagerIF {

    /**
     * Return single parent tree.
     * @return Tree.
     */
    public SingleParentTree getSingleParentTree();

    /**
     * Build business unit tree as single parent tree.
     * @return Single parent tree.
     */
    public SingleParentTree buildOrganizationTree( final List<ParentChildNode> listNodes );

    /**
     * Retrieve node from tree for specified organization ID.
     * @param organizationId Organization ID.
     * @return Single parent node, or <i>null</i> if not found..
     */
    public SingleParentNode fetchOrganizationNode( final String organizationId );

    /**
     * Return ancestry for specified organization.
     * @param organizationId Organization ID.
     * @return List of ancestors nodes.
     */
    public List<SingleParentNode> fetchAncestors( final String organizationId );

    /**
     * Fetch all nodes from tree below specified organization.
     * @param organizationId    Organization Id.
     * @return List of descendant nodes.
     */
    public List<SingleParentNode> fetchSubTreeNodes( final String organizationId );

}
