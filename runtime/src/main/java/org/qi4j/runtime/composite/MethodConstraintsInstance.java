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

package org.qi4j.runtime.composite;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.qi4j.composite.Composite;
import org.qi4j.composite.ConstraintViolation;
import org.qi4j.composite.ParameterConstraintViolationException;

/**
 * TODO
 */
public final class MethodConstraintsInstance
{
    private List<ValueConstraintsInstance> valueConstraintsInstances;
    private Method method;

    public MethodConstraintsInstance()
    {
    }

    public MethodConstraintsInstance( Method method, List<ValueConstraintsModel> parameterConstraintsModels )
    {
        this.method = method;
        valueConstraintsInstances = new ArrayList<ValueConstraintsInstance>();
        for( ValueConstraintsModel parameterConstraintModel : parameterConstraintsModels )
        {
            ValueConstraintsInstance valueConstraintsInstance = parameterConstraintModel.newInstance( method );
            valueConstraintsInstances.add( valueConstraintsInstance );
        }
    }

    public void checkValid( Object instance, Object[] params )
        throws ParameterConstraintViolationException
    {
        if( valueConstraintsInstances == null )
        {
            return; // No constraints to check
        }

        List<ConstraintViolation> violations = null;
        for( int i = 0; i < params.length; i++ )
        {
            Object param = params[ i ];
            List<ConstraintViolation> paramViolations = valueConstraintsInstances.get( i ).checkConstraints( param );
            if( !paramViolations.isEmpty() )
            {
                if( violations == null )
                {
                    violations = new ArrayList<ConstraintViolation>();
                }
                violations.addAll( paramViolations );
            }
        }

        if( violations != null )
        {
            throw new ParameterConstraintViolationException( (Composite) instance, method, violations );
        }
    }
}
