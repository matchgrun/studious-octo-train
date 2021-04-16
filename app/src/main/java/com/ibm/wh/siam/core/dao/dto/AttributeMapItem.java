/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.dto;

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
public class AttributeMapItem
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String name;
    private String id;
}
