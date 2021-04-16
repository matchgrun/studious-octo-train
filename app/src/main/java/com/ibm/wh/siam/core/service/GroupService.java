/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.Group;
import com.ibm.wh.siam.core.request.group.GroupAssignmentRequest;
import com.ibm.wh.siam.core.request.group.GroupListAssignmentRequest;
import com.ibm.wh.siam.core.request.group.GroupSaveRequest;
import com.ibm.wh.siam.core.response.group.GroupAssignmentResponse;
import com.ibm.wh.siam.core.response.group.GroupListAssignmentResponse;
import com.ibm.wh.siam.core.response.group.GroupListResponse;
import com.ibm.wh.siam.core.response.group.GroupResponse;
import com.ibm.wh.siam.core.response.group.OrganizationGroupListResponse;

/**
 * @author Match Grun
 *
 */
public interface GroupService {
    public GroupResponse findByCode( final String groupCode );
    public GroupResponse findById( final String groupId );
    public GroupResponse save( final RecordUpdaterIF recordUpdater, final Group grp );
    public GroupResponse save( final RecordUpdaterIF recordUpdater, final GroupSaveRequest req );
    public GroupResponse deleteById( RecordUpdaterIF recordUpdater, final String groupId );

    public OrganizationGroupListResponse findByOrganizationId( final String orgId );
    public GroupListResponse findByAccountGroup( final String acctGroup );

    public GroupAssignmentResponse assignGroup( final RecordUpdaterIF recordUpdater, final GroupAssignmentRequest request );
    public GroupAssignmentResponse unassignGroup( final RecordUpdaterIF recordUpdater, final GroupAssignmentRequest request );

    public GroupListAssignmentResponse assignGroup( final RecordUpdaterIF recordUpdater, final GroupListAssignmentRequest request );
    public GroupListAssignmentResponse unassignGroup( final RecordUpdaterIF recordUpdater, final GroupListAssignmentRequest request );

}
