package example.billingjob.billingjob;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

public class BillingDataRetryListener implements RetryListener {
    int count = 0;

    @Override
    public void onSuccess(RetryContext context,
        @SuppressWarnings("rawtypes") RetryCallback callback, Object result) {
            System.out.println("Retry count: " + count);
            count = 0;
        }

    @Override
    public void onError(RetryContext context, 
        @SuppressWarnings("rawtypes") RetryCallback callback,
            Throwable throwable) {
                count++;
        }
}
