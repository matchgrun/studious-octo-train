/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.dto.CredentialRefLW;
import com.ibm.wh.siam.core.dto.CredentialRef;

/**
 * @author Match Grun
 *
 */
public interface CredentialRefDAO {

    public CredentialRef findById( final String credentialId );
    public CredentialRef findByEntryDn( final String externalLdapId, final String ldapEntryDN );
    public Iterable<CredentialRef> findByUserId( final String userId );
    public Iterable<CredentialRef> findByLastName( final String lastName );
    public Iterable<CredentialRef> findByEmailAddress( final String emailAddress );
    public Iterable<CredentialRef> findByPhoneNumber( final String phoneNumber );

    public Iterable<CredentialRefLW> findByGroupId( final String groupId );

    public CredentialRef insert( final RecordUpdaterIF recordUpdater, final CredentialRef cred );
    public CredentialRef updateById( final RecordUpdaterIF recordUpdater, final CredentialRef cred );
    public CredentialRef deleteById( final RecordUpdaterIF recordUpdater, final CredentialRef cred );

}
