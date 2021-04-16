/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

import java.io.Serializable;

import com.ibm.wh.siam.core.annotations.SiamInteger;
import com.ibm.wh.siam.core.annotations.SiamString;

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
public class SecurityQuestionAnswer
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

   private String questionId;

   @SiamString(empty=true)
   private String credentialId;

   @SiamInteger(range = true, minValue = 1, maxValue = 10)
   private Integer displaySequence;

   @SiamString(maxLength=60)
   private String question;

   @SiamString(minLength=3, maxLength=50)
   private String answer;

   @SiamString(maxLength=20)
   private String encryptionCode;

   private String ldapEntryDN;

}
