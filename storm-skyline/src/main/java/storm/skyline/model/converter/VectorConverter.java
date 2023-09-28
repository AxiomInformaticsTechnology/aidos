package storm.skyline.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class VectorConverter implements AttributeConverter<Object, String> {

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        return String.valueOf(attribute);
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        try {
            return Integer.parseInt(dbData);
        } catch (NumberFormatException e) {
            try {
                return Long.parseLong(dbData);
            } catch (NumberFormatException e1) {
                try {
                    return Double.parseDouble(dbData);
                } catch (NumberFormatException e2) {
                    return dbData;
                }
            }
        }
    }

}
