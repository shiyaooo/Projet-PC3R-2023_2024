package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
	
	private String id;
	
	private String username;
	
	private String realname;
	
	private String password;
	
	private String mail;
	
	//private String codeActive;
	
	private boolean isConnected;
	
	private Date birthday;
	
	private boolean isVIP;
	
	private String profilImage;
	
	private List<String> myfollowers;
	
	private List<String> myfollowings;
	
	private List<String> myfriends;
	
	private String introduction;
	
	/*public Users(){
		
	}*/
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getRealname() {
		return this.realname;
	}
	
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getMail() {
		return this.mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public boolean getIsConnected() {
		return this.isConnected;
	}
	
	public void setIsConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public Date getBirthday() {
		return this.birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public boolean getIsVIP() {
		return this.isVIP;
	}

	public void setIsVIP(boolean isVIP) {
		this.isVIP = isVIP;
	}
	
	public String getProfilImage() {
		return this.profilImage;
	}
	
	public void setProfilImage(String profilImage) {
		this.profilImage = profilImage;
	}
	
	public List<String> getMyfollowers(){
		return new ArrayList<String>(this.myfollowers);
	}
	
	public void setMyfollowers(List<String> myfollowers){
		this.myfollowers = new ArrayList<String>(myfollowers);
	}
	
	public List<String> getMyfollowings(){
		return new ArrayList<String>(this.myfollowings);
	}
	
	public void setMyfollowings(List<String> myfollowings){
		this.myfollowings = new ArrayList<String>(myfollowings);
	}
	
	public List<String> getMyfriends(){
		return new ArrayList<String>(this.myfriends);
	}
	
	public void setMyfriends(List<String> myfriends){
		this.myfriends = new ArrayList<String>(myfriends);
	}
	
	public String getIntroduction() {
		return this.introduction;
	}
	
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
}
