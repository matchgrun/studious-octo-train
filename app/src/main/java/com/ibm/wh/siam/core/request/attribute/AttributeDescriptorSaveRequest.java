/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.request.attribute;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.AttributeDescriptor;
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
public class AttributeDescriptorSaveRequest
implements Serializable
{
   private static final long serialVersionUID = 1L;

   private Metadata metadata;
   private AttributeDescriptor attributeDescriptor;

}
