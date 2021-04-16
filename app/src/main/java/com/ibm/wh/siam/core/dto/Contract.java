/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

import java.io.Serializable;
import java.util.Date;

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
public class Contract
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String contractId;
    private String contractCode;
    private String organizationId;
    private Date contractStartDate;
    private Date contractEndDate;

}

