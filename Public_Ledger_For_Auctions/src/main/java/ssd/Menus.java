package ssd;

import java.util.Scanner;


public class Menus {

    static Scanner in = new Scanner(System.in);

    public static int callInitialMenu(){
        System.out.println();
        System.out.println("What you want to do?:");
        System.out.println();
        System.out.println("1 -> Create user");
        System.out.println("2 -> Verify users with existing wallets"); //
        System.out.println("0 -> Exit");
        System.out.print("Please insert an option: ");
        System.out.println();
        int choice = in.nextInt();
        return choice;
    }

    public static int userMenu() {
        System.out.println("1 -> Create more Wallets");
        System.out.println("2 -> Check balance");
        System.out.println("3 -> Do the mining");
        System.out.println("4 -> Check Transactions");
        System.out.println("0 -> Logout");
        System.out.println("Please insert an option: ");
        int choice = in.nextInt();
        return choice;
    }
}

