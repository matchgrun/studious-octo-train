/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.tree;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.wh.siam.common.tree.CyclicLoopException;
import com.ibm.wh.siam.common.tree.DuplicateNodeException;
import com.ibm.wh.siam.common.tree.NodeCreationException;
import com.ibm.wh.siam.common.tree.SingleParentNode;
import com.ibm.wh.siam.common.tree.SingleParentTree;
import com.ibm.wh.siam.core.dao.dto.ParentChildNode;

/**
 * @author Match Grun
 *
 */
@Component
public class OrganizationTreeManager
implements OrganizationTreeManagerIF
{
    private static final Logger logger = LoggerFactory.getLogger( OrganizationTreeManager.class );

    private SingleParentTree organizationTree = null;

    @Override
    public SingleParentTree getSingleParentTree() {
        return organizationTree;
    }

    @Override
    public SingleParentTree buildOrganizationTree(List<ParentChildNode> listNodes) {
        if( logger.isInfoEnabled() ) {
            logger.info("buildOrganizationTree()");
        }

        SingleParentTree tree = buildTree(listNodes);
        if( logger.isInfoEnabled() ) {
            logger.info( "tree" );
            logger.info( tree.toString() );
        }

        assignItemId(tree, listNodes);

        List<SingleParentNode> listTreeNodes = SingleParentTree.unrollTree( tree );
        if( logger.isInfoEnabled() ) {
            logger.info( "unRolledTree" );
            dumpNodes( listTreeNodes);
        }

        organizationTree = tree;
        return tree;
    }

    private void dumpNodes( final List<SingleParentNode> listNodes ) {
        if( listNodes == null ) {
            logger.info( "listNodes IS-NULL" );
        }
        else {
            listNodes.forEach( node -> {
                logger.info( "node: " + node );
            });
        }
    }

    private SingleParentTree buildTree( final List<ParentChildNode> listNodes ) {
        if( logger.isInfoEnabled() ) {
            logger.info("buildTree()");
        }

        SingleParentTree tree = new SingleParentTree();
        listNodes.forEach( node -> {
            try {
                String parentId = node.getParentId();
                String childId = node.getChildId();

                try {
                    tree.addParentChild( parentId, childId );
                }
                catch (DuplicateNodeException e) {
                    if( logger.isInfoEnabled() ) {
                        logger.info( e.getMessage() );
                    }
                }
                catch (CyclicLoopException e) {
                    if( logger.isInfoEnabled() ) {
                        logger.info( e.getMessage() );
                    }
                }
            }
            catch (NodeCreationException e) {
                if( logger.isInfoEnabled() ) {
                    logger.info( e.getMessage() );
                }
            }
        });

        return tree;
    }

    private void assignItemId(
            final SingleParentTree tree,
            final List<ParentChildNode> listNodes )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("assignItemId()");
        }

        listNodes.forEach( node -> {
            String parentId = node.getChildId();
            SingleParentNode nodeParent = tree.fetchNode(parentId);
            if( nodeParent != null ) {
                nodeParent.setObject(node.getItemId());
            }
        });
    }

    public SingleParentNode fetchOrganizationNode( final String organizationId ) {
        return organizationTree.fetchNode(organizationId);
    }

    @Override
    public List<SingleParentNode> fetchAncestors( final String organizationId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("fetchAncestors()");
            logger.info("organizationId=" +organizationId );
        }

        List<SingleParentNode> listAncestors = null;
        SingleParentNode nodeOrganization = organizationTree.fetchNode(organizationId);

        if( nodeOrganization != null ) {
            listAncestors = nodeOrganization.ancestry();
        }

        if( logger.isInfoEnabled() ) {
            logger.info( "Ancestors" );
            dumpNodes(listAncestors);
        }

        return  listAncestors;
    }


    @Override
    public List<SingleParentNode> fetchSubTreeNodes( final String organizationId) {
        if( logger.isInfoEnabled() ) {
            logger.info("fetchSubTreeNodes()");
        }

        List<SingleParentNode> listNodes= null;
        SingleParentNode nodeOrganization = organizationTree.fetchNode(organizationId);
        if( nodeOrganization != null ) {
            listNodes = organizationTree.fetchNodesSubTree( nodeOrganization, false );
        }

        if( logger.isInfoEnabled() ) {
            logger.info( "SubTree" );
            dumpNodes(listNodes);
        }

        return  listNodes;
    }


}
