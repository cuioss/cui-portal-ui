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
package de.cuioss.portal.ui.runtime.application.configuration;

import de.cuioss.portal.configuration.util.ConfigurationHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import lombok.NonNull;

import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Expose the application configuration as a resource bundle to allow EL
 * expressions like
 *
 * <pre>
 * #{configuration['xzy']}
 * </pre>
 * <p>
 * .
 *
 * @author Matthias Walliczek
 */
@Named("configuration")
@Dependent
public class ELConfigurationResolverBean extends ResourceBundle {

    @Override
    protected Object handleGetObject(final @NonNull String key) {
        return ConfigurationHelper.resolveConfigProperty(key).orElse("Undefined");
    }

    @Override
    public @NonNull Enumeration<String> getKeys() {
        return Collections.enumeration(ConfigurationHelper.resolveConfigPropertyNames());
    }

}
