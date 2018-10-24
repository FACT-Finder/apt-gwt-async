package net.omikron.apt.util;

import com.google.testing.compile.JavaFileObjects;
import java.lang.reflect.Field;
import javax.tools.JavaFileObject;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

/** Populate test class fields of type {@link JavaFileObject} from {@link Location}. */
public class JavaFileObjectResolver implements TestInstancePostProcessor {

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context)
      throws Exception {

    final Field[] fields = testInstance.getClass().getDeclaredFields();
    for (final Field field : fields) {

      if (field.getType() != JavaFileObject.class) {
        continue;
      }

      final Location file = field.getAnnotation(Location.class);
      if (file != null) {
        final JavaFileObject it = JavaFileObjects.forResource(file.value());
        field.setAccessible(true);
        field.set(testInstance, it);
      }
    }
  }
}
