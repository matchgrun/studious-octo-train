package com.ibm.wh.siam.busunit.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dao.dto.ParentChildNode;
import com.ibm.wh.siam.core.dao.impl.mapper.BaseSiamRowMapper;

public class ParentChildNodeRowMapper
extends BaseSiamRowMapper
implements RowMapper<ParentChildNode>
{
    private String nameItem = null;
    private String nameParent = null;
    private String nameChild = null;

    public void setNameItem( final String nameItem) {
        this.nameItem = nameItem;
    }

    public void setNameParent( final String nameParent) {
        this.nameParent = nameParent;
    }

    public void setNameChild( final String nameChild) {
        this.nameChild = nameChild;
    }

    @Override
    public ParentChildNode mapRow(ResultSet rs, int rowNum) throws SQLException {

        ParentChildNode node = new ParentChildNode();

        node.setItemId(rs.getString(nameItem));
        node.setParentId(rs.getString(nameParent));
        node.setChildId(rs.getString(nameChild));

        return node;
    }

}
