/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.dto;

import java.io.Serializable;
import java.util.Date;

import com.ibm.wh.siam.core.annotations.SiamString;
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
public class Entitlement
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String entitlementId;
    @SiamString(empty = false)
    private String ownerType;
    @SiamString(empty = false)
    private String ownerId;
    @SiamString(empty = false)
    private String productId;
    private Date startDate;
    private Date endDate;
    private String ldapEntryDN;

}
