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
package de.cuioss.portal.ui.runtime.application.storage;

import static de.cuioss.portal.ui.api.PortalCoreBeanNames.SESSION_STORAGE_BEAN_NAME;

import java.io.Serializable;

import javax.annotation.Priority;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.core.storage.SessionStorage;
import de.cuioss.portal.core.storage.impl.MapStorageImpl;

/**
 * Represents the session scoped runtime representation of
 * {@link PortalSessionStorage}
 *
 * @author Oliver Wolff
 */
@PortalSessionStorage
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@SessionScoped
@Named(SESSION_STORAGE_BEAN_NAME)
public class PortalSessionStorageImpl extends MapStorageImpl<Serializable, Serializable> implements SessionStorage {

    private static final long serialVersionUID = 1573344347429735050L;

}
