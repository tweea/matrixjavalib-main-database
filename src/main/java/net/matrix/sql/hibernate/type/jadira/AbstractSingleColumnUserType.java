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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.BasicType;
import org.hibernate.type.SerializationException;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;

public abstract class AbstractSingleColumnUserType<T, J, C extends ColumnMapper<T, J>>
    implements EnhancedUserType<T>, ParameterizedType, Serializable {
    private static final long serialVersionUID = 1L;

    private final C columnMapper;

    private final Class<T> javaType;

    private final int sqlType;

    public AbstractSingleColumnUserType() {
        List<Class<?>> typeArguments = TypeHelper.getTypeArguments(AbstractSingleColumnUserType.class, getClass());
        try {
            Class<?> columnMapperClass = typeArguments.get(2);
            Constructor<?> columnMapperConstructor = columnMapperClass.getDeclaredConstructor();
            columnMapper = (C) columnMapperConstructor.newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            throw new HibernateException("Could not initialise column mapper for " + getClass(), ex);
        } catch (IllegalAccessException ex) {
            throw new HibernateException("Could not access column mapper for " + getClass(), ex);
        }
        javaType = (Class<T>) typeArguments.get(0);
        sqlType = getColumnMapper().getSqlType();
    }

    public final C getColumnMapper() {
        return columnMapper;
    }

    @Override
    public final int getSqlType() {
        return sqlType;
    }

    @Override
    public Class<T> returnedClass() {
        return javaType;
    }

    @Override
    public boolean equals(T x, T y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(T x) {
        return Objects.hashCode(x);
    }

    @Override
    public T nullSafeGet(ResultSet resultSet, int position, SharedSessionContractImplementor session, Object owner)
        throws SQLException {
        J converted = doNullSafeGet(resultSet, position, session);
        if (converted == null) {
            return null;
        }

        return getColumnMapper().fromNonNullValue(converted);
    }

    protected J doNullSafeGet(ResultSet resultSet, int position, SharedSessionContractImplementor session)
        throws SQLException {
        BasicType<? super J> basicType = session.getTypeConfiguration().getBasicTypeRegistry().resolve(getColumnMapper().getHibernateType());
        return (J) basicType.getJdbcValueExtractor().extract(resultSet, position, session);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, T value, int index, SharedSessionContractImplementor session)
        throws SQLException {
        final J transformedValue;
        if (value == null) {
            transformedValue = null;
        } else {
            transformedValue = getColumnMapper().toNonNullValue(value);
        }

        doNullSafeSet(preparedStatement, transformedValue, index, session);
    }

    protected void doNullSafeSet(PreparedStatement preparedStatement, J transformedValue, int index, SharedSessionContractImplementor session)
        throws SQLException {
        BasicType<? super J> basicType = session.getTypeConfiguration().getBasicTypeRegistry().resolve(getColumnMapper().getHibernateType());
        basicType.getJdbcValueBinder().bind(preparedStatement, transformedValue, index, session);
    }

    @Override
    public T deepCopy(T value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(T value) {
        if (value == null) {
            return null;
        }

        final T deepCopy = deepCopy(value);
        if (!(deepCopy instanceof Serializable)) {
            throw new SerializationException(String.format("deepCopy of %s is not serializable", value), null);
        }
        return (Serializable) deepCopy;
    }

    @Override
    public T assemble(Serializable cached, Object owner) {
        return deepCopy((T) cached);
    }

    @Override
    public String toSqlLiteral(T object) {
        if (object == null) {
            return null;
        }

        J convertedObject = getColumnMapper().toNonNullValue(object);
        if (convertedObject == null) {
            return null;
        }

        return getColumnMapper().toSqlLiteral(convertedObject);
    }

    @Override
    public String toString(T object) {
        return getColumnMapper().toNonNullString(object);
    }

    @Override
    public T fromStringValue(CharSequence string) {
        return getColumnMapper().fromNonNullString(string);
    }

    @Override
    public void setParameterValues(Properties parameters) {
    }
}
