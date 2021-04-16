/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.credential;

import java.io.Serializable;
import java.util.List;

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
public class CredentialListAssignmentResponse
extends BaseSiamResponse
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String operation = "";
    private String assignmentId;
    private Group group;
    private List<String> listCredentialsExisting;
    private List<String> listCredentialsAssigned;
    private List<String> listCredentialsFailed;

}
