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
package org.apache.polygene.library.http;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContextListener;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.library.http.ConstraintInfo.Constraint;
import org.apache.polygene.library.http.ConstraintInfo.HttpMethod;

import static org.apache.polygene.api.common.Visibility.layer;

public final class Servlets
{

    private Servlets()
    {
    }

    public static ContextListenerDeclaration listen()
    {
        return new ContextListenerDeclaration();
    }

    public static ContextListenerAssembler addContextListeners( ContextListenerDeclaration... contextListenerDeclarations )
    {
        return new ContextListenerAssembler( contextListenerDeclarations );
    }

    public static class ContextListenerAssembler
    {

        final ContextListenerDeclaration[] contextListenerDeclarations;

        ContextListenerAssembler( ContextListenerDeclaration... eventListenerDeclarations )
        {
            this.contextListenerDeclarations = eventListenerDeclarations;
        }

        public void to( ModuleAssembly module )
                throws AssemblyException
        {
            for ( ContextListenerDeclaration contextListenerDeclaration : contextListenerDeclarations ) {
                module.services( contextListenerDeclaration.contextListener() ).
                        setMetaInfo( contextListenerDeclaration.contextListenerInfo() ).
                        instantiateOnStartup().visibleIn( layer );
            }
        }

    }

    public static class ContextListenerDeclaration
    {

        Class<? extends ServletContextListener> contextListener;

        Map<String, String> initParams = Collections.emptyMap();

        public <T extends ServletContextListener> ContextListenerDeclaration with( Class<T> contextListener )
        {
            this.contextListener = contextListener;
            return this;
        }

        public Class<? extends ServletContextListener> contextListener()
        {
            return contextListener;
        }

        public ContextListenerDeclaration withInitParams( Map<String, String> initParams )
        {
            this.initParams = initParams;
            return this;
        }

        private ContextListenerInfo contextListenerInfo()
        {
            return new ContextListenerInfo( initParams );
        }

    }

    public static ServletDeclaration serve( String path )
    {
        return new ServletDeclaration( path );
    }

    public static ServletAssembler addServlets( ServletDeclaration... servletDeclarations )
    {
        return new ServletAssembler( servletDeclarations );
    }

    public static class ServletAssembler
    {

        final ServletDeclaration[] servletDeclarations;

        ServletAssembler( ServletDeclaration... servletDeclarations )
        {
            this.servletDeclarations = servletDeclarations;
        }

        public void to( ModuleAssembly module )
                throws AssemblyException
        {
            for ( ServletDeclaration servletDeclaration : servletDeclarations ) {
                module.services( servletDeclaration.servlet() ).
                        setMetaInfo( servletDeclaration.servletInfo() ).
                        instantiateOnStartup().visibleIn( layer );
            }
        }

    }

    public static class ServletDeclaration
    {

        String path;

        Class<? extends Servlet> servlet;

        Map<String, String> initParams = Collections.emptyMap();

        ServletDeclaration( String path )
        {
            this.path = path;
        }

        public <T extends Servlet> ServletDeclaration with( Class<T> servlet )
        {
            this.servlet = servlet;
            return this;
        }

        public ServletDeclaration withInitParams( Map<String, String> initParams )
        {
            this.initParams = initParams;
            return this;
        }

        Class<? extends Servlet> servlet()
        {
            return servlet;
        }

        ServletInfo servletInfo()
        {
            return new ServletInfo( path, initParams );
        }

    }

    public static FilterAssembler filter( String path )
    {
        return new FilterAssembler( path );
    }

    public static FilterDeclaration addFilters( FilterAssembler... filterAssemblers )
    {
        return new FilterDeclaration( filterAssemblers );
    }

    public static class FilterDeclaration
    {

        final FilterAssembler[] filterAssemblers;

        FilterDeclaration( FilterAssembler... filterAssemblers )
        {
            this.filterAssemblers = filterAssemblers;
        }

        @SuppressWarnings( "unchecked" )
        public void to( ModuleAssembly module )
                throws AssemblyException
        {
            for ( FilterAssembler filterAssembler : filterAssemblers ) {
                module.services( filterAssembler.filter() ).
                        setMetaInfo( filterAssembler.filterInfo() ).
                        instantiateOnStartup().visibleIn( layer );
            }
        }

    }

    public static class FilterAssembler
    {

        String path;

        Class<? extends Filter> filter;

        EnumSet<DispatcherType> dispatchers;

        Map<String, String> initParams = Collections.emptyMap();

        FilterAssembler( String path )
        {
            this.path = path;
        }

        public <T extends Filter> FilterAssembler through(
                Class<T> filter )
        {
            this.filter = filter;
            return this;
        }

        public FilterAssembler on( DispatcherType first, DispatcherType... rest )
        {
            dispatchers = EnumSet.of( first, rest );
            return this;
        }

        public FilterAssembler withInitParams( Map<String, String> initParams )
        {
            this.initParams = initParams;
            return this;
        }

        Class<? extends Filter> filter()
        {
            return filter;
        }

        FilterInfo filterInfo()
        {
            return new FilterInfo( path, initParams, dispatchers );
        }

    }

    public static ConstraintAssembler constrain( String path )
    {
        return new ConstraintAssembler( path );
    }

    public static ConstraintDeclaration addConstraints( ConstraintAssembler... constraintAssemblers )
    {
        return new ConstraintDeclaration( constraintAssemblers );
    }

    public static class ConstraintDeclaration
    {

        private final ConstraintAssembler[] constraintAssemblers;

        private ConstraintDeclaration( ConstraintAssembler[] constraintAssemblers )
        {
            this.constraintAssemblers = constraintAssemblers;
        }

        public void to( ModuleAssembly module )
                throws AssemblyException
        {
            // TODO Refactor adding Map<ServiceAssembly,T> ServiceDeclaration.getMetaInfos( Class<T> type ); in bootstrap & runtime
            // This would allow removing the ConstraintServices instances and this horrible hack with random UUIDs
            for ( ConstraintAssembler eachAssembler : constraintAssemblers ) {
                module.addServices( ConstraintService.class ).identifiedBy( UUID.randomUUID().toString() ).setMetaInfo( eachAssembler.constraintInfo() );
            }
        }

    }

    public static class ConstraintAssembler
    {

        private final String path;

        private Constraint constraint;

        private HttpMethod[] omittedHttpMethods = new HttpMethod[]{};

        private ConstraintAssembler( String path )
        {
            this.path = path;
        }

        public ConstraintAssembler by( Constraint constraint )
        {
            this.constraint = constraint;
            return this;
        }

        public ConstraintAssembler butNotOn( HttpMethod... omittedHttpMethods )
        {
            this.omittedHttpMethods = omittedHttpMethods;
            return this;
        }

        ConstraintInfo constraintInfo()
        {
            return new ConstraintInfo( path, constraint, omittedHttpMethods );
        }

    }

}
