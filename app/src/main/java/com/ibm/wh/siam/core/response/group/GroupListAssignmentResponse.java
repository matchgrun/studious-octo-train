/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.group;

import java.io.Serializable;
import java.util.List;

import com.ibm.wh.siam.core.dto.Organization;
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
public class GroupListAssignmentResponse
extends BaseSiamResponse
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String operation = "";
    private String assignmentId;
    private Organization organization;
    private List<String> listGroupsExisting;
    private List<String> listGroupsAssigned;
    private List<String> listGroupsFailed;

}
