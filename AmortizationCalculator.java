import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

public class AmortizationCalculator {

    //getting input
    private static Scanner scanner = new Scanner(System.in);
    private static double monthlyRate;

    public static void main(String[] args) {
        System.out.println("Please enter your total loan ammount: ");
        double principal =scanner.nextDouble();
        System.out.println("Please enter your APR interest rate(formate is 00.00): ");
        double interest =scanner.nextDouble();
        System.out.println("Please enter your total number of payments: ");
        int numberOfPayments =scanner.nextInt();
        monthlyRate =(double) interest /1200;
        double amortization = calculate(principal,numberOfPayments);
        System.out.println("************************************");
        System.out.printf("Your periodic payment amount is %.4f\n",amortization);
        System.out.println("************************************");
        printTable(principal,numberOfPayments);
    }
    private static double calculate(double principle ,int numberOfPayments){
        double amortization =0.00;
        if(monthlyRate>0){
            amortization =(principle * monthlyRate * Math.pow(1+monthlyRate,(double) numberOfPayments))/
                    (Math.pow(1+monthlyRate,(double)numberOfPayments)-1);
        }
        else{
            amortization =principle/numberOfPayments;
        }
        return amortization;
    }
    public static void printTable(double principal,int numberOfPayments){
        Object[] headers ={"Payments #" ,"Amount Due" ,"Interest Due", "Amount left"};
        System.out.println("\n************************************Your Mortgage Payoff Table************************************");
        System.out.println("************************************----------------------------************************************");
        //space between table data
        System.out.format("%5s %12s %18s %18s   \n", headers);
      System.out.println("*******************************************************************************************");
      double monthlyPayment =calculate(principal,numberOfPayments);
      double interestPayment =0.0;
      double principalDue =0.0;
      double principalLeft =principal;
      boolean loanPaid =false;
      //To save file to Csv
      Object[][] rows =new Object[numberOfPayments][];
      //till here
      for(int i=0;i<numberOfPayments;i++){
          interestPayment =monthlyRate * principalLeft;
          principalDue = monthlyPayment-interestPayment;
          principalLeft =Math.abs(principalLeft-principalDue);

          Object[] row ={i+1,monthlyPayment,interestPayment,principalDue,principalLeft};
          rows[i] =row;
          System.out.format("%5d%17.4f%19.4f%19.4f%17.4f\n",row);
      }
      saveInCSV(rows);
    }
    private static void saveInCSV(Object[][] rows){
        System.out.println("\nWould you like to save the result to Csv file? [Yes/No]");
        String response =scanner.next();
        if("yes".equalsIgnoreCase(response)){
          try {
              FileWriter fileWriter =new FileWriter("amortization_table.csv");
              BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);
              //to  build string to bufferwriter
              StringBuilder stringBuilder =new StringBuilder();

              for(Object[] row:rows){
                  for (Object column :row){
                      if(stringBuilder.length()!=0){
                          stringBuilder.append(',');
                      }
                      stringBuilder.append(column);
                  }
                  stringBuilder.append("\n");
                  bufferedWriter.write(stringBuilder.toString());
                  stringBuilder.setLength(0);
              }
              //clears the details
              bufferedWriter.close();
              System.out.println("Results are  saved to CSV");
          }catch (IOException e){
              e.printStackTrace();
          }
        }else{
            System.out.println("Results are not saved to CSV");
        }
    }
}
