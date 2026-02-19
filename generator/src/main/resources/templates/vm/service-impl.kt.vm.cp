package ${package.Service}.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ${package.Mapper}.${table.mapperName}
import ${package.Entity}.${table.entityName}
import ${package.Service}.${table.serviceName}

/**
 * @author xkl
 * @since 2026-02-12
 */
@Service
class ${table.serviceImplName}: ServiceImpl<${table.mapperName}, ${table.entityName}>(), ${table.serviceName} {

}
