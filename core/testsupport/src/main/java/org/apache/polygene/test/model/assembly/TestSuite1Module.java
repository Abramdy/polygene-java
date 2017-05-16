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

package org.apache.polygene.test.model.assembly;

import org.apache.polygene.api.common.Visibility;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;
import org.apache.polygene.test.indexing.layered.Suite1Case1;
import org.apache.polygene.test.indexing.layered.Suite1Case2;
import org.apache.polygene.test.indexing.layered.TestCase;

class TestSuite1Module
    implements ModuleAssembler
{

    @Override
    public ModuleAssembly assemble( LayerAssembly layer, ModuleAssembly module )
        throws AssemblyException
    {
        declareTestCase( module, Suite1Case1.class );
        declareTestCase( module, Suite1Case2.class );
        return module;
    }

    private void declareTestCase( ModuleAssembly module, Class<?> testcaseMixin )
    {
        module.services( TestCase.class )
            .withMixins( testcaseMixin )
            .visibleIn( Visibility.layer )
            .taggedWith( testcaseMixin.getSimpleName() );

    }
}
