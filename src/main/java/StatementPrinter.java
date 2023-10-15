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
      double thisAmount = 0;

      switch (play.type) {
        case TRAGEDY:
          thisAmount = 400.0;
          thisAmount = Math.max(thisAmount, thisAmount + 10.00 * (perf.audience - 30));

          break;
        case COMEDY:
          thisAmount = 300.00;
          thisAmount = Math.max(thisAmount, thisAmount + 100.00 + 5.00 * (perf.audience - 20));
          thisAmount += 3.00 * perf.audience;
          break;
        default:
          throw new Error("unknown type: ${play.type}");
      }

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
}
