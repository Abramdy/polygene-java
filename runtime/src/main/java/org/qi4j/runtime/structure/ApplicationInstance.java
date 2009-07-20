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

package org.qi4j.runtime.structure;

import org.qi4j.api.common.MetaInfo;
import org.qi4j.api.structure.Application;
import org.qi4j.api.structure.Layer;
import org.qi4j.api.structure.Module;
import org.qi4j.runtime.bootstrap.migration.MigrationImpl;
import org.qi4j.runtime.entity.EntityModel;
import org.qi4j.spi.Qi4jSPI;
import org.qi4j.spi.entity.EntityTypeRegistry;
import org.qi4j.spi.service.Activator;
import org.qi4j.spi.structure.ApplicationSPI;
import org.qi4j.spi.structure.DescriptorVisitor;

import java.util.List;

/**
 * JAVADOC
 */
public class ApplicationInstance
    implements Application, ApplicationSPI
{
    private final ApplicationModel model;
    private final Qi4jSPI runtime;
    private final List<LayerInstance> layerInstances;
    private MigrationImpl migration;
    private final Activator layerActivator;

    public ApplicationInstance( ApplicationModel model, Qi4jSPI runtime, List<LayerInstance> layerInstances, MigrationImpl migration )
    {
        this.model = model;
        this.runtime = runtime;
        this.layerInstances = layerInstances;
        this.migration = migration;
        layerActivator = new Activator();
    }

    public ApplicationModel model()
    {
        return model;
    }

    public Qi4jSPI runtime()
    {
        return runtime;
    }

    public String name()
    {
        return model.name();
    }

    public Mode mode()
    {
        return model.mode();
    }

    public MetaInfo metaInfo()
    {
        return model.metaInfo();
    }

    public List<LayerInstance> layers()
    {
        return layerInstances;
    }

    public Layer findLayer( String layerName )
    {
        for( LayerInstance layerInstance : layerInstances )
        {
            if( layerInstance.model().name().equals( layerName ) )
            {
                return layerInstance;
            }
        }

        return null;
    }

    public Module findModule( String layerName, String moduleName )
    {
        for( LayerInstance layerInstance : layerInstances )
        {
            if( layerInstance.model().name().equals( layerName ) )
            {
                return layerInstance.findModule( moduleName );
            }
        }

        return null;
    }

    public void activate()
        throws Exception
    {
        layerActivator.activate( layerInstances );
    }

    public void passivate()
        throws Exception
    {
        layerActivator.passivate();
    }

    public void visitDescriptor( DescriptorVisitor visitor )
    {
        model.visitDescriptor( visitor );
    }

    public void migrate()
    {
        // Find all types and corresponding registries
        visitInstance(new InstanceVisitor()
        {
            @Override
            public void visit(Module module)
            {
                EntitiesInstance entitiesInstance = ((ModuleInstance) module).entities();
                EntityTypeRegistry registry = entitiesInstance.entityTypeRegistry();
                entitiesInstance.model().visitModel(new ModelVisitor()
                {
                    @Override
                    public void visit(EntityModel entityModel)
                    {
                        
                        super.visit(entityModel);
                    }
                });
                super.visit(module);
            }
        });
    }

    public MigrationImpl migration()
    {
        return migration;
    }

    public void visitInstance(InstanceVisitor visitor)
    {
        visitor.visit(this);

        for (LayerInstance layerInstance : layerInstances)
        {
            visitor.visit(layerInstance);
            for (Module module : layerInstance.modules())
            {
                visitor.visit(module);
            }
        }
    }
}
