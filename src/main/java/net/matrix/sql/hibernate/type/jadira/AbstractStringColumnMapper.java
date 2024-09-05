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

public abstract class AbstractStringColumnMapper<T>
    implements Serializable, ColumnMapper<T, String> {
    private static final long serialVersionUID = 1L;

    @Override
    public final int getSqlType() {
        return Types.VARCHAR;
    }

    @Override
    public final BasicTypeReference<String> getHibernateType() {
        return StandardBasicTypes.STRING;
    }

    @Override
    public abstract T fromNonNullValue(String s);

    @Override
    public final T fromNonNullString(CharSequence s) {
        return fromNonNullValue(s.toString());
    }

    @Override
    public abstract String toNonNullValue(T value);

    @Override
    public final String toNonNullString(T value) {
        return toNonNullValue(value);
    }

    @Override
    public String toSqlLiteral(String object) {
        return object;
    }
}
