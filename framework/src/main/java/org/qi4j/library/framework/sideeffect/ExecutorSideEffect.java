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

package org.qi4j.library.framework.sideeffect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import org.qi4j.composite.AppliesTo;
import org.qi4j.composite.GenericSideEffect;
import org.qi4j.composite.ObjectBuilderFactory;
import org.qi4j.composite.scope.Invocation;
import org.qi4j.composite.scope.Service;
import org.qi4j.composite.scope.Structure;

/**
 * TODO
 */
@AppliesTo( ExecuteSideEffect.class )
public class ExecutorSideEffect extends GenericSideEffect
{
    @Structure ObjectBuilderFactory obf;
    @Service Executor executor;

    @Invocation ExecuteSideEffect execute;

    @Override public Object invoke( final Object o, final Method method, final Object[] objects ) throws Throwable
    {
        executor.execute( new Runnable()
        {
            public void run()
            {
                Object executed = obf.newObject( execute.value() );
                if( executed instanceof InvocationHandler )
                {
                    try
                    {
                        ( (InvocationHandler) executed ).invoke( o, method, objects );
                    }
                    catch( Throwable throwable )
                    {
                        throwable.printStackTrace();
                    }
                }
                else
                {
                    try
                    {
                        method.invoke( executed, objects );
                    }
                    catch( IllegalAccessException e )
                    {
                        e.printStackTrace();
                    }
                    catch( InvocationTargetException e )
                    {
                        e.printStackTrace();
                    }
                }
            }
        } );

        return super.invoke( o, method, objects );
    }
}
