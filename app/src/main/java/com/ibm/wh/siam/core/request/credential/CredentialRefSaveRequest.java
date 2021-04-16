/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.request.credential;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.CredentialRef;
import com.ibm.wh.siam.core.request.Metadata;

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
public class CredentialRefSaveRequest
implements Serializable
{
   private static final long serialVersionUID = 1L;

   private Metadata metadata;
   private CredentialRef credential;

}
