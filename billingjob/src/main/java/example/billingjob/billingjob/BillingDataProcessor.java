package example.billingjob.billingjob;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class BillingDataProcessor implements ItemProcessor<BillingData, ReportingData> {
    
    // @Value("${spring.cellular.pricing.data:0.01}")
    // private float dataPricing;

    // @Value("${spring.cellular.pricing.call:0.5}")
    // private float callPricing;

    // @Value("${spring.cellular.pricing.sms:0.1}")
    // private float smsPricing;

    @Autowired
    private PricingService pricingService;

    @Value("${spring.cellular.spending.threshold:150}")
    private float spendingThreshold;

    @Override
    @Nullable
    public ReportingData process(@NonNull BillingData item) {
        double billingTotal = 
            // item.dataUsage() * dataPricing + 
            // item.callDuration() * callPricing +
            // item.smsCount() * smsPricing;
            item.dataUsage() * pricingService.getDataPricing() + 
            item.callDuration() * pricingService.getCallPricing() +
            item.smsCount() * pricingService.getSmsPricing();
        if (billingTotal < spendingThreshold) {
            return null;
        }

        return new ReportingData(item, billingTotal);
    }
}
