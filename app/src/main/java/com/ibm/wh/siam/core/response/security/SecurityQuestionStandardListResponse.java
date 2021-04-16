/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.security;

import java.io.Serializable;
import java.util.List;

import com.ibm.wh.siam.core.dto.SecurityQuestionStandard;
import com.ibm.wh.siam.core.response.BaseSiamResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SecurityQuestionStandardListResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String questionSetId;
    private List<SecurityQuestionStandard> listSecurityQuestions;

}
