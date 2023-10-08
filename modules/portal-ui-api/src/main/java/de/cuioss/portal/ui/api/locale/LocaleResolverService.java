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
package de.cuioss.portal.ui.api.locale;

import java.util.List;
import java.util.Locale;

/**
 * Provides methods like access on configured locales and changing the locale on
 * per user basis.
 *
 * @author Oliver Wolff
 */
public interface LocaleResolverService {

	/**
	 * @return the list of available locales for the current user.
	 */

	List<Locale> getAvailableLocales();

	/**
	 * Saves the locale changed by user interaction
	 *
	 * @param locale to be updated. Must be one of {@link #getAvailableLocales()}.
	 *               Otherwise it will throws an {@link IllegalArgumentException}
	 */
	void saveUserLocale(Locale locale);

	/**
	 * @return The currently acitve user-locale
	 *
	 */
	Locale resolveUserLocale();
}
