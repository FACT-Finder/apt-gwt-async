package net.omikron.apt.gwt.example;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import net.omikron.apt.gwt.ResultWrapper;

public interface ReturnTypeServiceAsync {

	Request voidOperation(AsyncCallback<Void> callback);

	Request primitiveOperation(AsyncCallback<Integer> callback);

	Request objectOperation(AsyncCallback<String> callback);

	Request genericTypeOperation1(AsyncCallback<List<String>> callback);

	Request genericTypeOperation2(AsyncCallback<ResultWrapper<List<String>>> callback);
}