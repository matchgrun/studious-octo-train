/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.util;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Match Grun
 *
 */
public class AttributeUtil {

    public final static String GROUP = "GROUP";
    public final static String ORGANIZATION = "ORGANIZATION";
    public final static String PRODUCT = "PRODUCT";
    public final static String PERSON = "PERSON";
    public final static String ACCOUNT = "ACCOUNT";
    public final static String CREDENTIAL = "CREDENTIAL";
    public final static String ROLE = "ROLE";
    public final static String CONTENT_SET = "CONTENT-SET";
    public final static String CONTENT_SET_ACCESS = "CONTENT-SET-ACCESS";

    private static Set<String> SET_ATTRIBUTE_OWNERS = buildAttributeOwners();

    private static Set<String> buildAttributeOwners() {
        Set<String> set = new HashSet<String>();
        set.add(GROUP);
        set.add(ORGANIZATION);
        set.add(PRODUCT);
        set.add(PERSON);
        set.add(ACCOUNT);
        set.add(CREDENTIAL);
        set.add(ROLE);
        set.add(CONTENT_SET);
        set.add(CONTENT_SET_ACCESS);
        return set;
    }

    public static boolean isValid( final String ownerType ) {
        if( ownerType == null ) return false;
        return SET_ATTRIBUTE_OWNERS.contains(ownerType.toUpperCase());
    }

}
