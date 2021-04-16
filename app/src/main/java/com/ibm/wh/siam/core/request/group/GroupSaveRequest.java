/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.request.group;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.Group;
import com.ibm.wh.siam.core.request.Metadata;

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
public class GroupSaveRequest
implements Serializable
{
   private static final long serialVersionUID = 1L;

   private Metadata metadata;
   private Group group;

}
