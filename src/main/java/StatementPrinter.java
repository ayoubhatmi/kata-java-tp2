import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {

  public static final String TRAGEDY = "tragedy";
  public static final String COMEDY = "comedy";

  public String print(Invoice invoice, HashMap<String, Play> plays) {
    double totalAmount = 0;
    int volumeCredits = 0;
    StringBuilder result = new StringBuilder();
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    result.append(String.format("Statement for %s\n", invoice.customer));

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      double thisAmount = calculatePerformanceAmount(play,perf);

      // add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // add extra credit for every ten comedy attendees
      if (COMEDY.equals(play.type)) volumeCredits +=
        Math.floor(perf.audience / 5);

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
  private double calculatePerformanceAmount(Play play, Performance perf) {
    double thisAmount =0;
    switch (play.type) {
      case TRAGEDY:
          return calculateTragedyAmount(perf.audience);
      case COMEDY:
          return calculateComedyAmount(perf.audience);
      default:
        throw new Error("unknown type: ${play.type}");
    }
}
  private double calculateTragedyAmount(int audience) {
      double baseAmount = 400.0;
      return Math.max(baseAmount, baseAmount + 10.00 * (audience - 30));
}

  private double calculateComedyAmount(int audience) {
      double baseAmount = 300.00;
      double amount = Math.max(baseAmount, baseAmount + 100.00 + 5.00 * (audience - 20));
      amount += 3.00 * audience;
      return amount;
  }


  
}
