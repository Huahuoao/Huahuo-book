package com.huahuo.huahuobook.converter;

/**
 * @作者 花火
 * @创建日期 2023/1/29 16:33
 */

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * excel性别转换器
 * Created by macro on 2021/12/29.
 */
public class TypeTwoConverter implements Converter<Integer> {
    @Override
    public Class<?> supportJavaTypeKey() {
        //对象属性类型
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        //CellData属性类型
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) throws Exception {
        //CellData转对象属性
        String cellStr = context.getReadCellData().getStringValue();
        if (StrUtil.isEmpty(cellStr)) return null;
        if ("支出".equals(cellStr)) {
            return 1;
        } else if ("收入".equals(cellStr)) {
            return 2;
        } else {
            return null;
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        //对象属性转CellData
        Integer cellValue = context.getValue();
        if (cellValue == null) {
            return new WriteCellData<>("");
        }
        if (cellValue == 1) {
            return new WriteCellData<>("支出");
        } else if (cellValue == 2) {
            return new WriteCellData<>("收入");
        } else {
            return new WriteCellData<>("");
        }
    }
}