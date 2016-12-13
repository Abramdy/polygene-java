/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package org.apache.zest.library.rdf.model;

import org.apache.zest.api.composite.MethodDescriptor;
import org.apache.zest.api.composite.TransientDescriptor;
import org.apache.zest.api.entity.EntityDescriptor;
import org.apache.zest.api.object.ObjectDescriptor;
import org.apache.zest.api.structure.ApplicationDescriptor;
import org.apache.zest.api.structure.LayerDescriptor;
import org.apache.zest.api.structure.ModuleDescriptor;
import org.apache.zest.api.util.HierarchicalVisitorAdapter;
import org.apache.zest.library.rdf.PolygeneRdf;
import org.apache.zest.library.rdf.serializer.SerializerContext;

/**
 * JAVADOC
 */
class ApplicationVisitor extends HierarchicalVisitorAdapter<Object, Object, RuntimeException>
{
    private SerializerContext context;

    private String appUri;

    private String layerUri;

    private String moduleUri;

    private String compositeUri;

    ApplicationVisitor( SerializerContext context )
    {
        this.context = context;
    }

    @Override
    public boolean visitEnter( Object visited )
        throws RuntimeException
    {
        if( visited instanceof ApplicationDescriptor )
        {
            ApplicationDescriptor applicationDescriptor = (ApplicationDescriptor) visited;
            appUri = context.createApplicationUri( applicationDescriptor.name() );
            context.setNameAndType( appUri, applicationDescriptor.name(), PolygeneRdf.TYPE_APPLICATION );
        }

        if( visited instanceof LayerDescriptor )
        {
            LayerDescriptor layerDescriptor = (LayerDescriptor) visited;
            layerUri = context.createLayerUri( appUri, layerDescriptor.name() );
            context.setNameAndType( layerUri, layerDescriptor.name(), PolygeneRdf.TYPE_LAYER );
            context.addRelationship( appUri, PolygeneRdf.RELATIONSHIP_LAYER, layerUri );
        }

        if( visited instanceof ModuleDescriptor )
        {
            ModuleDescriptor moduleDescriptor = (ModuleDescriptor) visited;
            moduleUri = context.createModuleUri( layerUri, moduleDescriptor.name() );
            context.setNameAndType( layerUri, moduleDescriptor.name(), PolygeneRdf.TYPE_MODULE );

            context.addRelationship( layerUri, PolygeneRdf.RELATIONSHIP_MODULE, moduleUri );
        }

        if( visited instanceof TransientDescriptor )
        {
            TransientDescriptor transientDescriptor = (TransientDescriptor) visited;
            compositeUri = context.createCompositeUri( moduleUri, transientDescriptor.types().findFirst().orElse( null ) );
            context.addType( compositeUri, PolygeneRdf.TYPE_COMPOSITE );
            context.addRelationship( moduleUri, PolygeneRdf.RELATIONSHIP_COMPOSITE, compositeUri );
        }

        if( visited instanceof EntityDescriptor )
        {
            EntityDescriptor entityDescriptor = (EntityDescriptor) visited;
            compositeUri = context.createCompositeUri( moduleUri, entityDescriptor.types().findFirst().orElse( null ));
            context.addType( compositeUri, PolygeneRdf.TYPE_ENTITY );
            context.addRelationship( moduleUri, PolygeneRdf.RELATIONSHIP_ENTITY, compositeUri );
        }

        if( visited instanceof ObjectDescriptor )
        {
            ObjectDescriptor objectDescriptor = (ObjectDescriptor) visited;
            compositeUri = context.createCompositeUri( moduleUri, objectDescriptor.types().findFirst().orElse( null ) );
            context.addType( compositeUri, PolygeneRdf.TYPE_OBJECT );
            context.addRelationship( moduleUri, PolygeneRdf.RELATIONSHIP_OBJECT, compositeUri );
        }

        if( visited instanceof MethodDescriptor )
        {
            MethodDescriptor compositeMethodDescriptor = (MethodDescriptor) visited;
            String compositeMethodUri = context.createCompositeMethodUri( compositeUri, compositeMethodDescriptor.method() );
            context.addType( compositeMethodUri, PolygeneRdf.TYPE_METHOD );
            context.addRelationship( compositeUri, PolygeneRdf.RELATIONSHIP_METHOD, compositeMethodUri );
        }

        return true;
    }
}