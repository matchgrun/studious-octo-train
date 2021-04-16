/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.response.tree;

import java.io.Serializable;

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
public class AncestorItem
implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String businessUnitId;
    private String businessUnitType;

}
