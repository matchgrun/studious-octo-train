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
public class SecurityQuestionSet
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

   private String questionSetId;

   @SiamString(maxLength=60)
   private String questionSetName;

   @SiamString(maxLength=255)
   private String description;

   @SiamInteger(range = true, minValue = 1, maxValue = 10)
   private int requiredQuestions;

   @SiamInteger(range = true, minValue = 1, maxValue = 3)
   private int securityLevel;

   private String ldapEntryDN;

}
