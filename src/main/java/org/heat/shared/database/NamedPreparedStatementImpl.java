package org.heat.shared.database;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.heat.shared.Pair;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public final class NamedPreparedStatementImpl extends StatementDelegate implements NamedPreparedStatement {
    private final PreparedStatement statement;
    private final Multimap<String, Integer> indexes;

    public NamedPreparedStatementImpl(PreparedStatement statement, Multimap<String, Integer> indexes) {
        this.statement = statement;
        this.indexes = indexes;
    }

    public static Pair<String, Multimap<String, Integer>> parse(String sql) {
        Multimap<String, Integer> indexes = HashMultimap.create();
        StringBuilder parsed = new StringBuilder();

        int placeholder = 1;
        int cursor = 0;
        while (cursor < sql.length()) {
            int startPlaceholder = sql.indexOf(':', cursor);
            if (startPlaceholder == -1) {
                parsed.append(sql.substring(cursor, sql.length()));
                break;
            }

            parsed.append(sql.substring(cursor, startPlaceholder));

            int startIdentifier = startPlaceholder + 1;
            int endIdentifier = startIdentifier;
            while (endIdentifier < sql.length() && Character.isJavaIdentifierPart(sql.charAt(endIdentifier))) {
                endIdentifier++;
            }

            String identifier = sql.substring(startIdentifier, endIdentifier);
            indexes.put(identifier, placeholder++);

            parsed.append('?');

            cursor = endIdentifier;
        }

        return Pair.of(parsed.toString(), indexes);
    }

    @Override protected PreparedStatement delegate() { return statement; }

    @Override public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }
    @Override public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }
    @Override public long executeLargeUpdate() throws SQLException {
        return statement.executeLargeUpdate();
    }
    @Override public boolean execute() throws SQLException {
        return statement.execute();
    }
    @Override public void addBatch() throws SQLException {
        statement.addBatch();
    }
    @Override public void clearParameters() throws SQLException {
        statement.clearParameters();
    }
    @Override public ResultSetMetaData getMetaData() throws SQLException {
        return statement.getMetaData();
    }
    @Override public ParameterMetaData getParameterMetaData() throws SQLException {
        return statement.getParameterMetaData();
    }

    @Override
    public void setObject(String parameterName, Object x, SQLType targetSqlType) throws SQLException {
        for (Integer index : indexes.get(parameterName)) {
            statement.setObject(index, x, targetSqlType);
        }
    }

    @Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
        for (Integer index : indexes.get(parameterName)) {
            statement.setNull(index, sqlType);
        }
    }

    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        for (Integer index : indexes.get(parameterName)) {
            statement.setBoolean(index, x);
        }
    }

    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
        for (Integer index : indexes.get(parameterName)) {
            statement.setByte(index, x);
        }
    }

    @Override
    public void setShort(String parameterName, short x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setShort(index, x);
		}
	}

    @Override
    public void setInt(String parameterName, int x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setInt(index, x);
		}
	}

    @Override
    public void setLong(String parameterName, long x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setLong(index, x);
		}
	}

    @Override
    public void setFloat(String parameterName, float x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setFloat(index, x);
		}
	}

    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setDouble(index, x);
		}
	}

    @Override
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setBigDecimal(index, x);
		}
	}

    @Override
    public void setString(String parameterName, String x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setString(index, x);
		}
	}

    @Override
    public void setBytes(String parameterName, byte[] x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setBytes(index, x);
		}
	}

    @Override
    public void setDate(String parameterName, Date x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setDate(index, x);
		}
	}

    @Override
    public void setTime(String parameterName, Time x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setTime(index, x);
		}
	}

    @Override
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setTimestamp(index, x);
		}
	}

    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setAsciiStream(index, x, length);
		}
	}

    @Override
    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setBinaryStream(index, x, length);
		}
	}

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setObject(index, x, targetSqlType);
		}
	}

    @Override
    public void setObject(String parameterName, Object x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setObject(index, x);
		}
	}

    @Override
    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setCharacterStream(index, reader, length);
		}
	}

    @Override
    public void setRef(String parameterName, Ref x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setRef(index, x);
		}
	}

    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setBlob(index, x);
		}
	}

    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setClob(index, x);
		}
	}

    @Override
    public void setArray(String parameterName, Array x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setArray(index, x);
		}
	}

    @Override
    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setDate(index, x, cal);
		}
	}

    @Override
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setTime(index, x, cal);
		}
	}

    @Override
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setTimestamp(index, x, cal);
		}
	}

    @Override
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setNull(index, sqlType, typeName);
		}
	}

    @Override
    public void setURL(String parameterName, URL x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setURL(index, x);
		}
	}

    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setRowId(index, x);
		}
	}

    @Override
    public void setNString(String parameterName, String value) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setNString(index, value);
		}
	}

    @Override
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setNCharacterStream(index, value, length);
		}
	}

    @Override
    public void setNClob(String parameterName, NClob value) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setNClob(index, value);
		}
	}

    @Override
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setClob(index, reader, length);
		}
	}

    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setBlob(index, inputStream, length);
		}
	}

    @Override
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setNClob(index, reader, length);
		}
	}

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setSQLXML(index, xmlObject);
		}
	}

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setObject(index, x, targetSqlType, scaleOrLength);
		}
	}

    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setAsciiStream(index, x, length);
		}
	}

    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setBinaryStream(index, x, length);
		}
	}

    @Override
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setCharacterStream(index, reader, length);
		}
	}

    @Override
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setAsciiStream(index, x);
		}
	}

    @Override
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setBinaryStream(index, x);
		}
	}

    @Override
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setCharacterStream(index, reader);
		}
	}

    @Override
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setNCharacterStream(index, value);
		}
	}

    @Override
    public void setClob(String parameterName, Reader reader) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setClob(index, reader);
		}
	}

    @Override
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setBlob(index, inputStream);
		}
	}

    @Override
    public void setNClob(String parameterName, Reader reader) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setNClob(index, reader);
		}
	}

    @Override
    public void setObject(String parameterName, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
		for (Integer index : indexes.get(parameterName)) {
			statement.setObject(index, x, targetSqlType, scaleOrLength);
		}
	}
}
