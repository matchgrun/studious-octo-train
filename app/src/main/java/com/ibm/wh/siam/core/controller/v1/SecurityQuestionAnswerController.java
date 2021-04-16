/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.controller.v1;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.exc.UsernameException;
import com.ibm.wh.siam.core.request.security.SecurityQuestionAnswerSaveRequest;
import com.ibm.wh.siam.core.response.security.SecurityQuestionAnswerListResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionAnswerResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionAnswerSaveResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionListResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionSetResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionStandardListResponse;
import com.ibm.wh.siam.core.service.SecurityQuestionAnswerService;
import com.ibm.wh.siam.core.service.SecurityQuestionService;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/v1" )
public class SecurityQuestionAnswerController
extends BaseSiamController
{
    @Resource
    SecurityQuestionAnswerService securityQuestionAnswerSvc;

    @Resource
    SecurityQuestionService securityQuestionSvc;

    private static final Logger logger = LoggerFactory.getLogger( SecurityQuestionAnswerController.class );

    @GetMapping("/securityQuestionAnswer/questionId/{id}")
    public @ResponseBody SecurityQuestionAnswerResponse getById(
        @RequestHeader HttpHeaders headers,
        @PathVariable( name="id", required=true ) String questionId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getById()");
            logger.info( "questionId=" + questionId );
        }

        return securityQuestionAnswerSvc.findById( questionId );
    }

    @GetMapping("/securityQuestionAnswers/credential/{id}")
    public SecurityQuestionAnswerListResponse getSecurityQuestionAnswers(
            @PathVariable( name="id", required=true ) String credentialId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getSecurityQuestionAnswers()...");
            logger.info("credentialId=" + credentialId );
        }

        return securityQuestionAnswerSvc.findByCredential(credentialId);
    }

    @PostMapping(
            path = "/securityQuestionAnswers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody SecurityQuestionAnswerSaveResponse saveSecurityQuestionAnswers(
            @RequestHeader HttpHeaders headers,
            @RequestBody SecurityQuestionAnswerSaveRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("saveSecurityQuestionAnswers...");
            logger.info("Req=" + req );
        }

        try {
            RecordUpdaterIF recordUpdater = fetchUpdater(headers);
            SecurityQuestionAnswerSaveResponse response = securityQuestionAnswerSvc.save(recordUpdater, req);
            if( logger.isInfoEnabled() ) {
                logger.info("response=" + response);
            }
            return response;
        }
        catch (UsernameException e) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                e.getMessage(),
                e );
        }
    }

    @GetMapping("/securityQuestions/credential/{id}")
    public SecurityQuestionListResponse getSecurityQuestions(
            @PathVariable( name="id", required=true ) String credentialId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getSecurityQuestions()...");
            logger.info("credentialId=" + credentialId );
        }

        return securityQuestionAnswerSvc.fetchQuestionsByCredential(credentialId);
    }

    @GetMapping("/securityQuestions/questionSet/{id}")
    public SecurityQuestionStandardListResponse getStandardSecurityQuestions(
            @PathVariable( name="id") String questionSetId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getStandardSecurityQuestions()...");
            logger.info("questionSetId=" + questionSetId );
        }
        return securityQuestionSvc.fetchQuestionsBySet(questionSetId);
    }

    @GetMapping("/securityQuestionSet/id/{id}")
    public SecurityQuestionSetResponse getQuestionSetById(
            @PathVariable( name="id") String questionSetId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getQuestionSetById()...");
            logger.info("questionSetId=" + questionSetId );
        }
        return securityQuestionSvc.findQuestionSetById(questionSetId);
    }

    @GetMapping("/securityQuestionSet/name/{name}")
    public SecurityQuestionSetResponse getQuestionSetByName(
            @PathVariable( name="name") String questionSetName )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getQuestionSetByName()...");
            logger.info("questionSetName=" + questionSetName );
        }
        return  securityQuestionSvc.findQuestionSetByName(questionSetName);
    }

    @GetMapping("/securityQuestionSet/default")
    public SecurityQuestionSetResponse getQuestionSetDefault()
    {
        if( logger.isInfoEnabled() ) {
            logger.info("getQuestionSetDefault()...");
        }
        return securityQuestionSvc.findDefaultQuestionSet();
    }

}
