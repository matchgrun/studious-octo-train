/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.request.security;

import java.io.Serializable;
import java.util.List;

import com.ibm.wh.siam.core.dto.SecurityQuestionAnswer;

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
public class SecurityAnswersItem
implements Serializable
{

   private static final long serialVersionUID = 1L;

   private String credentialId;
   private List<SecurityQuestionAnswer> listAnswers;

}
