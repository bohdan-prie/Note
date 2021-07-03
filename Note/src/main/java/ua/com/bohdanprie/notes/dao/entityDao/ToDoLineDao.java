package ua.com.bohdanprie.notes.dao.entityDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.DaoUtils;
import ua.com.bohdanprie.notes.dao.exception.DBException;
import ua.com.bohdanprie.notes.dao.exception.DaoException;
import ua.com.bohdanprie.notes.domain.entity.ToDoLine;
import ua.com.bohdanprie.notes.domain.entity.User;

public class ToDoLineDao {
	private DaoFactory daoFactory;
	private static final Logger LOG = LogManager.getLogger(ToDoLineDao.class.getName());

	public ToDoLineDao() {
		daoFactory = DaoFactory.getInstance();
	}

	public void deleteAll(User user) {
		LOG.trace("Deleting all toDoLists from user " + user.getLogin());
		String SQL = "DELETE FROM notes.to_do_line WHERE user_login = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());

				LOG.trace("Executing SQL");
				statement.execute();
				LOG.trace("Deleting all toDo from user " + user.getLogin());
			} catch (SQLException e) {
				LOG.error("Fail to delete all toDoLists from user " + user.getLogin(), e);
				throw new DaoException("Fail to delete all toDoLists from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("All toDoLists were deleted from user " + user.getLogin());
	}

	public void delete(int id, User user) {
		LOG.trace("Deleting toDoLine from user " + user.getLogin());
		String SQL = "DELETE FROM notes.to_do_line WHERE user_login = ? AND id = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setInt(2, id);

				LOG.trace("Executing SQL");
				statement.execute();
				LOG.trace("Deleting all toDo from toDoList at user " + user.getLogin());
			} catch (SQLException e) {
				LOG.error("Fail to delete toDoLine of user " + user.getLogin(), e);
				throw new DaoException("Fail to delete toDoLine of user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("toDoLine was deleted from user " + user.getLogin());
	}

	public void change(ToDoLine toDoLine, User user) {
		LOG.trace("Changing toDoLine of user " + user.getLogin());
		String SQL = "UPDATE notes.to_do_line set title = ?, time_change = ? WHERE user_login = ? AND id = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, toDoLine.getTitle());
				statement.setTimestamp(2, new Timestamp(toDoLine.getTimeChange().getTime()));
				statement.setString(3, user.getLogin());
				statement.setInt(4, toDoLine.getId());

				LOG.trace("Executing SQL");
				statement.execute();
			} catch (SQLException e) {
				LOG.error("Fail to change the toDoLine of user " + user.getLogin(), e);
				throw new DaoException("Fail to change the toDoLine of user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("ToDoLine was changed at user " + user.getLogin());
	}

	public ToDoLine create(int id, User user) {
		StringBuffer SQLInsert = new StringBuffer(
				"INSERT INTO notes.to_do_line (user_login, title, id, time_change, time_creation) VALUES ");
		String SQL = SQLInsert.append(DaoUtils.buildInsertValuesQuery(1, 5)).append(";").toString();

		ToDoLine toDoLine = null;

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				
				statement.setString(1, user.getLogin());
				statement.setString(2, "Title");
				statement.setInt(3, id);
				statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				
				statement.execute();
				resultSet = statement.getGeneratedKeys();
				if(resultSet.next()) {
					toDoLine = new ToDoLine(resultSet.getString("title"));
					toDoLine.setTimeCreation(resultSet.getTimestamp("time_creation"));
					toDoLine.setTimeChange(resultSet.getTimestamp("time_change"));
				}
			} catch (SQLException e) {
				LOG.warn("Fail to create toDoLine at user " + user.getLogin(), e);
				throw new DaoException("Fail to create toDoLine at user " + user.getLogin());
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}

		return null;
	}

	public ArrayList<ToDoLine> getAll(User user) {
		String SQL = "SELECT * FROM notes.to_do_line WHERE user_login = ?;";

		ArrayList<ToDoLine> toDoLines = new ArrayList<>();

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());

				LOG.trace("Getting result set");
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					ToDoLine toDoLine = new ToDoLine(resultSet.getString("title"));
					toDoLine.setId(resultSet.getInt("id"));
					toDoLine.setTimeChange(resultSet.getTimestamp("time_change"));
					toDoLine.setTimeCreation(resultSet.getTimestamp("time_creation"));
					toDoLines.add(toDoLine);
				}
			} catch (SQLException e) {
				throw new DaoException("Fail to get all toDoLines at user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		return toDoLines;
		
	}

	public ArrayList<ToDoLine> searchByPattern(int[] id, User user) {
		String SQL = "SELECT * FROM notes.to_do_line WHERE user_login = ? AND id IN(?);";
		
		ArrayList<ToDoLine> toDoLines = new ArrayList<>();

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setObject(2, id);

				LOG.trace("Getting result set");
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					ToDoLine toDoLine = new ToDoLine(resultSet.getString("title"));
					toDoLine.setId(resultSet.getInt("id"));
					toDoLine.setTimeChange(resultSet.getTimestamp("time_change"));
					toDoLine.setTimeCreation(resultSet.getTimestamp("time_creation"));
					toDoLines.add(toDoLine);
				}
			} catch (SQLException e) {
				throw new DaoException("Fail to get toDoLines by pattern at user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		return toDoLines;
	}

	public int[] getAllIdByPattern(User user, String pattern) {
		Set<Integer> allLinesId = new HashSet<>();
		String SQL = "SELECT id FROM notes.to_do_line WHERE (user_login = ? AND title ILIKE ?) OR id IN(SELECT list_id FROM notes.to_do WHERE user_login = ? AND body ILIKE ?));";

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setString(2, pattern);
				statement.setString(3, user.getLogin());
				statement.setString(4, pattern);
				LOG.trace("Executing SQL");
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					allLinesId.add(resultSet.getInt("id"));
				}
			} catch (SQLException e) {
				LOG.error("Fail to change the toDoLine of user " + user.getLogin(), e);
				throw new DaoException("Fail to change the toDoLine of user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		return allLinesId.stream().mapToInt(value -> value).toArray();
	}
}