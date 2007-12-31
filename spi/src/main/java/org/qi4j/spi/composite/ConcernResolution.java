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
package org.qi4j.spi.composite;

/**
 * Modifiers provide stateless modifications of methodModel invocation behaviour.
 * <p/>
 * Modifiers can either be classes implementing the interfaces of the modified
 * methods, or they can be generic InvocationHandler mixins.
 */
public final class ConcernResolution
    extends ModifierResolution
{
    public ConcernResolution( ConcernModel concernModel, Iterable<ConstructorResolution> constructorResolutions, Iterable<FieldResolution> fieldResolutions, Iterable<MethodResolution> methodResolutions )
    {
        super( concernModel, constructorResolutions, fieldResolutions, methodResolutions );
    }

    public ConcernModel getConcernModel()
    {
        return (ConcernModel) getAbstractModel();
    }
}