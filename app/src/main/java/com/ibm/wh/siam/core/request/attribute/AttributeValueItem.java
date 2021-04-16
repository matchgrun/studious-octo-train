/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.request.attribute;

import java.io.Serializable;
import java.util.List;

import com.ibm.wh.siam.core.annotations.SiamString;
import com.ibm.wh.siam.core.dto.AttributeNameValueList;

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
public class AttributeValueItem
implements Serializable
{
    private static final long serialVersionUID = 1L;

    @SiamString(empty = false)
    private String ownerId;
    @SiamString(empty = false)
    private String ownerType;
    private List<AttributeNameValueList> listAttributes;

}
