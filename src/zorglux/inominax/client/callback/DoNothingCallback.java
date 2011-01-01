package zorglux.inominax.client.callback;


public class DoNothingCallback<T> extends BasicCallback<T> {

   @Override
   public void onResult(T result) {
      // do nothing
   }
}
