package de.cuioss.portal.ui.runtime.application.view;

import java.io.Serializable;

import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import lombok.Data;

/**
 * Http header setting
 *
 * @author Matthias Walliczek
 */
@Data
class HttpHeader implements Serializable, Comparable<HttpHeader> {

    private static final long serialVersionUID = -8089609030535127942L;

    private String key;

    private String value;

    private boolean enabled;

    private ViewMatcher viewMatcher;

    /**
     * Compare two HttpHeader based on the specification of the view matcher to find
     * the most specific http header. Returns a negative integer, zero, or a
     * positive integer as this object is less than, equal to, or greater than the
     * specified object.
     *
     * @param other the object to be compared.
     *
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it from
     *                              being compared to this object.
     */
    @Override
    public int compareTo(final HttpHeader other) {
        return Integer.compare(pathLength(this.getViewMatcher()), pathLength(other.getViewMatcher()));
    }

    private static int pathLength(final ViewMatcher matcher) {
        if (null == matcher) {
            return 0;
        }
        return matcher.toString().length();
    }
}
