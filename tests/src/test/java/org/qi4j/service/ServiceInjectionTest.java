/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.service;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.bootstrap.SingletonAssembly;
import org.qi4j.composite.Mixins;
import org.qi4j.composite.scope.Service;
import org.qi4j.spi.service.provider.Singleton;
import org.qi4j.test.AbstractQi4jTest;

/**
 * TODO
 */
public class ServiceInjectionTest
    extends AbstractQi4jTest
{
    public void testInjectSingleton()
        throws Exception
    {
        SingletonAssembly assembly = new SingletonAssembly()
        {
            public void configure( ModuleAssembly module ) throws AssemblyException
            {
                module.addComposites( MyServiceComposite.class );
                module.addServices( Singleton.class, MyServiceComposite.class );
                module.addObjects( ServiceUser.class );
            }
        };

        ServiceUser user = assembly.getObjectBuilderFactory().newObjectBuilder( ServiceUser.class ).newInstance();

        assertEquals( "Hello World!", user.doStuff() );
    }

    public void configure( ModuleAssembly module )
        throws AssemblyException
    {
        module.addComposites( MyServiceComposite.class );
    }


    @Mixins( MyServiceMixin.class )
    public static interface MyServiceComposite
        extends MyService, ServiceComposite
    {
    }

    public static interface MyService
    {
        String doStuff();
    }

    public static class MyServiceMixin
        implements MyService
    {

        public String doStuff()
        {
            return "Hello World";
        }
    }

    public static class ServiceUser
    {
        @Service MyService service;

        public String doStuff()
        {
            return service.doStuff() + "!";
        }
    }
}
