package net.omikron.apt.gwt;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static java.util.Arrays.asList;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import java.util.ArrayList;
import java.util.List;
import javax.tools.JavaFileObject;
import net.omikron.apt.util.JavaFileObjectResolver;
import net.omikron.apt.util.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JavaFileObjectResolver.class)
@SuppressWarnings("unused" /* annotated fields populated using extension */)
class GwtAsyncGeneratorTest {

  @Location("input/ReturnTypeService.java")
  private JavaFileObject returnTypeService;

  @Location("expected/ReturnTypeServiceAsync.java")
  private JavaFileObject returnTypeServiceAsync;

  @Location("input/ArgumentTypeService.java")
  private JavaFileObject argumentTypeService;

  @Location("expected/ArgumentTypeServiceAsync.java")
  private JavaFileObject argumentTypeServiceAsync;

  @Location("input/BaseService.java")
  private JavaFileObject baseService;

  @Location("input/ExtendingService.java")
  private JavaFileObject extendingService;

  @Location("expected/ExtendingServiceAsync.java")
  private JavaFileObject extendingServiceAsync;

  @Location("input/ResultWrapper.java")
  private JavaFileObject resultWrapper;

  @Test
  void differentReturnTypes() {
    final Compilation compilation = compile(returnTypeService);

    assertThat(compilation).succeeded();
    assertThat(compilation)
        .generatedSourceFile("net.omikron.apt.gwt.example.ReturnTypeServiceAsync") //
        .hasSourceEquivalentTo(returnTypeServiceAsync);
  }

  @Test
  void differentArgumentTypes() {
    final Compilation compilation = compile(argumentTypeService);

    assertThat(compilation).succeeded();
    assertThat(compilation)
        .generatedSourceFile("net.omikron.apt.gwt.example.ArgumentTypeServiceAsync") //
        .hasSourceEquivalentTo(argumentTypeServiceAsync);
  }

  @Test
  void extendingServiceWithGenerics() {
    final Compilation compilation = compile(baseService, extendingService);

    assertThat(compilation).succeeded();
    assertThat(compilation)
        .generatedSourceFile("net.omikron.apt.gwt.example.ExtendingServiceAsync") //
        .hasSourceEquivalentTo(extendingServiceAsync);
  }

  private Compilation compile(final JavaFileObject... files) {
    final List<JavaFileObject> allFiles = new ArrayList<>();
    allFiles.add(resultWrapper);
    allFiles.addAll(asList(files));
    return compiler().compile(allFiles);
  }

  private Compiler compiler() {
    return Compiler.javac().withProcessors(new GwtAsyncGenerator());
  }
}
