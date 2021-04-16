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
public class SecurityQuestionStandard
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

   private String questionId;
   private String questionSetId;

   @SiamInteger(range = true, minValue = 1, maxValue = 30)
   private Integer displaySequence;

   @SiamString(maxLength=60)
   private String question;

   private String ldapEntryDN;

}
