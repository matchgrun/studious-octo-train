/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

import java.io.Serializable;

import com.ibm.wh.siam.core.annotations.SiamString;

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
public class CredentialRef
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String credentialId;

    @SiamString(empty=false, maxLength=30)
    private String userId;

    @SiamString(empty=false, maxLength=30)
    private String lastName;

    @SiamString(empty=false)
    private String firstName;

    @SiamString(maxLength=60)
    private String displayName;

    @SiamString(maxLength=30)
    private String emailAddress;

    @SiamString(maxLength=30)
    private String telephoneNumber;

    private boolean internalUser;

    // @SiamString(empty=false)
    private String externalLdapId;

    @SiamString(empty=false)
    private String externalLdapEntryDN;

    private String externalAdEntryDN;

}

