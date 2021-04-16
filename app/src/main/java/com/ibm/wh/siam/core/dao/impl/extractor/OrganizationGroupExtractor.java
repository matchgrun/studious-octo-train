/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * @author Match Grun
 *
 */
public class OrganizationGroupExtractor
implements ResultSetExtractor<Map<String,String>>
{

    @Override
    public Map<String,String> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String,String> map = new HashMap<String,String>();
        while( rs.next() ) {
            map.put(
                    rs.getString("group_id"),
                    rs.getString("organization_group_id" ) );
        }
        return map;
    }

}
