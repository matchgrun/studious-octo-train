/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import java.util.List;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.GroupCredential;

/**
 * @author Match Grun
 *
 */
public interface GroupCredentialDAO {
    public GroupCredential findById( final String groupCredentialId );
    public GroupCredential findByIds( final String groupId, final String credentialId );

    public GroupCredential insert( final RecordUpdaterIF recordUpdater, final GroupCredential groupCred );
    public GroupCredential deleteById( final RecordUpdaterIF recordUpdater, final GroupCredential groupCred );

    public void insertBulk( final RecordUpdaterIF recordUpdater, final String groupId, final List<String> listCredentialId );
    public void deleteBulk( final RecordUpdaterIF recordUpdater, final String groupId, final List<String> listOrgCredentialId );

}
