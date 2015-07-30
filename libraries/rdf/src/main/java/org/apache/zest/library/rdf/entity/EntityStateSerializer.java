/*
 * Copyright (c) 2008-2011, Rickard Öberg. All Rights Reserved.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zest.library.rdf.entity;

import org.openrdf.model.BNode;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.GraphImpl;
import org.apache.zest.api.Qi4j;
import org.apache.zest.api.association.AssociationDescriptor;
import org.apache.zest.api.composite.Composite;
import org.apache.zest.api.entity.EntityDescriptor;
import org.apache.zest.api.entity.EntityReference;
import org.apache.zest.api.injection.scope.Service;
import org.apache.zest.api.property.PropertyDescriptor;
import org.apache.zest.api.service.qualifier.Tagged;
import org.apache.zest.api.type.ValueCompositeType;
import org.apache.zest.api.type.ValueType;
import org.apache.zest.api.util.Classes;
import org.apache.zest.api.value.ValueComposite;
import org.apache.zest.api.value.ValueSerialization;
import org.apache.zest.api.value.ValueSerializer;
import org.apache.zest.api.value.ValueSerializer.Options;
import org.apache.zest.library.rdf.Rdfs;
import org.apache.zest.spi.entity.EntityState;
import org.apache.zest.spi.entity.ManyAssociationState;

import static org.apache.zest.functional.Iterables.first;

/**
 * JAVADOC
 */
public class EntityStateSerializer
{

    @Service
    @Tagged( ValueSerialization.Formats.JSON )
    private ValueSerializer valueSerializer;

    public URI createEntityURI( ValueFactory valueFactory, EntityReference identity )
    {
        return valueFactory.createURI( identity.toURI() );
    }

    public Iterable<Statement> serialize( final EntityState entityState )
    {
        return serialize( entityState, true );
    }

    public Iterable<Statement> serialize( final EntityState entityState,
                                          final boolean includeNonQueryable )
    {
        Graph graph = new GraphImpl();
        serialize( entityState, includeNonQueryable, graph );
        return graph;
    }

    public void serialize( final EntityState entityState,
                           final boolean includeNonQueryable,
                           final Graph graph )
    {
        ValueFactory values = graph.getValueFactory();
        EntityReference identity = entityState.identity();
        URI entityUri = createEntityURI( values, identity );

        graph.add( entityUri,
                   Rdfs.TYPE,
                   values.createURI( Classes.toURI( first( entityState.entityDescriptor().types() ) ) ) );

        serializeProperties( entityState,
                             graph,
                             entityUri,
                             entityState.entityDescriptor(),
                             includeNonQueryable );

        serializeAssociations( entityState,
                               graph,
                               entityUri,
                               entityState.entityDescriptor().state().associations(),
                               includeNonQueryable );

        serializeManyAssociations( entityState,
                                   graph,
                                   entityUri,
                                   entityState.entityDescriptor().state().manyAssociations(),
                                   includeNonQueryable );
    }

    private void serializeProperties( final EntityState entityState,
                                      final Graph graph,
                                      final Resource subject,
                                      final EntityDescriptor entityType,
                                      final boolean includeNonQueryable )
    {
        // Properties
        for( PropertyDescriptor persistentProperty : entityType.state().properties() )
        {
            Object property = entityState.propertyValueOf( persistentProperty.qualifiedName() );
            if( property != null )
            {
                serializeProperty( persistentProperty, property, subject, graph, includeNonQueryable );
            }
        }
    }

    private void serializeProperty( PropertyDescriptor persistentProperty,
                                    Object property,
                                    Resource subject,
                                    Graph graph,
                                    boolean includeNonQueryable )
    {
        if( !( includeNonQueryable || persistentProperty.queryable() ) )
        {
            return; // Skip non-queryable
        }

        ValueType valueType = persistentProperty.valueType();

        final ValueFactory valueFactory = graph.getValueFactory();

        String propertyURI = persistentProperty.qualifiedName().toURI();
        URI predicate = valueFactory.createURI( propertyURI );
        String baseURI = propertyURI.substring( 0, propertyURI.indexOf( '#' ) ) + "/";

        if( valueType instanceof ValueCompositeType )
        {
            serializeValueComposite( subject, predicate, (ValueComposite) property, valueType,
                                     graph, baseURI, includeNonQueryable );
        }
        else
        {
            String stringProperty = valueSerializer.serialize( new Options().withoutTypeInfo(), property );
            final Literal object = valueFactory.createLiteral( stringProperty );
            graph.add( subject, predicate, object );
        }
    }

    private void serializeValueComposite( Resource subject, URI predicate,
                                          ValueComposite value,
                                          ValueType valueType,
                                          Graph graph,
                                          String baseUri,
                                          boolean includeNonQueryable )
    {
        final ValueFactory valueFactory = graph.getValueFactory();
        BNode collection = valueFactory.createBNode();
        graph.add( subject, predicate, collection );

        for( PropertyDescriptor persistentProperty : ( (ValueCompositeType) valueType ).properties() )
        {
            Object propertyValue = Qi4j.FUNCTION_COMPOSITE_INSTANCE_OF
                .map( (Composite) value )
                .state()
                .propertyFor( persistentProperty.accessor() )
                .get();

            if( propertyValue == null )
            {
                continue; // Skip null values
            }

            ValueType type = persistentProperty.valueType();
            if( type instanceof ValueCompositeType )
            {
                URI pred = valueFactory.createURI( baseUri, persistentProperty.qualifiedName().name() );
                serializeValueComposite( collection, pred, (ValueComposite) propertyValue, type, graph,
                                         baseUri + persistentProperty.qualifiedName().name() + "/",
                                         includeNonQueryable );
            }
            else
            {
                serializeProperty( persistentProperty, propertyValue, collection, graph, includeNonQueryable );
            }
        }
    }

    private void serializeAssociations( final EntityState entityState,
                                        final Graph graph, URI entityUri,
                                        final Iterable<? extends AssociationDescriptor> associations,
                                        final boolean includeNonQueryable )
    {
        ValueFactory values = graph.getValueFactory();

        // Associations
        for( AssociationDescriptor associationType : associations )
        {
            if( !( includeNonQueryable || associationType.queryable() ) )
            {
                continue; // Skip non-queryable
            }

            EntityReference associatedId = entityState.associationValueOf( associationType.qualifiedName() );
            if( associatedId != null )
            {
                URI assocURI = values.createURI( associationType.qualifiedName().toURI() );
                URI assocEntityURI = values.createURI( associatedId.toURI() );
                graph.add( entityUri, assocURI, assocEntityURI );
            }
        }
    }

    private void serializeManyAssociations( final EntityState entityState,
                                            final Graph graph,
                                            final URI entityUri,
                                            final Iterable<? extends AssociationDescriptor> associations,
                                            final boolean includeNonQueryable )
    {
        ValueFactory values = graph.getValueFactory();

        // Many-Associations
        for( AssociationDescriptor associationType : associations )
        {
            if( !( includeNonQueryable || associationType.queryable() ) )
            {
                continue; // Skip non-queryable
            }

            BNode collection = values.createBNode();
            graph.add( entityUri, values.createURI( associationType.qualifiedName().toURI() ), collection );
            graph.add( collection, Rdfs.TYPE, Rdfs.SEQ );

            ManyAssociationState associatedIds = entityState.manyAssociationValueOf( associationType.qualifiedName() );
            for( EntityReference associatedId : associatedIds )
            {
                URI assocEntityURI = values.createURI( associatedId.toURI() );
                graph.add( collection, Rdfs.LIST_ITEM, assocEntityURI );
            }
        }
    }
}
