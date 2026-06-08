package model;

import java.util.Date;

public class Anime {
	private int id;
	private String title;
	private Date releaseDate;
	private int episodes;
	private boolean isFinished;
	private User autor;
	
	public Anime() {
		this(0)
	}
	
	public Anime(int id) {
		this.id = id;
		setTitle("");
	}
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public Date getReleaseDate() { return releaseDate; }
	public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }
	
	public int getEpisodes() { return episodes; }
	public void setEpisodes(int episodes) { this.episodes = episodes; }
	
	public boolean getIsFinished() { return isFinished; }
	public void setIsFinished(boolean isFinished) { this.isFinished = isFinished; }

	public User getAutor() { return autor; }
	public void setAutor(User autor) { this.autor = autor; }
}
