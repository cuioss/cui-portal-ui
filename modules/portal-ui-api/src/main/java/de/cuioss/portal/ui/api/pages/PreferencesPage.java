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
package de.cuioss.portal.ui.api.pages;

import java.io.Serializable;

/**
 * Specifies the page bean backing the preferences page.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1214") // We allow constants in the page interfaces, because they belong together
// (coherence).
public interface PreferencesPage extends Serializable {

    /**
     * Bean name for looking up instances.
     */
    String BEAN_NAME = "preferencesPageBean";

    /**
     * The outcome used for navigation to the preferences page.
     */
    String OUTCOME = "preferences";

}
