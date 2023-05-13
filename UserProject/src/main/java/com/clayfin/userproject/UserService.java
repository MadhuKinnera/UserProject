package com.clayfin.userproject;

public interface UserService {

	String registerUser(User user);

	String loginUser(LoginDTO user);

	void downloadVideo(Quality quality);
	
	String forgotPassword(String email,String newPassword);

}
