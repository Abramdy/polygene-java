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

package org.apache.polygene.sample.rental.domain;

import java.time.LocalDate;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.entity.EntityComposite;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.query.Query;

@Mixins( Car.CarMixin.class )
public interface Car
    extends EntityComposite
{
    Property<String> licensePlate();

    Property<String> model();

    Association<CarCategory> category();

    @Optional
    Property<LocalDate> purchasedDate();

    @Optional
    Property<LocalDate> soldDate();

    Booking currentBooking();

    Query<Booking> pastBookings();

    abstract class CarMixin
        implements Car
    {

        public Booking currentBooking()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Query<Booking> pastBookings()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
