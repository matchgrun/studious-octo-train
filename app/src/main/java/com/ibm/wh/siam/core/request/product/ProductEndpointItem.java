/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.request.product;

import java.io.Serializable;
import java.util.List;

import com.ibm.wh.siam.core.dto.ProductEndpoint;

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
public class ProductEndpointItem
implements Serializable
{

   private static final long serialVersionUID = 1L;

   private String productId;
   private List<ProductEndpoint> listEndpoints;

}
