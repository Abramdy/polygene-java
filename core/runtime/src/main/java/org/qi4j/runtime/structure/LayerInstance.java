/*
 * Copyright (c) 2008, Rickard Öberg. All Rights Reserved.
 * Copyright (c) 2012, Paul Merlin.
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

import java.util.ArrayList;
import java.util.List;
import org.qi4j.api.activation.ActivationEventListener;
import org.qi4j.api.activation.ActivationException;
import org.qi4j.api.activation.PassivationException;
import org.qi4j.api.common.Visibility;
import org.qi4j.api.composite.TransientDescriptor;
import org.qi4j.api.entity.EntityDescriptor;
import org.qi4j.api.object.ObjectDescriptor;
import org.qi4j.api.service.ServiceReference;
import org.qi4j.api.structure.Layer;
import org.qi4j.api.value.ValueDescriptor;
import org.qi4j.functional.Function;
import org.qi4j.runtime.activation.ActivationDelegate;
import org.qi4j.spi.module.ModelModule;

import static org.qi4j.functional.Iterables.flattenIterables;
import static org.qi4j.functional.Iterables.map;

/**
 * Instance of a Qi4j application layer. Contains a list of modules which are managed by this layer.
 */
public class LayerInstance
    implements Layer
{

    // Constructor parameters
    private final LayerModel layerModel;
    private final ApplicationInstance applicationInstance;
    private final UsedLayersInstance usedLayersInstance;
    // Eager instance objects
    private final ActivationDelegate activation;
    private final List<ModuleInstance> moduleInstances;

    public LayerInstance( LayerModel model,
                          ApplicationInstance applicationInstance,
                          UsedLayersInstance usedLayersInstance
    )
    {
        // Constructor parameters
        this.layerModel = model;
        this.applicationInstance = applicationInstance;
        this.usedLayersInstance = usedLayersInstance;

        // Eager instance objects
        activation = new ActivationDelegate( this );
        moduleInstances = new ArrayList<>();
    }

    @Override
    public String toString()
    {
        return layerModel.toString();
    }

    // Implementation of Layer
    @Override
    public String name()
    {
        return layerModel.name();
    }

    // Implementation of MetaInfoHolder
    @Override
    public <T> T metaInfo( Class<T> infoType )
    {
        return layerModel.metaInfo( infoType );
    }

    // Implementation of Activation
    @Override
    public void activate()
        throws ActivationException
    {
        activation.activate( layerModel.newActivatorsInstance(), moduleInstances );
    }

    @Override
    public void passivate()
        throws PassivationException
    {
        activation.passivate();
    }

    @Override
    public void registerActivationEventListener( ActivationEventListener listener )
    {
        activation.registerActivationEventListener( listener );
    }

    @Override
    public void deregisterActivationEventListener( ActivationEventListener listener )
    {
        activation.deregisterActivationEventListener( listener );
    }

    // Other methods
    /* package */ void addModule( ModuleInstance module )
    {
        module.registerActivationEventListener( activation );
        moduleInstances.add( module );
    }

    /* package */ LayerModel model()
    {
        return layerModel;
    }

    public ApplicationInstance applicationInstance()
    {
        return applicationInstance;
    }

    /* package */ UsedLayersInstance usedLayersInstance()
    {
        return usedLayersInstance;
    }

    /* package */ Iterable<ModelModule<ObjectDescriptor>> visibleObjects( final Visibility visibility )
    {
        return flattenIterables( map( new Function<ModuleInstance, Iterable<ModelModule<ObjectDescriptor>>>()
        {

            @Override
            public Iterable<ModelModule<ObjectDescriptor>> map( ModuleInstance moduleInstance )
            {
                return moduleInstance.visibleObjects( visibility );
            }
        }, moduleInstances ) );
    }

    /* package */ Iterable<ModelModule<TransientDescriptor>> visibleTransients( final Visibility visibility )
    {
        return flattenIterables( map( new Function<ModuleInstance, Iterable<ModelModule<TransientDescriptor>>>()
        {

            @Override
            public Iterable<ModelModule<TransientDescriptor>> map( ModuleInstance moduleInstance )
            {
                return moduleInstance.visibleTransients( visibility );
            }
        }, moduleInstances ) );
    }

    /* package */ Iterable<ModelModule<EntityDescriptor>> visibleEntities( final Visibility visibility )
    {
        return flattenIterables( map( new Function<ModuleInstance, Iterable<ModelModule<EntityDescriptor>>>()
        {

            @Override
            public Iterable<ModelModule<EntityDescriptor>> map( ModuleInstance moduleInstance )
            {
                return moduleInstance.visibleEntities( visibility );
            }
        }, moduleInstances ) );
    }

    /* package */ Iterable<ModelModule<ValueDescriptor>> visibleValues( final Visibility visibility )
    {
        return flattenIterables( map( new Function<ModuleInstance, Iterable<ModelModule<ValueDescriptor>>>()
        {

            @Override
            public Iterable<ModelModule<ValueDescriptor>> map( ModuleInstance moduleInstance )
            {
                return moduleInstance.visibleValues( visibility );
            }
        }, moduleInstances ) );
    }

    /* package */ Iterable<ServiceReference<?>> visibleServices( final Visibility visibility )
    {
        return flattenIterables( map( new Function<ModuleInstance, Iterable<ServiceReference<?>>>()
        {

            @Override
            public Iterable<ServiceReference<?>> map( ModuleInstance moduleInstance )
            {
                return moduleInstance.visibleServices( visibility );
            }
        }, moduleInstances ) );
    }

    /* package */ ModuleInstance findModule( String moduleName )
    {
        for( ModuleInstance moduleInstance : moduleInstances )
        {
            if( moduleInstance.model().name().equals( moduleName ) )
            {
                return moduleInstance;
            }
        }

        throw new IllegalArgumentException( "No such module:" + moduleName );
    }
}
