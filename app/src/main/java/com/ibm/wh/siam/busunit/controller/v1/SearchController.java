/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.controller.v1;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.wh.siam.busunit.request.search.SearchRequest;
import com.ibm.wh.siam.busunit.response.search.SearchResponse;
import com.ibm.wh.siam.busunit.service.BusinessUnitSearchService;

@RestController
@RequestMapping( "/v1" )
/**
 * @author Match Grun
 *
 */
public class SearchController {

    @Resource
    BusinessUnitSearchService searchSvc;

    private static final Logger logger = LoggerFactory.getLogger( SearchController.class );

    @PostMapping(
            path = "/businessUnit/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody SearchResponse search(
            @RequestBody SearchRequest searchRequest )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("search");
            logger.info("searchRequest=" + searchRequest );
        }

        SearchResponse response = searchSvc.search(searchRequest);
        /*
        if( logger.isInfoEnabled() ) {
            logger.info("response=" + response );
        }
        */
        return response;
    }

}
