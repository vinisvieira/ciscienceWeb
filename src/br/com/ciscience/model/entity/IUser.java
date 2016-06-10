package br.com.ciscience.model.entity;

public interface IUser {
	
	public boolean passwordsMatch(String password, String confirmPassword);

}
