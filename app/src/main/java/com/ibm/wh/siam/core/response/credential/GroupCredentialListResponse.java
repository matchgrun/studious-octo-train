/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.credential;

import com.ibm.wh.siam.core.dao.dto.CredentialRefLW;
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
public class GroupCredentialListResponse
extends BaseSiamResponse
{
    private static final long serialVersionUID = 1L;

    private String groupId;
    private Iterable<CredentialRefLW> listCredentials;

}
