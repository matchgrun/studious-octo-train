/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.request;

import java.io.Serializable;
import java.util.Date;

import com.ibm.wh.siam.core.annotations.SiamString;
import com.ibm.wh.siam.core.dto.BaseSiamStatus;

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
public class EntitlementSaveItem
extends BaseSiamStatus
implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String entitlementId;
    private String productId;

    @SiamString(empty = false)
    private String status;

    private Date startDate;
    private Date endDate;

}
