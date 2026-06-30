package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Repository;

/**
 * 【${table.comment!}】 持久层
 * <p>不要写业务代码！！！</p>
 *
 * @author ${author}
 * @since ${date}
 */
@Repository
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> {

}