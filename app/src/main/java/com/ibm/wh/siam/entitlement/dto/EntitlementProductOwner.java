/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.dto;

import java.io.Serializable;
import java.util.Date;

import com.ibm.wh.siam.core.dto.BaseSiamObject;

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
public class EntitlementProductOwner
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String ownerType;
    private String ownerId;
    private String ownerCode;
    private String ownerName;

    private String entitlementId;
    private String productId;
    private Date entitlementStartDate;
    private Date entitlementEndDate;

    private String productCode;
    private String productName;
    private String productStatus;
    private Date productStartDate;
    private Date productEndDate;

    private String ldapEntryDN;

}
