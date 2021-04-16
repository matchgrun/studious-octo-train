/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

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
public class ContentSetDetail
extends ContentSet
implements Serializable
{
    private static final long serialVersionUID = -1L;
    private String ownerCode;
    private String ownerName;
}
