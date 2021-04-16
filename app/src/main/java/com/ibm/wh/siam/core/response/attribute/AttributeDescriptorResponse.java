/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.attribute;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.AttributeDescriptor;
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
public class AttributeDescriptorResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = 1L;

    private AttributeDescriptor attributeDescriptor;

}
