/*  Copyright 2008 Edward Yakop.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
* implied.
*
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.qi4j.library.swing.visualizer.model;

import static org.qi4j.composite.NullArgumentException.validateNotNull;
import org.qi4j.spi.composite.InjectedFieldDescriptor;

/**
 * @author edward.yakop@gmail.com
 * @since 0.5
 */
public final class InjectedFieldDetailDescriptor
{
    private final InjectedFieldDescriptor descriptor;
    private ObjectDetailDescriptor object;
    private MixinDetailDescriptor mixin;

    InjectedFieldDetailDescriptor( InjectedFieldDescriptor aDescriptor )
        throws IllegalArgumentException
    {
        validateNotNull( "aDescriptor", aDescriptor );
        descriptor = aDescriptor;
    }

    /**
     * @return Descriptor of this {@code InjectedFieldDetailDescriptor}. Never returns {@code null}.
     * @since 0.5
     */
    public final InjectedFieldDescriptor descriptor()
    {
        return descriptor;
    }

    /**
     * @return Object that own this {@code InjectedFieldDetailDescriptor}.
     *         If {@code null}, this {@code InjectedFieldDetailDescriptor} is owned by a mixin.
     * @see #mixin()
     * @since 0.5
     */
    public final ObjectDetailDescriptor object()
    {
        return object;
    }

    /**
     * @return Mixin that own this {@code InjectedFieldDetailDescriptor}.
     *         If {@code null}, this {@code InjectedFieldDetailDescriptor} is owned by an object.
     * @see #object()
     * @since 0.5
     */
    public final MixinDetailDescriptor mixin()
    {
        return mixin;
    }

    final void setObject( ObjectDetailDescriptor aDescriptor )
        throws IllegalArgumentException
    {
        validateNotNull( "aDescriptor", aDescriptor );

        object = aDescriptor;
    }

    final void setMixin( MixinDetailDescriptor aDescriptor )
        throws IllegalArgumentException
    {
        validateNotNull( "aDescriptor", aDescriptor );

        mixin = aDescriptor;
    }

    @Override
    public final String toString()
    {
        return descriptor.field().getName();
    }
}
