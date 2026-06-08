package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelException;
import model.Anime;
import model.User;
import model.dao.DAOFactory;
import model.dao.AnimeDAO;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/animes", "/anime/form", "/anime/delete", "/anime/insert", "/anime/update"})
public class AnimesController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getRequestURI();
		
		switch (action) {
		case "/crud-manager/anime/form": {
			CommonsController.listUsers(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-anime.jsp");
			break;
		}
		case "/crud-manager/anime/update": {
			CommonsController.listUsers(req);
			Anime a = loadAnime(req);
			req.setAttribute("anime", a);
			req.setAttribute("action", "update");
			ControllerUtil.forward(req, resp, "/form-anime.jsp");
			break;
		}
		default:
			listAnimes(req);
			ControllerUtil.transferSessionMessagesToRequest(req);
			ControllerUtil.forward(req, resp, "/animes.jsp");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getRequestURI();
		
		if (action == null || action.equals("") ) {
			ControllerUtil.forward(req, resp, "/animes.jsp");
			return;
		}
		
		switch (action) {
		case "/crud-manager/anime/delete":
			deleteAnime(req, resp);
			break;
		case "/crud-manager/anime/insert": {
			insertAnime(req, resp);
			break;
		}
		case "/crud-manager/anime/update": {
			updateAnime(req, resp);
			break;
		}
		default:
			System.out.println("URL inválida " + action);
		}
			
		ControllerUtil.redirect(resp, req.getContextPath() + "/animes");
	}
	
	private Anime loadAnime(HttpServletRequest req) {
		String animeIdParameter = req.getParameter("animeId");
		int animeId = Integer.parseInt(animeIdParameter);
		
		AnimeDAO dao = DAOFactory.createDAO(AnimeDAO.class);
		
		try {
			Anime a = dao.findById(animeId);
			if (a == null)
				throw new ModelException("Anime não encontrado para alteração");
			return a;
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		return null;
	}

	private void updateAnime(HttpServletRequest req, HttpServletResponse resp) {
		Anime anime = loadAnime(req);
		bindAnimeProperties(req, anime);
		
		AnimeDAO dao = DAOFactory.createDAO(AnimeDAO.class);
		
		try {
			if (dao.update(anime)) {
				ControllerUtil.sucessMessage(req, "Anime '" + anime.getTitle() + "' atualizado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Anime '" + anime.getTitle() + "' não pode ser atualizado.");
			}				
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void insertAnime(HttpServletRequest req, HttpServletResponse resp) {
		Anime anime = new Anime();
		bindAnimeProperties(req, anime);
		
		AnimeDAO dao = DAOFactory.createDAO(AnimeDAO.class);
		
		try {
			if (dao.save(anime)) {
				ControllerUtil.sucessMessage(req, "Anime '" + anime.getTitle() + "' salvo com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Anime '" + anime.getTitle() + "' não pode ser salvo.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void deleteAnime(HttpServletRequest req, HttpServletResponse resp) {
		String animeIdParameter = req.getParameter("id");
		int animeId = Integer.parseInt(animeIdParameter);
		
		AnimeDAO dao = DAOFactory.createDAO(AnimeDAO.class);
		
		try {
			Anime a = dao.findById(animeId);
			if (a == null)
				throw new ModelException("Anime não encontrado para deleção");
			
			if (dao.delete(a)) {
				ControllerUtil.sucessMessage(req, "Anime '" + a.getTitle() + "' deletado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Anime '" + a.getTitle() + "' não pode ser deletado.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void listAnimes(HttpServletRequest req) {
		AnimeDAO dao = DAOFactory.createDAO(AnimeDAO.class);
		List<Anime> animes = null;
		
		try {
			animes = dao.listAll();
		} catch (ModelException e) {
			e.printStackTrace();
		}
		
		if (animes != null)
			req.setAttribute("animes", animes);
	}
	
	private void bindAnimeProperties(HttpServletRequest req, Anime anime) {
		try {
			anime.setTitle(req.getParameter("title"));
			anime.setReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("releaseDate")));
			anime.setEpisodes(Integer.parseInt(req.getParameter("episodes")));
			anime.setIsFinished(req.getParameter("isFinished") != null);
			anime.setAutor(new User(Integer.parseInt(req.getParameter("autor"))));
		} catch (ParseException | NumberFormatException e) {
			e.printStackTrace();
		}
	}
}