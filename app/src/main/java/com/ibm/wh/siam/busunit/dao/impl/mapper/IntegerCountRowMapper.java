/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author Match Grun
 *
 */
public class IntegerCountRowMapper
implements RowMapper<Integer>
{

    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Integer.valueOf( rs.getInt( "count_search" ) );
    }

    /**
     * Return single count value from list of counts.
     * @param listCounts List of integers that may have been returned by mapRow method.
     * @return Count.
     */
    public static int fetchCounts( final List<Integer> listCounts ) {
        int retVal = -1;
        if( listCounts != null ) {
            if( listCounts.size() > 0 ) {
                Integer iCount = listCounts.get(0).intValue();
                retVal = iCount.intValue();
            }
        }
        return retVal;
    }
}
