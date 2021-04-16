/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ibm.wh.siam.busunit.dao.OrganizationRelationshipDAO;
import com.ibm.wh.siam.busunit.dao.impl.mapper.OrganizationRelationshipRowMapper;
import com.ibm.wh.siam.busunit.dao.impl.mapper.ParentChildNodeRowMapper;
import com.ibm.wh.siam.busunit.dao.impl.node.OrganizationNodeSqlBuilderIF;
import com.ibm.wh.siam.core.dao.dto.ParentChildNode;
import com.ibm.wh.siam.core.dao.impl.BaseSiamDAO;
import com.ibm.wh.siam.core.dao.impl.SiamTableNames;
import com.ibm.wh.siam.core.dto.OrganizationRelationship;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOOrganizationRelationshipImpl
extends BaseSiamDAO
implements OrganizationRelationshipDAO
{
    private static final String RELTYPE_CHILD = "CHILD";
    private static final String RELTYPE_PARENT_OF_CHILD = "C-PARENT";

    private static final String Q_ORGANIZATION_REL = SiamTableNames.SIAM_DB_NAME + "." + SiamTableNames.ORGANIZATION_RELATION;
    private static final String COLS_ALL = SiamTableNames.ORGANIZATION_RELATION + ".*";

    @SuppressWarnings("unused")
    private static final String PRIMARY_KEY = "relationship_id";

    @SuppressWarnings("unused")
    private static final String SQL_ALL =
            "select " + COLS_ALL + " from " + Q_ORGANIZATION_REL +
            " and relationship_type = :relationshipType" +
            " order by organization_id, related_ord_id";

    // Returns children for specified organization
    private static final String SQL_FOR_CHILDREN =
            "select " + COLS_ALL + " from " + Q_ORGANIZATION_REL +
            " where organization_id = :orgID" +
            " and relationship_type = :relationshipType" +
            " order by related_org_id";

    // Returns parents for specified organization
    private static final String SQL_FOR_PARENTS =
            "select " + COLS_ALL + " from " + Q_ORGANIZATION_REL +
            " where related_org_id = :orgID" +
            " and relationship_type = :relationshipType" +
            " order by organization_id";

    private static final Logger logger = LoggerFactory.getLogger( DAOOrganizationRelationshipImpl.class );

    @Resource
    OrganizationNodeSqlBuilderIF organizationNodeSqlBuilder;

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;
    public DAOOrganizationRelationshipImpl(NamedParameterJdbcTemplate template) {
        this.namedTemplate = template;
    }

    @Override
    public List<OrganizationRelationship> loadRelationships( final String orgID )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("loadRelationships()");
            logger.info( "orgID=" + orgID );
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue( "orgID", orgID );
        params.addValue( "relationshipType", RELTYPE_CHILD );

        List<OrganizationRelationship> listChildren =
                namedTemplate.query( SQL_FOR_CHILDREN, params, new OrganizationRelationshipRowMapper() );
        List<OrganizationRelationship> listParents =
                namedTemplate.query( SQL_FOR_PARENTS, params, new OrganizationRelationshipRowMapper() );

        listParents.forEach(item -> {
            item.setRelationshipType( RELTYPE_PARENT_OF_CHILD );
        });

        if( logger.isInfoEnabled() ) {
            dumpList( "Children:", listChildren );
            dumpList( "Parents:", listParents );
            logger.info( "====================================================" );
        }

        listParents.addAll(listChildren);
        return listParents;
    }

    private void dumpList( final String msg, List<OrganizationRelationship> list ) {
        if( logger.isInfoEnabled() ) {
            logger.info(msg + "{");
            list.forEach( item -> {
                logger.info( "  - " + item );
            });
            logger.info("}");
        }
    }

    @SuppressWarnings("unused")
    private void dumpListNodes( final String msg, List<ParentChildNode> list ) {
        if( logger.isInfoEnabled() ) {
            logger.info(msg + "{");
            list.forEach( item -> {
                logger.info( "  - " + item );
            });
            logger.info("}");
        }
    }

    @Override
    public List<ParentChildNode> loadNodes() {
        if( logger.isInfoEnabled() ) {
            logger.info("loadNodes()");
        }

        ParentChildNodeRowMapper mapper = organizationNodeSqlBuilder.buildMapper();
        String sqlNodes = organizationNodeSqlBuilder.buildNodeQuery( RELTYPE_CHILD );
        if( logger.isInfoEnabled() ) {
            logger.info( sqlNodes );
        }

        List<ParentChildNode> listNode =
                namedTemplate.query( sqlNodes, mapper );

//        if( logger.isInfoEnabled() ) {
//            dumpListNodes( "Nodes:", listNode );
//            logger.info( "====================================================" );
//        }

        return listNode;
    }
}
