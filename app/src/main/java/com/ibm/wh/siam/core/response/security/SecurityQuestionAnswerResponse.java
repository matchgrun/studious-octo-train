/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.security;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.SecurityQuestionAnswer;
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
public class SecurityQuestionAnswerResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private SecurityQuestionAnswer answer;

}
