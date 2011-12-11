package zorglux.inominax.client.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class BasicCallback<T> implements AsyncCallback<T> {

  @Override
  public final void onFailure(Throwable caught) {
    // Should never be used...
  }

  @Override
  public final void onSuccess(T result) {
    onResult(result);
  }

  public abstract void onResult(T result);
}
