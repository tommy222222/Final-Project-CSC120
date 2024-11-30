import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
//=================================================================================================
/**
 * Load fleet data from a CSV file. Manipulate fleet data by either: Adding a boat, Removing a Boat,
 * or Handling expenses for a boat. When exiting program, save information to FleetData.db for subsequent run(s).
 * @author Thomas Potts
 */
public class FleetManagement {

    /**
     * Global Scanner object to use keyboard
     */
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * Initialize array to store boats in fleet
     */
    private static final ArrayList<Boat> fleetData = new ArrayList<>();
//-------------------------------------------------------------------------------------------------
    /**
     * The main method
     * @param args Passed in from the command line
     */
    public static void main(String[] args) {
//---- Call method to import boat information from either CSV file or saved FleetData.db
        importFleetData(args);
//---- Welcome user to the program
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

//---- Define menuOption variable and prompt user to choose a menu option; run corresponding method for each option
        char menuOption;
        do {
            menuOption = displayMenu();
            switch (menuOption) {
                case 'P':
                    printFleet();
                    break;
                case 'A':
                    addNewBoat();
                    break;
                case 'R':
                    removeBoat();
                    break;
                case 'E':
                    handleExpenses();
                    break;
//---- Case to exit the program and save information into FleetData.db
                case 'X':
                    saveFleetData();
                    System.out.println("Exiting the Fleet Management System :)");
                    break;
//---- Default to ensure menuOption is an acceptable character
                default:
                    System.out.println("Invalid menu option, try again");
                    break;
            }
        } while (menuOption != 'X');
    }
//-------------------------------------------------------------------------------------------------

    /**
     * Displays a menu of user options and validates input
     * @return Character of user menu option choice (P, A, R, E, X).
     */
    public static char displayMenu() {
        System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
//---- Read input and trim whitespace
        String line = keyboard.nextLine().trim();
//---- Convert input to an uppercase character
        char menuOption = Character.toUpperCase(line.charAt(0));

//---- Validate input to ensure it matches an allowable option
        while (menuOption != 'P' && menuOption != 'A' && menuOption != 'R' &&
             menuOption != 'E' && menuOption != 'X') {
//---- If invalid input, prompt user to enter again
            System.out.print("Invalid menu option, try again: ");
                line = keyboard.nextLine().trim();
                menuOption = Character.toUpperCase(line.charAt(0));
        }
//---- Return valid menu option chosen by the user
        return menuOption;
    }
//-------------------------------------------------------------------------------------------------

    /**
     * Create Boat objects from CSV data to use in program
     * If no CSV is provided, it attempts to load from previous saved data
     * @param args CSV data provided in args Array
     */
    private static void importFleetData(String[] args) {
//---- Check if file path is provided in arguments
        if (args.length > 0) {
            String csvFile = args[0];

//---- Try to read file and initialize boat objects
            try (Scanner scanner = new Scanner(new FileReader(csvFile))) {

//---- Reads the file and creates a new boat for each line
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

//---- Creates an array to store the data for the boat, separated by a comma
                    String[] boatData = line.split(",");

//---- Parse each piece of data to correct type
                    BoatType type = BoatType.valueOf(boatData[0].toUpperCase());
                    String boatName = boatData[1];
                    int yearOfManufacture = Integer.parseInt(boatData[2]);
                    String makeAndModel = boatData[3];
                    double lengthInFeet = Double.parseDouble(boatData[4]);
                    double purchasePrice = Double.parseDouble(boatData[5]);

//---- Creates a new Boat object and adds it to the fleet data list
                    fleetData.add(new Boat(type, boatName, yearOfManufacture,
                            makeAndModel, lengthInFeet, purchasePrice));
                }
            }
//---- Catches potential exceptions and prints an error message if there is an issue with the file
            catch (IOException | IllegalArgumentException exception) {
                System.out.println("Error reading CSV file: " + exception.getMessage());
            }

        }
        else {
//---- If no file is provided, load data from previously saved fleet data
            loadFleetData();
        }
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Allow user to add a new boat to the fleet in CSV format.
     */
    private static void addNewBoat() {
//---- Prompts user to enter new boat information separated by commas
        System.out.print("Please enter the new boat CSV data          : ");
        String boatInput = keyboard.nextLine();

        try {
//---- Parse each piece of data to correct type
            String[] newBoatData = boatInput.split(",");
            BoatType type = BoatType.valueOf(newBoatData[0].toUpperCase());
            String boatName = newBoatData[1];
            int yearOfManufacture = Integer.parseInt(newBoatData[2]);
            String makeAndModel = newBoatData[3];
            double lengthInFeet = Double.parseDouble(newBoatData[4]);
            double purchasePrice = Double.parseDouble(newBoatData[5]);

//---- Creates a new boat object and adds it to the fleet data list
            fleetData.add(new Boat(type, boatName, yearOfManufacture,
                        makeAndModel, lengthInFeet, purchasePrice));
            System.out.println(boatName + " added successfully.");

        }
//---- Catches incorrect formatting and prompts user to correct it
        catch (IllegalArgumentException exception) {
            System.out.println("Invalid input. Make sure data is correctly formatted.");
        }
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Allow user to remove a boat in the fleet if it exists.
     */
    private static void removeBoat() {
//---- Prompts user to input the name of the Boat to be removed
        System.out.print("Which boat do you want to remove?           : ");
        String boatToRemove = keyboard.nextLine();

//---- Searches fleet data list for the Boat to be removed and removes it if in list
        for (int index = 0; index < fleetData.size(); ++index) {
            if (fleetData.get(index).getName().equalsIgnoreCase(boatToRemove)) {
                fleetData.remove(index);
                System.out.println(boatToRemove + " removed successfully.");
//---- Returns to main if the boat was successfully removed
                return;
            }
        }
//---- If the boat is not found, prints a message indicating so
        System.out.println("Cannot find boat " + boatToRemove + ".");
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Displays the fleet data including boat type, boat name, year of manufacture, make and model,
     * length in feet, purchase price, and maintenance expenses
     */
    private static void printFleet() {
//---- If fleet data list is empty, prints a message and returns to main
        if (fleetData.isEmpty()) {
            System.out.println("No boats are in the fleet.");
            return;
        }

        System.out.println("Fleet report:");

//---- Declares variables totals for total paid and spent, initializes to zero
        double totalPaid = 0.0;
        double totalSpent = 0.0;

//---- Loops over fleet data list and prints toString method from the boat class for each Boat
        for (Boat fleetDatum : fleetData) {
            System.out.println(fleetDatum.toString());

//---- Adds purchase price and maintenance expense together for each boat to tally the total paid and spent
            totalPaid += fleetDatum.getPurchasePrice();
            totalSpent += fleetDatum.getMaintenanceExpense();
        }
//---- Prints total paid and spent for fleet data
        System.out.printf("Total                                            : Paid $%10.2f : Spent $%10.2f%n",
                totalPaid, totalSpent);
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Allows user to spend money on a boat as long as it costs less than the purchase price of the boat
     */
    private static void handleExpenses() {
//---- Prompts user to enter the name of the Boat to spend money on
        System.out.print("Which boat do you want to spend on?         : ");
        String boatToSpendOn = keyboard.nextLine();

//---- Loops over fleet data list to find if inputted Boat is in the list
        for (Boat fleetDatum : fleetData) {

            if (fleetDatum.getName().equalsIgnoreCase(boatToSpendOn)) {
//---- If the Boat is in the list, prompt the user to enter the amount to spend on the Boat
                System.out.print("How much do you want to spend?              : ");
                double amountToSpend = keyboard.nextDouble();
                keyboard.nextLine();
//---- If the amount to spend is less than the purchase price of the boat, add amount to
//---- maintenance expense and print authorization message
                if (fleetDatum.addExpense(amountToSpend)) {
                    double totalSpent = fleetDatum.getMaintenanceExpense();
                    System.out.printf("Expense authorized. $%.2f spent.%n", totalSpent);
                }
//---- If amount to spend is more than the purchase price, do not allow user to spend money
                else {
                    System.out.printf("Expense not permitted, only $%.2f left to spend.%n",
                            fleetDatum.getRemainingBudget());
                }
                return;
            }
        }
//---- Print message if Boat is not found in fleet data list
        System.out.println("Cannot find boat " + boatToSpendOn);
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Saves fleet data into FleetData.db to user for subsequent runs.
     */
    private static void saveFleetData() {
//---- Tries to save fleet data to a file using ObjectOutputStream
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("FleetData.db"))) {
//---- Serializes the ArrayList and writes it to the file
            out.writeObject(fleetData);
        }
//---- Handles exceptions during file writing process and prints message
        catch (IOException e) {
            System.out.println("Error saving fleet data: " + e.getMessage());
        }
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Loads data from FleetData.db to use in the run.
     * If the file is not found, start with an empty fleet
     */
    private static void loadFleetData() {
//---- Tries to load the fleet data from the file using ObjectInputStream
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("FleetData.db"))) {
//---- Clears existing fleet data to avoid duplicate entries
            fleetData.clear();
//---- Adds saved list to fleetData list
            fleetData.addAll((ArrayList<Boat>) in.readObject());
            System.out.println("Fleet data loaded successfully.");
        }
//---- Starts with empty fleet if no file is found
        catch (FileNotFoundException e) {
            System.out.println("No saved fleet data found. Starting with an empty fleet.");
        }
//---- Handles additional file errors and prints message
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading fleet data: " + e.getMessage());
        }
    }
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================

