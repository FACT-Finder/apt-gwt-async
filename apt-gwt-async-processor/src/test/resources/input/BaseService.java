package net.omikron.apt.gwt.example;

/**
 * Base service that showcases generic parameters for return and argument types.
 */
public interface BaseService<T, U> {

	T get();

	void set(U u);
}