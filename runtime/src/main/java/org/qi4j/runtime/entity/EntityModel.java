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

import org.qi4j.api.common.ConstructionException;
import org.qi4j.api.common.MetaInfo;
import org.qi4j.api.common.QualifiedName;
import org.qi4j.api.common.Visibility;
import org.qi4j.api.composite.CompositeInstance;
import org.qi4j.api.constraint.ConstraintViolationException;
import org.qi4j.api.entity.*;
import org.qi4j.api.entity.association.AssociationDescriptor;
import org.qi4j.api.property.Immutable;
import org.qi4j.api.property.PersistentPropertyDescriptor;
import org.qi4j.api.property.StateHolder;
import org.qi4j.api.unitofwork.EntityCompositeAlreadyExistsException;
import org.qi4j.api.util.Annotations;
import org.qi4j.api.util.Classes;
import org.qi4j.bootstrap.AssociationDeclarations;
import org.qi4j.bootstrap.ManyAssociationDeclarations;
import org.qi4j.bootstrap.PropertyDeclarations;
import org.qi4j.functional.Iterables;
import org.qi4j.runtime.bootstrap.AssemblyHelper;
import org.qi4j.runtime.composite.*;
import org.qi4j.runtime.entity.association.AssociationsModel;
import org.qi4j.runtime.entity.association.ManyAssociationsModel;
import org.qi4j.runtime.property.PersistentPropertyModel;
import org.qi4j.runtime.structure.ModuleInstance;
import org.qi4j.runtime.structure.ModuleUnitOfWork;
import org.qi4j.spi.entity.EntityState;
import org.qi4j.spi.entitystore.EntityAlreadyExistsException;
import org.qi4j.spi.entitystore.EntityStoreException;
import org.qi4j.spi.entitystore.EntityStoreUnitOfWork;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * JAVADOC
 */
public final class EntityModel
    extends CompositeModel
    implements EntityDescriptor
{
    private static final Method IDENTITY_METHOD;

    static
    {
        try
        {
            IDENTITY_METHOD = Identity.class.getMethod( "identity" );
        }
        catch( NoSuchMethodException e )
        {
            throw new InternalError( "Qi4j Core Runtime codebase is corrupted. Contact Qi4j team: ModuleUnitOfWork" );
        }
    }

    private final boolean queryable;

    public EntityModel( Class<?> type,
                        Iterable<Class<?>> types,
                         Visibility visibility,
                         MetaInfo info,
                         EntityMixinsModel mixinsModel,
                         EntityStateModel stateModel,
                         CompositeMethodsModel compositeMethodsModel
    )
    {
        super( type, types, visibility, info, mixinsModel, stateModel, compositeMethodsModel );

        final Queryable queryable = Iterables.first( Iterables.<Queryable, Annotation>cast(Iterables.filter( Annotations.isType( Queryable.class ), Iterables.flattenIterables( Iterables.map( Annotations.ANNOTATIONS_OF, types ) ) )));
        this.queryable = queryable == null || queryable.value();
    }

    public boolean queryable()
    {
        return queryable;
    }

    @Override
    public EntityStateModel state()
    {
        return (EntityStateModel) super.state();
    }

    public boolean hasRole( Class roleType )
    {
        return roleType.isAssignableFrom( proxyClass );
    }

    public EntityInstance newInstance( ModuleUnitOfWork uow, ModuleInstance moduleInstance, EntityState state )
    {
        EntityInstance instance = new EntityInstance( uow, moduleInstance, this, state );
        return instance;
    }

    public Object[] newMixinHolder()
    {
        return mixinsModel.newMixinHolder();
    }

    public EntityStateModel.EntityStateInstance newStateHolder( ModuleUnitOfWork uow, EntityState entityState )
    {
        return ( (EntityStateModel) stateModel ).newInstance( uow, entityState );
    }

    public Object newMixin( Object[] mixins,
                            EntityStateModel.EntityStateInstance entityState,
                            EntityInstance entityInstance,
                            Method method
    )
    {
        return ( (EntityMixinsModel) mixinsModel ).newMixin( entityInstance, entityState, mixins, method );
    }

    public EntityState newEntityState( EntityStoreUnitOfWork store, EntityReference identity )
        throws ConstraintViolationException, EntityStoreException
    {
        try
        {
            // New EntityState
            EntityState entityState = store.newEntityState( identity, this );

            // Set identity property
            PersistentPropertyDescriptor persistentPropertyDescriptor = state().getPropertyByQualifiedName( QualifiedName.fromAccessor( IDENTITY_METHOD ) );
            entityState.setProperty( persistentPropertyDescriptor.qualifiedName(), identity.identity() );

            return entityState;
        }
        catch( EntityAlreadyExistsException e )
        {
            throw new EntityCompositeAlreadyExistsException( identity );
        }
        catch( EntityStoreException e )
        {
            throw new ConstructionException( "Could not create new entity in store", e );
        }
    }

    @Override
    public String toString()
    {
        return type().getName();
    }

    public void initState( ModuleInstance module, EntityState entityState )
    {
        {
            // Set new properties to default value
            Set<PersistentPropertyModel> entityProperties = state().properties();
            for( PersistentPropertyModel propertyDescriptor : entityProperties )
            {
                entityState.setProperty( propertyDescriptor.qualifiedName(), propertyDescriptor.initialValue( module ) );
            }
        }

        {
            // Set new manyAssociations to null
            Set<AssociationDescriptor> entityAssociations = state().associations();
            for( AssociationDescriptor associationDescriptor : entityAssociations )
            {
                entityState.setAssociation( associationDescriptor.qualifiedName(), null );
            }
        }

        {
            // Set new many-manyAssociations to empty
            Set<AssociationDescriptor> entityAssociations = state().manyAssociations();
            for( AssociationDescriptor associationDescriptor : entityAssociations )
            {
                entityState.getManyAssociation( associationDescriptor.qualifiedName() );
            }
        }
    }

    public void invokeLifecycle( boolean create, Object[] mixins, CompositeInstance instance, StateHolder state )
    {
        ( (EntityMixinsModel) mixinsModel ).invokeLifecycle( create, mixins, instance, state );
    }
}
