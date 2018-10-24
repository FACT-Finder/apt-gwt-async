package net.omikron.apt.gwt;

import static java.util.stream.Collectors.toList;

import com.google.auto.service.AutoService;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

/**
 * Generate GWT Async Services.
 *
 * @see AsyncService
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({
  "net.omikron.apt.gwt.AsyncService",
  "com.google.gwt.user.client.rpc.RemoteServiceRelativePath"
})
public class GwtAsyncGenerator extends AbstractProcessor {

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  @Override
  public boolean process(
      final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {

    for (final Element element : getAnnotatedElements(roundEnv)) {
      final Name name = ((TypeElement) element).getQualifiedName();
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found " + name);
      writeOutput(process(element));
    }
    return true;
  }

  private Set<Element> getAnnotatedElements(final RoundEnvironment roundEnv) {
    final Set<Element> elements = new LinkedHashSet<>();
    elements.addAll(roundEnv.getElementsAnnotatedWith(AsyncService.class));
    elements.addAll(roundEnv.getElementsAnnotatedWith(RemoteServiceRelativePath.class));
    return elements;
  }

  private JavaFile process(final Element sourceInterface) {
    final TypeSpec.Builder asyncInterface = newAsyncInterface(sourceInterface);
    addMethods(sourceInterface, asyncInterface);
    return JavaFile.builder(packageOf(sourceInterface), asyncInterface.build())
        .skipJavaLangImports(true)
        .build();
  }

  private TypeSpec.Builder newAsyncInterface(final Element sourceInterface) {
    final String generatedClassName = sourceInterface.getSimpleName() + "Async";
    return TypeSpec.interfaceBuilder(generatedClassName).addModifiers(Modifier.PUBLIC);
  }

  private String packageOf(final Element type) {
    final PackageElement pkg = processingEnv.getElementUtils().getPackageOf(type);
    return pkg.getQualifiedName().toString();
  }

  private void writeOutput(final JavaFile file) {
    try {
      file.writeTo(processingEnv.getFiler());
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void addMethods(final Element sourceInterface, final TypeSpec.Builder asyncInterface) {
    allMethodsOf(sourceInterface)
        .forEach(sourceMethod -> addMethod(sourceInterface, sourceMethod, asyncInterface));
  }

  private void addMethod(
      final Element sourceInterface,
      final ExecutableElement sourceMethod,
      final TypeSpec.Builder asyncInterface) {
    final MethodSpec.Builder asyncMethod = newAsyncMethod(sourceMethod);
    copyParameters(sourceInterface, sourceMethod, asyncMethod);
    addCallbackParameter(sourceInterface, sourceMethod, asyncMethod);
    asyncInterface.addMethod(asyncMethod.build());
  }

  private MethodSpec.Builder newAsyncMethod(final Element sourceMethod) {
    final MethodSpec.Builder method =
        MethodSpec.methodBuilder(sourceMethod.getSimpleName().toString());
    method.returns(Request.class).addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
    return method;
  }

  private void copyParameters(
      final Element sourceInterface,
      final ExecutableElement sourceMethod,
      final MethodSpec.Builder asyncMethod) {

    final List<String> names = parameterNames(sourceMethod);
    final List<? extends TypeMirror> types =
        methodAsMemberOf(sourceInterface, sourceMethod).getParameterTypes();

    for (int index = 0; index < names.size(); ++index) {
      asyncMethod.addParameter(
          ParameterSpec.builder(TypeName.get(types.get(index)), names.get(index)).build());
    }
  }

  private List<String> parameterNames(ExecutableElement method) {
    return method.getParameters().stream().map(p -> p.getSimpleName().toString()).collect(toList());
  }

  private void addCallbackParameter(
      final Element sourceInterface,
      final ExecutableElement sourceMethod,
      final MethodSpec.Builder asyncMethod) {

    final TypeMirror returnType = methodAsMemberOf(sourceInterface, sourceMethod).getReturnType();
    final TypeName boxedReturnType = TypeName.get(returnType).box();
    final ClassName callback = ClassName.get(AsyncCallback.class);
    asyncMethod.addParameter(ParameterizedTypeName.get(callback, boxedReturnType), "callback");
  }

  private ExecutableType methodAsMemberOf(final Element type, final ExecutableElement method) {
    return (ExecutableType)
        processingEnv.getTypeUtils().asMemberOf((DeclaredType) type.asType(), method);
  }

  private List<ExecutableElement> allMethodsOf(final Element type) {
    final Set<ExecutableElement> methods = new LinkedHashSet<>();
    methodsOfRecursive(type, methods);
    return new ArrayList<>(methods);
  }

  private void methodsOfRecursive(final Element type, final Set<ExecutableElement> methods) {
    methods.addAll(ElementFilter.methodsIn(type.getEnclosedElements()));
    for (final TypeMirror e : ((TypeElement) type).getInterfaces()) {
      methodsOfRecursive(processingEnv.getTypeUtils().asElement(e), methods);
    }
  }
}
