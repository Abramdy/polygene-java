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
package org.apache.polygene.library.osgi;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Properties;
import org.apache.polygene.api.activation.ActivatorAdapter;
import org.apache.polygene.api.activation.Activators;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.service.ServiceComposite;
import org.apache.polygene.api.service.ServiceReference;
import org.apache.polygene.api.service.qualifier.HasMetaInfo;
import org.apache.polygene.api.util.Classes;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import static org.apache.polygene.api.util.Classes.interfacesOf;

/**
 * Export Polygene services to an OSGi Bundle.
 */
@Mixins( OSGiServiceExporter.OSGiServiceExporterMixin.class )
@Activators( OSGiServiceExporter.Activator.class )
public interface OSGiServiceExporter
{
    void registerServices()
        throws Exception;

    void unregisterServices()
        throws Exception;

    class Activator
        extends ActivatorAdapter<ServiceReference<OSGiServiceExporter>>
    {

        @Override
        public void afterActivation( ServiceReference<OSGiServiceExporter> activated )
            throws Exception
        {
            activated.get().registerServices();
        }

        @Override
        public void beforePassivation( ServiceReference<OSGiServiceExporter> passivating )
            throws Exception
        {
            passivating.get().unregisterServices();
        }

    }

    abstract class OSGiServiceExporterMixin
        implements OSGiServiceExporter
    {

        @Service
        @HasMetaInfo( BundleContext.class )
        private Iterable<ServiceReference<ServiceComposite>> services;
        private ArrayList<ServiceRegistration> registrations = new ArrayList<>();

        @Override
        public void registerServices()
            throws Exception
        {
            for( ServiceReference<ServiceComposite> ref : services )
            {
                Class<? extends BundleContext> type = BundleContext.class;
                BundleContext context = ref.metaInfo( type );
                ServiceComposite service = ref.get();
                Dictionary properties = ref.metaInfo( Dictionary.class );
                if( properties == null )
                {
                    properties = new Properties();
                }
                properties.put( "org.apache.polygene.api.service.active", ref.isActive() );
                properties.put( "org.apache.polygene.api.service.available", ref.isAvailable() );
                properties.put( "org.apache.polygene.api.service.reference", ref.identity() );


                String[] interfaceNames = interfacesOf( service.getClass() )
                    .map( Classes.RAW_CLASS ).toArray( String[]::new );

                registrations.add( context.registerService( interfaceNames, service, properties ) );
            }
        }

        @Override
        public void unregisterServices()
            throws Exception
        {
            for( ServiceRegistration reg : registrations )
            {
                reg.unregister();
            }
        }

    }

}
