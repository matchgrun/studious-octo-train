/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.group;

import com.ibm.wh.siam.core.dto.Group;
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
public class GroupListResponse
extends BaseSiamResponse
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Iterable<Group> listGroups;

}
