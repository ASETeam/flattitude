package com.aseupc.flattitude.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "chatRoom")
/*@XmlType(propOrder = { "roomName", "naturalName", "description", "password", "subject", "creationDate",
		"modificationDate", "maxUsers", "persistent", "publicRoom", "registrationEnabled", "canAnyoneDiscoverJID",
		"canOccupantsChangeSubject", "canOccupantsInvite", "canChangeNickname", "logEnabled",
		"loginRestrictedToNickname", "membersOnly", "moderated", "broadcastPresenceRoles", "owners", "admins",
		"members", "outcasts" })*/
public class MUCRoomEntity {

	private String roomName;
	private String description;
	private String password;
	private String subject;
	private String naturalName;

	private int maxUsers;

	private Date creationDate;
	private Date modificationDate;

	private boolean persistent;
	private boolean publicRoom;
	private boolean registrationEnabled;
	private boolean canAnyoneDiscoverJID;
	private boolean canOccupantsChangeSubject;
	private boolean canOccupantsInvite;
	private boolean canChangeNickname;
	private boolean logEnabled;
	private boolean loginRestrictedToNickname;
	private boolean membersOnly;
	private boolean moderated;

	/*private List<String> broadcastPresenceRoles;
	private List<String> owners;
	private List<String> admins;
	private List<String> members;
	private List<String> outcasts;*/

	public MUCRoomEntity(String roomName, String naturalName, String description) {
		this.naturalName = naturalName;
		this.roomName = roomName;
		this.description = description;
	}

	@Element(name="naturalName")
	public String getNaturalName() {
		return naturalName;
	}

	public void setNaturalName(String naturalName) {
		this.naturalName = naturalName;
	}

	@Element(name="roomName")
	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	@Element(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Element(name="password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Element(name="subject")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Element(name="maxUsers")
	public int getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}

	@Element(name="creationDate")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Element(name="modificationDate")
	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	@Element(name="persistent")
	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	@Element(name="publicRoom")
	public boolean isPublicRoom() {
		return publicRoom;
	}

	public void setPublicRoom(boolean publicRoom) {
		this.publicRoom = publicRoom;
	}

	@Element(name="registrationEnabled")
	public boolean isRegistrationEnabled() {
		return registrationEnabled;
	}

	public void setRegistrationEnabled(boolean registrationEnabled) {
		this.registrationEnabled = registrationEnabled;
	}

	@Element(name="canAnyoneDiscoverJID")
	public boolean isCanAnyoneDiscoverJID() {
		return canAnyoneDiscoverJID;
	}

	public void setCanAnyoneDiscoverJID(boolean canAnyoneDiscoverJID) {
		this.canAnyoneDiscoverJID = canAnyoneDiscoverJID;
	}

	@Element(name="canOccupantsChangeSubject")
	public boolean isCanOccupantsChangeSubject() {
		return canOccupantsChangeSubject;
	}

	public void setCanOccupantsChangeSubject(boolean canOccupantsChangeSubject) {
		this.canOccupantsChangeSubject = canOccupantsChangeSubject;
	}

	@Element(name="canOccupantsInvite")
	public boolean isCanOccupantsInvite() {
		return canOccupantsInvite;
	}

	public void setCanOccupantsInvite(boolean canOccupantsInvite) {
		this.canOccupantsInvite = canOccupantsInvite;
	}


	@Element(name="canChangeNickname")
	public boolean isCanChangeNickname() {
		return canChangeNickname;
	}

	public void setCanChangeNickname(boolean canChangeNickname) {
		this.canChangeNickname = canChangeNickname;
	}

	@Element(name="logEnabled")
	public boolean isLogEnabled() {
		return logEnabled;
	}

	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;
	}

	@Element(name="loginRestrictedToNickname")
	public boolean isLoginRestrictedToNickname() {
		return loginRestrictedToNickname;
	}

	public void setLoginRestrictedToNickname(boolean loginRestrictedToNickname) {
		this.loginRestrictedToNickname = loginRestrictedToNickname;
	}

	@Element(name="membersOnly")
	public boolean isMembersOnly() {
		return membersOnly;
	}

	public void setMembersOnly(boolean membersOnly) {
		this.membersOnly = membersOnly;
	}

	@Element(name="moderated")
	public boolean isModerated() {
		return moderated;
	}

	public void setModerated(boolean moderated) {
		this.moderated = moderated;
	}

	//@Element(name = "broadcastPresenceRole")
	/*@ElementList(entry = "broadcastPresenceRole")
	public List<String> getBroadcastPresenceRoles() {
		return broadcastPresenceRoles;
	}

	public void setBroadcastPresenceRoles(List<String> broadcastPresenceRoles) {
		this.broadcastPresenceRoles = broadcastPresenceRoles;
	}
	
	public void addBroadcastPresenceRole(String bpr) {
		this.broadcastPresenceRoles.add(bpr);
	}
	
	//@ElementWrapper(name = "owners")
	@ElementList(entry = "owner")
	public List<String> getOwners() {
		return owners;
	}

	public void setOwners(List<String> owners) {
		this.owners = owners;
	}

	//@ElementWrapper(name = "members")
	@ElementList(entry = "member")
	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	//@ElementWrapper(name = "outcasts")
	@ElementList(entry = "outcast")
	public List<String> getOutcasts() {
		return outcasts;
	}

	public void setOutcasts(List<String> outcasts) {
		this.outcasts = outcasts;
	}

	//@ElementWrapper(name = "admins")
	@ElementList(entry = "admin")
	public List<String> getAdmins() {
		return admins;
	}

	public void setAdmins(List<String> admins) {
		this.admins = admins;
	}*/

}