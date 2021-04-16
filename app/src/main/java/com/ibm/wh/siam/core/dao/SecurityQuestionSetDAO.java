/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import com.ibm.wh.siam.core.dto.SecurityQuestionSet;

/**
 * @author Match Grun
 *
 */
public interface SecurityQuestionSetDAO {
    public SecurityQuestionSet findById(final String questionSetId);
    public SecurityQuestionSet findByName(final String questionSetName);
}
