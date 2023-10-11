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
package de.cuioss.portal.ui.test.mocks;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import de.cuioss.jsf.api.application.bundle.CuiResourceBundle;
import de.cuioss.portal.common.bundle.UnifiedResourceBundle;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Mock variant of {@link CuiResourceBundle}. Simulate
 * {@link #getString(String)} (={@link #getObject(String)}) by simply returning
 * the key {@link #getKeys()} will return an empty list.
 *
 * @author Oliver Wolff
 */
@Named("msgs")
@UnifiedResourceBundle
@Dependent
@EqualsAndHashCode(callSuper = false)
@ToString
public class PortalMirrorResourceBundle extends ResourceBundle implements Serializable {

    private static final long serialVersionUID = 3953649686127640297L;

    @Override
    protected Object handleGetObject(final String key) {
        return key;
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.emptyEnumeration();
    }
}
