/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao.impl.search;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.ibm.wh.siam.busunit.dao.impl.mapper.SearchItemRowMapper;
import com.ibm.wh.siam.common.search.SearchDefinition;

/**
 * @author Match Grun
 *
 */
public interface SearchBuilderIF {

    /**
     * Build SQL to search for specified search criteria.
     * @param searchDefinition  Search definition.
     * @return SQL.
     */
    public String buildSqlSearch( final SearchDefinition searchDefinition );

    /**
     * Build SQL parameter source for specified search criteria.
     * @param searchDefinition  Search definition.
     * @return Parameter source.
     */
    public MapSqlParameterSource buildSqlParameterSource( final SearchDefinition searchDefinition );

    /**
     * Build row mapper for specified target.
     * @param searchDefinition  Search definition.
     * @return  Row mapper.
     */
    public SearchItemRowMapper buildRowMapper( final SearchDefinition searchDefinition );

    /**
     * Build SQL count to search for specified search criteria.
     * @param searchDefinition  Search definition.
     * @return SQL.
     */
    public String buildSqlCount( final SearchDefinition searchDefinition );

}
