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

import java.util.List;
import org.apache.zest.api.property.Property;
import org.apache.zest.api.value.ValueComposite;

/**
 * Schedule
 *
 * A schedule is a series of {@link CarrierMovement}s.
 *
 * List of carrier movements is mandatory and immutable.
 */
public interface Schedule
    extends ValueComposite
{
    Property<List<CarrierMovement>> carrierMovements();
}