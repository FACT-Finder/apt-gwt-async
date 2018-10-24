package net.omikron.apt.gwt.example;

import java.util.List;

import net.omikron.apt.gwt.AsyncService;
import net.omikron.apt.gwt.ResultWrapper;

/**
 * Test service showcasing arguments of different type.
 */
@AsyncService
public interface ArgumentTypeService {

	ResultWrapper<Void> doService(char primitive, String object, List<String> objects);
}