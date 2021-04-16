/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
public class BaseSiamResponse
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ResponseStatus status;

}
