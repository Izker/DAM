package Interaction;
/*
 * @author Chau Nguyen
 */
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.beanutils.BeanUtils;

import Annotation.Column;
import Annotation.Primarykey;
import Annotation.Table;
import Helpers.ProcessParams;

public class SQLQuery extends QueryTemplate {

	private String sql = null;
	private String sqlFinal = null;
	private ArrayList<Object> args = new ArrayList<Object>();
	private SQLQuery rhs = null;

	
	@Override
	public ArrayList mappingData(ResultSet rs, Class<?> clazz)
			throws InstantiationException, IllegalAccessException, SQLException, InvocationTargetException {
		// TODO Auto-generated method stub
		ArrayList result = new ArrayList();

		if (rs == null)
			return null;
		try {
			while (rs.next()) {
				Object obj = clazz.newInstance();
				ResultSetMetaData rsmd = rs.getMetaData();

				if (clazz.isAnnotationPresent(Table.class)) {
					Field[] fields = clazz.getDeclaredFields();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						String columnName = rsmd.getColumnName(i);
						Object columnValue = rs.getObject(i);
						// System.out.println("column " + i + " : " + columnName
						// + "value: " + columnValue.toString());

						for (Field f : fields) {
							// Use Annotation to get columnName
							String name = null;
							Annotation[] anols = f.getAnnotations();
							for (Annotation a : anols) {
								if (a instanceof Column) {
									if (((Column) a).name().equalsIgnoreCase(columnName)) {
										BeanUtils.setProperty(obj, f.getName(), columnValue);
										break;
									}

								}
								if (a instanceof Primarykey) {
									if (((Primarykey) a).primarykey().equalsIgnoreCase(columnName)) {
										BeanUtils.setProperty(obj, f.getName(), columnValue);
										break;
									}
								}
							}
						}

					}
				} else {
					obj = rs;
				}
				result.add(obj);
				// System.out.println(obj.getClass());
				/*if (!result.isEmpty()) {
					System.out.println("Item 0: "+result.get(0).toString());
				}*/
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		//System.out.println("result: " + result.size());
		return result;
	}

	// Getter and Setter
	public String getSQL() {
		Build();
		return sqlFinal;
	}
	
	public void setSQL(String sqlFinal) {
		this.sqlFinal = sqlFinal;
	}

	

	// Constructor
	public SQLQuery() {

	}

	/*
	 * Constructor SQL statement with SQL and arguments
	 * 
	 * @param sql SQL statement or SQL fragment
	 * 
	 * @param _args Arguments to every parameters embedded in the SQL
	 */
	public SQLQuery(String _sql, Object... _args) {
		this.sql = _sql;
		if (_args != null) {
			for (Object obj : _args) {
				this.args.add(obj);
			}
		}
	}

	// Private Operations
	private void Build() {
		// Have this built yet?
		if (this.sqlFinal != null)
			return;

		// Build this
		StringBuilder sb = new StringBuilder();
		// ArrayList<Object> _args = new ArrayList<Object>();
		Build(sb, this.args, null);
		this.sqlFinal = sb.toString();
		// this.argsFinal = _args;

	}

	private void Build(StringBuilder sb, ArrayList<Object> args, SQLQuery lhs) {

		if (this.sql != null && !this.sql.isEmpty()) {
			// Add SQL to String
			if (sb != null) {
				sb.append("\n");
			}
			String _sql = ProcessParams.ProcessParamToSql(this.sql, args);
			// Connect SQL statement if this has the same type with the previous
			// statement
			if (Is(lhs, "WHERE ") && Is(this, "WHERE ")) {
				_sql = "AND " + _sql.substring(6);
			}
			if (Is(lhs, "ORDER BY ") && Is(this, "ORDER BY ")) {
				_sql = ", " + _sql.substring(9);
			}
			if (Is(lhs, "SET ") && Is(this, "SET ")) {
				_sql = ", " + _sql.substring(4);
			}
			sb.append(_sql);
		}
		// If the right is not null => built it
		if (rhs != null) {
			rhs.Build(sb, rhs.args, this);
		}
	}
	
	/*
	 * @Description Check if SQL has sqlType
	 * @param sql : SQL statement to check
	 * @param sqlType: SQL statement type
	 */
	private boolean Is(SQLQuery sql, String sqlType) {
		return (sqlType != null && sql != null && sql.sql != null && sql.sql.startsWith(sqlType));
	}

	/*
	 * Append a fragment to the right-hand-side of this SQLQuery
	 * 
	 * @param sql the fragment that will be add to the right-hand-side
	 */
	public SQLQuery append(SQLQuery sql) {
		if (rhs != null) {
			rhs.append(sql);
		} else {
			rhs = sql;
		}
		return this;
	}

	/*
	 * Append a SQL SELECT clause to this SQL builder
	 * 
	 * @param columns * or null for select all, @attr for attribute in entity
	 * columnsName for name of columns in database
	 */
	public SQLQuery Select(Object... columns) {
		if (columns.toString() == "*" || columns == null) {
			return this.append(new SQLQuery("SELECT *", null));
		}
		String _sql = "SELECT ";
		for (Object c : columns) {
			_sql += c.toString();
			_sql += " ,";
		}
		if (_sql.length() > 7) {
			_sql = _sql.substring(0, _sql.length() - 2);
		}
		return this.append(new SQLQuery(_sql, null));
	}

	/*
	 * Append a SQL SELECT clause to this SQL builder
	 * 
	 * @param tables tableName.Class or table in String
	 */
	public SQLQuery From(Object... tables) {
		String _sql = "FROM ";
		for (Object t : tables) {
			_sql += t.toString();
			_sql += " ,";
		}

		if (_sql.length() > 5) {
			_sql = _sql.substring(0, _sql.length() - 2);
		}

		return this.append(new SQLQuery(_sql, null));
	}

	public SQLQuery Where(String _sql, Object... args) {
		_sql = "WHERE " + _sql;
		return this.append(new SQLQuery(_sql, args));
	}

	public SQLQuery OrderBy(Object... columns) {
		String _sql = "ORDER BY ";
		for (Object c : columns) {
			_sql += c.toString();
			_sql += " ,";
		}
		if (_sql.length() > 9) {
			_sql = _sql.substring(0, _sql.length() - 2);
		}
		return this.append(new SQLQuery(_sql, null));
	}

	public SQLQuery GroupBy(Object... columns) {
		String _sql = "GROUP BY ";
		for (Object c : columns) {
			_sql += c.toString();
			_sql += " ,";
		}
		if (_sql.length() > 9) {
			_sql = _sql.substring(0, _sql.length() - 2);
		}
		return this.append(new SQLQuery(_sql, null));
	}

	@Override
	public String doTranslate(Connection conn)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SQLException {
		// TODO Auto-generated method stub
		return this.getSQL();
	}

}
