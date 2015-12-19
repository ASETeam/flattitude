package com.aseupc.flattitude.Models;

import java.io.Serializable;
import java.util.List;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

/**
 * The Class UserEntity.
 */

@Root(name = "user")
//@Type(propOrder = { "username", "name", "email", "password", "properties" })
public class UserEntity {

	/** The username. */
	private String username;

	/** The name. */
	private String name;

	/** The email. */
	private String email;

	/** The password. */
	private String password;

	/**
	 * Instantiates a new user entity.
	 */

	/**
	 * Instantiates a new user entity.
	 *
	 * @param username            the username
	 * @param name            the name
	 * @param email            the email
	 * @param password the password
	 */
	public UserEntity(@Element(name="username")String username, @Element(name="name") String name, @Element(name="email")String email, @Element(name="password")String password) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	@Element(name = "username")
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username
	 *            the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@Element(name="name")
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	@Element(name="email")
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email
	 *            the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	@Element(name="password")
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
