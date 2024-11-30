import java.io.Serializable;
//=================================================================================================
/**
 * Represents a Boat with various attributes: type, name, year of manufacture, make and model,
 * length in feet, purchase price, and maintenance expense.
 * Implements Serializable to allow boat data to save and load for subsequent runs
 * @author Thomas Potts
 */
public class Boat implements Serializable {
    /**
     * The type of boat (Power or Sailing)
     */
    private final BoatType type;
    /**
     * The name of the boat
     */
    private final String name;
    /**
     * The year that the boat was manufactured
     */
    private final int yearOfManufacture;
    /**
     * The make and model of the boat
     */
    private final String makeAndModel;
    /**
     * The length of the boat in feet
     */
    private final double lengthInFeet;
    /**
     * The price the boat was purchased for
     */
    private final double purchasePrice;
    /**
     * The amount of money spent on boat maintenance
     */
    private double maintenanceExpense;

    /**
     * Creates a new Boat object with different attributes
     * @param type The type of the boat (enum BoatType)
     * @param name The name of the boat
     * @param yearOfManufacture The year the boat was manufactured
     * @param makeAndModel The make and model of the boat
     * @param lengthInFeet The length of the boat in feet
     * @param purchasePrice The purchase price of the boat
     */
//-------------------------------------------------------------------------------------------------
    public Boat(BoatType type, String name, int yearOfManufacture, String makeAndModel,
                double lengthInFeet, double purchasePrice) {
//---- Assigns variables to the boat
        this.type = type;
        this.name = name;
        this.yearOfManufacture = yearOfManufacture;
        this.makeAndModel = makeAndModel;
        this.lengthInFeet = lengthInFeet;
        this.purchasePrice = purchasePrice;
//---- Initializes maintenance expense to zero
        this.maintenanceExpense = 0.00;
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Retrieves the name of the boat
     * @return the name of the boat
     */
    public String getName() {
        return name;
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Retrieves the purchase price of the boat
     * @return the purchase price of the boat
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Retrieves amount spent on boat maintenance
     * @return the total maintenance expenses
     */
    public double getMaintenanceExpense() {
        return maintenanceExpense;
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Adds money to spend on boat maintenance if total expenses don't exceed the purchase price
     * @param amount The amount to of money requested to spend on the boat
     * @return True if the boat was successfully added and False if not
     */
    public boolean addExpense(double amount) {
//---- Checks if total maintenance expenses after adding the amount will exceed the purchase price
        if (maintenanceExpense + amount <= purchasePrice) {
            maintenanceExpense += amount;
//---- Returns true if expense is permitted
            return true;
        }
        else {
//---- Returns false if expense is not permitted
            return false;
        }
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Returns a string including boat information including, type, name, year of manufacture, make and model,
     * length in feet, purchase price, and maintenance expenses
     * @return A string describing the boat
     */
    public String toString() {
//---- Formats and returns boat information so data looks clean and readable
        return String.format(
                "%-8s %-20s %4d %-12s %3.0f' : Paid $%10.2f : Spent $%10.2f",
                type, name, yearOfManufacture, makeAndModel, lengthInFeet, purchasePrice, maintenanceExpense
        );
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Calculates and returns the remaining budget available for maintenance
     * @return The remaining budget
     */
    public double getRemainingBudget() {
//--- Remaining budget is the purchase price minus maintenance expenses
        return purchasePrice - maintenanceExpense;
    }
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================
