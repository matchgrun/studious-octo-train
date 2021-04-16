/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response;

import com.ibm.wh.siam.core.dto.Contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ContractListResponse
extends BaseSiamResponse
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Iterable<Contract> listContracts;

}
