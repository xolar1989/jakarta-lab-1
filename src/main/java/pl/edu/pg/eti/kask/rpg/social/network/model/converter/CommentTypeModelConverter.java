package pl.edu.pg.eti.kask.rpg.social.network.model.converter;

import pl.edu.pg.eti.kask.rpg.social.network.entity.CommentType;
import pl.edu.pg.eti.kask.rpg.social.network.model.CommentTypeModel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Optional;

@FacesConverter(forClass = CommentTypeModel.class, managed = true)
public class CommentTypeModelConverter implements Converter<CommentTypeModel> {
    @Override
    public CommentTypeModel getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        CommentType commentType = CommentType.fromString(value);
        return CommentTypeModel.entityToModelMapper().apply(commentType);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, CommentTypeModel value) {
        return value == null ? "" : value.getType();
    }
}
