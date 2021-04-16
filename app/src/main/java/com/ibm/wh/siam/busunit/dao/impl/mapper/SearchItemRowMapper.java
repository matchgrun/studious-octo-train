/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.common.search.SearchItem;

/**
 * @author Match Grun
 *
 */
public class SearchItemRowMapper
implements RowMapper<SearchItem>{

    private String nameItem = null;
    private String nameDescription = null;
    private String nameStatus = null;

    public void setNameItem( final String nameItem) {
        this.nameItem = nameItem;
    }

    public void setNameDescription( final String nameDescription) {
        this.nameDescription = nameDescription;
    }

    public void setNameStatus( final String nameStatus) {
        this.nameStatus = nameStatus;
    }


    @Override
    public SearchItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        // TODO Auto-generated method stub
        SearchItem item = new SearchItem();
        item.setItemId(rs.getString(nameItem));
        item.setItemName(rs.getString(nameDescription));
        item.setItemStatus(rs.getString(nameStatus));
        return item;
    }

}
