/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao.impl.node;

import com.ibm.wh.siam.busunit.dao.impl.mapper.ParentChildNodeRowMapper;

/**
 * @author Match Grun
 *
 */
public interface OrganizationNodeSqlBuilderIF {

    /**
     * Build SQL statement for retrieving parent/child nodes.
     * @param relationshipType Relationship type.
     * @return SQL.
     */
    public String buildNodeQuery( final String relationshipType );

    /**
     * Build mapper for processing result set.
     * @return Mapper.
     */
    public ParentChildNodeRowMapper buildMapper();

}
