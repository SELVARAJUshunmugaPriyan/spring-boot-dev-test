package example.billingjob.billingjob;

public record ReportingData(
    BillingData billingData,
    double billingTotal
) {
}
