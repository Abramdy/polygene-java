/*
 * Copyright (c) 2008, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.runtime.entity;

import org.qi4j.api.entity.Entity;
import org.qi4j.api.entity.EntityComposite;
import org.qi4j.api.property.StateHolder;
import org.qi4j.runtime.composite.AbstractMixinsModel;
import org.qi4j.runtime.composite.MixinDeclaration;
import org.qi4j.runtime.composite.MixinModel;
import org.qi4j.runtime.composite.UsesInstance;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * JAVADOC
 */
public final class EntityMixinsModel extends AbstractMixinsModel
    implements Serializable
{
    public EntityMixinsModel( Class<? extends EntityComposite> compositeType, List<Class<?>> assemblyMixins )
    {
        super( compositeType, assemblyMixins );
        mixins.add( new MixinDeclaration( EntityMixin.class, Entity.class ) );
    }

    public Object newMixin( EntityInstance entityInstance, StateHolder state, Object[] mixins, Method method )
    {
        MixinModel model = methodImplementation.get( method );
        Object mixin = model.newInstance( entityInstance, state, UsesInstance.NO_USES );
        mixins[ methodIndex.get( method ) ] = mixin;
        return mixin;
    }
}
