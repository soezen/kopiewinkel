/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain.interfaces;

import database.DatabaseManager;
import domain.Optie;
import domain.OptieType;
import domain.Prijs;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 *
 * @author soezen
 */
public class ConstrainerType implements UserType {
   
    private static final int[] SQL_TYPES = {Types.VARCHAR};
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class returnedClass() {
        return Constrainer.class;
    }

    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException {
        Long id = resultSet.getLong(names[0]);
//        if (id >= 5000000 && id < 6000000) {
//            return (Optie) DatabaseManager.getObjectWithId(Optie.class, id);
//        }
//        if (id >= 4000000 && id < 5000000) {
//            return (OptieType) DatabaseManager.getObjectWithId(OptieType.class, id);
//        }
//        if (id >= 8000000 && id < 9000000) {
//            return (Prijs) DatabaseManager.getObjectWithId(Prijs.class, id);
//        }
        return null;
    }

    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(index, Types.VARCHAR);
        } else {
            preparedStatement.setString(index, ((Enum)value).name());
        }
    }

    public Object deepCopy(Object value) throws HibernateException{
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
         return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable)value;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y)
            return true;
        if (null == x || null == y)
            return false;
        return x.equals(y);
    }

    public static <T extends Enum<T>> T valueOf(Class<T> enumType,
                                                String name) {
        System.out.println(enumType.getName());
        T t = null;
        System.out.println(name);
        return t;
    }
}
