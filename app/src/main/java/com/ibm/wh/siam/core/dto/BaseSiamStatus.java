/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
public abstract class BaseSiamStatus
implements Serializable
{
   private static final long serialVersionUID = -1L;
   private String status;

}
