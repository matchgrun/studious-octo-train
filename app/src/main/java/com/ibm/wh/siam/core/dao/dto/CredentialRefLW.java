/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.dto;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.BaseSiamStatus;

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
public class CredentialRefLW
extends BaseSiamStatus
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String credentialId;
    private String userId;
    private String emailAddress;

}

