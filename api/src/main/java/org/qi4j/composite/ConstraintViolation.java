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

package org.qi4j.composite;

import java.lang.annotation.Annotation;

/**
 * When a constraint violation has occurred it is registered as a ConstraintViolation
 * and exposed through the InvocationContext for concerns and mixins to use.
 */
public class ConstraintViolation
{
    private Annotation constraint;
    private Object value;

    public ConstraintViolation( Annotation constraint, Object value )
    {
        this.constraint = constraint;
        this.value = value;
    }

    public Annotation constraint()
    {
        return constraint;
    }

    public Object value()
    {
        return value;
    }
}
