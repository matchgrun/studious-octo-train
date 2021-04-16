/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.product;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.ProductEndpoint;
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
public class ProductEndpointResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private ProductEndpoint endpoint;

}
