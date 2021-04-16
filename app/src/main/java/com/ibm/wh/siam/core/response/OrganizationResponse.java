/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.Organization;

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
public class OrganizationResponse
extends BaseSiamResponse
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Organization organization;

}
