package com.gm.ace.common.convert;

import org.mapstruct.control.MappingControl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * MapStruct 的 mappingControl，用于实现同类型间的深拷贝（排除 DIRECT）
 * <p>
 * 例：{@code @BeanMapping(mappingControl = NoDirectMapping.class)}
 * <p>
 * 与 MapStruct 内置 {@code @DeepClone} 语义一致：排除 DIRECT 后，MapStruct 会为同类型字段生成子映射方法，
 * 避免源与目标共享同一引用。
 *
 * @author guoym
 */
@Retention(RetentionPolicy.CLASS)
@MappingControl(MappingControl.Use.BUILT_IN_CONVERSION)
@MappingControl(MappingControl.Use.MAPPING_METHOD)
@MappingControl(MappingControl.Use.COMPLEX_MAPPING)
public @interface NoDirectMapping {
}
