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

package org.apache.polygene.library.rest.client.spi;

import org.restlet.Request;
import org.restlet.resource.ResourceException;

/**
 * JAVADOC
 */
public interface RequestWriter
{
   /**
    * Write the given request object to the request.
    *
    * @param requestObject The object to be written.
    * @param request The Request destination of the requestObject
    * @return true if the object was written to the Request instance, false if handler can not write the object.
    * @throws org.restlet.resource.ResourceException
    */
   boolean writeRequest(Object requestObject, Request request)
      throws ResourceException;
}
