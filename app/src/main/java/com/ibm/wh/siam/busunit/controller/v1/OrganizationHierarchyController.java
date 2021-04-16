/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.controller.v1;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.wh.siam.busunit.response.OrganizationRelationshipListResponse;
import com.ibm.wh.siam.busunit.response.tree.OrganizationAncestryResponse;
import com.ibm.wh.siam.busunit.response.tree.OrganizationSubTreeResponse;
import com.ibm.wh.siam.busunit.service.BusinessUnitHierarchyServiceIF;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class OrganizationHierarchyController
{
    @Resource
    BusinessUnitHierarchyServiceIF busUnitHierarchyService;

    private static final Logger logger = LoggerFactory.getLogger( OrganizationHierarchyController.class );

    @GetMapping("/organization/hierarchy/tree/{id}")
    public @ResponseBody OrganizationRelationshipListResponse getTreeForOrganization(
        @PathVariable( name="id", required=true ) String orgId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getTreeForOrganization()...");
            logger.info("orgId=" + orgId );
        }

        OrganizationRelationshipListResponse response = busUnitHierarchyService.findByOrganizationId( orgId );
        return response;
    }

    @GetMapping("/organization/hierarchy/ancestors/{id}")
    public @ResponseBody OrganizationAncestryResponse getAncestorsForOrganization(
        @PathVariable( name="id", required=true ) String orgId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getAncestorsForOrganization()...");
            logger.info("orgId=" + orgId );
        }

        return busUnitHierarchyService.findAncestorsForOrganization(orgId);
    }

    @GetMapping("/organization/hierarchy/subtree/{id}")
    public @ResponseBody OrganizationSubTreeResponse getSubTreeNodesForOrganization(
        @PathVariable( name="id", required=true ) String orgId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getSubTreeNodesForOrganization()...");
            logger.info("orgId=" + orgId );
        }

        return busUnitHierarchyService.findSubTreeNodesForOrganization(orgId);
    }

}
