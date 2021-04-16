/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import java.util.List;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.SecurityQuestion;
import com.ibm.wh.siam.core.dto.SecurityQuestionAnswer;

/**
 * @author Match Grun
 *
 */
public interface SecurityQuestionAnswerDAO {
    public List<SecurityQuestionAnswer> findByCredential( final String credentialId );

    public SecurityQuestionAnswer findById( final String questionId );

    public SecurityQuestionAnswer insert( final RecordUpdaterIF recordUpdater, final SecurityQuestionAnswer sqa );
    public SecurityQuestionAnswer updateById( final RecordUpdaterIF recordUpdater, final SecurityQuestionAnswer sqa );
    public SecurityQuestionAnswer deleteById( final RecordUpdaterIF recordUpdater, final SecurityQuestionAnswer sqa );

    public List<SecurityQuestion> findQuestionsByCredential( final String credentialId );
}
