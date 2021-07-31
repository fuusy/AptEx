package com.fuusy.lib_annotation_compiler;

import com.fuusy.lib_annotation.Route;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;


@AutoService(Processor.class)
public class RouteProcessor extends AbstractProcessor {

    Filer filer;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Route.class);
        Map<String, String> map = new HashMap<>();
        for (Element element :
                elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element;
            Route annotation = typeElement.getAnnotation(Route.class);
            String path = annotation.path();
            Name qualifiedName = typeElement.getQualifiedName();
            map.put(path, qualifiedName + ".class");
        }

        if (map.size() > 0) {
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String activityKey = iterator.next();
                String cls = map.get(activityKey);
                //使用Javapoet
                MethodSpec methodSpec = MethodSpec.methodBuilder("putActivity")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(void.class)
                        .addStatement("ARouter.getInstance().putActivity("+"\""+activityKey+"\","+cls+")")
                        .build();
                final ClassName InterfaceName = ClassName.get("com.fuusy.arouterapi","IRouter");
                TypeSpec typeSpec = TypeSpec.classBuilder("ActivityUtil"+System.currentTimeMillis())
                        .addSuperinterface(InterfaceName)
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(methodSpec)
                        .build();

                JavaFile javaFile = JavaFile.builder("com.fuusy.arouterapi", typeSpec).build();
                try {
                    javaFile.writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


//            Writer writer = null;
//            String className = "ActivityUtil" + System.currentTimeMillis();
//            try {
//                JavaFileObject classFile = filer.createSourceFile("com.fuusy.arouterapi." + className);
//                writer = classFile.openWriter();
//                writer.write("package com.fuusy.arouterapi;\n" +
//                        "\n" +
//                        "import com.fuusy.arouterapi.ARouter;\n" +
//                        "import com.fuusy.arouterapi.IRouter;\n" +
//                        "\n" +
//                        "public class " + className + " implements IRouter {\n" +
//                        "    @Override\n" +
//                        "    public void putActivity() {\n");
//
//                Iterator<String> iterator = map.keySet().iterator();
//                while (iterator.hasNext()) {
//                    String activityKey = iterator.next();
//                    String cls = map.get(activityKey);
//                    writer.write("        ARouter.getInstance().putActivity(");
//                    writer.write("\"" + activityKey + "\"," + cls + ");");
//                }
//                writer.write("\n}\n" +
//                        "}");
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (writer != null) {
//                    try {
//                        writer.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

        }
        return false;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> types = new HashSet<>();
        types.add(Route.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

}
