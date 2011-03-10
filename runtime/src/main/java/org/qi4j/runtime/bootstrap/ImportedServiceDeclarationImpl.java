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

package org.qi4j.runtime.bootstrap;

import java.io.Serializable;
import java.util.List;
import org.qi4j.api.common.InvalidApplicationException;
import org.qi4j.api.common.MetaInfo;
import org.qi4j.api.common.Visibility;
import org.qi4j.api.service.ServiceImporter;
import org.qi4j.bootstrap.ImportedServiceDeclaration;
import org.qi4j.runtime.service.ImportedServiceModel;
import org.qi4j.spi.service.importer.InstanceImporter;

/**
 * Declaration of an imported Service.
 */
public final class ImportedServiceDeclarationImpl
    implements ImportedServiceDeclaration, Serializable
{
    private Iterable<ImportedServiceAssemblyImpl> assemblies;

    public ImportedServiceDeclarationImpl( Iterable<ImportedServiceAssemblyImpl> assemblies)
    {
        this.assemblies = assemblies;
    }

    public ImportedServiceDeclaration visibleIn( Visibility visibility )
    {
        for( ImportedServiceAssemblyImpl assembly : assemblies )
        {
            assembly.visibility = visibility;
        }
        return this;
    }

    public ImportedServiceDeclaration importedBy( Class<? extends ServiceImporter> sip )
    {
        for( ImportedServiceAssemblyImpl assembly : assemblies )
        {
            assembly.serviceProvider = sip;
        }
        return this;
    }

    public ImportedServiceDeclaration identifiedBy( String identity )
    {
        for( ImportedServiceAssemblyImpl assembly : assemblies )
        {
            assembly.identity = identity;
        }
        return this;
    }

    public ImportedServiceDeclaration setMetaInfo( Object serviceAttribute )
    {
        for( ImportedServiceAssemblyImpl assembly : assemblies )
        {
            assembly.metaInfo.set( serviceAttribute );
        }
        return this;
    }
}