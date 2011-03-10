/*  Copyright 2008 Rickard Öberg.
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
package org.qi4j.entitystore.file;

import java.io.File;
import org.junit.After;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.spi.uuid.UuidIdentityGeneratorService;
import org.qi4j.test.entity.AbstractEntityStoreTest;

/**
 * JAVADOC
 */
public class FileEntityStoreTest
    extends AbstractEntityStoreTest
{
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        super.assemble( module );
        module.services( FileEntityStoreService.class, UuidIdentityGeneratorService.class );

        ModuleAssembly config = module.layerAssembly().moduleAssembly( "config" );
        config.entities( FileEntityStoreConfiguration.class ).visibleIn( Visibility.layer );
        config.services( MemoryEntityStoreService.class );
    }

    @Override
    @After
    public void tearDown()
        throws Exception
    {
        super.tearDown();
        File rootDirectory = new File( System.getProperty("user.dir"), "qi4j/filestore/" );
        deleteDir( rootDirectory );
    }

    @SuppressWarnings( { "ResultOfMethodCallIgnored" } )
    private void deleteDir( File dir )
    {
        File[] files = dir.listFiles();
        for( File file : files )
        {
            if( file.isDirectory())
                deleteDir( file );
            else
                file.delete();
        }
        dir.delete();
    }
}