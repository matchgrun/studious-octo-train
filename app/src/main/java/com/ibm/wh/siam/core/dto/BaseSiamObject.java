/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

import java.io.Serializable;
import java.util.Date;

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
public abstract class BaseSiamObject
extends BaseSiamStatus
implements Serializable
{
   private static final long serialVersionUID = -1L;

   private Date createDate;
   private Date modifyDate;
   private String createdBy;
   private String createdByApp;
   private String modifiedBy;
   private String modifiedByApp;

}
