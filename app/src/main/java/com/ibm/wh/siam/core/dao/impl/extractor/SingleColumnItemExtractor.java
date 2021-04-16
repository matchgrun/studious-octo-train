/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * @author Match Grun
 *
 */
public class SingleColumnItemExtractor
implements ResultSetExtractor<List<String>>
{
    private String nameItem = null;
    public void setNameItem( final String nameItem) {
        this.nameItem = nameItem;
    }

    @Override
    public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<String> list = new ArrayList<String>();
        while( rs.next() ) {
            list.add( rs.getString(nameItem) );
        }
        return list;
    }

}
