package de.whitescan.demoproject.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import de.whitescan.api.database.AbstractDAO;
import de.whitescan.demoproject.DemoProject;

/**
 * 
 * @author Whitescan
 *
 */
public class DemoDAO extends AbstractDAO {

	private String warnsTable;

	public DemoDAO(DemoProject plugin) {
		super(plugin.getLogger(), plugin.getMysqlDatabase());
	}

	@Override
	public void checkTables() {

		this.warnsTable = database.getPrefix() + "warns";

		final String sql = "CREATE TABLE IF NOT EXISTS " + warnsTable + " (" + //
				"id INT(11) NOT NULL AUTO_INCREMENT," + //
				"authorId CHAR(36) NOT NULL COLLATE 'utf8mb4_unicode_520_ci'," + //
				"targetId CHAR(36) NOT NULL COLLATE 'utf8mb4_unicode_520_ci'," + //
				"reason VARCHAR(120) NOT NULL COLLATE 'utf8mb4_unicode_520_ci'," + //
				"created DATETIME NOT NULL," + //
				"pardoned TINYINT(1) NOT NULL DEFAULT '0'," + //
				"PRIMARY KEY (id) USING BTREE" + //
				")" + //
				"COLLATE='utf8mb4_unicode_520_ci'" + //
				"ENGINE=InnoDB;";

		try {

			PreparedStatement ps = getConntion().prepareStatement(sql);
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private Connection getConntion() {
		return database.getConnection();
	}

	public void createWarn(String authorId, UUID targetId, String reason) {

		final String sql = "INSERT INTO " + warnsTable + " (authorId, targetId, reason, created) VALUES (?, ?, ?, ?);";

		try {

			PreparedStatement ps = getConntion().prepareStatement(sql);
			ps.setString(1, authorId);
			ps.setString(2, targetId.toString());
			ps.setString(3, reason);
			ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public int getWarnCount(UUID uniqueId) {
		
		int count = 0;

		final String sql = "SELECT COUNT(id) AS count FROM " + warnsTable + " WHERE targetId = ? AND pardoned = 0";
		
		try {

			PreparedStatement ps = getConntion().prepareStatement(sql);
			ps.setString(1, uniqueId.toString());

			ResultSet rs = ps.executeQuery();
			if (rs.next())
				count = rs.getInt("count");
			
			rs.close();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
		
	}

}
