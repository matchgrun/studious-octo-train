/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import java.util.List;

import com.ibm.wh.siam.core.dto.SecurityQuestion;
import com.ibm.wh.siam.core.dto.SecurityQuestionStandard;

/**
 * @author Match Grun
 *
 */
public interface SecurityQuestionStandardDAO {
    public List<SecurityQuestionStandard> fetchStandardQuestions( final String questionSetId );
    public SecurityQuestion findById(final String questionId);
}
