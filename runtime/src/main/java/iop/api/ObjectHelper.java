/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
 * Copyright (c) 2007, Niclas Hedhman. All Rights Reserved.
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
package iop.api;

import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import iop.runtime.ProxyReferenceInvocationHandler;
import iop.runtime.ObjectInvocationHandler;

public final class ObjectHelper
{
    public static <T> T getThat( T proxy )
    {
        InvocationHandler handler = Proxy.getInvocationHandler( proxy );
        if( handler instanceof ProxyReferenceInvocationHandler  )
        {
            return (T) ((ProxyReferenceInvocationHandler) handler).getProxy();
        }
        if( handler instanceof ObjectInvocationHandler )
        {
            return proxy;
        }
        return null;
    }
}
