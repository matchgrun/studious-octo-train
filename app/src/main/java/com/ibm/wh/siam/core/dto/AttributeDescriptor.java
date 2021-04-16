/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

import java.io.Serializable;

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
public class AttributeDescriptor
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String attributeDescriptorId;

    @SiamString(maxLength=50)
    private String attributeName;

    @SiamString(maxLength=255)
    private String description;

}
