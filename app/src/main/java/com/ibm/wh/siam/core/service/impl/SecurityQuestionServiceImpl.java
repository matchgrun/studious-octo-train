/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ibm.wh.siam.core.dao.SecurityQuestionSetDAO;
import com.ibm.wh.siam.core.dao.SecurityQuestionStandardDAO;
import com.ibm.wh.siam.core.dto.SecurityQuestionSet;
import com.ibm.wh.siam.core.dto.SecurityQuestionStandard;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.security.SecurityQuestionSetResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionStandardListResponse;
import com.ibm.wh.siam.core.service.SecurityQuestionService;

/**
 * @author Match Grun
 *
 */
@Component
@Configuration
public class SecurityQuestionServiceImpl
extends BaseSiamService
implements SecurityQuestionService
{

    @SuppressWarnings("unused")
    private static String defaultQuestionSetName = "DefaultSet";

    private static final String ERRMSG_NOT_FOUND = "Security Question(s) not Found.";

    private static final String ERRMSG_NOT_FOUND_SET = "Security Question Set not Found.";

    @SuppressWarnings("unused")
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Security Question.";

    @Resource
    SecurityQuestionStandardDAO questionDao;

    @Resource
    SecurityQuestionSetDAO questionSetDao;

    @Autowired
    private Environment env;


    private static final Logger logger = LoggerFactory.getLogger( SecurityQuestionServiceImpl.class );

    @Override
    public SecurityQuestionStandardListResponse fetchQuestionsBySet( final String questionSetId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("fetchQuestionsBySet()");
            logger.info( "questionSetId=" + questionSetId );
        }
        ResponseStatus sts = null;
        SecurityQuestionStandardListResponse response = new SecurityQuestionStandardListResponse();
        List<SecurityQuestionStandard> list = questionDao.fetchStandardQuestions(questionSetId);
        response.setQuestionSetId(questionSetId);
        response.setListSecurityQuestions(list);
        if( list == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public SecurityQuestionSetResponse findQuestionSetById(String questionSetId) {
        if( logger.isInfoEnabled() ) {
            logger.info("fetchQuestionsBySet()");
            logger.info( "questionSetId=" + questionSetId );
        }
        ResponseStatus sts = null;
        SecurityQuestionSetResponse response = new SecurityQuestionSetResponse();
        SecurityQuestionSet questionSet = questionSetDao.findById(questionSetId);

        response.setSecurityQuestionSet(questionSet);
        if( questionSet == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_SET );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public SecurityQuestionSetResponse findQuestionSetByName(String questionSetName) {
        ResponseStatus sts = null;
        SecurityQuestionSetResponse response = new SecurityQuestionSetResponse();
        SecurityQuestionSet questionSet = questionSetDao.findByName(questionSetName);

        response.setSecurityQuestionSet(questionSet);
        if( questionSet == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND_SET );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public SecurityQuestionSetResponse findDefaultQuestionSet() {
        return this.findQuestionSetByName(env.getProperty("siam.question-set.name.default"));
    }

}
