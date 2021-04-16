/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.response.security.SecurityQuestionSetResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionStandardListResponse;

/**
 * @author Match Grun
 *
 */
public interface SecurityQuestionService {

    SecurityQuestionStandardListResponse fetchQuestionsBySet( final String questionSetId );

    SecurityQuestionSetResponse findQuestionSetById( final String questionSetId );
    SecurityQuestionSetResponse findQuestionSetByName( final String questionSetName );
    SecurityQuestionSetResponse findDefaultQuestionSet();

}
