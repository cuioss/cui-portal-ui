/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.view;

import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Http header setting
 *
 * @author Matthias Walliczek
 */
@Data
class HttpHeader implements Serializable, Comparable<HttpHeader> {

    @Serial
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
