/*
 * Copyright (c) 2008, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.spi.entity.association;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import org.qi4j.api.common.QualifiedName;

/**
 * JAVADOC
 */
public final class ManyAssociationType
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    public enum ManyAssociationTypeEnum
    {
        MANY, LIST, SET
    }

    private final QualifiedName qualifiedName;
    private final ManyAssociationTypeEnum associationType;
    private final String type;
    private final String rdf;
    private final boolean queryable;

    public ManyAssociationType( final QualifiedName qualifiedName,
                                final ManyAssociationTypeEnum associationType,
                                final String type,
                                final String rdf,
                                final boolean queryable )
    {
        this.qualifiedName = qualifiedName;
        this.associationType = associationType;
        this.type = type;
        this.rdf = rdf;
        this.queryable = queryable;
    }

    public QualifiedName qualifiedName()
    {
        return qualifiedName;
    }

    public ManyAssociationTypeEnum associationType()
    {
        return associationType;
    }

    public String type()
    {
        return type;
    }

    public String rdf()
    {
        return rdf;
    }

    public boolean queryable()
    {
        return queryable;
    }

    @Override public String toString()
    {
        return qualifiedName + "(" + type + ")";
    }

    public void calculateVersion( MessageDigest md ) throws UnsupportedEncodingException
    {
        md.update( qualifiedName.toString().getBytes("UTF-8" ));
        md.update( type.getBytes("UTF-8" ));
    }
}
