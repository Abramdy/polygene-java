/*
 * Copyright (c) 2007, Rickard �berg. All Rights Reserved.
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

package org.qi4j.composite;

import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;
import org.qi4j.test.model1.Object1;

/**
 * TODO
 */
public class ObjectPropertyTest
    extends AbstractQi4jTest
{
    @Override public void configure( ModuleAssembly module )
    {
        module.addObjects( Object1.class );
    }

    public void testPropertyInjection()
        throws Exception
    {
        ObjectBuilder<Object1> builder = objectBuilderFactory.newObjectBuilder( Object1.class );
        builder.properties( PropertyValue.property( "foo", "Test1" ) );
        builder.properties( PropertyValue.property( "bar", "Test2" ) );
        builder.properties( PropertyValue.property( "xyzzy", 42 ) );

        Object1 object = builder.newInstance();

        assertEquals( "Test1", object.getFoo() );
        assertEquals( "Test2", object.getBar() );
        assertEquals( 42, object.getXyzzy() );
    }
}
