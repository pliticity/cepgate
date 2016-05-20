package pl.iticity.dbfds.controller;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.query.QDocumentInfoBinderCustomizer;
import pl.iticity.dbfds.service.DocumentService;

import javax.annotation.Nullable;
import java.util.List;

@Controller
@RequestMapping("/document")
public class DocumentController extends AbstractCrudController<DocumentInfo,DocumentService>{

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String getDocumentView(){
        return "document";
    }

    @RequestMapping(value ="/query", method = RequestMethod.GET)
    public @ResponseBody
    List<DocumentInfo> getAllDocuments(@QuerydslPredicate(root = DocumentInfo.class,bindings = QDocumentInfoBinderCustomizer.class) Predicate predicate){
        return service.findByPredicate(predicate);
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public @ResponseBody DocumentInfo getNewDocument(){
        return service.createNewDocumentInfo();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody boolean postUploadFiles(@RequestParam("file") MultipartFile file){
        System.out.println("start");
            System.out.println(file);
        System.out.println("end");
        return true;
    }

}
