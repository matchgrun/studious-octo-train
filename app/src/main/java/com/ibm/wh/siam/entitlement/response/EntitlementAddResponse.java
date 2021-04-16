/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.response;

import java.io.Serializable;
import java.util.List;

import com.ibm.wh.siam.core.response.BaseSiamResponse;
import com.ibm.wh.siam.entitlement.dto.Entitlement;
import com.ibm.wh.siam.entitlement.request.EntitlementAddItem;

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
public class EntitlementAddResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<EntitlementAddItem> listToAdd;
    private Iterable<Entitlement> listEntitlements;
}
