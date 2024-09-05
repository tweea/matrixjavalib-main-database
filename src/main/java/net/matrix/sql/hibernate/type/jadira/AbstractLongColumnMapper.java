/*
 * Copyright 2010, 2011 Christopher Pheby
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.matrix.sql.hibernate.type.jadira;

import java.io.Serializable;
import java.sql.Types;

import org.hibernate.type.BasicTypeReference;
import org.hibernate.type.StandardBasicTypes;

public abstract class AbstractLongColumnMapper<T>
    implements Serializable, ColumnMapper<T, Long> {
    private static final long serialVersionUID = 1L;

    @Override
    public final int getSqlType() {
        return Types.BIGINT;
    }

    @Override
    public final BasicTypeReference<Long> getHibernateType() {
        return StandardBasicTypes.LONG;
    }

    @Override
    public abstract T fromNonNullValue(Long value);

    @Override
    public abstract T fromNonNullString(CharSequence s);

    @Override
    public abstract Long toNonNullValue(T value);

    @Override
    public abstract String toNonNullString(T value);

    @Override
    public String toSqlLiteral(Long object) {
        return object.toString();
    }
}
