/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.attribute;

import java.io.Serializable;

import com.ibm.wh.siam.core.dao.dto.OwnerDescriptorItem;
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
public class OwnersWithAttributeNameResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String ownerType;
    private String descriptorName;
    private Iterable<OwnerDescriptorItem> listOwners;

}
