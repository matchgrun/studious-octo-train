/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.product;

import com.ibm.wh.siam.core.dto.Product;
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
public class ProductListResponse
extends BaseSiamResponse
{
    private static final long serialVersionUID = -1L;

    private Iterable<Product> listProducts;
}
