/*
 * Copyright (c) 2007, Rickard �berg. All Rights Reserved.
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

package org.qi4j.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * TODO
 */
public class QueryableIterable
    implements Queryable
{
    private Iterable source;

    public QueryableIterable( Iterable source )
    {
        this.source = source;
    }

    public <T> T find( Query<T> query )
    {
        QueryImpl<T> queryImpl = (QueryImpl<T>) query;

        next:
        for( Object candidate : source )
        {
            List<BooleanExpression> expressions = queryImpl.getWhere();
            for( BooleanExpression expression : expressions )
            {
                boolean result = expression.evaluate( candidate, null );
                if( !result )
                {
                    continue next;
                }
            }

            // Candidate matches all expressions
            return (T) candidate;
        }
        return null;
    }

    public <T> Iterable<T> iterable( Query<T> query )
    {
        QueryImpl<T> queryImpl = (QueryImpl<T>) query;
        List<T> resultList = new ArrayList<T>();

        next:
        for( Object candidate : source )
        {
            List<BooleanExpression> expressions = queryImpl.getWhere();
            for( BooleanExpression expression : expressions )
            {
                boolean result = expression.evaluate( candidate, queryImpl.getSetVariables() );
                if( !result )
                {
                    continue next;
                }
            }

            // Candidate matches all expressions
            resultList.add( (T) candidate );
        }

        // Order results
        List<OrderBy> orderByList = queryImpl.getOrderBy();
        OrderByComparator comparator = new OrderByComparator(orderByList);
        Collections.sort( resultList, comparator);

        // Cut results
        int firstResult = queryImpl.getFirstResult();
        int maxResults = queryImpl.getFirstResult();
        if (firstResult != -1 || maxResults != -1)
        {
            int firstIdx = 0;
            int lastIdx = resultList.size();
            if (firstResult != -1)
                firstIdx = firstResult;
            if (maxResults != -1)
                lastIdx = Math.min(firstIdx+maxResults, resultList.size());

            resultList = resultList.subList(firstIdx, lastIdx);
        }

        return resultList;
    }
}
