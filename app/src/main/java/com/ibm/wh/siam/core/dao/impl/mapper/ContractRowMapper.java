/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.wh.siam.core.dto.Contract;

/**
 * @author Match Grun
 *
 */
public class ContractRowMapper
extends BaseSiamRowMapper
implements RowMapper<Contract>
{

    @Override
    public Contract mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Contract contract = new Contract();
        contract.setContractId(rs.getString("contract_id"));
        contract.setContractCode(rs.getString("contract_code"));
        contract.setOrganizationId(rs.getString("organization_id"));
        contract.setContractStartDate(rs.getDate("contract_start_date"));
        contract.setContractEndDate(rs.getDate("contract_end_date"));

        contract.setStatus(rs.getString("status"));
        super.mapCreator(rs, contract);

        return contract;
    }

}
