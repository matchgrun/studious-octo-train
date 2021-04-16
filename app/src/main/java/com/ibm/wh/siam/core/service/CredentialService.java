/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.CredentialRef;
import com.ibm.wh.siam.core.request.credential.CredentialAssignmentRequest;
import com.ibm.wh.siam.core.request.credential.CredentialRefSaveRequest;
import com.ibm.wh.siam.core.response.credential.CredentialAssignmentResponse;
import com.ibm.wh.siam.core.response.credential.CredentialRefListResponse;
import com.ibm.wh.siam.core.response.credential.CredentialRefResponse;
import com.ibm.wh.siam.core.response.credential.GroupCredentialListResponse;

/**
 * @author Match Grun
 *
 */
public interface CredentialService {

    public CredentialRefResponse findById( final String credentialId );

    public CredentialRefListResponse findByUserId( final String userId );
    public CredentialRefListResponse findByLastName( final String lastName );
    public CredentialRefListResponse findByEmail( final String emailAddress );
    public CredentialRefListResponse findByTelephone( final String phoneNumber );

    public GroupCredentialListResponse findByGroupId( final String groupId );

    public CredentialRefResponse save( final RecordUpdaterIF recordUpdater, final CredentialRef cred );
    public CredentialRefResponse deleteById( final RecordUpdaterIF recordUpdater, final String credentialId );

    public CredentialRefResponse save( final RecordUpdaterIF recordUpdater, final CredentialRefSaveRequest req );

    public CredentialAssignmentResponse assignCredential( final RecordUpdaterIF recordUpdater, final CredentialAssignmentRequest request );
    public CredentialAssignmentResponse unassignCredential( final RecordUpdaterIF recordUpdater, final CredentialAssignmentRequest request );

}
