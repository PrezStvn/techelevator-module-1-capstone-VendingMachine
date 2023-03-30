package com.techelevator.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachineInventory {
    //Instance Variables
    private List<VendingItem> inventory = new ArrayList<>();
    private static final BigDecimal NICKEL = new BigDecimal(".05");
    private static final BigDecimal DIME = new BigDecimal(".10");
    private static final BigDecimal QUARTER = new BigDecimal(".25");
    private static final BigDecimal PENNY = new BigDecimal(".01");
    private static final BigDecimal DOLLAR = new BigDecimal("1.00");
    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private BigDecimal currBalance = new BigDecimal("0.00");
    private int hasDiscount = 1;


    public VendingMachineInventory() {

    }
    public VendingItem searchById(String s) {
        for(VendingItem item : inventory) {
            if(item.getVendingId().equals(s)) return item;
        }
        return null;
    }
    public VendingItem searchByName(String s) {
        for(VendingItem item: inventory) {
            if(item.getCandyName().equals(s)) return item;
        }
        return null;
    }


    public void parseInventory() throws FileNotFoundException {
        File cateringFile = new File("catering.csv");
        Scanner scnr = new Scanner(cateringFile);

        while(scnr.hasNext()) {
            String[] oneItem = scnr.nextLine().split(",");
            inventory.add(new VendingItem(oneItem[0], oneItem[1], oneItem[2], oneItem[3]));
        }
    }

    public void displayInventory() {
        for(VendingItem item : inventory) {
            System.out.println(item.getVendingId() + " " + item.getCandyName() + ": " + item.getPrice() + " " + item.getStock());
        }
    }

    public BigDecimal getCurrBalance() {
        return currBalance;
    }

    //TODO: potentially write custom exception in items to throw out when no stock left.
    public String purchaseItem(String s) {

        if (this.currBalance.compareTo(searchById(s).getPrice()) == 1 && searchById(s).getStock() > 0) {
            searchById(s).itemPurchased();
            if(hasDiscount < 0) {
                this.currBalance = this.currBalance.add(new BigDecimal("1.00"));
            }
            this.currBalance = this.currBalance.subtract(searchById(s).getPrice());
            //Dispensing string
            if (searchById(s).getCategory().equals("Munchy")) {
                return "Item Dispensing..." + searchById(s).getVendingId() + " " +  searchById(s).getCandyName() + " Munchy, Munchy, so Good!";
            }
            if (searchById(s).getCategory().equals("Candy")) {
                return "Item Dispensing..."  + searchById(s).getVendingId() + " " +  searchById(s).getCandyName() +  " Sugar, Sugar, so Sweet!";
            }
            if (searchById(s).getCategory().equals("Drink")) {
                return "Item Dispensing..."  + searchById(s).getVendingId() + " " +  searchById(s).getCandyName() +  " Drinky, Drinky, Slurp Slurp!";
            }
            if (searchById(s).getCategory().equals("Gum")) {
                return "Item Dispensing..."  + searchById(s).getVendingId() + " " +  searchById(s).getCandyName() +  " Chewy, Chewy, Lots O Bubbles!";
            }
            return "";
        } else {
            //logic to check if purchase failed because of no money or no stock
            return this.currBalance.compareTo(searchById(s).getPrice()) == -1 ? "You don't have enough money!" : "Item out of stock!";
        }
    }
    public void feedMoney(String inputVal) {
        this.currBalance = this.currBalance.add(new BigDecimal(inputVal));
        System.out.println("***** CURRENT MONEY PROVIDED: " + this.currBalance + " *****");
    }

    public String finishTransaction() {
        String changeOutput = "";
        int amountOfDollars = (this.currBalance.divide(DOLLAR)).intValue();
        int amountOfQuarters = (this.currBalance.remainder(DOLLAR).divide(QUARTER)).intValue();
        int amountOfDimes = (this.currBalance.remainder(DOLLAR).remainder(QUARTER).divide(DIME)).intValue();
        int amountOfNickels = (this.currBalance.remainder(DOLLAR).remainder(QUARTER).remainder(DIME).divide(NICKEL)).intValue();
        int amountOfPennies = (this.currBalance.remainder(DOLLAR).remainder(QUARTER).remainder(DIME).remainder(NICKEL).divide(PENNY)).intValue();
        if (amountOfDollars > 0) {
            changeOutput += amountOfDollars + " Dollars ";
        }
        if (amountOfQuarters > 0) {
            changeOutput += amountOfQuarters + " Quarters ";
        }
        if (amountOfDimes > 0) {
            changeOutput += amountOfDimes + " Dimes ";
        }
        if (amountOfNickels>0) {
            changeOutput += amountOfNickels + " Nickels ";
        }
        if (amountOfPennies>0) {
            changeOutput += amountOfPennies + " Pennies ";
        }
        this.currBalance = ZERO;
        System.out.println(changeOutput);
            return changeOutput;

    }
}
