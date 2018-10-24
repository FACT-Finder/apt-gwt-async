package net.omikron.apt.gwt.example;

import net.omikron.apt.gwt.AsyncService;

/**
 * Service for testing that the processor can indeed find methods from super-interfaces and is capable of resolving generic argument / return types properly.
 */
@AsyncService
public interface ExtendingService extends BaseService<String, Integer> {

}