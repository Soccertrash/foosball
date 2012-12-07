package model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

/**
 * The Class Team represents a combination of two players
 */
@Entity
public class Team extends Model {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id */
	@Id
	private long id;

	/** The player1. */
	/* Fetch eager, team always should have access to the players */
	@OneToOne(fetch = FetchType.EAGER)
	private Player player1;

	/** The player2. */
	/* Fetch eager, team always should have access to the players */
	@OneToOne(fetch = FetchType.EAGER)
	private Player player2;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the player1
	 */
	public Player getPlayer1() {
		return player1;
	}

	/**
	 * Sets the player1.
	 *
	 * @param player1 the player1 to set
	 */
	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	/**
	 * @return the player2
	 */
	public Player getPlayer2() {
		return player2;
	}

	/**
	 * @param player2 the player2 to set
	 */
	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	/** The finder is used to get instances of Team */
	public static Finder<Long, Team> finder = new Finder<>(Long.class,Team.class);

}
