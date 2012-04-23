package tikainaction.chapter4;

import java.util.Map;
import java.util.Set;

import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MediaTypeRegistry;

public class MediaTypeExample {

    public static void describeMediaType() {
//<start id="media_type"/> 
MediaType type = MediaType.parse("text/plain; charset=UTF-8");

System.out.println("type:    " + type.getType());
System.out.println("subtype: " + type.getSubtype());

Map<String, String> parameters = type.getParameters();
System.out.println("parameters:");
for (String name : parameters.keySet()) {
    System.out.println("  " + name + "=" + parameters.get(name));
}
//<end id="media_type"/>
    }

    public static void listAllTypes() {
//<start id="media_type_registry"/> 
MediaTypeRegistry registry = MediaTypeRegistry.getDefaultRegistry();

for (MediaType type : registry.getTypes()) {
    Set<MediaType> aliases = registry.getAliases(type);
    System.out.println(type + ", also known as " + aliases);
}
//<end id="media_type_registry"/>
    }

    public static void main(String[] args) throws Exception {
//<start id="get_super_type"/> 
MediaTypeRegistry registry = MediaTypeRegistry.getDefaultRegistry();

MediaType type = MediaType.parse("image/svg+xml");
while (type != null) {
    System.out.println(type);
    type = registry.getSupertype(type);
}
//<end id="get_super_type"/>
          }

}
