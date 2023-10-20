import java.text.NumberFormat;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class StatementPrinter {

  public static final String TRAGEDY = "tragedy";
  public static final String COMEDY = "comedy";

  public String print(Invoice invoice, HashMap<String,Play> plays) {

    StringBuilder result = new StringBuilder();
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    double totalAmount = 0;
    int volumeCredits = 0;

    result.append(String.format("Statement for %s\n", invoice.customer));

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);


      double thisAmount = calculatePerformanceAmount(play,perf);

      volumeCredits += calculateVolumeCredits(play, perf);

      // append line for this order
      result.append(
        String.format(
          "  %s: %s (%s seats)\n",
          play.name,
          frmt.format(thisAmount),
          perf.audience
        )
      );
      totalAmount += thisAmount;
    }
    result.append(
      String.format("Amount owed is %s\n", frmt.format(totalAmount))
    );
    result.append(String.format("You earned %s credits\n", volumeCredits));
    return result.toString();
  }


  public static void toHtml(Invoice invoice, HashMap<String,Play> plays, String htmlFilePath) {

    StringBuilder result = new StringBuilder();
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    double totalAmount = 0;
    int volumeCredits = 0;

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);


      double thisAmount = calculatePerformanceAmount(play,perf);

      volumeCredits += calculateVolumeCredits(play, perf);

      // append line for this order
      result.append(
        String.format(
          "<tr> <td>%s</td> <td>%s</td> <td>%s </td></tr> ",
          play.name,
          frmt.format(thisAmount),
          perf.audience
        )
      );
      totalAmount += thisAmount;
    }
    result.append(
      String.format("<tr><td>Total owed</td><td>%s</td></tr>", frmt.format(totalAmount))
    );
    result.append(String.format("<tr><td>Fidelity points earned</td><td>%s</td></tr>", volumeCredits));
 

String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Generated HTML File</title>\n" +
                "<style> table, th, td {border: 1px solid black;}</style>"+
                "</head>\n" +
                "<body>\n" +
                "<h1>" + "Invoice" + "</h1>" +
                "<p>" + "Client :" + invoice.customer + "</p>" +
                "<table>"+
                "<tr> "+ "<th>"+"Piece"+"</th>"+"<th>"+"Seats Solde"+"</th>"+"<th>"+"Price"+"</th>"+"</tr>"+ 
                result +
                "</table>"+
                "</body>\n" +
                "</html>";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(htmlFilePath));
            writer.write(htmlContent);
            writer.close();
            System.out.println("HTML file generated successfully at: " + htmlFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }


  }


 public static void main(String[] args) {
         String htmlFilePath = "/mnt/local.isima.fr/yabahou/kata-java-tp2/src/html/output.html"; // Path to the output HTML file
         HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet",  new Play("Hamlet", "tragedy"));
        plays.put("as-like",  new Play("As You Like It", "comedy"));
        plays.put("othello",  new Play("Othello", "tragedy"));

        Invoice invoice = new Invoice("BigCo", List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

            toHtml(invoice, plays, htmlFilePath);
     }



  private static double calculatePerformanceAmount(Play play, Performance perf) {
    switch (play.type) {
      case TRAGEDY:
          return calculateTragedyAmount(perf.audience);
      case COMEDY:
          return calculateComedyAmount(perf.audience);

      default:
        throw new Error("unknown type: ${play.type}");
    }
}

  private static double calculateTragedyAmount(int audience) {
      double baseAmount = 400.0;
      return Math.max(baseAmount, baseAmount + 10.00 * (audience - 30));
}


  private static double calculateComedyAmount(int audience) {
      double baseAmount = 300.00;
      double amount = Math.max(baseAmount, baseAmount + 100.00 + 5.00 * (audience - 20));
      amount += 3.00 * audience;
      return amount;
  }
  
  private static int calculateVolumeCredits(Play play, Performance perf) {
        int credits = Math.max(perf.audience - 30, 0);

        if (COMEDY.equals(play.type)) {
            credits += perf.audience / 5;
        }
        return credits;
    }
}
