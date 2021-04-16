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
public class AttributeValue
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String attributeId;

    @SiamString(empty = false)
    private String attributeDescriptorId;

    @SiamString(empty = false)
    private String ownerType;
    @SiamString(empty = false)
    private String ownerId;

    @SiamString(empty = false, maxLength=2000)
    private String attributeValue;

    private String ldapEntryDN;

}
