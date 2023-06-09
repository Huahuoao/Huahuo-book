package com.huahuo.huahuobook.converter;

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
public class TypeThreeConverter implements Converter<Integer> {
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
        if ("手动记账".equals(cellStr)) {
            return 1;
        } else if ("自动记账".equals(cellStr)) {
            return 2;
        }
        else if ("图像记账".equals(cellStr)) {
            return 3;
        }
        else if ("语音记账".equals(cellStr)) {
            return 4;
        }
        else if ("日用品".equals(cellStr)) {
            return 5;
        }
        else if ("其他".equals(cellStr)) {
            return 6;
        }
        else {
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
            return new WriteCellData<>("手动记账");
        } else if (cellValue == 2) {
            return new WriteCellData<>("自动记账");
        }
        else if (cellValue == 3) {
            return new WriteCellData<>("图像记账");
        }
        else if (cellValue == 4) {
            return new WriteCellData<>("语音记账");
        }
        else if (cellValue == 5) {
            return new WriteCellData<>("日用品");
        }
        else if (cellValue == 6) {
            return new WriteCellData<>("其他");
        }
        else {
            return new WriteCellData<>("");
        }
    }
}