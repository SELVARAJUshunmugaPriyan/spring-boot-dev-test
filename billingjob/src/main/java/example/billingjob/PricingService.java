package example.billingjob;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;

public class PricingService {

    @Value("${spring.cellular.pricing.data:0.01}")
    private float dataPricing;

    @Value("${spring.cellular.pricing.call:0.5}")
    private float callPricing;

    @Value("${spring.cellular.pricing.sms:0.1}")
    private float smsPricing;

    private final Random random = new Random();

    public float getDataPricing()
    {
        if (random.nextInt(1000) % 7 == 0)
            throw new PricingException("Error fetching data pricing");
        return dataPricing;
    }

    public float getCallPricing()
    {
        return callPricing;
    }

    public float getSmsPricing()
    {
        return smsPricing;
    }

}
