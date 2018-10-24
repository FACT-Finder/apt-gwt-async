package net.omikron.apt.gwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Mark a service interface to be eligible for generation of a asynchronous variant.
 *
 * <p>Servers have a synchronous programming model, that is, they implement a service method which
 * computes and directly returns a result. However, on the client side, asynchronous models prevail:
 * why wait for the request to complete if more work can be done in the meantime? Second, how to
 * cancel a pending request?
 *
 * <p>For the asynchronous service, a new service interface is generated in the same package,
 * following the {@code *Async} naming convention. The transformation of the synchronous service
 * methods to the asynchronous variants consists of the following steps:
 *
 * <ul>
 *   <li>All parameters of the synchronous variant are copied unchanged to the asynchronous method.
 *   <li>A new callback parameter is appended to the argument list. This callback gets invoked after
 *       the service call completed and processes the result, either success or failure. The success
 *       branch must accept objects of the synchronous methods' return type.
 *   <li>The return type of the asynchronous service methods becomes {@code Request}, such that
 *       pending requests can be canceled.
 * </ul>
 *
 * By definition, the async service must include methods not only of the service itself, but also
 * all methods of the services supertype, because there is no such thing as a partial async service.
 * Additionally, if the supertype carried generic type parameters in the argument lists, the async
 * services' argument list consist of the actual type arguments supplied by the service.
 *
 * <p>Usage: actually two annotations are processed by this annotation processor:
 *
 * <ol>
 *   <li>{@code com.google.gwt.user.client.rpc.RemoteServiceRelativePath}: To ease adoption of this
 *       annotation processor, all services with a configured remote path are in the scope of async
 *       service generation.
 *   <li>{@link AsyncService}: Use this annotation in case no remote path is configured or other
 *       means are used for service deployment. Also intermediate interfaces (in terms of an service
 *       inheritance hierarchy) can be annotated using this annotation to have their async variant
 *       generated.
 * </ol>
 */
@Target(ElementType.TYPE)
public @interface AsyncService {}
