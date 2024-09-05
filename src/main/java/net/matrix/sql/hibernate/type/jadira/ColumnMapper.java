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

import org.hibernate.type.BasicTypeReference;

public interface ColumnMapper<T, J> {
    int getSqlType();

    BasicTypeReference<? super J> getHibernateType();

    T fromNonNullValue(J value);

    T fromNonNullString(CharSequence s);

    J toNonNullValue(T value);

    String toNonNullString(T value);

    String toSqlLiteral(J object);
}
