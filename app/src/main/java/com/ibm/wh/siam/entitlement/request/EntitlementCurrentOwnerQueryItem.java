/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.request;

import java.io.Serializable;
import java.util.List;

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
public class EntitlementCurrentOwnerQueryItem
implements Serializable
{
    private static final long serialVersionUID = 1L;

    @SiamString(empty = false)
    private String ownerType;

    private List<String> listOwnerIds;
}
