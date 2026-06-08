package model.dao;

import java.util.List;
import model.Anime;
import model.ModelException;

public interface AnimeDAO {
	boolean save(Anime anime) throws ModelException;
	boolean update(Anime anime) throws ModelException;
	boolean delete(Anime anime) throws ModelException;
	List<Anime> listAll() throws ModelException;
	Anime findById(int id) throws ModelException;
}