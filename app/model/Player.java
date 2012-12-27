package model;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Represents a single player with a {@link #firstName}, {@link #lastName} and a
 * {@link #nickname}
 * 
 * @author mpa
 */
@Entity
public class Player extends Model {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	private long id;

	/** The first name. */
	@Required
	private String firstName;

	/** The last name. */
	@Required
	private String lastName;

	/** The nickname. */
	@Required
	private String acronym;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the first name.
	 * 
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 * 
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 * 
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the nickname.
	 * 
	 * @return the nickname
	 */
	public String getAcronym() {
		return acronym;
	}

	/**
	 * Sets the nickname.
	 * 
	 * @param nickname
	 *            the nickname to set
	 */
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	/** The finder is used to get instances of Player */
	public static Finder<Long, Player> finder = new Finder<>(Long.class,
			Player.class);
}
