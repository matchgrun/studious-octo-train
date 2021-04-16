/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao.impl.node;

import org.springframework.stereotype.Component;

import com.ibm.wh.siam.busunit.dao.impl.mapper.ParentChildNodeRowMapper;
import com.ibm.wh.siam.core.dao.impl.SiamTableNames;

/**
 * @author Match Grun
 *
 */
@Component
public class OrganizationNodeSqlBuilder
implements OrganizationNodeSqlBuilderIF
{
    // Column names
    private static final String COLNAME_ID_REL = "relationship_id";
    private static final String COLNAME_ID_ORG = "organization_id";
    private static final String COLNAME_ID_REL_ORG = "related_org_id";
    private static final String COLNAME_ID_REL_TYPE = "relationship_type";

    /**
     * Build column into builder.
     * @param sb        Builder.
     * @param colName   Column name.
     * @return Builder.
     */
    private StringBuilder buildColumn( final StringBuilder sb, final String colName ) {
        return sb.append( SiamTableNames.ORGANIZATION_RELATION ).append( "." ).append(colName);
    }

    @Override
    public String buildNodeQuery( final String relationshipType ) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("select ");
        buildColumn( sb, COLNAME_ID_REL ).append( ", " );
        buildColumn( sb, COLNAME_ID_ORG ).append( ", " );
        buildColumn( sb, COLNAME_ID_REL_ORG );

        sb.append( "\nfrom ").append(SiamTableNames.SIAM_DB_NAME ).append(".").append(SiamTableNames.ORGANIZATION_RELATION );
        sb.append( "\nwhere ").append(COLNAME_ID_REL_TYPE ).append( " = '" ).append( relationshipType ).append( "'" );
        sb.append( "\norder by ").append( COLNAME_ID_ORG ).append( ", " ).append( COLNAME_ID_REL_ORG );

        return sb.toString();
    }

    @Override
    public ParentChildNodeRowMapper buildMapper() {
        ParentChildNodeRowMapper mapper = new ParentChildNodeRowMapper();
        mapper.setNameItem(COLNAME_ID_REL);
        mapper.setNameParent(COLNAME_ID_ORG);
        mapper.setNameChild(COLNAME_ID_REL_ORG);
        return mapper;
    }

}
