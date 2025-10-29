package com.mthree.flooringmastery.view;

import java.math.BigDecimal;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " ");
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double result;
        do {
            result = readDouble(prompt);
        } while (result < min || result > max);
        return result;
    }

    @Override
    public float readFloat(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " ");
                return Float.parseFloat(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float result;
        do {
            result = readFloat(prompt);
        } while (result < min || result > max);
        return result;
    }

    @Override
    public int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int result;
        do {
            result = readInt(prompt);
        } while (result < min || result > max);
        return result;
    }

    @Override
    public long readLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " ");
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid whole number.");
            }
        }
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        long result;
        do {
            result = readLong(prompt);
        } while (result < min || result > max);
        return result;
    }

    @Override
    public String readString(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        BigDecimal value = null;
        boolean valid = false;

        while (!valid) {
            try {
                String input = readString(prompt);

                // ✅ Stop infinite loops if user enters nothing or scanner breaks
                if (input == null || input.trim().isEmpty()) {
                    System.out.println("⚠️ Input cannot be empty. Please try again.");
                    continue;
                }

                value = new BigDecimal(input.trim());
                valid = true;

            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid numeric value for area.");
            } catch (Exception e) {
                System.out.println("⚠️ Unexpected input error. Returning to menu.");
                break; // ✅ prevents infinite recursion
            }
        }

        return value;
    }

}
