package example.billingjob;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

public class BillingDataProcessor implements ItemProcessor<BillingData, ReportingData> {
    
    private PricingService pricingService;

    public BillingDataProcessor(PricingService pricingService)
    {
        this.pricingService = pricingService;
    }

    @Value("${spring.cellular.spending.threshold:150}")
    private float spendingThreashold;

    @Override
    public ReportingData process(@NonNull BillingData item)
    {
        double billingTotal = item.dataUsage() * pricingService.getDataPricing() + 
        item.callDuration() * pricingService.getCallPricing() +
            item.smsCount() * pricingService.getSmsPricing();
        
        if (billingTotal < spendingThreashold)
            return null;
        
        return new ReportingData(item, billingTotal);
    }
}
