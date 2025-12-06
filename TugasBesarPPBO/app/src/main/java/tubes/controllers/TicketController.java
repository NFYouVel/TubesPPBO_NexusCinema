package tubes.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tubes.models.AuditRecord;
import tubes.models.exceptions.InvalidDateException;
import tubes.repositories.TicketRepository;

public class TicketController {
    TicketRepository ticketRepository;

    public TicketController() {
        ticketRepository = new TicketRepository();
    }

    public String getTicketListByMonth(String startDate, String endDate) throws InvalidDateException {

        if (startDate.compareTo(endDate) > 0) {
            throw new InvalidDateException(
                    "Invalid date range: start date " + startDate + " is later than end date " + endDate + ".");
        }

        List<AuditRecord> auditList = ticketRepository.getTicketListByMonth(startDate, endDate);
        int grandTotal = 0;
        // StringBuilder report = new StringBuilder();
        String report = "";

        Map<String, List<Integer>> studioPriceMap = new HashMap<>();

        for (AuditRecord r : auditList) {
            studioPriceMap
                    .computeIfAbsent(r.getStudioNumber(), k -> new ArrayList<>())
                    .add(r.getTicketPrice());
        }

        report += ("AUDIT REPORT — Nexus Cinema Monthly Revenue\n");
        report += ("--------------------------------------\n");

        for (AuditRecord auditRecord : auditList) {
            String studio = auditRecord.getStudioNumber();
            int price = auditRecord.getTicketPrice();
            List<Integer> prices = studioPriceMap.get(studio);
            int minPrice = prices.stream().min(Integer::compareTo).orElse(price);
            int maxPrice = prices.stream().max(Integer::compareTo).orElse(price);

            report += ("Studio: " + auditRecord.getStudioNumber() + "\n");
            report += ("Type: " + auditRecord.getStudioType() + "\n");
            if (minPrice == maxPrice) {
                report += ("Ticket Price (Weekday & Weekend): " + price + "\n");
            } else if (price == maxPrice) {
                report += ("Ticket Price (Weekend): "+ price + "\n");
            } else {
                report += ("Ticket Price (Weekday): "+ price + "\n");
            }
            report += ("Tickets Sold: " + auditRecord.getTicketCount() + "\n");
            report += ("Total Income: " + auditRecord.getTotalIncome() + "\n\n");

            grandTotal += auditRecord.getTotalIncome();
        }
        report += ("--------------------------------------\n");
        report += ("Grand Total Income: " + grandTotal);

        // return report.toString();
        return report;
    }
}
