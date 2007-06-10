/*
 * Copyright 2007 Rickard Öberg
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
*/
package org.qi4j.api.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import org.qi4j.api.annotation.Modifies;

/**
 * Modifiers provide stateless modifications of method invocation behaviour.
 * <p/>
 * Modifiers can either be classes implementing the interfaces of the modified
 * methods, or they can be generic InvocationHandler mixins.
 *
 * @author rickard
 * @version $Revision: 1.0 $
 */
public final class Modifier
    extends Fragment
{
    Field modifiesField;

    // Constructors --------------------------------------------------
    public Modifier( Class modifierClass )
    {
        super( modifierClass );

        this.modifiesField = findModifies( modifierClass );

    }

    public Field getModifiesField()
    {
        return modifiesField;
    }

    // Object overrides ---------------------------------------------

    public String toString()
    {
        String string = super.toString();

        StringWriter str = new StringWriter();
        PrintWriter out = new PrintWriter( str );
        out.println( "  @Modifies" );
        out.println( "    " + modifiesField.getType().getName() );

        if( appliesTo != null )
        {
            out.println( "  @AppliesTo" );
            out.println( "    " + appliesTo.getName() );
        }
        out.close();
        return string + str.toString();
    }

    // Private ------------------------------------------------------
    private Field findModifies( Class<? extends Object> aModifierClass )
    {
        Field[] fields = aModifierClass.getDeclaredFields();
        for( Field field : fields )
        {
            if( field.getAnnotation( Modifies.class ) != null )
            {
                field.setAccessible( true );
                return field;
            }
        }

        Class<?> parent = aModifierClass.getSuperclass();
        if( parent != Object.class )
        {
            return findModifies( parent );
        }
        else
        {
            return null;
        }
    }

}
