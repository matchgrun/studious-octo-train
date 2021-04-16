/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.response.tree;

import java.io.Serializable;
import java.util.List;

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
public class OrganizationAncestryResponse
extends BaseSiamResponse
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String organizationId;
    private String relationshipType;
    private String relationshipId;
    private List<AncestorItem> listAncestors;

}
