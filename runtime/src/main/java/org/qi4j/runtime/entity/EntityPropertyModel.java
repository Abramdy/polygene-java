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

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.qi4j.api.common.MetaInfo;
import org.qi4j.api.constraint.ConstraintViolationException;
import org.qi4j.api.entity.Queryable;
import org.qi4j.api.property.GenericPropertyInfo;
import org.qi4j.api.property.Property;
import org.qi4j.api.property.PropertyInfo;
import org.qi4j.api.util.Classes;
import org.qi4j.runtime.composite.ValueConstraintsInstance;
import org.qi4j.runtime.property.AbstractPropertyModel;
import org.qi4j.runtime.structure.ModuleInstance;
import org.qi4j.runtime.unitofwork.UnitOfWorkInstance;
import org.qi4j.runtime.value.ValueModel;
import org.qi4j.spi.entity.EntityState;
import org.qi4j.spi.property.PropertyType;
import org.qi4j.spi.property.PropertyTypeDescriptor;
import org.qi4j.spi.value.CompoundType;
import org.qi4j.spi.value.ValueState;

/**
 * TODO
 */
public final class EntityPropertyModel extends AbstractPropertyModel
    implements PropertyTypeDescriptor
{
    private final boolean queryable;
    private final PropertyType propertyType;
    private final PropertyInfo propertyInfo;

    public EntityPropertyModel( Method anAccessor,
                                boolean immutable,
                                ValueConstraintsInstance constraints,
                                MetaInfo metaInfo,
                                Object defaultValue )
    {
        super( anAccessor, immutable, constraints, metaInfo, defaultValue );
        final Queryable queryable = anAccessor.getAnnotation( Queryable.class );
        this.queryable = queryable == null || queryable.value();

        PropertyType.PropertyTypeEnum type;
        if( isComputed() )
        {
            type = PropertyType.PropertyTypeEnum.COMPUTED;
        }
        else if( isImmutable() )
        {
            type = PropertyType.PropertyTypeEnum.IMMUTABLE;
        }
        else
        {
            type = PropertyType.PropertyTypeEnum.MUTABLE;
        }

        Type valueType = Classes.getRawClass( type() );

        propertyType = new PropertyType( qualifiedName(), createValueType( valueType), toURI(), toRDF(), this.queryable, type );

        propertyInfo = new GenericPropertyInfo( metaInfo, isImmutable(), isComputed(), name(), qualifiedName(), type() );
    }


    public PropertyType propertyType()
    {
        return propertyType;
    }

    public boolean isQueryable()
    {
        return queryable;
    }

    public Property<?> newInstance( Object value )
    {
        // Unused
        return null;
    }

    public Property newInstance( EntityState state, UnitOfWorkInstance uow )
    {
        Property property;
        if( isComputed() )
        {
            property = new ComputedPropertyInfo<Object>( propertyInfo );
        }
        else
        {
            property = new EntityPropertyInstance( this, state, this, uow );
        }

        return wrapProperty( property );
    }

    public void setState( Property property, EntityState entityState )
        throws ConstraintViolationException
    {
        Object value = property.get();

        // Check constraints
        checkConstraints( value, false );

        entityState.setProperty( qualifiedName(), value );
    }

    public <T> T getValue( ModuleInstance moduleInstance, ValueState valueState )
    {
        T value;
        if ( propertyType.type() instanceof CompoundType )
        {
            CompoundType compoundType = (CompoundType) propertyType.type();
            Class valueClass = moduleInstance.findClassForName( compoundType.type() );
            ValueModel model = (ValueModel) moduleInstance.findModuleForValue( valueClass ).findValueFor( valueClass );
            value = model.newValueInstance( moduleInstance, valueState ).<T>proxy();
        } else
        {
            value = (T) valueState.getProperty( qualifiedName() );
        }

        return value;
    }
}
