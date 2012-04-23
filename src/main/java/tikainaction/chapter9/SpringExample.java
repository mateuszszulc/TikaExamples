package tikainaction.chapter9;

import java.io.ByteArrayInputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.WriteOutContentHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringExample {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "tikainaction/chapter9/spring.xml" });
        Parser parser = context.getBean("tika", Parser.class);
        parser.parse(
                new ByteArrayInputStream("Hello, World!".getBytes()),
                new WriteOutContentHandler(System.out),
                new Metadata(),
                new ParseContext());
    }

}
