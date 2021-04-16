/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.search;

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
public class SearchItem
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String itemId;
    private String itemName;
    private String itemStatus;
}
