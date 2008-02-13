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

package org.qi4j.spi.structure;

import java.util.Set;
import org.qi4j.spi.composite.ObjectModel;

/**
 * TODO
 */
public final class ObjectDescriptor
{
    private ObjectModel objectModel;
    private Set<Object> objectInfos;
    private Visibility visibility;

    public ObjectDescriptor( ObjectModel objectModel, Set<Object> objectInfos, Visibility visibility )
    {
        this.objectModel = objectModel;
        this.objectInfos = objectInfos;
        this.visibility = visibility;
    }

    public ObjectModel getObjectModel()
    {
        return objectModel;
    }

    public Set<Object> getObjectInfos()
    {
        return objectInfos;
    }

    public Visibility getVisibility()
    {
        return visibility;
    }


    public boolean equals( Object o )
    {
        if( this == o )
        {
            return true;
        }
        if( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        ObjectDescriptor that = (ObjectDescriptor) o;

        if( !objectModel.equals( that.objectModel ) )
        {
            return false;
        }

        return true;
    }

    public int hashCode()
    {
        return objectModel.hashCode();
    }
}
