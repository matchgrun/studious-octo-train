/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.SecurityQuestionAnswer;
import com.ibm.wh.siam.core.request.security.SecurityQuestionAnswerSaveRequest;
import com.ibm.wh.siam.core.response.security.SecurityQuestionAnswerListResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionAnswerResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionAnswerSaveResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionListResponse;

/**
 * @author Match Grun
 *
 */
public interface SecurityQuestionAnswerService {
    SecurityQuestionAnswerListResponse findByCredential( final String credentialId );
    SecurityQuestionAnswerResponse findById( final String questionId);
    SecurityQuestionAnswerResponse save( final RecordUpdaterIF recordUpdater, final SecurityQuestionAnswer sqa );
    SecurityQuestionAnswerResponse deleteById( final RecordUpdaterIF recordUpdater, final String questionId );

    SecurityQuestionAnswerSaveResponse save( final RecordUpdaterIF recordUpdater, final SecurityQuestionAnswerSaveRequest req );

    SecurityQuestionListResponse fetchQuestionsByCredential( final String credentialId );

}
