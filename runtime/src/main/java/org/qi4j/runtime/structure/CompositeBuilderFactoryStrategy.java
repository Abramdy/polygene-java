/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.runtime.structure;

import java.util.Map;
import org.qi4j.composite.Composite;
import org.qi4j.composite.CompositeBuilder;
import org.qi4j.composite.CompositeBuilderFactory;

/**
 * CBF that delegates to a number of other factories. This is used to implement
 * instantiation of public Composites in other Modules.
 */
public final class CompositeBuilderFactoryStrategy
    implements CompositeBuilderFactory
{
    private Map<Class<? extends Composite>, CompositeBuilderFactory> factories;

    public CompositeBuilderFactoryStrategy( Map<Class<? extends Composite>, CompositeBuilderFactory> factories )
    {
        this.factories = factories;
    }

    public <T extends Composite> CompositeBuilder<T> newCompositeBuilder( Class<T> compositeType )
    {
        CompositeBuilderFactory factory = factories.get( compositeType );
        CompositeBuilder<T> builder = factory.newCompositeBuilder( compositeType );
        return builder;
    }
}
