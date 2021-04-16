/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.wh.siam.busunit.common.BusinessUnitTypes;
import com.ibm.wh.siam.busunit.dao.OrganizationRelationshipDAO;
import com.ibm.wh.siam.busunit.response.OrganizationRelationshipListResponse;
import com.ibm.wh.siam.busunit.response.tree.AncestorItem;
import com.ibm.wh.siam.busunit.response.tree.OrganizationAncestryResponse;
import com.ibm.wh.siam.busunit.response.tree.OrganizationSubTreeResponse;
import com.ibm.wh.siam.busunit.response.tree.TreeNodeItem;
import com.ibm.wh.siam.busunit.service.BusinessUnitHierarchyServiceIF;
import com.ibm.wh.siam.busunit.tree.OrganizationTreeManagerIF;
import com.ibm.wh.siam.common.tree.SingleParentNode;
import com.ibm.wh.siam.common.tree.SingleParentTree;
import com.ibm.wh.siam.core.dao.dto.ParentChildNode;
import com.ibm.wh.siam.core.dto.OrganizationRelationship;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.service.impl.BaseSiamService;
/**
 * @author Match Grun
 *
 */
@Component
public class BusinessUnitHierarchyService
extends BaseSiamService
implements BusinessUnitHierarchyServiceIF
{
    private static final Logger logger = LoggerFactory.getLogger( BusinessUnitHierarchyService.class );

    @SuppressWarnings("unused")
    private static final String ERRMSG_NOT_FOUND = "Organization Hierarchy not Found.";
    private static final String ERRMSG_NO_HIERARCHY = "Organization Hierarchy not Defined.";
    private static final String ERRMSG_NO_SUB_TREE = "No SubTree Defined.";

    @Resource
    OrganizationRelationshipDAO relationshipDao;

    @Resource
    OrganizationTreeManagerIF organizationTreeManager;

    @Override
    public OrganizationRelationshipListResponse findByOrganizationId( final String organizationId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOrganizationId()");
            logger.info( "organizationId=" + organizationId );
        }

        OrganizationRelationshipListResponse response = new OrganizationRelationshipListResponse();
        response.setOrganizationId(organizationId);

        List<OrganizationRelationship> list = relationshipDao.loadRelationships(organizationId);
        response.setListRelationships(list);
        response.setStatus(statusSuccess());

        return response;
    }

    @Override
    public void buildOrganizationTree() {
        if( logger.isInfoEnabled() ) {
            logger.info("buildOrganizationTree()");
        }

        SingleParentTree tree = organizationTreeManager.getSingleParentTree();
        if( tree == null ) {
            List<ParentChildNode> listNodes = relationshipDao.loadNodes();
            organizationTreeManager.buildOrganizationTree( listNodes );
        }
    }

    @Override
    public OrganizationAncestryResponse findAncestorsForOrganization( final String organizationId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findAncestorsForOrganization()");
        }

        OrganizationAncestryResponse response = new OrganizationAncestryResponse();
        response.setRelationshipType( "Organization-Hierarchy" );
        response.setOrganizationId( organizationId );

        // Build organization tree
        buildOrganizationTree();

        // Retrieve organization's node
        SingleParentNode nodeOrganization = organizationTreeManager.fetchOrganizationNode(organizationId);
        if( nodeOrganization != null ) {
            response.setRelationshipId( nodeOrganization.getID() );
        }

        // Identify ancestors
        ResponseStatus sts = null;
        List<AncestorItem> listAncestors = new ArrayList<AncestorItem>();
        List<SingleParentNode> listNodes = organizationTreeManager.fetchAncestors(organizationId);
        if( listNodes != null ) {
            listNodes.forEach( node -> {
                AncestorItem item = new AncestorItem();
                item.setBusinessUnitId( node.getID() );
                item.setBusinessUnitType( BusinessUnitTypes.ORGANIZATION );
                listAncestors.add(item);
            });
            sts = statusSuccess();
        }
        else {
            sts = statusNotFound( ERRMSG_NO_HIERARCHY );
        }
        response.setListAncestors(listAncestors);
        response.setStatus(sts);

        return response;
    }

    @Override
    public OrganizationSubTreeResponse findSubTreeNodesForOrganization( final String organizationId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findSubTreeNodesForOrganization()");
        }

        OrganizationSubTreeResponse response = new OrganizationSubTreeResponse();
        response.setRelationshipType( "Organization-SubTree" );
        response.setOrganizationId( organizationId );

        // Identify sub-tree nodes
        ResponseStatus sts = null;

        // Build organization tree
        buildOrganizationTree();

        // Retrieve organization's node
        SingleParentNode nodeOrganization = organizationTreeManager.fetchOrganizationNode(organizationId);
        if( nodeOrganization != null ) {
            response.setRelationshipId( nodeOrganization.getID() );
        }

        List<TreeNodeItem> listSubTreeNodes = new ArrayList<TreeNodeItem>();
        List<SingleParentNode> listNodes = organizationTreeManager.fetchSubTreeNodes(organizationId);
        if( listNodes != null ) {
            listNodes.forEach( node -> {
                TreeNodeItem item = new TreeNodeItem();
                item.setBusinessUnitId( node.getID() );
                item.setBusinessUnitType( BusinessUnitTypes.ORGANIZATION );
                item.setLevel(node.getLevel());

                Object obj = node.getObject();
                if( obj != null ) {
                    item.setRelationshipId( obj.toString() );
                }

                listSubTreeNodes.add(item);
            });
            sts = statusSuccess();
        }
        else {
            sts = statusNotFound( ERRMSG_NO_SUB_TREE );
        }

        response.setListNodes(listSubTreeNodes);
        response.setStatus(sts);

        // TODO Auto-generated method stub
        return response;
    }

}
