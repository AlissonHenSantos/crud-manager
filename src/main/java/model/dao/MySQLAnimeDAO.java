package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.Anime;
import model.User;

public class MySQLAnimeDAO implements AnimeDAO {

	@Override
	public boolean save(Anime anime) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO animes VALUES "
				+ " (DEFAULT, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		db.setString(1, anime.getTitle());
		db.setDate(2, new java.sql.Date(anime.getReleaseDate().getTime()));
		db.setInt(3, anime.getEpisodes());
		db.setBoolean(4, anime.getIsFinished());
		db.setInt(5, anime.getAutor().getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(Anime anime) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE animes "
						 + " SET title = ?,"
						 + " release_date = ?,"
						 + " episodes = ?,"
						 + " is_finished = ?,"
						 + " autor_id = ?"
						 + " WHERE id = ?";
		
		db.prepareStatement(sqlUpdate);
		
		db.setString(1, anime.getTitle());
		db.setDate(2, new java.sql.Date(anime.getReleaseDate().getTime()));
		db.setInt(3, anime.getEpisodes());
		db.setBoolean(4, anime.getIsFinished());
		db.setInt(5, anime.getAutor().getId());
		db.setInt(6, anime.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Anime anime) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlDelete = " DELETE FROM animes "
				         + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, anime.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Anime> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		List<Anime> animes = new ArrayList<Anime>();
			
		String sqlQuery = " SELECT u.id AS user_id, a.* "
				        + " FROM users u "
				        + " INNER JOIN animes a "
				        + " ON u.id = a.autor_id "
				        + " ORDER BY a.title";
		
		db.createStatement();
		db.executeQuery(sqlQuery);

		while (db.next()) {
			Anime a = createAnime(db);
			animes.add(a);
		}
		
		return animes;
	}

	@Override
	public Anime findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
				
		String sql = "SELECT * FROM animes WHERE id = ?;";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Anime a = null;
		while (db.next()) {
			a = createAnime(db);
			break;
		}
		
		return a;
	}
	
	private Anime createAnime(DBHandler db) throws ModelException {
		Anime a = new Anime(db.getInt("id"));
		a.setTitle(db.getString("title"));
		a.setReleaseDate(db.getDate("release_date"));
		a.setEpisodes(db.getInt("episodes"));
		a.setIsFinished(db.getBoolean("is_finished"));
		
		UserDAO userDAO = DAOFactory.createDAO(UserDAO.class); 
		
		User autor = userDAO.findById(db.getInt("autor_id"));
		a.setAutor(autor);
		
		return a;
	}
}