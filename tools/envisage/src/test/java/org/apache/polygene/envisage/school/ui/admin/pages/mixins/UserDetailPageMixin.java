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

package org.apache.polygene.envisage.school.ui.admin.pages.mixins;

import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.structure.Layer;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.envisage.school.domain.person.Person;
import org.apache.polygene.envisage.school.ui.admin.pages.UserDetailPage;

public class UserDetailPageMixin
    implements UserDetailPage
{
    // Note: Don't remove
    @Structure
    private Layer layer;

    // Note: Don't remove
    public UserDetailPageMixin( @Structure Module module )
    {
    }

    @Override
    public void edit( Person context )
    {
        System.err.println( "Edit user [" + context + "]" );
    }

    @Override
    public String generateHtml()
    {
        return "UserDetailPage";
    }
}
