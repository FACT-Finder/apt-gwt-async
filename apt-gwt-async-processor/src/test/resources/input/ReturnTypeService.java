package net.omikron.apt.gwt.example;

import java.util.List;

import net.omikron.apt.gwt.AsyncService;
import net.omikron.apt.gwt.ResultWrapper;

/**
 * Showcase different kinds of return type.
 */
@AsyncService
public interface ReturnTypeService {

	void voidOperation();

	int primitiveOperation();

	String objectOperation();

	List<String> genericTypeOperation1();

	ResultWrapper<List<String>> genericTypeOperation2();
}