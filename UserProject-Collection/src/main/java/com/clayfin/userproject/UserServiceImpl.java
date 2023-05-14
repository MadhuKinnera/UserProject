package com.clayfin.userproject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

	private User currentUser;

	static Set<User> users = new HashSet<>();

	private String encrypt(String password) {
		try {

			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			byte[] hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));

			String encodedPassword = Base64.getEncoder().encodeToString(hashedPassword);

			return encodedPassword;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "Encryption Failed";
		}
	}

	@Override
	public String registerUser(User user) {
		if (user.getEmail() == null || user.getPassword() == null || user.getId() == null || user.getName() == null)
			return "Registration failed";

		user.setPassword(encrypt(user.getPassword()));

		users.add(user);
		printUsers();
		return "Hello " + user.getName() + " registration Successful";
	}

	@Override
	public String loginUser(LoginDTO loginDTO) {
		loginDTO.setPassword(encrypt(loginDTO.getPassword()));
		List<User> loginUser = users.stream()
				.filter(u -> u.getEmail().equals(loginDTO.getEmail()) && u.getPassword().equals(loginDTO.getPassword()))
				.collect(Collectors.toList());

		if (loginUser.isEmpty()) {
			return "Invalid Login Credentials";
		} else {
			currentUser = loginUser.get(0);
			return "Login Success with email " + loginDTO.getEmail();
		}

	}

	@Override
	public void downloadVideo(Quality quality) {

		if (currentUser.getUserType() == UserType.BASIC) {
			if (quality == Quality.Q_360P || quality == Quality.Q_480P) {
				System.out.println("Downloading... video in " + String.valueOf(quality).substring(2) + " Quality");
			} else {
				System.out.println("Upgrade to Premium to Download the video in " + String.valueOf(quality).substring(2)
						+ " Quality");
			}
		} else {
			System.out.println("Downloading... video in " + String.valueOf(quality).substring(2) + " Quality");
		}

	}

	@Override
	public String forgotPassword(String email, String newPassword) {

		List<User> loginUser = users.stream().filter(u -> u.getEmail().equals(email)).collect(Collectors.toList());

		if (loginUser.isEmpty())
			return "User Doesnot exist with the email " + email;

		String otp = String.valueOf(generateRandomNumber());

	//	System.out.println("Sending OTP to " + email);

		GmailSender gs = new GmailSender();
		gs.sendGmail(email, "OTP for Forgot Password ", "Your OTP is " + otp);

		System.out.println("Enter Your OTP ");
		String otpFromUser = new Scanner(System.in).next();

		if (otp.equals(otpFromUser)) {
			System.out.println("OTP Matched ....");
			User user = loginUser.get(0);

			user.setPassword(encrypt(newPassword));

			printUsers();

			return "password Updated Success";
		} else {
			System.out.println("OTP Doesnot Matched....");
			return "Password Updation Failed..";
		}

	}

	public void printUsers() {
		// System.out.println("Printing users");
		for (User user : users) {
			System.out.println(user);
		}
	}

	public static int generateRandomNumber() {
		Random random = new Random();
		int min = 100000;
		int max = 999999;
		return random.nextInt(max - min + 1) + min;
	}

}
