/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.product;

import com.ibm.wh.siam.core.dao.dto.ProductRange;
import com.ibm.wh.siam.core.response.BaseSiamResponse;

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
public class ProductRangeListResponse
extends BaseSiamResponse
{
    private static final long serialVersionUID = -1L;

    private Iterable<ProductRange> listProducts;
}
