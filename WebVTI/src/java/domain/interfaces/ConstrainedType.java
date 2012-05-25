/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain.interfaces;

import database.DatabaseManager;
import domain.InputVeld;
import domain.MenuItem;
import domain.Opdracht;
import domain.OpdrachtType;
import domain.Optie;
import domain.OptieType;
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
public class ConstrainedType implements UserType {
   
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
//        } else if (id >= 4000000 && id < 5000000) {
//            return (OptieType) DatabaseManager.getObjectWithId(OptieType.class, id);
//        } else if (id >= 3000000 && id < 4000000) {
//            return (Opdracht) DatabaseManager.getObjectWithId(Opdracht.class, id);
//        } else if (id >= 6000000 && id < 7000000) {
//            return (OpdrachtType) DatabaseManager.getObjectWithId(OpdrachtType.class, id);
//        } else if (id >= 12000000 && id < 13000000) {
//            return (MenuItem) DatabaseManager.getObjectWithId(MenuItem.class, id);
//        } else if (id >= 15000000 && id < 15000000) {
//            return (InputVeld) DatabaseManager.getObjectWithId(InputVeld.class, id);
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
