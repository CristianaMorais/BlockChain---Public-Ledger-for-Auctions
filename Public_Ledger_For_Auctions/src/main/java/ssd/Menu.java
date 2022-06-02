package ssd;

import java.util.Scanner;


public class Menu {

    public static int callMenu(){

        System.out.println("-------------------Menu--------------------");
        System.out.println("1-> Creating Wallets\n2-> Creating Transactions\n3-> Do the mining\n0->Exit\n");
        System.out.println("Please insert an option: ");
        Scanner in = new Scanner(System.in);
        return in.nextInt();
    }

    public static void main(String[] args) {

        int choice = callMenu();
        switch (choice){
            case 1:

                break;

            case 2:

                break;


            case 3:

                break;


            case 0:
                System.exit(0);
                break;

            default:
                System.out.println("Invalid option! Pls try again.");
                System.out.println();
                System.out.println();
                break;
        }
    }
}

