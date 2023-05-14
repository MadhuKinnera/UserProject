package com.clayfin.userproject;

import java.util.Scanner;

public class Main {

	static UserServiceImpl services = new UserServiceImpl();

	public static void main(String[] args) {

		System.out.println("Welcome to MyTube App");

		Scanner sc = new Scanner(System.in);

		while (true) {

			System.out.println("Enter Numbers to Select");
			System.out.println("====================");

			System.out.println("1. Register");
			System.out.println("2. Login");
			System.out.println("3. Forgot Password");

			int choice = sc.nextInt();

			if (choice == 1) {
				System.out.println("Enter User Details");
				System.out.println("====================");
				System.out.println("Enter id");
				Integer id = sc.nextInt();
				System.out.println("Enter Name");
				String name = sc.next();
				System.out.println("Enter Email");
				String email = sc.next();
				System.out.println("Enter Password");
				String password = sc.next();

				UserType userType = null;

				while (true) {
					System.out.println("Choose User Type ");
					System.out.println("1. BASIC USER ");
					System.out.println("2. PREMIUM USER ");

					int userTypeChoice = sc.nextInt();

					if (userTypeChoice == 1) {
						userType = userType.BASIC;
					} else if (userTypeChoice == 2) {
						userType = userType.PREMIUM;
					} else {
						System.out.println("Invalid Choice ...!");
						continue;
					}

					User user = new User(id, name, email, password, userType);

					System.out.println(services.registerUser(user));
					break;

				}

			} else if (choice == 2) {
				System.out.println("Enter Login Details ");
				System.out.println("====================");

				System.out.println("Enter email address");
				String email = sc.next();

				System.out.println("Enter Password");
				String password = sc.next();

				LoginDTO loginDTO = new LoginDTO(email, password);

				String loginResponse = services.loginUser(loginDTO);
				System.out.println(loginResponse);

				if (loginResponse.equals("Invalid Login Credentials"))
					continue;

				showDownloadOption();

			} else if (choice == 3) {

				System.out.println("Forgot Password ");
				System.out.println("================");

				System.out.println("Enter Email ");

				String email = sc.next();

				System.out.println("Enter New Password ");

				String password = sc.next();

				System.out.println(services.forgotPassword(email, password));

			} else {
				System.out.println("Invalid Choice ! Even it is also a Choice ");
			}

		}

	}

	public static void showDownloadOption() {

		Scanner sc = new Scanner(System.in);

		System.out.println("Enter Choice Quality ");

		Quality quality = null;

		while (true) {

			int count = 0;

			for (Quality q : Quality.values()) {
				System.out.println((++count) + " ." + q);
			}

			System.out.println("Enter 9 to Quit From Downlaoding..");

			int qualityChoice = sc.nextInt();

			if (qualityChoice == 1) {
				quality = Quality.Q_360P;
			} else if (qualityChoice == 2) {
				quality = Quality.Q_480P;
			} else if (qualityChoice == 3) {
				quality = Quality.Q_720P;
			} else if (qualityChoice == 4) {
				quality = Quality.Q_1080P;
			} else if (qualityChoice == 9) {
				System.out.println("Download Stopped...");
				break;
			} else {
				System.out.println("Invalid Choice ");
				continue;
			}

			services.downloadVideo(quality);

		}

	}

}
