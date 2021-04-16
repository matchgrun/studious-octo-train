/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.response;

import java.io.Serializable;

import com.ibm.wh.siam.core.response.BaseSiamResponse;
import com.ibm.wh.siam.entitlement.dto.Entitlement;

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
public class EntitlementCurrentResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Iterable<Entitlement> listEntitlements;
}
