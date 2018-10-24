package net.omikron.apt.gwt.example;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import net.omikron.apt.gwt.ResultWrapper;

public interface ArgumentTypeServiceAsync {

	Request doService(char primitive, String object, List<String> objects, AsyncCallback<ResultWrapper<Void>> callback);
}