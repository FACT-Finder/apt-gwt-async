package net.omikron.apt.gwt.example;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExtendingServiceAsync {

	Request get(AsyncCallback<String> callback);

	Request set(Integer u, AsyncCallback<Void> callback);
}