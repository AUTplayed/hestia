package at.karriere.components;

import org.springframework.stereotype.Component;
import redis.clients.jedis.BuilderFactory;

import java.util.List;

@Component
public class OutputConverterComponent {

    //Frech kopiert, github.com/ApesRise/jedis-cli/
    public String stringify(Object obj){
        if (obj instanceof byte[]) {
            return new String((byte[]) obj);
        } else if (obj instanceof Long) {
            return ((Long) obj).toString();
        } else if (obj instanceof List) {
            List temp = (List) obj;
            if (temp.size() == 0) {
                return "";
            }

            StringBuilder sb = new StringBuilder();

            String className = temp.get(0).getClass().getName();
            String className2 = null;
            if (temp.size() > 1) {
                className2 = temp.get(1).getClass().getName();
            }
            List build = null;
            if (className2 == null || className2.equals(className)) {
                if (className.equals("[B")) {
                    build = BuilderFactory.STRING_LIST.build((List<byte[]>) temp);
                } else if (className.equals("java.lang.Long")) {
                    build = BuilderFactory.STRING_LIST.build((List<Long>) temp);
                } else if (className.equals("java.lang.Object")) {
                    build = BuilderFactory.STRING_LIST.build((List<Long>) temp);
                }
            } else {
                sb.append(new String((byte[]) temp.get(0)));
                sb.append("\n");
                build = BuilderFactory.STRING_LIST.build(((List) (temp.subList(1, temp.size()))).get(0));
            }

            if (build == null) {
                return "";
            }

            for (Object cur : build) {
                sb.append(cur);
                sb.append("\n");
            }
            return sb.toString().trim();
        }
        return "";
    }

}
