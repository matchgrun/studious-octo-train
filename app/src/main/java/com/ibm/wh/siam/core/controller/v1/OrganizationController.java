package com.ibm.wh.siam.core.controller.v1;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ibm.wh.siam.busunit.request.search.RequestFilter;
import com.ibm.wh.siam.busunit.request.search.RequestFilterIF;
import com.ibm.wh.siam.busunit.request.search.RequestWindow;
import com.ibm.wh.siam.busunit.request.search.SearchRequest;
import com.ibm.wh.siam.busunit.response.search.SearchResponse;
import com.ibm.wh.siam.busunit.search.SearchConstants;
import com.ibm.wh.siam.busunit.service.BusinessUnitSearchService;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.exc.UsernameException;
import com.ibm.wh.siam.core.request.busentity.OrganizationSaveRequest;
import com.ibm.wh.siam.core.response.OrganizationListResponse;
import com.ibm.wh.siam.core.response.OrganizationResponse;
import com.ibm.wh.siam.core.service.OrganizationService;

@RestController
@RequestMapping( "/v1" )
public class OrganizationController
extends BaseSiamController
{

    @Resource
    OrganizationService organizationSvc;

    @Resource
    BusinessUnitSearchService searchSvc;

    private static final Logger logger = LoggerFactory.getLogger( OrganizationController.class );

    @GetMapping("/organizations/accountGroup/{acctGroup}")
    public OrganizationListResponse getByAccountGroups(
            @PathVariable( name="acctGroup", required=true ) String acctGroup )
    {
        return organizationSvc.findByAccountGroup(acctGroup);
    }

    @GetMapping("/organization/code/{code}")
    public @ResponseBody OrganizationResponse getByCode(
        @PathVariable( name="code", required=true ) String orgCode )
    {
        return organizationSvc.findByCode( orgCode );
    }

    @GetMapping("/organization/id/{id}")
    public @ResponseBody OrganizationResponse getById(
        @PathVariable( name="id", required=true ) String orgId )
    {
        return organizationSvc.findById( orgId );
    }

    @GetMapping("/organization/account/{acct}")
    public @ResponseBody OrganizationResponse getByAccount(
        @PathVariable( name="acct", required=true ) String acctNum )
    {
        return organizationSvc.findByAccount(acctNum);
    }

    @PostMapping(
            path = "/organization",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody OrganizationResponse saveOrganization(
            @RequestHeader HttpHeaders headers,
            @RequestBody OrganizationSaveRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("saveOrganization...");
            logger.info("req=" + req );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            OrganizationResponse response = organizationSvc.save(recordUpdater, req);
            if( logger.isInfoEnabled() ) {
                logger.info("response=" + response);
            }
            return response;
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @DeleteMapping( "/organization/id/{id}")
    public @ResponseBody OrganizationResponse deleteById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String orgId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById...");
            logger.info("Org ID=" + orgId );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            OrganizationResponse response = organizationSvc.deleteById(recordUpdater, orgId);
            if( logger.isInfoEnabled() ) {
                logger.info("response=" + response );
            }
            return response;
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    /**
     * Implements a hard-coded search for organizations. This shows what the search results
     * look like with default window sizes, etc.
     * @return Search response that contains a list of matching items.
     */
    @GetMapping("/organizations/searchOld")
    public SearchResponse searchOrganizationsOld() {
        SearchRequest request = new SearchRequest();
        request.setSearchTarget( SearchConstants.TARGET_ORGANIZATION );

        List<RequestFilter> listFilters = new ArrayList<RequestFilter>();
        {
            RequestFilter filter = new RequestFilter();
            filter.setFieldName( "organization_name" );
            filter.setScope( RequestFilterIF.BEGINS_WITH );
            filter.setSearchTerm( "vbc" );
            listFilters.add(filter);

            filter = new RequestFilter();
            filter.setFieldName( "account_number" );
            filter.setScope( RequestFilterIF.BEGINS_WITH );
            filter.setSearchTerm( "tvbc" );
            listFilters.add(filter);

            request.setListSearchFilters(listFilters);
        }

        // Simulate the request window.
        RequestWindow requestWindow = new RequestWindow();
        requestWindow.setStartPosition(0);
        requestWindow.setWindowSize( 100 );
        request.setRequestWindow(requestWindow);

        return searchSvc.search(request);
    }

}
