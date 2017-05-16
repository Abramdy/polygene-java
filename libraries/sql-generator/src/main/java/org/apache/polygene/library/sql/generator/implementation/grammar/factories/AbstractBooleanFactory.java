/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package org.apache.polygene.library.sql.generator.implementation.grammar.factories;

import org.apache.polygene.library.sql.generator.grammar.booleans.InPredicate;
import org.apache.polygene.library.sql.generator.grammar.builders.booleans.BooleanBuilder;
import org.apache.polygene.library.sql.generator.grammar.common.NonBooleanExpression;
import org.apache.polygene.library.sql.generator.grammar.factories.BooleanFactory;
import org.apache.polygene.library.sql.generator.implementation.grammar.common.SQLFactoryBase;
import org.apache.polygene.library.sql.generator.implementation.transformation.spi.SQLProcessorAggregator;
import org.apache.polygene.library.sql.generator.vendor.SQLVendor;

/**
 * @author Stanislav Muhametsin
 */
public abstract class AbstractBooleanFactory extends SQLFactoryBase
    implements BooleanFactory
{

    protected AbstractBooleanFactory( SQLVendor vendor, SQLProcessorAggregator processor )
    {
        super( vendor, processor );
    }

    public BooleanBuilder booleanBuilder()
    {
        return this.booleanBuilder( null );
    }

    public InPredicate in( NonBooleanExpression what, NonBooleanExpression... values )
    {
        return this.inBuilder( what ).addValues( values ).createExpression();
    }
}
