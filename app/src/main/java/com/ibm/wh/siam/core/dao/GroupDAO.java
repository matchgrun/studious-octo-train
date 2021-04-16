/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao;

import java.util.List;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.Group;

/**
 * @author Match Grun
 *
 */
public interface GroupDAO {

    public Group findByCode( final String grpCode );
    public Group findById( final String grpId );
    public Iterable<Group> findByAccountGroup( final String acctGroup );
    public Iterable<Group> findByOrganizationId( final String orgId );

    public Group insert( final RecordUpdaterIF recordUpdater, final Group grp );
    public Group updateById( final RecordUpdaterIF recordUpdater, final Group grp );
    public Group deleteById( final RecordUpdaterIF recordUpdater, final Group grp );

    public List<String> verifyGroupIds( final List<String> listGroupId );

}
