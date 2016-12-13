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
package org.apache.zest.tools.model.descriptor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.zest.api.common.Visibility;
import org.apache.zest.api.composite.CompositeDescriptor;
import org.apache.zest.api.service.ImportedServiceDescriptor;
import org.apache.zest.api.service.ServiceImporter;
import org.apache.zest.api.structure.ModuleDescriptor;
import org.apache.zest.api.util.Classes;

/**
 * XXX Workaround for inconsistency in Polygene core-api/spi
 * ImportedServiceDescriptor wrapper as composite
 */
public class ImportedServiceCompositeDescriptor
    implements CompositeDescriptor
{
    protected ImportedServiceDescriptor importedService;
    protected final List<Class<?>> mixins = new LinkedList<>();

    public ImportedServiceCompositeDescriptor( ImportedServiceDescriptor descriptor )
    {
        this.importedService = descriptor;
    }

    public ImportedServiceDescriptor importedService()
    {
        return importedService;
    }

    @Override
    public ModuleDescriptor module()
    {
        return importedService.module();
    }

    @Override
    public Stream<Class<?>> mixinTypes()
    {
        return mixins.stream();
    }

    @Override
    public Class<?> primaryType()
    {
        return importedService.type();
    }

    @Override
    public Visibility visibility()
    {
        return importedService.visibility();
    }

    @Override
    public <T> T metaInfo( Class<T> infoType )
    {
        return importedService.metaInfo( infoType );
    }

    @Override
    public Stream<Class<?>> types()
    {
        return importedService.types();
    }

    @Override
    public boolean isAssignableTo( Class<?> type )
    {
        return importedService.isAssignableTo( type );
    }

    public Class<? extends ServiceImporter> serviceImporter()
    {
        return importedService.serviceImporter();
    }

    public String toURI()
    {
        return Classes.toURI( primaryType() );
    }
}
