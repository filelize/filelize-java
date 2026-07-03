package io.github.filelize;

import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends the default Jackson introspector so that, in the absence of an explicit
 * {@code @JsonPropertyOrder} on the domain class, the field(s) annotated with Filelize's
 * own {@link Id} are always serialized first. Remaining properties fall back to whatever
 * ordering the ObjectMapper is otherwise configured with (e.g. alphabetical).
 */
public class FilelizeIdFirstAnnotationIntrospector extends JacksonAnnotationIntrospector {

    private static final long serialVersionUID = 1L;

    @Override
    public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
        var explicitOrder = super.findSerializationPropertyOrder(ac);
        if (explicitOrder != null) {
            return explicitOrder;
        }

        List<String> idFieldNames = new ArrayList<>();
        for (var field : ac.getRawType().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idFieldNames.add(field.getName());
            }
        }
        return idFieldNames.isEmpty() ? null : idFieldNames.toArray(new String[0]);
    }
}
