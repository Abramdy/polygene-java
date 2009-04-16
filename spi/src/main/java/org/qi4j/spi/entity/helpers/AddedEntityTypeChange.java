/*
 * Copyright (c) 2009, Rickard Öberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.qi4j.spi.entity.helpers;

import org.qi4j.spi.entity.EntityState;
import org.qi4j.spi.entity.EntityTypeReference;

/**
 * JAVADOC
 */
public class AddedEntityTypeChange
    extends EntityStateChange
{
    private EntityTypeReference entityTypeReference;

    public AddedEntityTypeChange( EntityTypeReference entityTypeReference )
    {
        super( null );
        this.entityTypeReference = entityTypeReference;
    }

    public void applyTo( EntityState state )
    {
        state.addEntityTypeReference( entityTypeReference );
    }
}