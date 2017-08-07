package at.karriere.hestia.component;

import org.springframework.stereotype.Component;
import redis.clients.jedis.BuilderFactory;

import java.util.List;

@Component
public class OutputConverterComponent {

    //Frech kopiert, github.com/ApesRise/jedis-cli/ - Kommentare wurden ergänzt
    public String stringify(Object obj){
        //When byte[]
        if (obj instanceof byte[]) {
            return new String((byte[]) obj);
        }
        //When Long
        else if (obj instanceof Long) {
            return ((Long) obj).toString();
        }
        //When List
        else if (obj instanceof List) {
            return stringifyList((List) obj);
        }
        return "";
    }

    private String stringifyList(List list) {
        //When list has no elements, return ""
        if (list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        //Determine dataType of first element
        String className = list.get(0).getClass().getName();
        String className2 = null;
        //Also determine dataType of second element - because 1st element can be a header
        if (list.size() > 1) {
            className2 = list.get(1).getClass().getName();
        }
        List build = null;
        //When there is no header as the first element just convert the whole list to a String list
        if (className2 == null || className2.equals(className)) {
            //When the elements are byte[]
            if (className.equals("[B")) {
                build = BuilderFactory.STRING_LIST.build((List<byte[]>) list);
            }
            //When the elements are Long
            else if (className.equals("java.lang.Long")) {
                build = BuilderFactory.STRING_LIST.build((List<Long>) list);
            }
            //Not really sure about this case
            else if (className.equals("java.lang.Object")) {
                build = BuilderFactory.STRING_LIST.build((List<Long>) list);
            }
        } else {
            //When the 1st element is a header, parse that one first, then append the rest of the list
            sb.append(new String((byte[]) list.get(0)));
            sb.append("\n");
            build = BuilderFactory.STRING_LIST.build(((List) (list.subList(1, list.size()))).get(0));
        }
        //case for empty list
        if (build == null) {
            return "";
        }

        //Append every element to StringBuilder
        for (Object cur : build) {
            sb.append(cur);
            sb.append("\n");
        }
        //Return built String
        return sb.toString().trim();
    }

}
