/*
 * Copyright 2011 Marc Grue.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zest.sample.dcicargo.sample_b.data.structure.voyage;

import java.util.Date;
import org.apache.zest.api.association.Association;
import org.apache.zest.api.property.Property;
import org.apache.zest.api.value.ValueComposite;
import org.apache.zest.sample.dcicargo.sample_b.data.structure.location.Location;

/**
 * CarrierMovement
 *
 * A carrier movement is a vessel voyage from one location to another.
 *
 * All properties are mandatory and immutable.
 */
public interface CarrierMovement
    extends ValueComposite
{
    Association<Location> departureLocation();

    Association<Location> arrivalLocation();

    Property<Date> departureTime();

    Property<Date> arrivalTime();
}