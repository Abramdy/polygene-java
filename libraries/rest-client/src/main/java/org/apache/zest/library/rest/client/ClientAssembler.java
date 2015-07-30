/**
 *
 * Copyright 2009-2011 Rickard Öberg AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zest.library.rest.client;

import org.apache.zest.api.common.Visibility;
import org.apache.zest.bootstrap.Assembler;
import org.apache.zest.bootstrap.AssemblyException;
import org.apache.zest.bootstrap.ModuleAssembly;
import org.apache.zest.library.rest.client.api.ContextResourceClient;
import org.apache.zest.library.rest.client.api.ContextResourceClientFactory;
import org.apache.zest.library.rest.client.requestwriter.FormRequestWriter;
import org.apache.zest.library.rest.client.requestwriter.ValueCompositeRequestWriter;
import org.apache.zest.library.rest.client.responsereader.DefaultResponseReader;
import org.apache.zest.library.rest.client.responsereader.JSONResponseReader;
import org.apache.zest.library.rest.client.responsereader.TableResponseReader;

/**
 * JAVADOC
 */
public class ClientAssembler
   implements Assembler
{
    @Override
   public void assemble( ModuleAssembly module ) throws AssemblyException
   {
      module.objects( ContextResourceClientFactory.class, ContextResourceClient.class ).visibleIn( Visibility.application );

      module.objects( ResponseReaderDelegator.class,
            DefaultResponseReader.class,
            JSONResponseReader.class,
            TableResponseReader.class ).visibleIn( Visibility.application );

      module.objects(RequestWriterDelegator.class,
            FormRequestWriter.class,
            ValueCompositeRequestWriter.class).visibleIn(Visibility.application);
   }
}