package Interaction;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ConnectDB.*;
//import Annotation.*;
//import java.lang.annotation.*;
//import java.lang.reflect.*;
import Helpers.ProcessParams;

public class SQLQuery extends QueryTemplate {

	private String sql = null;
	private String sqlFinal = null;
	private ArrayList<Object> args = new ArrayList<Object>();
	private ArrayList<Object> argsFinal = new ArrayList<Object>();
	private SQLQuery rhs = null;

	@Override
	public ArrayList<Class<?>> mappingData(ResultSet rs, Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	// Getter and Setter
	public String getSQL() {
		Build();
		return sqlFinal;
	}

	public void setSQL(String sqlFinal) {
		this.sqlFinal = sqlFinal;
	}

	public ArrayList<Object> getArguments() {
		Build();
		return argsFinal;
	}

	public void setArguments(ArrayList<Object> argsFinal) {
		this.argsFinal = argsFinal;
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

	@Override
	public String doTranslate(Connection conn)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SQLException {
		// TODO Auto-generated method stub
		return this.getSQL();
	}

}
