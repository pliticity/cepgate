package pl.iticity.dbfds.model.query;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.StringPath;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import pl.iticity.dbfds.model.QDocumentInfo;

import java.text.MessageFormat;

public class QDocumentInfoBinderCustomizer implements QuerydslBinderCustomizer<QDocumentInfo>{

    @Override
    public void customize(QuerydslBindings querydslBindings, QDocumentInfo entityPath) {
        querydslBindings.bind(entityPath.documentName).first(new SingleValueBinding<StringPath, String>() {
            @Override
            public Predicate bind(StringPath stringPath, String s) {
                return stringPath.like(MessageFormat.format("%{0}%",s));
            }
        });
    }
}
