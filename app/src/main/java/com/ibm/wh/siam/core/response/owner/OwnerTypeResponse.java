/**
 * 
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.owner;

import java.io.Serializable;

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
public class OwnerTypeResponse 
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String ownerId;
    private String ownerType;
}
